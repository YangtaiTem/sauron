package com.lhc.note;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @program: sauron
 * @description: 关于Java8 Stream
 * @author: hjt
 * @create: 2019-07-28 14:03
 **/

public class StreamTest {
    /**
     * 对于元素的匹配
     * 使用Predicate （断言，给一个参数T，返回boolean类型的结果）作为参数
     * allMatch 所有元素匹配
     * anyMatch 任一元素匹配
     */
    @Test
    public void match() {
        List list = getList();
        System.out.println(list.stream().allMatch(o -> (Long) o > 10));
        System.out.println(list.stream().anyMatch(o -> (Long) o > 10));
    }

    /**
     * 循环遍历，对每一个元素进行处理
     * 使用了Consumer,重写了其中的accept(T t)方法
     */
    @Test
    public void forEach() {
        List list = getList();
        Long a = 1L;
        list.stream().forEach(o -> {
            if (0 < (Long) o) {
                o = Long.valueOf((Long) o + a);
                System.out.println((Long) o);
            }
        });
    }

    /**
     * filter
     * 使用Predicate （断言，给一个参数T，返回boolean类型的结果）作为参数
     * 将集合中不符合条件的元素过滤
     */
    @Test
    public void filter() {
        List<Long> list = getList();
        List<Long> collect = list.stream().filter(o -> o > 10).collect(Collectors.toList());
        collect.forEach(a -> System.out.println(a));
    }

    /**
     * map
     * 每一个元素做指定操作，返回指定类型
     * 使用Function  重写了apply(T t)方法
     */
    @Test
    public void map() {
        List<Long> list = getList();
        List<Long> collect = list.stream().map(a -> a * a).collect(Collectors.toList());
        collect.forEach(a -> System.out.println(a));
    }

    /**
     * sorted
     * 使用Comparator，重写compare(T o1, T o2)方法进行排序
     * 这里是使用Long的compareTo
     */
    @Test
    public void sorted() {
        List<Long> list = getList();
        List<Long> collectLong = list.stream().sorted(Long::compareTo)
                .collect(Collectors.toList());
        collectLong.forEach(a -> System.out.println(a));
    }

    /**
     * 排序，自定义排序方法
     */
    @Test
    public void sortedDesc() {
        List<Long> list = getList();
        list.stream().sorted(new Comparator<Long>() {
            @Override
            public int compare(Long o1, Long o2) {
                return 0;
            }
        });
        List<Long> collect = list.stream().sorted((x, y) -> (y < x) ? -1 : ((x == y) ? 0 : 1))
                .collect(Collectors.toList());
        collect.forEach(a -> System.out.println(a));
    }

    /**
     * 拼接字符串
     */
    @Test
    public void joining() {
        List<Long> list = getList();
        String collect = list.stream().sorted(Long::compareTo).map(a -> a.toString()).collect(Collectors.joining(","));
        System.out.println(collect);
    }

    /**
     * 统计
     * 最大值，最小值，平均值，数目等一些方法
     */
    @Test
    public void stats() {
        List<Long> list = getList();
        double average = list.stream().mapToLong(a -> a).summaryStatistics().getAverage();
        System.out.println(average);
        double max = list.stream().mapToLong(a -> a).summaryStatistics().getMax();
        System.out.println(max);
        double count = list.stream().mapToLong(a -> a).summaryStatistics().getCount();
        System.out.println(count);
    }

    /**
     * 规约
     */
    @Test
    public void reduce() {
        Stream<String> s = Stream.of("test", "t1", "t2", "t3", "t4");
        System.out.println(s.reduce("[value]", (s1, s2) -> s1.concat(s2)));
    }

    /**
     * 并行，规约
     */
    @Test
    public void parallelReduce() {
        /**
         *  必须是在并行的情况下
         * (1+4)*(2+4)*(3+4)
         */
        System.out.println(Stream.of(1, 2, 3).parallel().
                reduce(4, (integer, integer2) -> integer + integer2, (integer, integer2) -> integer * integer2));

    }


    public List getList() {
        List<Long> list = new ArrayList<>();
        list.add(5L);
        list.add(3L);
        list.add(88L);
        list.add(46L);
        list.add(99L);
        return list;
    }

    @Test
    public void testMap(){
        List<String> list = new ArrayList<>();
        list.add("hello world");
        list.add("hello ted");
        List<String[]> collect = list.stream().map(s -> s.split(" ")).collect(Collectors.toList());

    }
}
