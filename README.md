jtestsuiteplugin
================

Annotations to extends JUnit Tests
----------------------------------

Update Site URL for eclipse plugin: 
 <a href="jar:http://dl.bintray.com/funthomas424242/eclipse-features/jtestsuiteplugin.updatesite-1.0.0-SNAPSHOT.zip!/">
 jar:http://dl.bintray.com/funthomas424242/eclipse-features/jtestsuiteplugin.updatesite-1.0.0-SNAPSHOT.zip!/</a>

Download URL for the annotation jar:
<a href="http://dl.bintray.com/funthomas424242/eclipse-features/#jtestsuiteplugin.annotations-1.0.0-SNAPSHOT.jar">
http://dl.bintray.com/funthomas424242/eclipse-features/#jtestsuiteplugin.annotations-1.0.0-SNAPSHOT.jar</a>

Example
-------

<code>
package test;

import gh.funthomas424242.junitsupport.annotations.TestSuite;
 
public class TestSuiteDefinition {
     
	@TestSuite(packageName="tests", className="IntenTestSuite", categories={"Integration", "kategorie1"})
	public void doIntegrationTests(){
		    
	}  
	  
	@TestSuite(packageName="testsuites", className="ModulTestSuite", categories={"Modul"})
	public void doModulTests(){
		
	}
	
}
</code>


