package agent;

import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import java.util.HashMap;
import it.unimi.dsi.fastutil.ints.IntSet;
import it.unimi.dsi.fastutil.ints.IntOpenHashSet;
import java.util.LinkedHashSet;

public class MethodTransformVisitor extends MethodVisitor implements Opcodes {

	protected int lastVisitedLine;
	protected String className,
					curMethod;
	protected static HashMap<String, IntSet> totalGatheredStatements = new HashMap<String, IntSet>();
	//protected static HashMap<String, HashMap<String, StringSet>> gatheredVariables = new HashMap<String, HashMap<String, StringSet>>();
	protected static HashMap<String[], LinkedHashSet<String>> gatheredVariables = new HashMap<String[], LinkedHashSet<String>>();

	
    public MethodTransformVisitor(final MethodVisitor mv, String className, String methodName) {
        super(ASM5, mv);
        this.className=className;
		this.curMethod=methodName;
    }
    
	@Override
	public void visitLocalVariable(String name,String desc,String signature,Label start,Label end,int index){
		if(!curMethod.equals("<init>") && !name.equals("this")){
			//System.out.println(" " + curMethod + " ~ [Var] " + name + " | " + desc + " | " + signature + " | " + " (" + index +")");
			String[] k = {className, curMethod};
			LinkedHashSet<String> vars = getVarsFromMap(k);
			if(vars != null){
				vars.add(name);
			}else{
				vars=new LinkedHashSet<String>();
				vars.add(name);
				gatheredVariables.put(k, vars);
			}
			/*System.out.println("{");
			for(String[] ks : gatheredVariables.keySet()){
				System.out.println("[" + gatheredVariables.get(ks).size() + " | " + ks[0] + ", " + ks[1]+"]");
				for(String s : gatheredVariables.get(ks))
					System.out.print(" " + s);
				System.out.print("\n");
			}
			System.out.println("}" + gatheredVariables.size());*/
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
			//mv.visitFieldInsn(INVOKESTATIC, "agent/CoverageBank", add, String desc)
		}
		//System.out.print(start.getOffset() + " ");
    	super.visitLineNumber(line, start);
	}
	
	@Override
	public void visitLabel(Label label) {
		if (lastVisitedLine != 0) {
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
	
	private LinkedHashSet<String> getVarsFromMap(String[] key){
		for(String[] ks : gatheredVariables.keySet()){
			if(ks[0].equals(key[0]) && ks[1].equals(key[1]))
				return gatheredVariables.get(ks);
		}
		return null;
	}
}