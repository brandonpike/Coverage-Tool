# Coverage-Tool

# Project Description
Use ASM byte-code manipulation framework to build an automated coverage collection tool
that can capture the statement coverage for the program under test. Then, apply your tool to 10
real-world Java projects (>1000 lines of code) with JUnit tests (>50 tests) from GitHub to
collect the statement coverage for its JUnit tests. Note that your tool should (1) use Java Agent to perform on-the-fly code instrumentation, (2) be able to store the coverage for each test
method in the file system, and (3) be integrated with the Maven build system so that your tool
can be triggered by simply typing “mvn test” after changing the pom.xml file of the project
under test. More implementation details are shown in the appendix. 

# Changing project under test
Change the module name in (COVERAGE-TOOL/pom.xml : line 19) from test-bed to the folder name. Be sure to add the following
to the new projects pom.xml

# Pom adjustments for new projects
```  
  <!--  [ <Pike edit> ] -->
  <parent>
    <groupId>asm-sample</groupId>
    <artifactId>parent</artifactId>
    <version>0.1-SNAPSHOT</version>
  </parent>
  <artifactId>test-bed</artifactId>
  <version>0.1-SNAPSHOT</version>
  <packaging>jar</packaging>
  <!--  [ </Pike edit> ] -->
  <build>
    <pluginManagement>
      <plugins>
	      <plugin>
		        <!--  [ <Pike edit> ] -->
            <groupId>org.apache.maven.plugins</groupId>
            <artifactId>maven-dependency-plugin</artifactId>
            <version>2.3</version>
            <executions>
                <execution>
                    <goals>
                        <goal>properties</goal>
                    </goals>
                </execution>
            </executions>
		      <!--  [ </Pike edit> ] -->
    	  </plugin>
      </plugins>
    </pluginManagement>
    <plugins>
      <plugin>
        <groupId>org.apache.maven.plugins</groupId>
        <artifactId>maven-surefire-plugin</artifactId>
		<version>2.19.1</version>
        <configuration>
          <excludes>
            <exclude>**/BaseTestCase.java</exclude>
          </excludes>
		      <!--  [ <Pike edit> ] -->
          <argLine>-javaagent:../agent/target/agent-0.1-SNAPSHOT.jar</argLine>
          <properties>
            <property>
              <name>listener</name>
			  <value>agent.JUnitListener</value>
            </property>
          </properties>
		      <!--  [ </Pike edit> ] -->
        </configuration>
      </plugin>```
