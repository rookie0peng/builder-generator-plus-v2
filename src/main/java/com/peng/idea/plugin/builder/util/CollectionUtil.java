package com.peng.idea.plugin.builder.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * <pre>
 *  @description:
 *  @author: qingpeng
 *  @date: 2021/6/18 17:12
 *  </pre>
 */
public class CollectionUtil {

    /**
     * 判断集合是否为空
     * @param coll 集合
     * @return true 为空；false 不为空
     */
    public static boolean isEmpty(Collection<?> coll) {
        return coll == null || coll.isEmpty();
    }

    /**
     * 判断集合是否不为空
     * @param coll 集合
     * @return true 不为空；false 为空
     */
    public static boolean nonEmpty(Collection<?> coll) {
        return !isEmpty(coll);
    }

    /**
     * 判断集合{@code src}是否包含集合{@code tar}任意一个元素
     * @param src 源集合
     * @param tar 目标集合
     * @return true 如果src包含tar任何一个元素；否则，反之。
     */
    public static boolean containsAny(Collection<?> src, Collection<?> tar) {
        if (isEmpty(tar))
            return true;
        else if (isEmpty(src))
            return false;
        else {
            for (Object obj : tar) {
                if (src.contains(obj))
                    return true;
            }
            return false;
        }
    }

    public static <T> Stream<T> safeStream(Collection<T> collection) {
        return isEmpty(collection) ? Stream.empty() : collection.stream();
    }

    public static <T> Collection<T> safeCollection(Collection<T> collection) {
        return isEmpty(collection) ? Collections.emptyList() : collection;
    }

    public static <T> List<T> safeList(List<T> list) {
        return isEmpty(list) ? Collections.emptyList() : list;
    }

    /**
     * 合并集合
     * @param collectionMaster 主集合
     * @param collectionSub 从集合
     * @param <F> 第一个类型
     * @param <S> 第二个类型
     * @return 合并结果流
     */
    public static <F, S> List<Pair<F, S>> mergeAsPairList(Collection<F> collectionMaster, Collection<S> collectionSub) {
        return mergeAsPairList(collectionMaster, collectionSub, null);
    }

    /**
     * 合并集合
     * @param collectionMaster 主集合
     * @param collectionSub 从集合
     * @param s 默认值
     * @param <F> 第一个类型
     * @param <S> 第二个类型
     * @return 合并结果流
     */
    public static <F, S> List<Pair<F, S>> mergeAsPairList(Collection<F> collectionMaster, Collection<S> collectionSub, S s) {
        List<F> firsts = safeStream(collectionMaster).collect(Collectors.toList());
        List<S> seconds = safeStream(collectionSub).collect(Collectors.toList());
        List<Pair<F, S>> first2Seconds = new ArrayList<>(firsts.size());
        for (int i = 0, firstSize = firsts.size(), secondSize = seconds.size(); i < firstSize; i++) {
            first2Seconds.add(Pair.of(firsts.get(i), i < secondSize ? seconds.get(i) : s));
        }
        return first2Seconds;
    }
}
