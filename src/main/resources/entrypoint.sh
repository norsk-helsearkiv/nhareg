#!/bin/bash

export JBOSS_HOME=/opt/jboss/wildfly/
export JBOSS_CLI=$JBOSS_HOME/bin/jboss-cli.sh

# Run initial configurations if it's the first time the image is run.
initfile=$JBOSS_HOME/.initialized
if [ ! -e $initfile ]; then
  echo "Configuring Wildfly"

  touch $initfile
  
  # If MySQL root pw is not set, set it.
  if [ -z "$MYSQL_ROOT_PASSWORD" ]; then
    export MYSQL_ROOT_PASSWORD=admin
  fi
  
  # Run JasperServer installation
  cd /opt/jasper/buildomatic &&
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

# Wait for Wildfly to be started
until $JBOSS_CLI -c ":read-attribute(name=server-state)" 2> /dev/null | grep -q running; do echo "$JBOSS_CLI -c ':read-attribute(name=server-state)' 2" > /dev/null; sleep 1; done

# Check if MySQL username and password is set, if so update Wildfly datasource
if [ -n "$MYSQL_USER" ] && [ -n "$MYSQL_PASSWORD" ]; then
  # Wait for Wildfly to start up
  echo "Updating datasource credentials"
  printf "DATASOURCE=%s\nUSER=%s\nPASSWORD=%s" "NharegDS" "$MYSQL_USER" "$MYSQL_PASSWORD" > /tmp/db.properties
  $JBOSS_CLI --connect --file=/update-datasource-credentials.cli --properties=/tmp/db.properties
  rm /tmp/db.properties
fi

# Check if LMR datasource should be updated
if [ -n "$LMR_USER" ] && [ -n "$LMR_PASSWORD" ]; then
  echo "Updating LMR datasource"
  # Wait until Wildfly is ready 
  printf "DATASOURCE=%s\nUSER=%s\nPASSWORD=%s" "LmrDS" "$LMR_USER" "$LMR_PASSWORD" > /tmp/lmr.properties
  $JBOSS_CLI --connect --file=/update-datasource-credentials.cli --properties=/tmp/lmr.properties
  $JBOSS_CLI --connect --command="/subsystem=datasources/data-source=LmrDS:write-attribute(name=connection-url, value=jdbc:mysql://${LMR_HOST}:3306/lmr)"
  rm /tmp/lmr.properties
fi

# If we updated any datasources we need to reload Wildfly
if [ -n "$MYSQL_USER" ] || [ -n "$LMR_USER" ]; then
  $JBOSS_CLI --connect reload
  $JBOSS_CLI --connect reload
fi

# Bring Wildfly to the front, else Docker shuts down the image.
fg