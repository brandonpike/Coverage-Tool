

import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;


class MethodTransformVisitor extends MethodVisitor implements Opcodes {

	String mName;
	int line;
	
	public MethodTransformVisitor(final MethodVisitor mv, String name) {
		super(ASM5, mv);
		this.mName=name;
	}
	
	// I added code from my homework because it looked incomplete
	// Unsure if you solved it a different way or used the code from the teacher
	// statement coverage collection
	@Override
	public void visitLabel(Label var1) {
		String lineNumber = "line " + line;
		mv.visitFieldInsn(GETSTATIC, "java/lang/System", "out", "Ljava/io/PrintStream;");
		mv.visitLdcInsn( lineNumber+ " executed");
		mv.visitMethodInsn(INVOKEVIRTUAL, "java/io/PrintStream", "println", "(Ljava/lang/String;)V", false);
		super.visitLabel(var1);
	}

	@Override
	public void visitLineNumber(int var1, Label var2) {
		String lineNumber = "line " + var1;
		mv.visitFieldInsn(GETSTATIC, "java/lang/System", "out", "Ljava/io/PrintStream;");
		mv.visitLdcInsn(lineNumber +" executed");
		mv.visitMethodInsn(INVOKEVIRTUAL, "java/io/PrintStream", "println", "(Ljava/lang/String;)V", false);
		line = var1;
		super.visitLineNumber(var1, var2);
	}
	// End of code added. Delete if unnecessary

	// method coverage collection
	@Override
	public void visitCode(){
		mv.visitFieldInsn(GETSTATIC, "java/lang/System", "out", "Ljava/io/PrintStream;");
		mv.visitLdcInsn(mName+" executed");
		mv.visitMethodInsn(INVOKEVIRTUAL, "java/io/PrintStream", "println", "(Ljava/lang/String;)V", false);
		super.visitCode();
	}

}
