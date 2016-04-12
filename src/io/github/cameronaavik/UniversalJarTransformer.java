package io.github.cameronaavik;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URLClassLoader;
import java.util.jar.JarFile;

public class UniversalJarTransformer {
    public static String mainClassName;
    private static Loader classLoader;

    private UniversalJarTransformer() {
        URLClassLoader ucl = (URLClassLoader) this.getClass().getClassLoader();
        classLoader = new Loader(ucl.getURLs());
    }

    public static void main(String[] args) {
        new UniversalJarTransformer().launch(args);
    }

    private void launch(String[] args) {
        File jarFile = new File(args[0]);
        if (args.length == 2) {
            mainClassName = args[1];
        } else {
            try {
                JarFile j = new JarFile(jarFile);
                mainClassName = j.getManifest().getMainAttributes().getValue("Main-Class");
            } catch (IOException e) {
                System.out.println("Failed to get main class from jar file, please pass the class name manually");
                e.printStackTrace();
            }
        }
        try {
            classLoader.addURL(jarFile.toURI().toURL());
            Class<?> mainClass = Class.forName(mainClassName, false, classLoader);
            Method mainMethod = mainClass.getMethod("main", String[].class);
            mainMethod.invoke(null, new Object[]{new String[]{}});
        } catch (MalformedURLException e) {
            System.out.println("Failed to get URL for Jar File");
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            System.out.println("Failed to find main class");
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            System.out.println("Failed to find main method");
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            System.out.println("Unable to access main method");
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            System.out.println("Main method of jar threw an exception");
            e.printStackTrace();
        }
    }
}