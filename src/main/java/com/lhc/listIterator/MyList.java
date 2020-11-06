package com.lhc.listIterator;

import java.util.ArrayList;
import java.util.List;

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

        for (String s : list) {
            if("a".equals(s)){
                //list.remove(s);
                list.add("a++");
            }
        }
    }
}
