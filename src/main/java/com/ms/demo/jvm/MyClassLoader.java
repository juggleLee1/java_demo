package com.ms.demo.jvm;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

/***
 * 自定义加载器: 不打破双亲委派
 */
public class MyClassLoader extends ClassLoader {
    private String classpath;


    public MyClassLoader(String classpath) {
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
        MyClassLoader loader = new MyClassLoader("D:\\test");

        Class<?> loadClass = loader.loadClass("com.ms.demo.jvm.User");

        System.out.println(loadClass);

        System.out.println(loadClass.getClassLoader());
    }
}
