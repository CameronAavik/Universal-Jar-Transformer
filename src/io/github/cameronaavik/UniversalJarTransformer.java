package io.github.cameronaavik;

import java.io.File;
import java.lang.reflect.Method;
import java.net.URLClassLoader;

public class UniversalJarTransformer
{
    public static String mainClassName;
    private static Loader classLoader;

    private UniversalJarTransformer()
    {
        URLClassLoader ucl = (URLClassLoader) this.getClass().getClassLoader();
        classLoader = new Loader(ucl.getURLs());
    }

    public static void main(String[] args)
    {
        new UniversalJarTransformer().launch(args);
    }

    @SuppressWarnings("unchecked")
    private void launch(String[] args)
    {
        File jarFile = new File(args[0]);
        mainClassName = args[1];
        try
        {
            classLoader.addURL(jarFile.toURI().toURL());
            Class mainClass = Class.forName(mainClassName, false, classLoader);
            Method mainMethod = mainClass.getMethod("main", String[].class);
            mainMethod.invoke(null, new Object[]{new String[]{}});
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}