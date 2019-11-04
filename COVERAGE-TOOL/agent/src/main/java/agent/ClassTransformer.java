package agent;

import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.security.ProtectionDomain;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;

public class ClassTransformer implements ClassFileTransformer {
    @Override
    public byte[] transform(ClassLoader classLoader, String s, Class<?> aClass, ProtectionDomain protectionDomain, byte[] bytes) throws IllegalClassFormatException {
        if (s.startsWith("org/apache/commons/dbutils") || 
        		s.startsWith("org/joda/time") || 
        		"other/Stuff".equals(s)) {
    		ClassReader cr = new ClassReader(bytes);
    		ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_FRAMES);
    		ClassTransformVisitor ca = new ClassTransformVisitor(cw);
    		cr.accept(ca, 0);
            return cw.toByteArray();
        }
        return bytes;
    }
}