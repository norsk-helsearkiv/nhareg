####### NODE #######
FROM node:13.6-alpine as web-build

RUN apk update && apk upgrade && \
    apk add --no-cache bash git openssh

WORKDIR /usr/src
COPY klient-web/ klient-web
RUN cd klient-web/src/main/ \
    && npm install \
    && npm update \
    && npm install -u bower \
    && node_modules/bower/bin/bower install --allow-root --config.interactive=false \
    && npm install -g grunt-cli \
    && grunt


####### MAVEN #######
FROM library/maven:3.6.3-jdk-8-slim as build

ARG MAVEN_ARGS=""

WORKDIR /usr/src/
COPY domene domene
COPY klient-web/ klient-web
COPY tjeneste/ tjeneste
COPY pom.xml .
COPY --from=web-build /usr/src/klient-web/src/main klient-web/src/main

RUN mvn clean package $MAVEN_ARGS


####### Wildfly #######
FROM jboss/wildfly:10.1.0.Final as wildfly

# Set the relevant environment variables
ENV WILDFLY_VERSION 10.1.0.Final
ENV WILDFLY_SHA1 9ee3c0255e2e6007d502223916cefad2a1a5e333
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
    && rm wildfly-$WILDFLY_VERSION.tar.gz 

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

RUN mkdir /cli/ \
    && chown jboss:0 /cli
COPY src/main/resources/env.properties /usr/src/
COPY src/main/resources/*.cli /cli/
COPY --from=build /root/.m2/repository/mysql/mysql-connector-java/$MYSQL_CONNECTOR/mysql-connector-java-$MYSQL_CONNECTOR.jar $JBOSS_HOME/standalone/deployments/

# Add Wildfly configurations
RUN echo "Configuring Wildfly" \
    && bash -c '$JBOSS_HOME/bin/standalone.sh &' \
    && bash -c 'until `$JBOSS_CLI -c ":read-attribute(name=server-state)" 2> /dev/null | grep -q running`; do echo `$JBOSS_CLI -c ":read-attribute(name=server-state)" 2> /dev/null`; sleep 1; done' \
    && $JBOSS_CLI --file=/cli/wildflyConfig.cli --properties=/usr/src/env.properties \
    && chown -R jboss:0 ${JBOSS_HOME} \
    && chmod -R g+rw ${JBOSS_HOME} \
    && rm -rf $JBOSS_HOME/standalone/configuration/standalone_xml_history/ $JBOSS_HOME/standalone/log/*

# Deploy apps
COPY --from=build /usr/src/klient-web/target/web.war $JBOSS_HOME/standalone/deployments/
COPY --from=build /usr/src/tjeneste/target/api.war $JBOSS_HOME/standalone/deployments/

####### ALPINE #######
FROM alpine:3.11

ENV JBOSS_HOME /opt/jboss/wildfly

RUN addgroup -S jboss && adduser -S jboss -G jboss

COPY --from=wildfly --chown=jboss $JBOSS_HOME $JBOSS_HOME
COPY --from=wildfly --chown=jboss /usr/src/jasperreports-server /opt/jasper/

COPY --chown=jboss src/main/resources/entrypoint.sh src/main/resources/update-datasource-credentials.cli src/main/resources/default_master.properties /

RUN apk --no-cache add openjdk8-jre sed dos2unix bash
RUN dos2unix /entrypoint.sh

# Ensure signals are forwarded to the JVM process correctly for graceful shutdown
ENV LAUNCH_JBOSS_IN_BACKGROUND true

# Expose the ports we're interested in
EXPOSE 8080 8443 9990

USER jboss

# Set the default command to run on boot
# This will boot WildFly in the standalone mode and bind to all interface
ENTRYPOINT ["/entrypoint.sh"]