keytool -genkeypair -alias sampleserver -dname "cn=localhost" -keypass changeit -validity 730 -keystore sampleserver.jks -storepass changeit
keytool -genkeypair -alias sampleclient -dname "cn=StoreTest" -keypass changeit -validity 730 -keystore sampleclient.jks -storepass changeit
keytool -exportcert -alias sampleserver -file sampleserver.cer -keystore sampleserver.jks -storepass changeit
keytool -importcert -alias sampleserver -file sampleserver.cer -noprompt -keystore sampleclient.jks -storepass changeit
keytool -exportcert -alias sampleclient -file sampleclient.cer -keystore sampleclient.jks -storepass changeit
keytool -importcert -alias sampleclient -file sampleclient.cer -noprompt -keystore sampleserver.jks -storepass changeit
