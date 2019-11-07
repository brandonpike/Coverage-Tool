package agent;

import java.util.HashMap;
import it.unimi.dsi.fastutil.ints.IntSet;
import it.unimi.dsi.fastutil.ints.IntOpenHashSet;

public class CoverageBank {
	
	public static String testName;
	public static HashMap<String, IntSet> coverage;
	public static HashMap<String, HashMap<String, IntSet>> test_Coverages;
	// # statistics #
	   // {className, line#}
	public static HashMap<String, IntSet> gatheredStatements = new HashMap<String, IntSet>();

    public static void addMethodLine(String className, Integer line){
		//System.out.println(className + " : " + Integer.toString(line));
		passLine(className, line);
    	if (coverage == null)
    		return;
    	IntSet lines = coverage.get(className);
        if (lines != null){
        	lines.add(line);
			//System.out.println(" " + lines);
        }else {
        	lines = new IntOpenHashSet(new int[]{line});
            coverage.put(className, lines);
			//System.out.println(" " + lines);
        }
    }
	
	public static void passLine(String className, Integer line){
		IntSet lines = gatheredStatements.get(className);
        if (lines != null)
        	lines.add(line);
        else {
        	lines = new IntOpenHashSet(new int[]{line});
            gatheredStatements.put(className, lines);
        }
	}
}
