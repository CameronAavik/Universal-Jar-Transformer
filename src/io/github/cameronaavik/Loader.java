package io.github.cameronaavik;

import org.apache.commons.io.IOUtils;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.*;

import java.io.InputStream;
import java.net.URL;
import java.net.URLClassLoader;

public class Loader extends URLClassLoader
{
    public Loader(URL[] sources)
    {
        super(sources, null);
    }

    @SuppressWarnings("EmptyMethod")
    @Override
    protected void addURL(URL url)
    {
        super.addURL(url);
    }

    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException
    {
        try
        {
            byte[] classBytes;
            String fullClassName = name.replace('.', '/').concat(".class");
            URL classResource = this.findResource(fullClassName);
            if (classResource != null)
            {
                InputStream classInputStream = classResource.openStream();
                classBytes = IOUtils.toByteArray(classInputStream);
                byte[] transformedClass = this.runTransformer(name, classBytes);
                return this.defineClass(name, transformedClass, 0, transformedClass.length);
            }
        }
        catch (Throwable e)
        {
            e.printStackTrace();
        }
        throw new ClassNotFoundException();
    }

    private byte[] runTransformer(String name, byte[] basicClass)
    {
        if (name.equals(UniversalJarTransformer.mainClassName))
        {
            return transformMainMethod(basicClass);
        }
        return basicClass;
    }

    private static byte[] transformMainMethod(byte[] basicClass)
    {
        ClassNode classNode = new ClassNode();
        ClassReader classReader = new ClassReader(basicClass);
        classReader.accept(classNode, 0);

        String METHOD_NAME = "main";
        String METHOD_DESC = "([Ljava/lang/String;)V";

        for (Object methodObj : classNode.methods)
        {
            MethodNode method = (MethodNode)methodObj;
            if (method.name.equals(METHOD_NAME) && method.desc.equals(METHOD_DESC))
            {
                InsnList toInject = new InsnList();
                toInject.add(new FieldInsnNode(Opcodes.GETSTATIC, "java/lang/System", "out", "Ljava/io/PrintStream;"));
                toInject.add(new LdcInsnNode("Hello World!"));
                toInject.add(new MethodInsnNode(Opcodes.INVOKEVIRTUAL, "java/io/PrintStream", "println", "(Ljava/lang/String;)V", false));

                method.instructions.insert(method.instructions.getFirst(), toInject);
            }
        }

        ClassWriter classWriter = new CustomClassWriter(ClassWriter.COMPUTE_FRAMES | ClassWriter.COMPUTE_MAXS);
        classNode.accept(classWriter);
        return classWriter.toByteArray();
    }
}