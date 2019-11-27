#!/bin/bash

# Load environment variables for add-user.sh
source /docker-entrypoint-initnha.d/env.properties

export JBOSS_HOME=/opt/jboss/wildfly/
export JBOSS_CLI=$JBOSS_HOME/bin/jboss-cli.sh

# Run initial configurations if it's the first time the image is run.
initfile=/opt/jboss/.initialized
if [[ ! -e $initfile ]]; then
  echo "Configuring Wildfly"
  
  touch $initfile
  
  # Allow properties to be used when running Jboss CLI
  sed -i "s/<resolve-parameter-values>false<\/resolve-parameter-values>/\<resolve-parameter-values>true<\/resolve-parameter-values>/" $JBOSS_HOME/bin/jboss-cli.xml
  
  # Add admin user
  $JBOSS_HOME/bin/add-user.sh $WILDFLY_USER $WILDFLY_PASSWORD --silent
  
  # Boot Wildfly and wait until its running
  $JBOSS_HOME/bin/standalone.sh &
  until `$JBOSS_CLI -c ":read-attribute(name=server-state)" 2> /dev/null | grep -q running`; do echo `$JBOSS_CLI -c ":read-attribute(name=server-state)" 2> /dev/null`; sleep 1; done
  
  # Configure Wildfly using the properties file, then shut down Wildfly.
  $JBOSS_CLI --file=/docker-entrypoint-initnha.d/wildflyConfig.cli --properties=/docker-entrypoint-initnha.d/env.properties
  
  # Run JasperServer installation
  cd /usr/src/jasperreports-server/buildomatic &&
    sed -i 's|dbPassword=.*|dbPassword='"$MYSQL_ROOT_PASSWORD"'|' default_master.properties &&
    yes n | /bin/bash js-install-ce.sh minimal
  # Fix connector driver
  cd $JBOSS_HOME/standalone/deployments/jasperserver.war/WEB-INF &&
    sed -i 's/<driver>mysql-connector-java-5.1.48.jar<\/driver>/<driver>mysql-connector-java-5.1.48.jar_com.mysql.jdbc.Driver_5_1<\/driver>/g' js-jboss7-ds.xml
fi

# Run Wildfly for real.
$JBOSS_HOME/bin/standalone.sh -b 0.0.0.0 -bmanagement 0.0.0.0