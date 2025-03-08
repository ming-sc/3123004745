package com.img;

import java.util.TreeMap;

/**
 * @author : IMG
 * @create : 2025/3/8
 */
@SuppressWarnings("unused")
public class PrefixTreeNode<T> {
    private T data;
    private TreeMap<Character, PrefixTreeNode<T>> next;

    public PrefixTreeNode() {
        this(null);
    }

    public PrefixTreeNode(T data) {
        this.data = data;
        this.next = new TreeMap<>();
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public TreeMap<Character, PrefixTreeNode<T>> getNext() {
        return next;
    }

    public void setNext(TreeMap<Character, PrefixTreeNode<T>> next) {
        this.next = next;
    }

    public void addNext(Character c, PrefixTreeNode<T> node) {
        next.put(c, node);
    }

    public boolean hasNext(Character c) {
        return next.containsKey(c);
    }

    public boolean isEnd() {
        return next.isEmpty();
    }

    @Override
    public String toString() {
        return "prefixTreeNode{" +
                "data=" + data +
                ", next=" + next.toString() +
                "}";
    }
}
