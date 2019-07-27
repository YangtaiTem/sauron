package com.lhc.note;

import com.google.common.collect.Lists;
import org.junit.Test;

import java.util.*;
import java.util.function.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @program: sauron
 * @description: 关于Java8 Stream
 * @author: hjt
 * @create: 2019-07-28 14:03
 **/

public class StreamTest {
    @Test
    public void match() {
        List list = getList();
        System.out.println(list.stream().allMatch(o -> (Long) o > 10));
        System.out.println(list.stream().anyMatch(o -> (Long) o > 10));
    }

    @Test
    public void forEach() {
        List list = getList();
        Long a = 1L;
        list.stream().forEach(o -> {
            if(0 < (Long)o){
                  o = Long.valueOf((Long) o + a);
                  System.out.println((Long) o);
            }
        });
    }

    @Test
    public void filter() {
        List<Long> list = getList();
        List<Long> collect = list.stream().filter(o ->  o > 10).collect(Collectors.toList());
        collect.forEach(a -> System.out.println(a));
    }

    @Test
    public void map() {
        List<Long> list = getList();
        List<Long> collect = list.stream().map(a -> a * a).collect(Collectors.toList());
        collect.forEach(a -> System.out.println(a));
    }

    @Test
    public void sorted() {
        List<Long> list = getList();
        List<Long> collectLong = list.stream().sorted(Long::compareTo)
                .collect(Collectors.toList());
        collectLong.forEach(a -> System.out.println(a));
    }

    @Test
    public void sortedDesc() {
        List<Long> list = getList();
        List<Long> collect = list.stream().sorted((x, y) -> (y < x) ? -1 : ((x == y) ? 0 : 1))
                .collect(Collectors.toList());
        collect.forEach(a -> System.out.println(a));
    }

    @Test
    public void joining() {
        List<Long> list = getList();
        String collect = list.stream().sorted(Long::compareTo).map(a -> a.toString()).collect(Collectors.joining(","));
        System.out.println(collect);
    }

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

    @Test
    public void reduce() {
        Stream<String> s = Stream.of("test", "t1", "t2", "t3", "t4");
        System.out.println(s.reduce("[value]", (s1, s2) -> s1.concat(s2)));
    }

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
        ArrayList<Long> arrayList = new ArrayList<>();
        arrayList.add(5L);
        arrayList.add(3L);
        arrayList.add(88L);
        arrayList.add(46L);
        arrayList.add(99L);
        return arrayList;
    }
}
