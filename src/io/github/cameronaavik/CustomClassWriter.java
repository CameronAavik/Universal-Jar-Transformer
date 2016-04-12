package io.github.cameronaavik;

import org.objectweb.asm.ClassWriter;

class CustomClassWriter extends ClassWriter {
    public CustomClassWriter(int flags) {
        super(flags);
    }

    @Override
    protected String getCommonSuperClass(String type1, String type2) {
        try {
            return super.getCommonSuperClass(type1, type2);
        } catch (Throwable t) {
            return "java/lang/Object";
        }
    }
}
