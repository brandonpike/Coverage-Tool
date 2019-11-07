package agent;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.LinkedHashSet;
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
		//System.out.println(CoverageBank.testName);
		CoverageBank.coverage = new HashMap<String, IntSet>();
	}

	// @After
	public void testFinished(Description description) {
		//System.out.println(" " + CoverageBank.testName + " | " + CoverageBank.coverage);
		CoverageBank.test_Coverages.put(CoverageBank.testName, CoverageBank.coverage);
		//System.out.println(CoverageBank.test_Coverages);
	}

	// @Finished
	public void testRunFinished(Result result) throws IOException {
		
			// stmt-statistics.txt
	        File fout = new File("../stmt-statistics.txt");
	        FileOutputStream fos = new FileOutputStream(fout);
	        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(fos));
			double sumUsed = 0,
				sumTotal = 0;
			Set<String> bank = new LinkedHashSet<String>();
			for(String cl : MethodTransformVisitor.totalGatheredStatements.keySet()){
				double usedSize = getUsedSize(cl),
					totalSize = getTotalSize(cl);
				bank.add(cl + " : " + Double.toString(usedSize) + "/" + Double.toString(totalSize) + " (" + Double.toString((usedSize/totalSize)*100) + "%)\n");
				sumUsed += usedSize;
				sumTotal += totalSize;
			}
			bw.write("TOTAL COVERAGE = " + sumUsed + "/" + sumTotal + " (" + (sumUsed/sumTotal)*100 + "%)\n");
			for(String s : bank)
				bw.write(s);
			bw.close();
			
			// stmt-cov.txt
			fout = new File("../stmt-cov.txt");
	        fos = new FileOutputStream(fout);
	        bw = new BufferedWriter(new OutputStreamWriter(fos));
	        for (String testName : CoverageBank.test_Coverages.keySet()) {
				bw.write(testName + "\n");
	        	HashMap<String, IntSet> caseCoverage = CoverageBank.test_Coverages.get(testName);
	            for (String className : caseCoverage.keySet()) {
	            	int[] lines = caseCoverage.get(className).toIntArray();
	            	Arrays.sort(lines);
	            	for (int i = 0; i < lines.length; i++) {
						bw.write(className + ":" + lines[i] + "\n");
					}
	            }
	        }
	        bw.close();
	}
	
	public double getTotalSize(String className){
		double totalSize = (double)MethodTransformVisitor.totalGatheredStatements.get(className).size();
		return totalSize;
	}
	
	public double getUsedSize(String className){
		double usedSize = 0.0;
		IntSet lines = CoverageBank.gatheredStatements.get(className);
		if (lines != null)
			usedSize = (double)lines.size();
		return usedSize;
	}
	
	public double getCoverage(String className) {
		IntSet lines = CoverageBank.gatheredStatements.get(className);
		double totalSize = (double)MethodTransformVisitor.totalGatheredStatements.get(className).size(),
			usedSize = 0.0;
		if (lines != null)
			usedSize = (double)lines.size();
		return (usedSize/totalSize) * 100;
	}
}