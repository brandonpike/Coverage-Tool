package agent;

import it.unimi.dsi.fastutil.ints.IntSet;
import it.unimi.dsi.fastutil.ints.IntOpenHashSet;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;

public class CoverageCollector {
	public static Object2ObjectOpenHashMap<String, Object2ObjectOpenHashMap<String, IntSet>> testCaseCoverages;
	public static Object2ObjectOpenHashMap<String, IntSet> testCaseCoverage;
	public static String testCaseName;

    public static void addMethodLine(String className, Integer line){
    	if (testCaseCoverage == null) {
    		return;
    	}
    	
    	IntSet lines = testCaseCoverage.get(className);
        if (lines != null) {
        	lines.add(line);
        }
        else {
        	lines = new IntOpenHashSet(new int[]{line});
            testCaseCoverage.put(className, lines);
        }
    }
}
