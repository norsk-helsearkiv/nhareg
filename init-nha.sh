#!/usr/bin/env bash

echo "Configuring Wildfly"

sed -i "s/<resolve-parameter-values>false<\/resolve-parameter-values>/\<resolve-parameter-values>true<\/resolve-parameter-values>/"

source /docker-initnha.d/env.properties
./opt/jboss/wildfly/bin/jboss.sh --file=/docker-initnha.d/wildflyConfig.cli --properties=/docker-initnha.d/env.properties
./opt/jboss/wildfly/bin/add-user.sh $WILDFLY_USER $WILDFLY_PASSWORD --silent
