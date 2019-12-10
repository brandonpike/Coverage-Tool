package agent;

import java.util.*;
import it.unimi.dsi.fastutil.ints.IntSet;
import it.unimi.dsi.fastutil.ints.IntOpenHashSet;

public class CoverageBank {
	
	public static String testName;
	public static HashMap<String, IntSet> coverage;
	public static HashMap<String, HashMap<String, IntSet>> test_Coverages;
	// # statistics #
	   // {className, line#}
	public static HashMap<String, IntSet> gatheredStatements = new HashMap<String, IntSet>();
	protected static HashMap<String[], LinkedHashMap<Integer,String>> varValues = new HashMap<String[], LinkedHashMap<Integer,String>>();

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
	
	public static void printThis(int x){
		System.out.println("[[[[ " + x + " ]]]]");
    }
	
	public static void printThis(String cl, String mthd, Integer x){
		System.out.println("[ " + mthd + " ][ " + x + " ]");
    }
	
	public static void trace(String cl, String mthd, Integer index, Boolean x){
		//System.out.println("Traced Boolean");
		String k[] = {cl, mthd};
		LinkedHashMap<Integer,String> vars = getVarsFromMap(k);
		String var = (Boolean.toString(x) + ",");
		if(vars != null){
			String cur = vars.get(index) + var;
			int i = cur.indexOf("null");
			while(i != -1 && Character.toLowerCase(cur.charAt(0)) == 'n') {
				cur = cur.substring(i+4);
				i = cur.indexOf("null");
			}
			vars.put(index, cur);
		}else{
			vars=new LinkedHashMap<Integer,String>();
			vars.put(index, var);
			varValues.put(k, vars);
		}
		for(Integer key : vars.keySet())
			System.out.println(vars.get(key));
	}
	
	public static void trace(String cl, String mthd, Integer index, Integer x){
		//System.out.println("Traced Integer");
		String k[] = {cl, mthd};
		LinkedHashMap<Integer,String> vars = getVarsFromMap(k);
		String var = (Integer.toString(x) + ",");
		if(vars != null){
			String cur = vars.get(index) + var;
			int i = cur.indexOf("null");
			while(i != -1 && Character.toLowerCase(cur.charAt(0)) == 'n') {
				cur = cur.substring(i+4);
				i = cur.indexOf("null");
			}
			vars.put(index, cur);
		}else{
			vars=new LinkedHashMap<Integer,String>();
			vars.put(index, var);
			varValues.put(k, vars);
		}
	}
	
	public static void trace(String cl, String mthd, Integer index, Float x){
		//System.out.println("Traced Float");
		String k[] = {cl, mthd};
		LinkedHashMap<Integer,String> vars = getVarsFromMap(k);
		String var = (Float.toString(x) + ",");
		if(vars != null){
			String cur = vars.get(index) + var;
			int i = cur.indexOf("null");
			while(i != -1 && Character.toLowerCase(cur.charAt(0)) == 'n') {
				cur = cur.substring(i+4);
				i = cur.indexOf("null");
			}
			vars.put(index, cur);
		}else{
			vars=new LinkedHashMap<Integer,String>();
			vars.put(index, var);
			varValues.put(k, vars);
		}
	}
	
	public static void trace(String cl, String mthd, Integer index, Long x){
		//System.out.println("Traced Long");
		String k[] = {cl, mthd};
		LinkedHashMap<Integer,String> vars = getVarsFromMap(k);
		String var = (Long.toString(x) + ",");
		if(vars != null){
			String cur = vars.get(index) + var;
			int i = cur.indexOf("null");
			while(i != -1 && Character.toLowerCase(cur.charAt(0)) == 'n') {
				cur = cur.substring(i+4);
				i = cur.indexOf("null");
			}
			vars.put(index, cur);
		}else{
			vars=new LinkedHashMap<Integer,String>();
			vars.put(index, var);
			varValues.put(k, vars);
		}
		for(Integer key : vars.keySet())
			System.out.println(vars.get(key));
	}
	
	public static void trace(String cl, String mthd, Integer index, Double x){
		//System.out.println("Traced Double");
		String k[] = {cl, mthd};
		LinkedHashMap<Integer,String> vars = getVarsFromMap(k);
		String var = (Double.toString(x) + ",");
		if(vars != null){
			String cur = vars.get(index) + var;
			int i = cur.indexOf("null");
			while(i != -1 && Character.toLowerCase(cur.charAt(0)) == 'n') {
				cur = cur.substring(i+4);
				i = cur.indexOf("null");
			}
			vars.put(index, cur);
		}else{
			vars=new LinkedHashMap<Integer,String>();
			vars.put(index, var);
			varValues.put(k, vars);
		}
		for(Integer key : vars.keySet())
			System.out.println(vars.get(key));
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
	
	private static LinkedHashMap<Integer, String> getVarsFromMap(String[] key){
		for(String[] ks : varValues.keySet()){
			if(ks[0].equals(key[0]) && ks[1].equals(key[1]))
				return varValues.get(ks);
		}
		return null;
	}
}
