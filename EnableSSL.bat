rem
rem Enable SSL
rem 
rem Hentet fra https://docs.jboss.org/author/display/WFLY8/Security+Realms
rem
cd %JBOSS_HOME%\standalone\configuration
"%JAVA_HOME%/bin/keytool" -genkey -alias server -keyalg RSA -keystore server.keystore -validity 365
rem
rem
rem I standalone.xml
{code}
            <security-realm name="ApplicationRealm">
...
                <server-identities>
                    <ssl>
                        <keystore path="server.keystore" relative-to="jboss.server.config.dir" keystore-password="keystore_password" alias="server" key-password="key_password" />
                    </ssl>
                </server-identities>
..    .            
                <authentication>

{code}