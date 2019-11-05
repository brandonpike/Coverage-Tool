package agent;

import java.util.HashMap;
import it.unimi.dsi.fastutil.ints.IntSet;
import it.unimi.dsi.fastutil.ints.IntOpenHashSet;

public class CoverageBank {
	
	public static String testName;
	public static HashMap<String, IntSet> coverage;
	public static HashMap<String, HashMap<String, IntSet>> test_Coverages;

    public static void addMethodLine(String className, Integer line){
    	if (coverage == null)
    		return;
    	IntSet lines = coverage.get(className);
        if (lines != null)
        	lines.add(line);
        else {
        	lines = new IntOpenHashSet(new int[]{line});
            coverage.put(className, lines);
        }
    }
}
