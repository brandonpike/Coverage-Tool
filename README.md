# Coverage-Tool

# Project Description
Uses the ASM byte-code manipulation framework to automate coverage collection. 10 real-world Java projects, each with more than 50 JUnit tests were used to test the tool on.
- Produces single-variable invariants
- Uses on-the-fly code instrumentation via a Java Agent
- Stores the statement coverage by each class, or test method
- Uses the Maven build system (simple command 'mvn test' to test projects)

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
