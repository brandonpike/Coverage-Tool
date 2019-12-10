package agent;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import org.junit.runner.Description;
import org.junit.runner.Result;
import org.junit.runner.notification.RunListener;
import it.unimi.dsi.fastutil.ints.IntSet;
import java.util.*;

public class JUnitListener extends RunListener {
	
	public static double timeSpent = 0;
	HashMap<String[], LinkedHashMap<Integer,String>> gV;
	

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
		gV = MethodTransformVisitor.gatheredVariables;
		
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
			
		// invariants.txt
		fout = new File("../invariants.txt");
	    fos = new FileOutputStream(fout);
	    bw = new BufferedWriter(new OutputStreamWriter(fos));
		HashMap<String[], LinkedHashSet<String>> invariants = new HashMap<String[], LinkedHashSet<String>>();
		for(String[] ks : gV.keySet()){  // for all class/methods
			// for this class/methods
			LinkedHashMap<Integer,String> names = gV.get(ks);
			LinkedHashMap<Integer,String> values = getVarsFromMap(ks);
			LinkedHashSet<String> invars = new LinkedHashSet<String>();
			if(values == null) continue;
			for(Integer key : values.keySet()){ // for all variable values
				// for this variables values
				String s = values.get(key);
				System.out.println(names.get(key) + " : " + s);
				String [] ss = s.split(",");
				int confidence = Math.min(7, ss.length);
				String sConstantV = "~";
				int constantV = -1,
					constantCount = 0,
					nconstantCount = 0,
					sConstantCount = 0,
					valueSetCount = 0,
					nvalueSetCount = 0,
					sValueSetCount = 0,
					nonZeroCount = 0,
					nNonZeroCount = 0,
					modulusCount = 0,
					nmodulusCount = 0;
				int intmin = Integer.MAX_VALUE,
					intmax = Integer.MIN_VALUE,
					ma = -1, mb = -1,
					nma = -1, nmb = -1;
				double nconstantV = -1,
						nintmin = Double.MAX_VALUE,
						nintmax = Double.MIN_VALUE;
				boolean constant = true,
						nconstant = true,
						sConstant = true,
						nonZero = true,
						nNonZero = true,
						modulus = true,
						nmodulus = true;
				LinkedHashSet<Integer> valueSet = new LinkedHashSet<Integer>();
				LinkedHashSet<Double> nvalueSet = new LinkedHashSet<Double>();
				LinkedHashSet<String> sValueSet = new LinkedHashSet<String>();
				for(String so : ss){ // for every value
					if(Character.isDigit(so.charAt(0))){
						// for this value
						if(!so.contains(".")){
							// intConstants
							int x = Integer.parseInt(so);
							if(constant)
								if(constantV == -1){
									constantV = x;
									constantCount++;
								}else if(constantV != x)
									constant = false;
								else{
									constantCount++;
								}
							// intValue set
							if(!valueSet.contains(x))
								valueSet.add(x);
							valueSetCount++;
							// intRanges
							if(x < intmin)
								intmin = x;
							if(x > intmax)
								intmax = x;
							// intNon-zero
							if(x == 0)
								nonZero = false;
							else
								nonZeroCount++;
							// intModulus
							if(modulus && x == (ma%mb))
								modulusCount++;
							else if(modulus)
								for(int g = 3; g <= 10; g++){
									for(int h = 2; h < g; h++){
										if(x == (g%h)){
											ma=g;
											mb=h;
											modulusCount++;
											break;
										}
									}
									if(g == 10)
										modulus = false;
								}
						}else{
							// nonintConstants
							Double x = Double.parseDouble(so);
							if(nconstant)
								if(nconstantV == -1){
									nconstantV = x;
									nconstantCount++;
								}else if(nconstantV != x)
									nconstant = false;
								else{
									nconstantCount++;
								}
							// nonintValue set
							if(!nvalueSet.contains(x))
								nvalueSet.add(x);
							nvalueSetCount++;
							// intRanges
							if(x < nintmin)
								nintmin = x;
							if(x > nintmax)
								nintmax = x;
							// intNon-zero
							if(x == 0)
								nNonZero = false;
							else
								nNonZeroCount++;
							// intModulus
							if(nmodulus && x == (nma%nmb))
								nmodulusCount++;
							else if(nmodulus)
								for(int g = 3; g <= 10; g++){
									for(int h = 2; h < g; h++){
										if(x == (g%h)){
											nma=g;
											nmb=h;
											nmodulusCount++;
											break;
										}
									}
									if(g == 10)
										nmodulus = false;
								}
						}
					}else{
						// StringConstants
						int x = Integer.parseInt(so);
						if(sConstant)
							if(sConstantV.equals("~")){
								sConstantV = so;
								sConstantCount++;
							}else if(!sConstantV.equals(so))
								sConstant = false;
							else{
								sConstantCount++;
							}
						// StringValue set
						if(!sValueSet.contains(so))
							sValueSet.add(so);
						sValueSetCount++;
					}
				}
				System.out.println(valueSetCount + " | " + confidence);
				// Ints
				// Constants
				if(constant && constantCount >= confidence)
					invars.add(names.get(key) + " = " + constantV);
				// Value set
				if(!constant && valueSetCount >= confidence){
					String adding = "{";
					for(Integer x : valueSet)
						adding += (x.toString() + ",");
					adding = adding.substring(0, adding.length()-1) + "}";
					invars.add(names.get(key) + " = " + adding);
				}
				// Ranges
				if(!constant && valueSetCount >= confidence){
					invars.add(names.get(key) + " >= " + intmin);
					invars.add(names.get(key) + " <= " + intmax);
				}
				// Non-zero
				if(nonZero && nonZeroCount >= confidence)
					invars.add(names.get(key) + " != 0");
				// Modulus
				if(modulus && modulusCount >= confidence)
					invars.add(names.get(key) + " = " + ma + " mod " + mb);
				else {}
				// Nonints
				// Constants
				if(nconstant && nconstantCount >= confidence)
					invars.add(names.get(key) + " = " + nconstantV);
				// Value set
				if(!nconstant && nvalueSetCount >= confidence){
					String adding = "{";
					for(Double x : nvalueSet)
						adding += (x.toString() + ",");
					adding = adding.substring(0, adding.length()-1) + "}";
					invars.add(names.get(key) + " = " + adding);
				}
				// Ranges
				if(!nconstant && nvalueSetCount >= confidence){
					invars.add(names.get(key) + " >= " + nintmin);
					invars.add(names.get(key) + " <= " + nintmax);
				}
				// Non-zero
				if(nNonZero && nNonZeroCount >= confidence)
					invars.add(names.get(key) + " != 0");
				// Modulus
				if(nmodulus && nmodulusCount >= confidence)
					invars.add(names.get(key) + " = " + nma + " mod " + nmb);
				else {}
				// Strings
				// Constants
				if(sConstant && sConstantCount >= confidence)
					invars.add(names.get(key) + " = " + sConstantV);
				// Value set
				if(!sConstant && sValueSetCount >= confidence){
					String adding = "{";
					for(String x : sValueSet)
						adding += (x + ",");
					adding = adding.substring(0, adding.length()-1) + "}";
					invars.add(names.get(key) + " = " + adding);
				}
			}
			for(String test : invars)
				System.out.println(test);
			invariants.put(ks, invars);
		}
		bank = new LinkedHashSet<String>();
		int line = 1;
		for(String[] ks : invariants.keySet()){
			bank.add((line++) + ". [" + ks[0] + ", " + ks[1] + "]\n");
			for(String s : invariants.get(ks))
				bank.add((line++) + ". " + s + "\n");
		}
		for(String s : bank)
			bw.write(s);
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
	
	private static LinkedHashMap<Integer, String> getVarsFromMap(String[] key){
		for(String[] ks : CoverageBank.varValues.keySet()){
			if(ks[0].equals(key[0]) && ks[1].equals(key[1]))
				return CoverageBank.varValues.get(ks);
		}
		return null;
	}
}