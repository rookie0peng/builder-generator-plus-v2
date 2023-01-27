package com.peng.idea.plugin.builder.util;

import java.util.*;

/**
 * <pre>
 *  @description:
 *  @author: qingpeng
 *  @date: 2020/7/7 19:41
 *  </pre>
 */
public class MapUtil<K, V> {

    private Map<K, V> map;

    public MapUtil() {

    }

    public MapUtil(Map<K, V> map) {
        this.map = Objects.requireNonNull(map);
    }

    public static <K, V> MapUtil<K, V> builder(Map<K, V> map) {
        return new MapUtil<>(Objects.requireNonNull(map));
    }

    public static <K, V> MapUtil<K, V> newHashMap(K key, V value) {
        MapUtil<K, V> mapUtil = new MapUtil<>();
        mapUtil.map = new HashMap<>(20);
        mapUtil.map.put(key, value);
        return mapUtil;
    }

    public static <K, V> MapUtil<K, V> newLinkedHashMap(K key, V value) {
        MapUtil<K, V> mapUtil = new MapUtil<>();
        mapUtil.map = new LinkedHashMap<>();
        mapUtil.map.put(key, value);
        return mapUtil;
    }

    public Map<K, V> getMap() {
        return map;
    }

    public HashMap<K, V> getHashMap() {
        @SuppressWarnings("unchecked")
        HashMap<K, V> hashMap = this.getSpecifyMap(HashMap.class);
        return hashMap;
    }

    public LinkedHashMap<K, V> getLinkedHashMap() {
        @SuppressWarnings("unchecked")
        LinkedHashMap<K, V> linkedHashMap = this.getSpecifyMap(LinkedHashMap.class);
        return linkedHashMap;
    }

    public <T extends Map<K, V>> T getSpecifyMap(Class<T> clazz) {
        if (map.getClass().equals(clazz)) {
            return clazz.cast(map);
        } else {
            return GsonUtil.GSON.fromJson(GsonUtil.GSON.toJson(map), clazz);
//            return JSON.parseObject(JSON.toJSONString(map), clazz);
        }
    }

    public MapUtil<K, V> put(K key, V value) {
        map.put(key, value);
        return this;
    }

    public MapUtil<K, V> putAll(Map<K, V> map) {
        if (nonEmpty(map)) {
            this.map.putAll(map);
        }
        return this;
    }

    /**
     * 获取map value，缺失则放置默认值
     *
     * @param map          map
     * @param key          键
     * @param defaultValue 默认值
     * @param <K>          键
     * @param <V>          值
     * @return 值
     */
    public static <K, V> V getValueIfAbsentPutDefault(Map<K, V> map, K key, V defaultValue) {
        V value = map.get(key);
        if (value == null) {
            map.put(key, defaultValue);
            return defaultValue;
        }
        return value;
    }

    public static <K, V> Map<K, V> merge(Map<K, V> first, Map<K, V> second) {
        Map<K, V> firstMap = safeMap(first);
        Map<K, V> secondMap = safeMap(second);
        HashMap<K, V> resultMap = new HashMap<>(firstMap.size() + secondMap.size());
        resultMap.putAll(firstMap);
        resultMap.putAll(secondMap);
        return resultMap;
    }

    public static boolean isEmpty(Map<?, ?> map) {
        return map == null || map.isEmpty();
    }

    public static boolean nonEmpty(Map<?, ?> map) {
        return !isEmpty(map);
    }

    public static <K, V> Map<K, V> safeMap(Map<K, V> map) {
        return isEmpty(map) ? Collections.emptyMap() : map;
    }

    public static void main(String[] args) {
        MapUtil<String, Object> integerIntegerMapUtil = MapUtil.newHashMap("123", 123);

        HashMap<String, Object> hashMap = integerIntegerMapUtil.getHashMap();
        test(MapUtil.newHashMap("123", (Object) 123).getHashMap());
        System.out.println(hashMap);

    }

    public static String test(HashMap<String, Object> map) {
        System.out.println("test: " + map);
        return "test";
    }

    public static final class Builder<K, V> {
        private Map<K, V> map;

        public Builder map(Map<K, V> map) {
            this.map = map;
            return this;
        }

        public MapUtil<K, V> build() {
            MapUtil<K, V> mapUtil = new MapUtil<>();
            mapUtil.map = this.map;
            return mapUtil;
        }
    }
}
