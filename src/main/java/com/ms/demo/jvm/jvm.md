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