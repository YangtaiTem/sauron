package com.lhc.note;

import java.util.HashMap;

/**
 * Description:
 * User: jt.hao
 * Date: 2020-11-19
 * Time: 10:01
 */
public class LRUCache<K, V> {
    private Node first;
    private Node last;

    private final int SIZE;
    private HashMap<K, Node> map;

    public LRUCache(int SIZE) {
        this.SIZE = SIZE;
        map = new HashMap<>();
    }


    public Node<K, V> get(K key){
        Node<K, V> node = map.get(key);
        if(node == null){
            return null;
        }
        moveToFirst(node);
        return node;
    }

    public void moveToFirst(Node node){
        if(node == first) return;
        if(node.pre != null) node.pre.next = node.next;
        if(node.next != null) node.next.pre = node.pre;
        if(node == last) last = last.pre;

        if(first == null || last == null){
            first = last = node;
            return;
        }

        first.pre = node;
        node.next = first;
        first = node;
        node.pre = null;
    }

    public void put(K key, V value){
        Node node = map.get(key);
        if(node == null){
            if(map.size() >= SIZE){
                map.remove(last.key);
                removeLast();
            }
            node = new Node();
            node.key = key;
        }
        node.value = value;
        moveToFirst(node);
        map.put(key, node);
    }

    public void removeLast(){
        if(last == null)return;
        last = last.pre;
        if(last == null){
            first = last = null;
        }else {
            last.pre = null;
        }
    }

    public void remove(K key){
        Node node = map.get(key);
        if(node != null){
            if(node.pre != null) node.pre.next = node.next;
            if(node.next != null) node.next.pre = node.pre;
            if (node == first) first = node.next;
            if(node == last) last = last.pre;
        }
        map.remove(key);
    }

    class Node<K, V> {
        public Node pre;
        public Node next;
        public K key;
        public V value;
    }
}
