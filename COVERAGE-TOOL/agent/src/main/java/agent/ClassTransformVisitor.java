package agent;

import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.Opcodes;

public class ClassTransformVisitor extends ClassVisitor implements Opcodes {
    protected String className;

    public ClassTransformVisitor(final ClassVisitor cv) {
        super(ASM5, cv);
    }

    @Override
    public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
        this.className = name;
        super.visit(version, access, name, signature, superName, interfaces);
    }
    
    @Override
    public MethodVisitor visitMethod(final int access, final String name, final String desc, final String signature, final String[] exceptions) {
        MethodVisitor mv = cv.visitMethod(access, name, desc, signature, exceptions);
		//System.out.println(" [Method] " + name + ": " + access + " | " + desc);
		if(desc.equals("(J)V")){
			//mv.visitLocalVariable(name, "J");
		}
		return mv == null ? null : new MethodTransformVisitor(mv, className, name, desc);
    }
	
	@Override
	public FieldVisitor visitField(int access, String name, String desc, String signature, Object value){
		FieldVisitor fv = cv.visitField(access, name, desc, signature, value);
		//System.out.println("VISITFIELD");
		//System.out.println(" [name] " + name + " [desc] " + desc + " [val] " + value + );
		return fv == null ? null : fv;
	}
}