package agent;

import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import java.util.HashMap;
import it.unimi.dsi.fastutil.ints.IntSet;
import it.unimi.dsi.fastutil.ints.IntOpenHashSet;

public class MethodTransformVisitor extends MethodVisitor implements Opcodes {

	protected int lastVisitedLine;
	protected String className;
	protected static HashMap<String, IntSet> totalGatheredStatements = new HashMap<String, IntSet>();
	
    public MethodTransformVisitor(final MethodVisitor mv, String className) {
        super(ASM5, mv);
        this.className=className;
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
	
	
}