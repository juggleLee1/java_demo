* 从对象创建的整体流程
  * 类的加载  ->  分配内存  -> 初始化  -> 设置对象头  -> 执行init方法
  * 类的加载： 验证  ->  准备  ->  解析  -> 初始化

## class 文件结构

* class 文件结构  （使用idea BinEd 可以查看十六进制）

  * 字符串常量池存放的元素
    * 字面量（字段名字，方法名字，常量等）
    * 类/接口信息（继承类，实现接口的信息，类中用到的其他类的信息）
    * 字段引用：类中已经用到的字段（不限于该类的字段）
    * 方法引用：类中已经用到的方法（不限于该类的方法）
    * CONSTANT_NameAndType_info： 组合字段/方法名称和字段类型/方法签名

  > Constant_Utf8_info :  字面量
  >
  > CONSTANT_NameAndType_info：组合字段/方法名称和字段类型/方法签名，两个属性都指向Constant_Utf8_info
  >
  > CONSTANT_class_info：类的全类名，指向Constant_Utf8_info
  >
  > CONSTANT_Methodref_info：class.NameAndType
  >
  > CONSTANT_Fieldref_info: class.NameAndType

![image-20240412104407337](F:\java_idea\demo\javase-demo\src\main\java\com\ms\demo\jvm\jvm.assets\image-20240412104407337.png)

## 类加载

* 类加载源码
  * 程序启动时，C++底层会创建一个引导类的加载器实例。然后C++调用JVM启动器示例sun.misc.Launcher类，执行一系列后续操作

```java
// Launcher类加载完毕后就已经创建了一个实例
private static Launcher launcher = new Launcher();

public Launcher() {
   	// 扩展类加载器
    ExtClassLoader var1;
    try {
        // 获取扩展类加载器   ExtClassLoader 是 Launcher的内部静态类
        var1 = Launcher.ExtClassLoader.getExtClassLoader();
    } catch (IOException var10) {
        throw new InternalError("Could not create extension class loader", var10);
    }

    try {
        // 将当前的loader 字段设置为应用程序加载器，并将扩展类加载器示例传入进去作为parent
        this.loader = Launcher.AppClassLoader.getAppClassLoader(var1);
    } catch (IOException var9) {
        throw new InternalError("Could not create application class loader", var9);
    }

    Thread.currentThread().setContextClassLoader(this.loader);
    String var2 = System.getProperty("java.security.manager");
    if (var2 != null) {
        SecurityManager var3 = null;
        if (!"".equals(var2) && !"default".equals(var2)) {
            try {
                var3 = (SecurityManager)this.loader.loadClass(var2).newInstance();
            } catch (IllegalAccessException var5) {
            } catch (InstantiationException var6) {
            } catch (ClassNotFoundException var7) {
            } catch (ClassCastException var8) {
            }
        } else {
            var3 = new SecurityManager();
        }

        if (var3 == null) {
            throw new InternalError("Could not create SecurityManager: " + var2);
        }

        System.setSecurityManager(var3);
    }

}

static class ExtClassLoader extends URLClassLoader {
    public static ExtClassLoader getExtClassLoader() throws IOException {
        final File[] var0 = getExtDirs();

        try {
            return (ExtClassLoader)AccessController.doPrivileged(new PrivilegedExceptionAction<ExtClassLoader>() {
                public ExtClassLoader run() throws IOException {
                    int var1 = var0.length;

                    for(int var2 = 0; var2 < var1; ++var2) {
                        MetaIndex.registerDirectory(var0[var2]);
                    }
					// 创建扩展类加载器
                    return new ExtClassLoader(var0);
                }
            });
        } catch (PrivilegedActionException var2) {
            throw (IOException)var2.getException();
        }
    }
}

public ExtClassLoader(File[] var1) throws IOException {
    // String var0 = System.getProperty("java.ext.dirs");
    // 第一个参数表示扩展类加载器所要加载的class文件路径
    super(getExtURLs(var1), (ClassLoader)null, Launcher.factory);
    SharedSecrets.getJavaNetAccess().getURLClassPath(this).initLookupCache(this);
}

public URLClassLoader(URL[] urls, ClassLoader parent,
                      URLStreamHandlerFactory factory) {
    super(parent);
    // this is to make the stack depth consistent with 1.1
    SecurityManager security = System.getSecurityManager();
    if (security != null) {
        security.checkCreateClassLoader();
    }
    acc = AccessController.getContext();
    ucp = new URLClassPath(urls, factory, acc);
}

protected SecureClassLoader(ClassLoader parent) {
    super(parent);
    // this is to make the stack depth consistent with 1.1
    SecurityManager security = System.getSecurityManager();
    if (security != null) {
        security.checkCreateClassLoader();
    }
    initialized = true;
}

protected ClassLoader(ClassLoader parent) {
    this(checkCreateClassLoader(), parent);
}

private ClassLoader(Void unused, ClassLoader parent) {
    // 设置parent，如果是扩展类加载器，则parent为null；如果是应用类加载器，则parent为扩展类加载器示例
    this.parent = parent;
    if (ParallelLoaders.isRegistered(this.getClass())) {
        parallelLockMap = new ConcurrentHashMap<>();
        package2certs = new ConcurrentHashMap<>();
        domains =
            Collections.synchronizedSet(new HashSet<ProtectionDomain>());
        assertionLock = new Object();
    } else {
        // no finer-grained lock; lock on the classloader instance
        parallelLockMap = null;
        package2certs = new Hashtable<>();
        domains = new HashSet<>();
        assertionLock = this;
    }
}

public static ClassLoader getAppClassLoader(final ClassLoader var0) throws IOException {
    // 传入app类加载器示例要加载的class路径
    final String var1 = System.getProperty("java.class.path");
    final File[] var2 = var1 == null ? new File[0] : Launcher.getClassPath(var1);
    return (ClassLoader)AccessController.doPrivileged(new PrivilegedAction<AppClassLoader>() {
        public AppClassLoader run() {
            URL[] var1x = var1 == null ? new URL[0] : Launcher.pathToURLs(var2);
            return new AppClassLoader(var1x, var0);
        }
    });
}

// 2. C++底层会调用 getLauncher方法得到这个实例
public static Launcher getLauncher() {
    return launcher;
}

// 3. 获取Lauunch的loader对象，app类加载器实例
public ClassLoader getClassLoader() {
    return this.loader;
}
```

> System.getProperty("java.class.path"):
>
> 当前类编译后的路径根目录：F:\java_idea\demo\javase-demo\target\classes
>
> jdk目录下的jre\lib下的文件：
>
> C:\Program Files\Java\jdk1.8.0_131\jre\lib\plugin.jar
>
> C:\ProgramFiles\Java\jdk1.8.0_131\jre\lib\resources.jar
> C:\Program Files\Java\jdk1.8.0_131\jre\lib\rt.jar

> System.getProperty("java.ext.dirs"): 扩展类加载器所加载的包
>
> C:\Program Files\Java\jdk1.8.0_131\jre\lib\ext
> C:\Windows\Sun\Java\lib\ext

* 类的加载（被分配到方法区）
  * 验证：检验class文件格式是否符合要求，魔术字CAFEBABE
  * 准备：为静态成员变量默认初始化，并分配内存
  * 解析：符号引用替换成直接引用
  * 初始化：静态代码块和静态方法执行（clinit）

> 类被加载过后，会创建一个Class实例对象放入堆中

## JVM内存分配

* 流程： 分配内存  -> 初始化  -> 设置对象头  -> 执行init方法
* 分配内存之栈上分配
* 分配内存
  * 指针碰撞（默认）：如果Java堆中内存是绝对规整的，所有用过的内存都放在一边，空闲的内存放在另一边，中间放着一个指针作为分界点 的指示器，那所分配内存就仅仅是把那个指针向空闲空间那边挪动一段与对象大小相等的距离。
  * 空闲列表（会发生很多碎片）：如果Java堆中的内存并不是规整的，已使用的内存和空 闲的内存相互交错，那就没有办法简单地进行指针碰撞了，虚拟 机就必须维护一个列表，记 录上哪些内存块是可用的，在分配的时候从列表中找到一块足够大的空间划分给对象实例， 并更新列表上的记录
* 内存分配并发问题解决
  * CAS
  * TLAB：为每个线程单独分配一个小区域，每个线程分配对象内存首先在这个区域被分配。如果不够，再在堆中进行CAS分配

* 初始化：分配内存后会将这块内存区域置0，相当于默认初始化
* 设置对象头（8字节对齐）
  * 指针压缩算法：类型指针（指向其方法区中类元信息的指针，非Class对象），本来是8字节寻址的。现在指针压缩使用4字节，理论上最多可以寻址到4GB的地址空间，但是对象头是以8字节为单位的，所以这个地址空间可以增大到32GB。但是如果堆内存大于32G了，指针压缩就失效了（不过可以设置对象头的单位扩大可寻址的单位）。
  * -XX:+UseCompressedOops ：开启指针压缩（默认开启），有助于减少内存占用
  * -XX:ObjectAlignmentInBytes=16 ：设置对象头对齐

![image-20240412134745340](F:\java_idea\demo\javase-demo\src\main\java\com\ms\demo\jvm\jvm.assets\image-20240412134745340.png)

* init方法执行：构造函数

## 堆空间内存分配流程

![image-20240412152204290](F:\java_idea\demo\javase-demo\src\main\java\com\ms\demo\jvm\jvm.assets\image-20240412152204290.png)

* 场景：需要尽可能的避免Full GC的产生
* JVM参数设置
  * 对象年龄应该设置多少合适：
    * 如果对象的生命周期出现严重的不平衡，有大量生命周期非常低的对象A，也有大量生命周期非常高的对象B，此时需要把对象年龄设置到比A稍微高一点
    * 大数据高并发应用，此时需要将对象年龄设高一点，避免数据进去老年代，频繁Full GC