package agent;

import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import java.util.*;
import it.unimi.dsi.fastutil.ints.IntSet;
import it.unimi.dsi.fastutil.ints.IntOpenHashSet;

public class MethodTransformVisitor extends MethodVisitor implements Opcodes {

	protected int lastVisitedLine;
	protected String className,
					curMethod;
	protected ArrayList<String> params;
	protected static HashMap<String, IntSet> totalGatheredStatements = new HashMap<String, IntSet>();
	//protected static HashMap<String, HashMap<String, StringSet>> gatheredVariables = new HashMap<String, HashMap<String, StringSet>>();
	protected static HashMap<String[], LinkedHashMap<Integer,String>> gatheredVariables = new HashMap<String[], LinkedHashMap<Integer,String>>();

	
    public MethodTransformVisitor(final MethodVisitor mv, String className, String methodName, String desc) {
        super(ASM5, mv);
        this.className=className;
		this.curMethod=methodName;
		this.params = getParams(desc);
    }
    
	@Override
	public void visitVarInsn(int opcode, int var) {
		//System.out.println("VISITVARINSN");
		//System.out.println(" [opcode] " + opcode + ", [int var?] " + var);
		super.visitVarInsn(opcode, var);
	}
	
	@Override
	public void visitCode() {
		String[] k = {className, curMethod};
		int i = 1;
		if(!curMethod.equals("<init>") && !curMethod.equals("createCaseInsensitiveHashMap")){// || curMethod.equals("add")){
			if(params != null){
				int index = 1;
				for(String s : params){
					//System.out.println("[type ("+curMethod+")] " + s);
					// z c b s i f j d
					if(s.equals("I") || s.equals("F") || s.equals("J") || s.equals("D")){ //s.equals("Z")) || s.equals("C") || s.equals("B") || s.equals("S") || s.equals("I") || s.equals("F") || s.equals("J") || s.equals("D")){
						//System.out.println("PASS A");
						mv.visitLdcInsn(className);
						mv.visitLdcInsn(curMethod);
						mv.visitLdcInsn(new Integer(index));
						mv.visitMethodInsn(INVOKESTATIC, "java/lang/Integer", "valueOf", "(I)Ljava/lang/Integer;", false);
						if(s.equals("Z")){//Boolean
							mv.visitMethodInsn(INVOKESTATIC, "java/lang/Boolean", "valueOf", "(Z)Ljava/lang/Boolean;", false);
							mv.visitMethodInsn(INVOKESTATIC, "agent/CoverageBank", "trace", "(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Integer;Ljava/lang/Boolean;)V", false);
						}else if(s.equals("C")){//Char
							mv.visitMethodInsn(INVOKESTATIC, "java/lang/Character", "valueOf", "(C)Ljava/lang/Character;", false);
							mv.visitMethodInsn(INVOKESTATIC, "agent/CoverageBank", "trace", "(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Integer;Ljava/lang/Character;)V", false);
						}else if(s.equals("B")){//Byte
							mv.visitMethodInsn(INVOKESTATIC, "java/lang/Byte", "valueOf", "(B)Ljava/lang/Byte;", false);
							mv.visitMethodInsn(INVOKESTATIC, "agent/CoverageBank", "trace", "(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Integer;Ljava/lang/Byte;)V", false);
						}else if(s.equals("S")){//Short
							mv.visitMethodInsn(INVOKESTATIC, "java/lang/Short", "valueOf", "(S)Ljava/lang/Short;", false);
							mv.visitMethodInsn(INVOKESTATIC, "agent/CoverageBank", "trace", "(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Integer;Ljava/lang/Short;)V", false);
						}else if(s.equals("I")){//Integer
							mv.visitVarInsn(Opcodes.ILOAD, new Integer(index));
							mv.visitMethodInsn(INVOKESTATIC, "java/lang/Integer", "valueOf", "(I)Ljava/lang/Integer;", false);
							mv.visitMethodInsn(INVOKESTATIC, "agent/CoverageBank", "trace", "(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Integer;Ljava/lang/Integer;)V", false);
						}else if(s.equals("F")){//Float
							mv.visitVarInsn(Opcodes.FLOAD, new Integer(index));
							mv.visitMethodInsn(INVOKESTATIC, "java/lang/Float", "valueOf", "(F)Ljava/lang/Float;", false);
							mv.visitMethodInsn(INVOKESTATIC, "agent/CoverageBank", "trace", "(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Integer;Ljava/lang/Float;)V", false);
						}else if(s.equals("J")){//Long
							mv.visitVarInsn(Opcodes.LLOAD, new Integer(index++));
							mv.visitMethodInsn(INVOKESTATIC, "java/lang/Long", "valueOf", "(J)Ljava/lang/Long;", false);
							mv.visitMethodInsn(INVOKESTATIC, "agent/CoverageBank", "trace", "(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Integer;Ljava/lang/Long;)V", false);
						}else if(s.equals("D")){//Double
							mv.visitVarInsn(Opcodes.DLOAD, new Integer(index));
							mv.visitMethodInsn(INVOKESTATIC, "java/lang/Double", "valueOf", "(D)Ljava/lang/Double;", false);
							mv.visitMethodInsn(INVOKESTATIC, "agent/CoverageBank", "trace", "(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Integer;Ljava/lang/Double;)V", false);
						}else {} // arrays and objects
					}
					index++;
				}
			}
		}
		super.visitCode();
	}
	
	@Override
	public void visitFrame(int type, int nLocal, Object[] local, int nStack, Object[] stack){
		//System.out.println("VISITFRAME");
		//for(Object x : local)
			//System.out.println(x);
		super.visitFrame(type, nLocal, local, nStack, stack);
	}
	
	@Override
	public void visitLocalVariable(String name,String desc,String signature,Label start,Label end,int index){
		if(!curMethod.equals("<init>") && !name.equals("this")){
			//System.out.println(" " + curMethod + " ~ [Var] " + name + " | " + desc + " | " + signature + " | " + " (" + index +")");
			String[] k = {className, curMethod};
			LinkedHashMap<Integer,String> vars = getVarsFromMap(k);
			//String var = (name + ":" + index);
			if(vars != null){
				vars.put(index, name);
			}else{
				vars=new LinkedHashMap<Integer,String>();
				vars.put(index,name);
				gatheredVariables.put(k, vars);
			}
		}
		super.visitLocalVariable(name, desc, signature, start, end, index);
	}
	
	@Override
	public void visitLineNumber(int line, Label start) {
		if (line != 0) {
			//System.out.println(" (" + className + ") vLN:" + Integer.toString(line));
			addStatement(line);
	    	lastVisitedLine = line;
			mv.visitLdcInsn(className);
			mv.visitLdcInsn(new Integer(line));
			mv.visitMethodInsn(INVOKESTATIC, "java/lang/Integer", "valueOf", "(I)Ljava/lang/Integer;", false);
			mv.visitMethodInsn(INVOKESTATIC, "agent/CoverageBank", "addMethodLine", "(Ljava/lang/String;Ljava/lang/Integer;)V", false);
		}
		//System.out.print(start.getOffset() + " ");
    	super.visitLineNumber(line, start);
	}
	
	@Override
	public void visitLabel(Label label) {
		if (lastVisitedLine != 0) {
			//System.out.println(" (" + className + ") vLabel");
			mv.visitLdcInsn(className);
			mv.visitLdcInsn(new Integer(lastVisitedLine));
			mv.visitMethodInsn(INVOKESTATIC, "java/lang/Integer", "valueOf", "(I)Ljava/lang/Integer;", false);
			mv.visitMethodInsn(INVOKESTATIC, "agent/CoverageBank", "addMethodLine", "(Ljava/lang/String;Ljava/lang/Integer;)V", false);
		}
    	super.visitLabel(label);
	}
	
	private void addStatement(int line){
		IntSet lines = totalGatheredStatements.get(className);
        if (lines != null)
        	lines.add(line);
        else {
        	lines = new IntOpenHashSet(new int[]{line});
            totalGatheredStatements.put(className, lines);
        }
	}
	
	private ArrayList<String> getParams(String desc) {
		ArrayList<String> params = new ArrayList<String>();
		int index = desc.indexOf(")");
		if(index == -1) return null; // bad param format
		String s = desc.substring(1,index);
		String[] ss = s.split(";");
		for(int i=0; i<ss.length; i++){
			String row = ss[i];
			for(int j=0; j<row.length(); j++) {
				if(row.charAt(j) == '['){
					if(j+1 < row.length() && row.charAt(j+1) != 'L' && row.charAt(j+1) != '[') {
						params.add("["+row.charAt(j+1));
						j++;
				    }else{
					    params.add(row.substring(j));
					    break;
					}
				}else if(Character.isUpperCase(row.charAt(j)) && row.charAt(j) != 'L'){
					params.add(""+row.charAt(j));
				}else if(Character.isUpperCase(row.charAt(j)) && row.charAt(j) == 'L'){
					params.add(row.substring(j));
					break;
				}else{
					params.add(row.substring(j));
					break;
				}
			}
		}
		return params;
	}
	
	private String cleanParam(String param){
		int index = param.indexOf("java");
		if(index == -1) return param;
		return param.substring(index);
	}
	
	private LinkedHashMap<Integer,String> getVarsFromMap(String[] key){
		for(String[] ks : gatheredVariables.keySet()){
			if(ks[0].equals(key[0]) && ks[1].equals(key[1]))
				return gatheredVariables.get(ks);
		}
		return null;
	}
}