REM
REM Installasjon av databasedriver og databasekilder.
REM
%JBOSS_HOME%\bin\jboss-cli.bat --file=mysql-database-config-wildfly.cli
REM
REM Installasjon av applikasjoner.
REM
%JBOSS_HOME%\bin\jboss-cli.bat --file=deployApp.cli
REM
REM Avinstallering av applikasjoner.
REM
REM %JBOSS_HOME%\bin\jboss-cli.bat --file=undeployApp.cli
REM