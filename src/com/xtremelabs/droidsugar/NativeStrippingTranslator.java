package com.xtremelabs.droidsugar;

import java.util.ArrayList;
import java.util.List;

import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtField;
import javassist.CtMethod;
import javassist.CtNewMethod;
import javassist.Modifier;
import javassist.NotFoundException;
import javassist.Translator;

import static com.xtremelabs.droidsugar.Type.find;

public class NativeStrippingTranslator implements Translator {
    private static final List<NativeStrippingTranslator> INSTANCES = new ArrayList<NativeStrippingTranslator>();

    private int index;
    private ClassHandler classHandler;

    public NativeStrippingTranslator(ClassHandler classHandler) {
        this.classHandler = classHandler;
        index = addInstance(this);
    }

    synchronized static private int addInstance(NativeStrippingTranslator androidTranslator) {
        INSTANCES.add(androidTranslator);
        return INSTANCES.size() - 1;
    }

    synchronized static public NativeStrippingTranslator get(int index) {
        return INSTANCES.get(index);
    }

    @Override
    public void start(ClassPool classPool) throws NotFoundException, CannotCompileException {
    }

    @Override
    public void onLoad(ClassPool classPool, String className) throws NotFoundException, CannotCompileException {
        boolean needsStripping = className.startsWith("android.")
                || className.startsWith("com.android")
                || className.startsWith("org.apache.http");

        if (className.equals("android.net.http.AndroidHttpClient")) {
            return;
        }
        if (className.equals("android.ddm.DdmHandleAppName")) {
            return;
        }

        CtClass ctClass = classPool.get(className);
        if (needsStripping) {
            int modifiers = ctClass.getModifiers();
            if (Modifier.isFinal(modifiers)) {
                ctClass.setModifiers(modifiers & ~Modifier.FINAL);
            }

            if (ctClass.isInterface()) return;

            classHandler.instrument(ctClass);
            fixMethods(ctClass);
        }
    }

    private void fixMethods(CtClass ctClass) throws NotFoundException, CannotCompileException {
        for (CtMethod ctMethod : ctClass.getDeclaredMethods()) {
            int modifiers = ctMethod.getModifiers();
            boolean wasNative = false;
            if (Modifier.isNative(modifiers)) {
                wasNative = true;
                modifiers = modifiers & ~Modifier.NATIVE;
            }
            ctMethod.setModifiers(modifiers);

            CtClass returnCtClass = ctMethod.getReturnType();
            Type returnType = find(returnCtClass);

            String methodName = ctMethod.getName();
            CtClass[] paramTypes = ctMethod.getParameterTypes();

            boolean returnsVoid = returnType.isVoid();
            boolean isStatic = Modifier.isStatic(modifiers);

            String methodBody;
            if (wasNative || methodName.equals("loadXmlResourceParser")) {
                methodBody = generateMethodBody(ctClass, ctMethod, returnCtClass, returnType, returnsVoid, isStatic);
                CtMethod newMethod = CtNewMethod.make(
                    ctMethod.getModifiers(),
                    returnCtClass,
                    methodName,
                    paramTypes,
                    ctMethod.getExceptionTypes(),
                    "{\n" + methodBody + "\n}",
                    ctClass);
                ctMethod.setBody(newMethod, null);
            }
        }
    }

    private String generateMethodBody(CtClass ctClass, CtMethod ctMethod, CtClass returnCtClass, Type returnType, boolean returnsVoid, boolean aStatic) throws NotFoundException {
        String methodBody;
        StringBuilder buf = new StringBuilder();
        if (!returnsVoid) {
            buf.append("Object x = ");
        }
        buf.append(getClass().getName());
        buf.append(".get(");
        buf.append(index);
        buf.append(").methodInvoked(");
        buf.append(ctClass.getName());
        buf.append(".class, \"");
        buf.append(ctMethod.getName());
        buf.append("\", ");
        if (!aStatic) {
            buf.append("this");
        } else {
            buf.append("null");
        }
        buf.append(", ");

        appendParamTypeArray(buf, ctMethod);
        buf.append(", ");
        appendParamArray(buf, ctMethod);

        buf.append(")");
        buf.append(";\n");

        if (!returnsVoid) {
            buf.append("if (x != null) return ((");
            buf.append(returnType.nonPrimitiveClassName(returnCtClass));
            buf.append(") x)");
            buf.append(returnType.unboxString());
            buf.append(";\n");
            buf.append("return ");
            buf.append(returnType.defaultReturnString());
            buf.append(";");
        }

        methodBody = buf.toString();
        return methodBody;
    }

    private void appendParamTypeArray(StringBuilder buf, CtMethod ctMethod) throws NotFoundException {
        CtClass[] parameterTypes = ctMethod.getParameterTypes();
        if (parameterTypes.length == 0) {
            buf.append("new String[0]");
        } else {
            buf.append("new String[] {");
            for (int i = 0; i < parameterTypes.length; i++) {
                if (i > 0) buf.append(", ");
                buf.append("\"");
                CtClass parameterType = parameterTypes[i];
                buf.append(parameterType.getName());
                buf.append("\"");
            }
            buf.append("}");
        }
    }

    private void appendParamArray(StringBuilder buf, CtMethod ctMethod) throws NotFoundException {
        int parameterCount = ctMethod.getParameterTypes().length;
        if (parameterCount == 0) {
            buf.append("new Object[0]");
        } else {
            buf.append("new Object[] {");
            for (int i = 0; i < parameterCount; i++) {
                if (i > 0) buf.append(", ");
                buf.append(getClass().getName());
                buf.append(".autobox(");
                buf.append("$").append(i + 1);
                buf.append(")");
            }
            buf.append("}");
        }
    }

    @SuppressWarnings({"UnusedDeclaration"})
    public Object methodInvoked(Class clazz, String methodName, Object instance, String[] paramTypes, Object[] params) {
        return classHandler.methodInvoked(clazz, methodName, instance, paramTypes, params);
    }

    @SuppressWarnings({"UnusedDeclaration"})
    public static Object autobox(Object o) {
        return o;
    }

    @SuppressWarnings({"UnusedDeclaration"})
    public static Object autobox(boolean o) {
        return o;
    }

    @SuppressWarnings({"UnusedDeclaration"})
    public static Object autobox(byte o) {
        return o;
    }

    @SuppressWarnings({"UnusedDeclaration"})
    public static Object autobox(char o) {
        return o;
    }

    @SuppressWarnings({"UnusedDeclaration"})
    public static Object autobox(short o) {
        return o;
    }

    @SuppressWarnings({"UnusedDeclaration"})
    public static Object autobox(int o) {
        return o;
    }

    @SuppressWarnings({"UnusedDeclaration"})
    public static Object autobox(long o) {
        return o;
    }

    @SuppressWarnings({"UnusedDeclaration"})
    public static Object autobox(float o) {
        return o;
    }

    @SuppressWarnings({"UnusedDeclaration"})
    public static Object autobox(double o) {
        return o;
    }

    private boolean declareField(CtClass ctClass, String fieldName, CtClass fieldType) throws CannotCompileException, NotFoundException {
        CtMethod ctMethod = getMethod(ctClass, "get" + fieldName, "");
        if (ctMethod == null) {
            return false;
        }
        CtClass getterFieldType = ctMethod.getReturnType();

        if (!getterFieldType.equals(fieldType)) {
            return false;
        }

        if (getField(ctClass, fieldName) == null) {
            CtField field = new CtField(fieldType, fieldName, ctClass);
            field.setModifiers(Modifier.PRIVATE);
            ctClass.addField(field);
        }

        return true;
    }

    private CtField getField(CtClass ctClass, String fieldName) {
        try {
            return ctClass.getField(fieldName);
        } catch (NotFoundException e) {
            return null;
        }
    }

    private CtMethod getMethod(CtClass ctClass, String methodName, String desc) {
        try {
            return ctClass.getMethod(methodName, desc);
        } catch (NotFoundException e) {
            return null;
        }
    }
}
