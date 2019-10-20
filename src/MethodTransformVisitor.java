

import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;


class MethodTransformVisitor extends MethodVisitor implements Opcodes {

	String mName;
	int line;
	
	// I added code from my homework because it looked incomplete
	// Unsure if you solved it a different way or used the code from the teacher
	@Override
    	public void visitLineNumber(int var1, Label var2) {
		if (this.mv != null) {
		    this.mv.visitLineNumber(var1, var2);
		}
	}

	@Override
	public void visitLabel(Label var1) {
	if (this.mv != null) {
		    this.mv.visitLabel(var1);
		}

	}
	// End of code added. Delete if unnecessary

	public MethodTransformVisitor(final MethodVisitor mv, String name) {
		super(ASM5, mv);
		this.mName=name;
	}

	// method coverage collection
	@Override
	public void visitCode(){
		mv.visitFieldInsn(GETSTATIC, "java/lang/System", "out", "Ljava/io/PrintStream;");
		mv.visitLdcInsn(mName+" executed");
		mv.visitMethodInsn(INVOKEVIRTUAL, "java/io/PrintStream", "println", "(Ljava/lang/String;)V", false);
		super.visitCode();
	}

}
