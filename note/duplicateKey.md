
### Java8 Duplicate key 异常解决

1.在我们使用Java8中提供的list 转换map方法时，可能回出现下面的问题：
    
    java.lang.IllegalStateException: Duplicate key...
    
    
    
&nbsp;&nbsp;&nbsp;&nbsp;产生这个问题的原因时我们参与转换的list中，作为key的属性有重复，没有办法确定使用哪个元素来作为
转换后map中的value,下面的例子可以复现这个场景：

```

    @Test
    public void duplicateKey(){
        List<String> list = new LinkedList<>();
        list.add("apple");
        list.add("banana");
        list.add("banana");
        list.add("pear");
        Map<String, String> map = list.stream().collect(Collectors.toMap(item -> item, item -> item));
        for (Map.Entry<String, String> entry : map.entrySet()) {
            System.out.println(entry.toString());
        }
    }

```

上面代码运行时，会产生这样的异常：

```

java.lang.IllegalStateException: Duplicate key banana

	at java.util.stream.Collectors.lambda$throwingMerger$0(Collectors.java:133)
	at java.util.HashMap.merge(HashMap.java:1253)
	at java.util.stream.Collectors.lambda$toMap$58(Collectors.java:1320)
	at java.util.stream.ReduceOps$3ReducingSink.accept(ReduceOps.java:169)
	at java.util.LinkedList$LLSpliterator.forEachRemaining(LinkedList.java:1235)
	at java.util.stream.AbstractPipeline.copyInto(AbstractPipeline.java:481)
	at java.util.stream.AbstractPipeline.wrapAndCopyInto(AbstractPipeline.java:471)
	at java.util.stream.ReduceOps$ReduceOp.evaluateSequential(ReduceOps.java:708)
	at java.util.stream.AbstractPipeline.evaluate(AbstractPipeline.java:234)
	at java.util.stream.ReferencePipeline.collect(ReferencePipeline.java:499)
	at com.lhc.dongpo.easy.MyTest.duplicateKey(MyTest.java:57)
	at sun.reflect.NativeMethodAccessorImpl.invoke0(Native Method)
	...

```
    
这个异常就是说，转换map的过程中，有两个key是banana的元素，不知道怎么取舍，因此产生异常。

2.解决。

&nbsp;&nbsp;&nbsp;&nbsp;解决办法就是我们去提供这样一个策略。
查看他的源码：

```

    public static <T, K, U>
    Collector<T, ?, Map<K,U>> toMap(Function<? super T, ? extends K> keyMapper,
                                    Function<? super T, ? extends U> valueMapper) {
        return toMap(keyMapper, valueMapper, throwingMerger(), HashMap::new);
    }

    public static <T, K, U>
    Collector<T, ?, Map<K,U>> toMap(Function<? super T, ? extends K> keyMapper,
                                    Function<? super T, ? extends U> valueMapper,
                                    BinaryOperator<U> mergeFunction) {
        return toMap(keyMapper, valueMapper, mergeFunction, HashMap::new);
    }

```

有这样两个方法，出错的地方我们是用的第一个方法，对于可能存在重复key的转换中，我们要使用第二个方法。

```

        Map<String, String> map = list.stream().
                collect(Collectors.
                        toMap(item -> item, item -> item, (item1, item2) -> item1));
```

我这里使用的是两个key 相同时，保留先存进去的那个元素。相比第一个方法，主要就是我们要去实现一个策略，来决定key相同时，元素如何保留。