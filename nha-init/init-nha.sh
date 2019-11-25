#!/bin/bash

echo "Configuring Wildfly"

# Load environment variables for add-user.sh
source /docker-entrypoint-initnha.d/env.properties

export JBOSS_HOME=/opt/jboss/wildfly/
export JBOSS_CLI=$JBOSS_HOME/bin/jboss-cli.sh

# Allow properties to be used when running Jboss CLI
sed -i "s/<resolve-parameter-values>false<\/resolve-parameter-values>/\<resolve-parameter-values>true<\/resolve-parameter-values>/" $JBOSS_HOME/bin/jboss-cli.xml

# Add admin user
$JBOSS_HOME/bin/add-user.sh $WILDFLY_USER $WILDFLY_PASSWORD --silent

# Boot Wildfly and wait until its running
$JBOSS_HOME/bin/standalone.sh &
until `$JBOSS_CLI -c ":read-attribute(name=server-state)" 2> /dev/null | grep -q running`; do echo `$JBOSS_CLI -c ":read-attribute(name=server-state)" 2> /dev/null`; sleep 1; done

# Configure Wildfly using the properties file, then shut down Wildfly.
$JBOSS_CLI --file=/docker-entrypoint-initnha.d/wildflyConfig.cli --properties=/docker-entrypoint-initnha.d/env.properties

# Run Wildfly for real.
$JBOSS_HOME/bin/standalone.sh -b 0.0.0.0 -bmanagement 0.0.0.0