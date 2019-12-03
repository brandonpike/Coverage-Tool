package agent;

import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.security.ProtectionDomain;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import java.util.HashSet;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.net.URL;
import java.net.URLClassLoader;

public class ClassTransformer implements ClassFileTransformer {
	
	public static HashSet<String> seenIt = new HashSet<String>();
	
    @Override
    public byte[] transform(ClassLoader classLoader, String s, Class<?> aClass, ProtectionDomain protectionDomain, byte[] bytes) throws IllegalClassFormatException {
        if (s.startsWith("org/apache/commons/dbutils") || s.startsWith("org/joda/time") || s.equals("other/Stuff") ||
			s.startsWith("org/LatencyUtils") || s.startsWith("org/bukkit")) {
			//if(aClass != null)
				//System.out.println("[Class] " + aClass);
			ClassReader cr = new ClassReader(bytes);
    		ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_FRAMES);
    		ClassTransformVisitor ca = new ClassTransformVisitor(cw);
    		cr.accept(ca, 0);
            bytes = cw.toByteArray();
			/*String test = "";
			try {
				test = new String(bytes, "UTF-8");
			} catch (Exception e) { System.out.print(e.getMessage()); }
			System.out.println("[Bytes] " + test);*/ 
			
			/*if (aClass.getName().equals("java.lang.Object")) {
				return bytes;
			}
			Field[] fields=aClass.getDeclaredFields();
			for(Field f: fields) {
				f.setAccessible(true);
				String name=f.getName();
				//Object val=f.get(obj);
				System.out.println(aClass.getName() + " " + name);//+" => "+val);
			}
			//System.out.print(" B");
			
			/*final URL[] urls = new URL[1];
			urls[0] =  protectionDomain.getCodeSource().getLocation();
			final ClassLoader delegateParent = classLoader.getParent();
			System.out.print(" C1");
			try (final URLClassLoader cl = new URLClassLoader(urls, delegateParent)) {
				System.out.print(" C2");
				try { //final Class<?> reloadedClazz = cl.loadClass(aClass.getName());
					aClass = cl.loadClass(s);//aClass.getCanonicalName());
				} catch (Exception e) { System.out.print("\n\n	[Error1]" + e.getMessage() + "\n\n"); }
				System.out.print(" C3");
				//System.out.println("Class reloaded: " + reloadedClazz.hashCode());
				//System.out.println("Are the same: " + (aClass != reloadedClazz) );
			}catch (Exception e) { System.out.print("\n\n	[Error2]" + e.getMessage() + "\n\n"); }
			System.out.print("	D");*/
        }
		//System.out.print("	" + s + ":E\n");
        return bytes;
    }
}