#!/bin/bash

export JBOSS_HOME=/opt/jboss/wildfly/
export JBOSS_CLI=$JBOSS_HOME/bin/jboss-cli.sh

# Run initial configurations if it's the first time the image is run.
initfile=/opt/jboss/.initialized
if [ ! -e $initfile ]; then
  echo "Configuring Wildfly"

  touch $initfile
  
  # If MySQL root pw is not set, set it.
  if [ -z "$MYSQL_ROOT_PASSWORD" ]; then
    export MYSQL_ROOT_PASSWORD=admin
  fi
  
  # Run JasperServer installation
  cd /usr/src/jasperreports-server/buildomatic &&
    sed -i 's|dbPassword=.*|dbPassword='"$MYSQL_ROOT_PASSWORD"'|' default_master.properties &&
    yes n | /bin/bash js-install-ce.sh minimal

  # Fix connector driver
  cd $JBOSS_HOME/standalone/deployments/jasperserver.war/WEB-INF &&
    sed -i 's/<driver>mysql-connector-java-5.1.48.jar<\/driver>/<driver>mysql-connector-java-5.1.48.jar_com.mysql.jdbc.Driver_5_1<\/driver>/g' js-jboss7-ds.xml
fi

# Check if Wildfly admin password is set, if so update it.
if [ -n "$WILDFLY_PASSWORD" ]; then
  echo "Updating Wildfly admin password"
  $JBOSS_HOME/bin/add-user.sh -u admin -p "$WILDFLY_PASSWORD" --silent
fi

# Allow job control
set -m 

# Run Wildfly in the background so the rest of the script can run
$JBOSS_HOME/bin/standalone.sh -b 0.0.0.0 -bmanagement 0.0.0.0 &

# Check if MySQL username and password is set, if so update Wildfly datasource
if [ -n "$MYSQL_USER" ] && [ -n "$MYSQL_PASSWORD" ]; then
  # Wait for Wildfly to start up
  echo "Updating datasource credentials"
  until $JBOSS_CLI -c ":read-attribute(name=server-state)" 2> /dev/null | grep -q running; do echo "$JBOSS_CLI -c ':read-attribute(name=server-state)' 2" > /dev/null; sleep 1; done
  printf "MYSQL_USER=%s\nMYSQL_PASSWORD=%s" "$MYSQL_USER" "$MYSQL_PASSWORD" > /tmp/db.properties
  $JBOSS_CLI --connect --file=/update-datasource-credentials.cli --properties=/tmp/db.properties
  rm /tmp/db.properties
fi

# Bring Wildfly to the front, else Docker shuts down the image.
fg