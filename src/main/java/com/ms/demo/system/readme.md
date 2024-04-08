* **System.arraycopy:**

```java
// 允许将一个数组中的部分内容复制到另一个数组中的指定位置。
public static native void arraycopy(Object src,  int  srcPos,
                                        Object dest, int destPos,
                                        int length);
```

* **Arrays.copyOf** ：复制指定数组的副本，并指定新数组的长度。

```java
        int[] srcArr = {1, 2, 3, 4, 5};

        int[] ints = Arrays.copyOf(srcArr, 10); // [1, 2, 3, 4, 5, 0, 0, 0, 0, 0]

        System.out.println(Arrays.toString(ints));
```

* **Arrays.copyOfRange**

```java
        int[] srcArr = {1, 2, 3, 4, 5};

        int[] ints = Arrays.copyOfRange(srcArr, 1, 4);  // [2, 3, 4]

        System.out.println(Arrays.toString(ints));
```

* 增强for循环  （解糖后的代码可以用idea反编译查看）
  * 数组解糖后换为 fori循环
  * 迭代器对象，可以使用迭代器遍历

```java
// 原始代码
ArrayList<Integer> arr = new ArrayList<>();
arr.add(1);
arr.add(2);

for (Integer integer : arr) {
    System.out.println(integer);
}
// 解糖后
ArrayList<Integer> arr = new ArrayList();
arr.add(1);
arr.add(2);
Iterator var2 = arr.iterator();

while(var2.hasNext()) {
    Integer integer = (Integer)var2.next();
    System.out.println(integer);
}

```

* 迭代器的源码

```java
int cursor;       // 指向下一个元素的下标
int lastRet = -1;  // 保存上一次成功调用next返回元素的下标。一般用于remove移除对应的元素
int expectedModCount = modCount;  // 记录集合被修改的次数

public E next() {
    // 如果在迭代器遍历期间，集合被修改了（非当前迭代器remove修改），抛异常
    checkForComodification();
    int i = cursor;
    if (i >= size)
        throw new NoSuchElementException();
    Object[] elementData = ArrayList.this.elementData;
    if (i >= elementData.length)
        throw new ConcurrentModificationException();
    cursor = i + 1;
    // 返回的时候会记录下lastRet
    return (E) elementData[lastRet = i];
}

public boolean hasNext() {
    // size 表示数组的大小
    return cursor != size;
}

public void remove() {
    if (lastRet < 0)
        throw new IllegalStateException();
    checkForComodification();

    try {
        // 如果一次next后，调用两次remove，则抛出异常。因此一次remove之后lastRet会变成-1
        ArrayList.this.remove(lastRet);
        // 下一个next的位置仍旧是lastRet
        cursor = lastRet;
        lastRet = -1;
        // 重新记录修改次数
        expectedModCount = modCount;
    } catch (IndexOutOfBoundsException ex) {
        throw new ConcurrentModificationException();
    }
}

```

