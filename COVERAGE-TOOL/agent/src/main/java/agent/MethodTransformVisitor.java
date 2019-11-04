package agent;

import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

public class MethodTransformVisitor extends MethodVisitor implements Opcodes {

	protected int lastVisitedLine;
	protected String className;
	
    public MethodTransformVisitor(final MethodVisitor mv, String className) {
        super(ASM5, mv);
        this.className=className;
    }
    
	@Override
	public void visitLineNumber(int line, Label start) {
		if (line != 0) {
	    	lastVisitedLine = line;
	    	
			mv.visitLdcInsn(className);
			mv.visitLdcInsn(new Integer(line));
			mv.visitMethodInsn(INVOKESTATIC, "java/lang/Integer", "valueOf", "(I)Ljava/lang/Integer;", false);
			mv.visitMethodInsn(INVOKESTATIC, "agent/CoverageCollector", "addMethodLine", "(Ljava/lang/String;Ljava/lang/Integer;)V", false);
	}
		
    	super.visitLineNumber(line, start);
	}
	
	@Override
	public void visitLabel(Label label) {
		if (0 != lastVisitedLine) {
			mv.visitLdcInsn(className);
			mv.visitLdcInsn(new Integer(lastVisitedLine));
			mv.visitMethodInsn(INVOKESTATIC, "java/lang/Integer", "valueOf", "(I)Ljava/lang/Integer;", false);
			mv.visitMethodInsn(INVOKESTATIC, "agent/CoverageCollector", "addMethodLine", "(Ljava/lang/String;Ljava/lang/Integer;)V", false);
		}

    	super.visitLabel(label);
	}
}