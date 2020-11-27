
## List集合增强for循环时产生的异常

### 1.介绍

在List 集合使用增强for循环遍历时，我们如果改变了集合的长度，会抛出异常。下面举个例子：

```java

    public static void main(String[] args){
        List<String> list = new ArrayList<>();
        list.add("a");
        list.add("b");
        list.add("c");

        for (String s : list) {
            if("a".equals(s)){
                //list.remove(s);
                list.add("a++");
            }
        }
    }

```
异常信息：

```

Exception in thread "main" java.util.ConcurrentModificationException
	at java.util.ArrayList$Itr.checkForComodification(ArrayList.java:901)
	at java.util.ArrayList$Itr.next(ArrayList.java:851)
    ···

```

### 2.增强for循环的原理

增强for循环是Java提供的语法糖，比如

```java

for (Integer i : list) {
   System.out.println(i);
}


```

反编译后，其实是这样的：

```java

Integer i;
for(Iterator iterator = list.iterator(); iterator.hasNext(); System.out.println(i)){
   i = (Integer)iterator.next();        
}


```

### 3.现在我们通过源代码来分析一开始例子中所报的错误：

位置是在ArrayList的850行和899行

```java

public E next() {
    checkForComodification();
    int i = cursor;
    if (i >= size)
        throw new NoSuchElementException();
    Object[] elementData = ArrayList.this.elementData;
    if (i >= elementData.length)
        throw new ConcurrentModificationException();
    cursor = i + 1;
    return (E) elementData[lastRet = i];
}

final void checkForComodification() {
    if (modCount != expectedModCount)
        throw new ConcurrentModificationException();
}

```
抛出异常的条件是 modCount != expectedModCount 。

提到的几个关键变量：
　　
* cursor：表示下一个要访问的元素的索引，从next()方法的具体实现就可看出

* lastRet：表示上一个访问的元素的索引

* expectedModCount：表示对ArrayList修改次数的期望值，它的初始值为modCount。

* modCount是AbstractList类中的一个成员变量

modCount表示对List的修改次数，他的初始值是0。查看ArrayList的add()和remove()方法就可以发现，每次调用add()方法或者remove()方法就会对modCount进行加1操作。

```java

public boolean add(E e) {
    ensureCapacityInternal(size + 1);  // Increments modCount!!
    elementData[size++] = e;
    return true;
}

private void ensureCapacityInternal(int minCapacity) {
    if (elementData == DEFAULTCAPACITY_EMPTY_ELEMENTDATA) {
        minCapacity = Math.max(DEFAULT_CAPACITY, minCapacity);
    }

    ensureExplicitCapacity(minCapacity);
}

private void ensureExplicitCapacity(int minCapacity) {
    modCount++;

    // overflow-conscious code
    if (minCapacity - elementData.length > 0)
        grow(minCapacity);
}

```

看到这里，问题的原因找到了，在直接调用add()或者remove()方法时，只修改了modCount,却没有修改expectedModCount！

ArrayList中给出的解决办法是提供了一个ListIterator：

```java

public ListIterator<E> listIterator() {
    return new ListItr(0);
}

private class ListItr extends Itr implements ListIterator<E> {}

```

它实现的迭代器实现了add(E e) 和remove()方法，可以解决抛出异常的问题，这两个方法里都修改了expectedModCount，让他和modCount保持一致。


**注意：上述两个方法在使用时要保证环境的线程安全。**