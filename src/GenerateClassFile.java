import java.io.FileOutputStream;

import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

public class GenerateClassFile implements Opcodes {
	public static void main(final String args[]) throws Exception {
		// generate the class
		ClassWriter cw = new ClassWriter(0);
		MethodVisitor mv;
		cw.visit(V1_7, ACC_PUBLIC + ACC_SUPER, "Generated", null, "java/lang/Object", null);
		// generate the constructor
		mv = cw.visitMethod(ACC_PUBLIC, "<init>", "()V", null, null);
		mv.visitCode();
		mv.visitVarInsn(ALOAD, 0);
		mv.visitMethodInsn(INVOKESPECIAL, "java/lang/Object", "<init>", "()V", false);
		mv.visitInsn(RETURN);
		mv.visitMaxs(1, 1);
		mv.visitEnd();
		// generate the main method
		mv = cw.visitMethod(ACC_PUBLIC + ACC_STATIC, "main", "([Ljava/lang/String;)V", null, null);
		mv.visitCode();
		mv.visitFieldInsn(GETSTATIC, "java/lang/System", "out", "Ljava/io/PrintStream;");
		mv.visitLdcInsn("I'm generated...");
		mv.visitMethodInsn(INVOKEVIRTUAL, "java/io/PrintStream", "println", "(Ljava/lang/String;)V", false);
		mv.visitInsn(RETURN);
		mv.visitMaxs(2, 1);
		mv.visitEnd();
		// output to disk
		cw.visitEnd();
		FileOutputStream fos = new FileOutputStream(args[0]);
		fos.write(cw.toByteArray());
		fos.close();
	}
}
