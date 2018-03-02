alias: kmvp
password: 123456


签名MD5： afc1f34733dcfe0e4f3486710ea175c2

keytool -exportcert -alias kmvp -keypass 123456 -keystore release_key.jks -storepass 123456 | md5sum
