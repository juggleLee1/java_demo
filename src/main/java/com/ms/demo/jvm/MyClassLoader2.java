package com.ms.demo.jvm;

import java.io.FileInputStream;

/***
 * 自定义加载器: 打破双亲委派；也可以重写 loadClass
 */
public class MyClassLoader2 extends ClassLoader {
    private String classpath;

    public MyClassLoader2(ClassLoader parent, String classpath) {
        super(parent);
        this.classpath = classpath;
    }

    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException {
        try {
            byte[] data = loadByte(name);
            return defineClass(name, data, 0, data.length);
        } catch (Exception e) {
            e.printStackTrace();
            throw new ClassNotFoundException();
        }
    }

    private byte[] loadByte(String name) throws Exception {
        name = name.replaceAll("\\.", "/");
        FileInputStream fileInputStream = new FileInputStream(classpath + "/" + name + ".class");
        int len = fileInputStream.available();

        byte[] buffer = new byte[len];
        fileInputStream.read(buffer, 0, len);
        fileInputStream.close();
        return buffer;
    }


    public static void main(String[] args) throws ClassNotFoundException {
        // 直接绕过了App类加载器
        MyClassLoader2 loader = new MyClassLoader2(ClassLoader.getSystemClassLoader(), "D:\\test");

        Class<?> loadClass = loader.loadClass("com.ms.demo.jvm.User");

        System.out.println(loadClass);

        System.out.println(loadClass.getClassLoader());
    }
}
