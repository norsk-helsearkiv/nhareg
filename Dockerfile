# Get node
FROM node:latest as web-build

WORKDIR /usr/src
COPY klient-web/ klient-web
RUN cd klient-web/src/main/ \
    && npm install \
    && npm update \
    && npm install -u bower \
    && node_modules/bower/bin/bower install --allow-root --config.interactive=false \
    && npm install -g grunt-cli \
    && grunt

# Get maven with jdk 8
FROM library/maven:3.6.3-jdk-8-slim as build

ARG MAVEN_ARGS=""

WORKDIR /usr/src/
COPY domene domene
COPY klient-web/ klient-web
COPY tjeneste/ tjeneste
COPY pom.xml .
COPY --from=web-build /usr/src/klient-web/src/main klient-web/src/main

RUN mvn clean package $MAVEN_ARGS

# Get Wildfly 8.2.0.Final
FROM jboss/wildfly:8.2.0.Final

# Set the relevant environment variables
ENV WILDFLY_VERSION 8.2.0.Final
ENV WILDFLY_SHA1 d78a864386a9bc08812eed9781722e45812a7826
ENV JBOSS_HOME /opt/jboss/wildfly
ENV JBOSS_CLI $JBOSS_HOME/bin/jboss-cli.sh
ENV MYSQL_CONNECTOR 5.1.48

USER root

# Add the WildFly distribution to /opt, and make wildfly the owner of the extracted tar content
# Make sure the distribution is available from a well-known place
RUN cd $HOME \
    && curl -O https://download.jboss.org/wildfly/$WILDFLY_VERSION/wildfly-$WILDFLY_VERSION.tar.gz \
    && sha1sum wildfly-$WILDFLY_VERSION.tar.gz | grep $WILDFLY_SHA1 \
    && tar xf wildfly-$WILDFLY_VERSION.tar.gz \
    && mv $HOME/wildfly-$WILDFLY_VERSION $JBOSS_HOME \
    && rm wildfly-$WILDFLY_VERSION.tar.gz \
    && chown -R jboss:0 ${JBOSS_HOME} \
    && chmod -R g+rw ${JBOSS_HOME}

# Get and configure JasperServer
WORKDIR /usr/src

COPY --from=build /root/.m2/repository/mysql/mysql-connector-java/$MYSQL_CONNECTOR/mysql-connector-java-$MYSQL_CONNECTOR.jar .
COPY src/main/resources/default_master.properties .

RUN curl -L -O https://iweb.dl.sourceforge.net/project/jasperserver/JasperServer/JasperReports%20Server%20Community%20Edition%206.4.3/TIB_js-jrs-cp_6.4.3_bin.zip \
    && unzip -q TIB_js-jrs-cp_6.4.3_bin.zip \
    && rm -f TIB_js-jrs-cp_6.4.3_bin.zip \
    && mv jasperreports-server-cp-6.4.3-bin jasperreports-server \
    && mv mysql-connector-java-$MYSQL_CONNECTOR.jar jasperreports-server/buildomatic/conf_source/db/mysql/jdbc/ \
    && mv default_master.properties jasperreports-server/buildomatic/ \
    && chown -R jboss:0 jasperreports-server \
    && chmod -R g+w jasperreports-server \
    && cd jasperreports-server/buildomatic \
    && sed -i 's/<resources>/<resources><resource-root path="WEB-INF\/lib\/mysql-connector-java-5.1.48.jar" use-physical-code-source="true"\/>/' install_resources/jboss7/wildfly/jboss-deployment-structure.xml \
    && sed -i 's|appServerDir = .*|appServerDir = '"$JBOSS_HOME"'|' default_master.properties

# Copy necessery files for configuration
COPY src/main/resources/server.keystore $JBOSS_HOME/standalone/configuration/

# Configure Wildfly
# Allow properties to be used when running Jboss CLI
RUN sed -i "s/<resolve-parameter-values>false<\/resolve-parameter-values>/\<resolve-parameter-values>true<\/resolve-parameter-values>/" $JBOSS_HOME/bin/jboss-cli.xml

# Add admin user
RUN $JBOSS_HOME/bin/add-user.sh -u admin -p admin --silent

COPY src/main/resources/env.properties /usr/src/
COPY src/main/resources/wildflyConfig.cli /usr/src/
COPY src/main/resources/update-datasource-credentials.cli /
COPY --from=build /root/.m2/repository/mysql/mysql-connector-java/$MYSQL_CONNECTOR/mysql-connector-java-$MYSQL_CONNECTOR.jar $JBOSS_HOME/standalone/deployments/

# Add Wildfly configurations
RUN echo "Configuring Wildfly" \
    && bash -c '$JBOSS_HOME/bin/standalone.sh &' \
    && bash -c 'until `$JBOSS_CLI -c ":read-attribute(name=server-state)" 2> /dev/null | grep -q running`; do echo `$JBOSS_CLI -c ":read-attribute(name=server-state)" 2> /dev/null`; sleep 1; done' \
    && $JBOSS_CLI --file=/usr/src/wildflyConfig.cli --properties=/usr/src/env.properties \
    && chown -R jboss:0 ${JBOSS_HOME} \
    && chmod -R g+rw ${JBOSS_HOME} \
    && rm -rf $JBOSS_HOME/standalone/configuration/standalone_xml_history/ $JBOSS_HOME/standalone/log/*

# Deploy apps
COPY --from=build /usr/src/klient-web/target/web.war $JBOSS_HOME/standalone/deployments/
COPY --from=build /usr/src/tjeneste/target/api.war $JBOSS_HOME/standalone/deployments/

# Ensure signals are forwarded to the JVM process correctly for graceful shutdown
ENV LAUNCH_JBOSS_IN_BACKGROUND true

# Expose the ports we're interested in
EXPOSE 8080 8443 9990

COPY src/main/resources/entrypoint.sh /entrypoint.sh
RUN chmod +x /entrypoint.sh \
    && chown jboss:0 /entrypoint.sh

USER jboss

# Set the default command to run on boot
# This will boot WildFly in the standalone mode and bind to all interface
ENTRYPOINT ["/entrypoint.sh"]