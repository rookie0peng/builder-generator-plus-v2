package com.peng.idea.plugin.builder.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * <pre>
 *  @description:
 *  @author: qingpeng
 *  @date: 2020/12/11 22:20
 *  </pre>
 */
public class ListUtil {

    private static ListUtil INSTANCE = null;
    private ListUtil() {}
    public static ListUtil getINSTANCE() {
        if (INSTANCE == null) {
            synchronized (ListUtil.class) {
                if (INSTANCE == null) {
                    INSTANCE = new ListUtil();
                }
            }
        }
        return INSTANCE;
    }

    public static <T> List<T> newArrayList(T t) {
        List<T> list = new ArrayList<>();
        list.add(t);
        return list;
    }

    public static <T> List<T> newArrayList(T t1, T t2) {
        List<T> list = new ArrayList<>();
        list.add(t1);
        list.add(t2);
        return list;
    }

    public static <T> List<T> newArrayList(T t1, T t2, T t3) {
        List<T> list = new ArrayList<>();
        list.add(t1);
        list.add(t2);
        list.add(t3);
        return list;
    }

    @SafeVarargs
    public static <T> List<T> newArrayList(T... ts) {
        return new ArrayList<>(Arrays.asList(ts));
    }

    public static <T> List<T> newCopyOnWriteArrayList(T t) {
        List<T> list = new CopyOnWriteArrayList<>();
        list.add(t);
        return list;
    }

    public static <T> List<T> newCopyOnWriteArrayList(T t1, T t2) {
        List<T> list = new CopyOnWriteArrayList<>();
        list.add(t1);
        list.add(t2);
        return list;
    }

    public static <T> List<T> newCopyOnWriteArrayList(T t1, T t2, T t3) {
        List<T> list = new CopyOnWriteArrayList<>();
        list.add(t1);
        list.add(t2);
        list.add(t3);
        return list;
    }

    @SafeVarargs
    public static <T> List<T> newCopyOnWriteArrayList(T... ts) {
        return new CopyOnWriteArrayList<>(Arrays.asList(ts));
    }
}

