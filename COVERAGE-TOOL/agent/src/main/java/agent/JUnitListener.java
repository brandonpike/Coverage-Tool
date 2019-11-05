package agent;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import org.junit.runner.Description;
import org.junit.runner.Result;
import org.junit.runner.notification.RunListener;
import it.unimi.dsi.fastutil.ints.IntSet;

public class JUnitListener extends RunListener {

	// @init
	public void testRunStarted(Description description) throws Exception {
		if (CoverageBank.test_Coverages == null) {
			CoverageBank.test_Coverages = new HashMap<String, HashMap<String, IntSet>>();
		}
	}

	// @Before
	public void testStarted(Description description) {
		CoverageBank.testName = "[TEST] " + description.getClassName() + ":" + description.getMethodName();
		CoverageBank.coverage = new HashMap<String, IntSet>();
	}

	// @After
	public void testFinished(Description description) {
		CoverageBank.test_Coverages.put(CoverageBank.testName, CoverageBank.coverage);
	}

	// @Finished
	public void testRunFinished(Result result) throws IOException {
	        File fout = new File("../stmt-cov.txt");
	        FileOutputStream fos = new FileOutputStream(fout);
	        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(fos));
			HashSet<String> test_bank = new HashSet<String>();
	        for (String testName : CoverageBank.test_Coverages.keySet()) {
	        	test_bank.add(testName + "\n");
	        	HashMap<String, IntSet> caseCoverage = CoverageBank.test_Coverages.get(testName);
	            for (String className : caseCoverage.keySet()) {
	            	int[] lines = caseCoverage.get(className).toIntArray();
	            	Arrays.sort(lines);
	            	for (int i = 0; i < lines.length; i++) {
	                	test_bank.add(className + ":" + lines[i] + "\n");
					}
	            }
	        }
			for(String s : test_bank)
				bw.write(s);
	        bw.close();
	}
}
