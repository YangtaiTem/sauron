package com.lhc.listIterator;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

/**
 * Description:
 * User: jt.hao
 * Date: 2020-11-03
 * Time: 16:45
 */
public class MyList {

    public static void main(String[] args){
        List<String> list = new ArrayList<>();
        list.add("a");
        list.add("b");
        list.add("c");

/*        for (String s : list) {
            if("a".equals(s)){
                //list.remove(s);
                list.add("a++");
            }
        }*/

        Iterator<String> iterator = list.iterator();

        while (iterator.hasNext()){
            if("a".equals(iterator.next())){
                //list.remove(s);
                list.add("a+a");
            }
        }
    }
}
