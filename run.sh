#-Xdebug -Xrunjdwp:transport=dt_socket,address=8000,server=y,suspend=n
java -jar jetty-runner-9.2.11.v20150529.jar --port 9010 target/DevoteeManagement-1.0.war
