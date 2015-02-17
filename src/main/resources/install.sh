#!/bin/sh
#echo "Installasjon av databasedriver og databasekilder."
#$JBOSS_HOME/bin/jboss-cli.sh --file=mysql-database-config-wildfly-nix.cli

#echo "Installasjon av applikasjoner."
$JBOSS_HOME/bin/jboss-cli.sh --file=deployApp-nix.cli

#echo Avinstallering av applikasjoner.
#$JBOSS_HOME/bin/jboss-cli.sh --file=undeployApp-nix.cli
