import java.io.FileInputStream;
import java.io.FileOutputStream;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;

public class ParseClassFile
{
	public static void main(final String args[]) throws Exception {
		FileInputStream is = new FileInputStream(args[0]);

		ClassReader cr = new ClassReader(is);
		ClassParseVisitor ca = new ClassParseVisitor();
		cr.accept(ca, 0);
	}
}
