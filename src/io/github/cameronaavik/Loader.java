package io.github.cameronaavik;

import org.apache.commons.io.IOUtils;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.tree.*;

import java.io.InputStream;
import java.net.URL;
import java.net.URLClassLoader;

import static org.objectweb.asm.Opcodes.*;

class Loader extends URLClassLoader {
    public Loader(URL[] sources) {
        super(sources, null);
    }

    @SuppressWarnings("EmptyMethod")
    @Override
    protected void addURL(URL url) {
        super.addURL(url);
    }

    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException {
        try {
            byte[] classBytes;
            String fullClassName = name.replace('.', '/').concat(".class");
            URL classResource = this.findResource(fullClassName);
            if (classResource != null) {
                InputStream classInputStream = classResource.openStream();
                classBytes = IOUtils.toByteArray(classInputStream);
                byte[] transformedClass = this.runTransformer(name, classBytes);
                return this.defineClass(name, transformedClass, 0, transformedClass.length);
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
        throw new ClassNotFoundException();
    }

    private MethodNode getMethod(ClassNode classNode, String methodName, String methodDesc) {
        for (Object methodObj : classNode.methods) {
            MethodNode method = (MethodNode) methodObj;
            if (method.name.equals(methodName) && method.desc.equals(methodDesc)) {
                return method;
            }
        }
        return null;
    }

    private byte[] runTransformer(String name, byte[] basicClass) {
        // Reads the class bytes into a ClassNode
        ClassNode classNode = new ClassNode();
        ClassReader classReader = new ClassReader(basicClass);
        classReader.accept(classNode, 0);

        if (name.equals(UniversalJarTransformer.mainClassName)) transformMainMethod(classNode);

        ClassWriter classWriter = new CustomClassWriter(ClassWriter.COMPUTE_FRAMES | ClassWriter.COMPUTE_MAXS);
        classNode.accept(classWriter);
        return classWriter.toByteArray();
    }

    private void transformMainMethod(ClassNode classNode) {
        MethodNode method = getMethod(classNode, "main", "([Ljava/lang/String;)V");
        if (method == null) return;

        // Creates the bytecode instructions for hello world
        InsnList toInject = new InsnList();
        toInject.add(new FieldInsnNode(GETSTATIC, "java/lang/System", "out", "Ljava/io/PrintStream;"));
        toInject.add(new LdcInsnNode("Hello World!"));
        toInject.add(new MethodInsnNode(INVOKEVIRTUAL, "java/io/PrintStream", "println", "(Ljava/lang/String;)V", false));

        // Inserts Hello World before the first instruction in the main method
        method.instructions.insertBefore(method.instructions.getFirst(), toInject);
    }
}