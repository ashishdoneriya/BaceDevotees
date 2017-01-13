# BaceDevotees

1. Create Database "devoteeManagement" in mysql.
2. Set mysql username and password in <code>src/main/webapp/WEB-INF/dispatcherServlet.xml</code> (line no. 35 & 36).
3. Compile the project using
<pre>mvn clean install</pre>
4. After successful build, run command
<pre>java -jar jetty-runner-9.2.11.v20150529.jar target/DevoteeManagement-1.0.war</pre>
It will start jetty server.
5. Now open your browser and open url <code>localhost:8080</code>
6. Leave the password field empty and click on login button.
7. That's it.
NOTE: Please read https://github.com/ashishdoneriya/LittleBlueBird/wiki
