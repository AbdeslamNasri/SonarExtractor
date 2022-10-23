
## Usage :

If you don't want to build from source, go to folder "lib" and jump to step 4. 

1. Build your project using maven or your preferred editor (skip tests if you don't feed the tests JSON strings correctly),
   
2. Copy the JAR file  sonar-report-extractor-1.0.0.jar generated in the "target" folder into the "lib" folder,
4. Run the following commandline 
    - &lt;sonar-url&gt; : your Sonar server, e.g.; http://localhost:9000
    - &lt;login&gt; : your Sonar account login,
    - &lt;password&gt; : your Sonar account password
  
java -cp "sonar-report-extractor-1.0.0.jar;commons-codec-1.10.jar;commons-collections4-4.1.jar;poi-3.16.jar;sonar-ws-client-4.5-RC3.jar;commons-httpclient-3.1.jar;commons-logging-1.2.jar" org.sonar.tools.SonarExtract &lt;sonar-url&gt; &lt;login&gt; &lt;password&gt;



