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

# Get maven with jdk 7
FROM library/maven:3.6.1-jdk-7-slim as build

WORKDIR /usr/src/
COPY . .
COPY --from=web-build /usr/src/klient-web/src/main klient-web/src/main
RUN mvn clean package

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

# Copy necessery files for configuration
COPY tjeneste/src/main/resources/server.keystore $JBOSS_HOME/standalone/configuration/

# Deploy apps
COPY --from=build /root/.m2/repository/mysql/mysql-connector-java/$MYSQL_CONNECTOR/mysql-connector-java-$MYSQL_CONNECTOR.jar $JBOSS_HOME/standalone/deployments/
COPY --from=build /usr/src/klient-web/target/web.war $JBOSS_HOME/standalone/deployments/
COPY --from=build /usr/src/tjeneste/target/api.war $JBOSS_HOME/standalone/deployments/

# Ensure signals are forwarded to the JVM process correctly for graceful shutdown
ENV LAUNCH_JBOSS_IN_BACKGROUND true

# Expose the ports we're interested in
EXPOSE 8080 8443 9990

USER jboss

# Set the default command to run on boot
# This will boot WildFly in the standalone mode and bind to all interface
CMD ["/opt/jboss/wildfly/bin/standalone.sh", "-b", "0.0.0.0", "-bmanagement", "0.0.0.0"]
