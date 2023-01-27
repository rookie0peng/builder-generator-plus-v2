package com.peng.idea.plugin.builder.util;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.lang.reflect.Type;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;
import static org.apache.commons.lang3.StringUtils.trim;

/**
 * <pre>
 *  @description:
 *  @author: qingpeng
 *  @date: 2022/12/27 9:41
 *  </pre>
 */
public class GsonUtil {

    private static final Logger LOGGER = LoggerFactory.getLogger(GsonUtil.class);

    private static final Pattern NUMERICAL_PATTERN = Pattern.compile("^\\d+$");

    public static final Gson GSON = new GsonBuilder()
            .disableHtmlEscaping()
            .registerTypeAdapter(Date.class, (JsonDeserializer<Date>) (json, typeOfT, context) -> {
//                String dateStr = json.getAsJsonPrimitive().getAsString();
//                long timestamp = Long.parseLong(dateStr.substring(6, dateStr.length() - 2));
//                return new Date(timestamp);
                return new Date(json.getAsLong());
            })
            .registerTypeAdapter(Date.class, (JsonSerializer<Date>) (date, typeOfT, context) -> date == null ? null : new JsonPrimitive(date.getTime()))
            .registerTypeAdapter(LocalDateTime.class, (JsonSerializer<LocalDateTime>) (src, typeOfSrc, context) -> new JsonPrimitive(src.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)))
            .registerTypeAdapter(LocalDate.class, (JsonSerializer<LocalDate>) (src, typeOfSrc, context) -> new JsonPrimitive(src.format(DateTimeFormatter.ISO_LOCAL_DATE)))
            .registerTypeAdapter(LocalDateTime.class, (JsonDeserializer<LocalDateTime>) (json, type, jsonDeserializationContext) -> {
                String datetimeStr = json.getAsJsonPrimitive().getAsString();
                Matcher matcher = NUMERICAL_PATTERN.matcher(datetimeStr);
                if (matcher.find()) {
                    Long timestamp = Long.valueOf(datetimeStr);
                    timestamp = datetimeStr.length() > 10 ? timestamp : timestamp * 1000;
                    return LocalDateTime.ofInstant(Instant.ofEpochMilli(timestamp), ZoneId.systemDefault().getRules().getOffset(Instant.now()));
                }
                return LocalDateTime.parse(datetimeStr);
            }).registerTypeAdapter(LocalDate.class, (JsonDeserializer<LocalDate>) (json, type, jsonDeserializationContext) -> {
                String datetime = json.getAsJsonPrimitive().getAsString();
                return LocalDate.parse(datetime);
            })
            .registerTypeAdapter(new TypeToken<Map<String, Object>>() {}.getType(), new MapJsonDeserializer())
            .registerTypeAdapter(new TypeToken<List<Object>>() {}.getType(), new ListJsonDeserializer())
            .create();

    public static final Gson NOT_IGNORE_NULL_GSON = new GsonBuilder().serializeNulls().create();

    public static JsonObject createNameObject(String value){
        return createObject("name", value);
    }

    public static JsonArray createArrayNameObject(String value){
        return createArrayNameObjects(Collections.singletonList(value));
    }

    public static JsonArray createArrayNameObjects(List<String> values){
        JsonArray array = new JsonArray();

        for(String value : values){
            JsonObject name = new JsonObject();
            name.add("name", createPrimitive(value));
            array.add(name);
        }

        return array;
    }

    public static JsonElement createNameObject(String value, boolean isArray){
        return isArray ? createArrayNameObject(value) : createNameObject(value);
    }

    public static JsonObject createIdObject(String value){
        return createObject("id", value);
    }

    public static JsonObject createObject(String property, String value){
        JsonObject name = new JsonObject();
        name.add(property, createPrimitive(value));

        return name;
    }

    public static JsonArray createArrayObject(String property, List<String> values){
        JsonArray array = new JsonArray();
        JsonObject o = new JsonObject();
        for(String value : values){
            o.add(property, createPrimitive(value));
        }

        array.add(o);
        return array;
    }


    public static boolean isEmpty(JsonArray jsonArray){
        return nonNull(jsonArray) && jsonArray.size() == 0;
    }

    public static JsonElement createPrimitive(String value){
        return new JsonPrimitive(trim(value));
    }

    public static JsonElement createPrimitive(Double value){
        return new JsonPrimitive(value);
    }

    public static JsonElement createPrimitive(Integer value){
        return new JsonPrimitive(value);
    }

    public static String getAsString(JsonElement element) {
        return getAs(element, String.class);
    }

    public static Date getAsDate(JsonElement element) {
        return getAs(element, Date.class);
    }

    public static <T> List<T> getAsList(JsonElement element, Class<T[]> clazz) {
        T[] values = getAs(element, clazz);
        if (isNull(values)) {
            return new ArrayList<>();
        }

        return Arrays.asList(values);
    }

    public static <T> T getAs(JsonElement element, Class<T> clazz) {
        return GSON.fromJson(element, clazz);
    }

    public static <T> T getAs(String response, Class<T> clazz) {
        return GSON.fromJson(response, clazz);
    }

    public static <T> List<T> getAsList(String response, Class<T[]> clazz) {
        T[] values = getAs(response, clazz);
        if (isNull(values)) {
            return new ArrayList<>();
        }

        return Arrays.asList(values);
    }

    public static class GsonTypeAdapter extends TypeAdapter<Object> {
        @Override
        public Object read(JsonReader in) throws IOException {
            // 反序列化
            JsonToken token = in.peek();
            switch (token) {
                case BEGIN_ARRAY:
                    List<Object> list = new ArrayList<Object>();
                    in.beginArray();
                    while (in.hasNext()) {
                        list.add(read(in));
                    }
                    in.endArray();
                    return list;

                case BEGIN_OBJECT:

                    Map<String, Object> map = new HashMap<String, Object>();
                    in.beginObject();
                    while (in.hasNext()) {
                        map.put(in.nextName(), read(in));
                    }
                    in.endObject();
                    return map;

                case STRING:

                    return in.nextString();

                case NUMBER:

                    /**
                     * 改写数字的处理逻辑，将数字值分为整型与浮点型。
                     */
                    double dbNum = in.nextDouble();

                    // 判断数字是否为整数值
                    // 数字超过long的最大值，返回浮点类型
                    if (dbNum > Long.MAX_VALUE) {
                        return dbNum;
                    }
                    // 判断数字是否为整数值
                    long lngNum = (long) dbNum;
                    if (dbNum == lngNum) {
                        if (lngNum > Integer.MAX_VALUE) {
                            return lngNum;
                        } else {
                            return (int) lngNum;
                        }
                    } else {
                        return dbNum;
                    }

                case BOOLEAN:
                    return in.nextBoolean();

                case NULL:
                    in.nextNull();
                    return null;

                default:
                    throw new IllegalStateException();
            }
        }

        @Override
        public void write(JsonWriter out, Object value) throws IOException {
            // 序列化不处理
        }
    }

    public static class MapJsonDeserializer implements JsonDeserializer<Map<String, Object>> {

        public static final MapJsonDeserializer INSTANCE = new MapJsonDeserializer();

        @Override
        public Map<String, Object> deserialize(JsonElement json, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
            // 因为Gson反序列化map时默认是 LinkedTreeMap，所以这里也必须用 LinkedTreeMap，否则值的类型会变成 JsonPrimitive ，导致出现双层引号
            Map<String, Object> treeMap = new HashMap<>();
            JsonObject jsonObject = json.getAsJsonObject();
            Set<Map.Entry<String, JsonElement>> entrySet = jsonObject.entrySet();
            for (Map.Entry<String, JsonElement> entry : entrySet) {
                JsonElement ot = entry.getValue();
                if (ot.isJsonArray()) {
                    List<Object> deserializerList = ListJsonDeserializer.INSTANCE.deserialize(ot, type, jsonDeserializationContext);
                    treeMap.put(entry.getKey(), deserializerList);
//                    List<?> list = GSON.fromJson(ot.getAsJsonArray(), List.class);
//                    treeMap.put(entry.getKey(), list);
                } else if (ot.isJsonObject()) {
                    treeMap.put(entry.getKey(), deserialize(ot, type, jsonDeserializationContext));
                } else if (ot.isJsonNull()) {
                    treeMap.put(entry.getKey(), null);
                } else if (ot.isJsonPrimitive()) {
                    JsonPrimitive jsonPrimitive = ot.getAsJsonPrimitive();
                    if (jsonPrimitive.isNumber()) {
                        double doubleNum = ot.getAsDouble();
                        // 判断数字是否为整数值
                        // 数字超过long的最大值，返回浮点类型
                        if (doubleNum > Long.MAX_VALUE) {
                            treeMap.put(entry.getKey(), doubleNum);
                        } else {
                            // 判断数字是否为整数值
                            long longNum = (long) doubleNum;
                            if (doubleNum == longNum) {
                                if (longNum > Integer.MAX_VALUE) {
                                    treeMap.put(entry.getKey(), longNum);
                                } else {
                                    treeMap.put(entry.getKey(), (int) longNum);
                                }
                            } else {
                                treeMap.put(entry.getKey(), doubleNum);
                            }
                        }
                    } else if (jsonPrimitive.isBoolean()) {
                        treeMap.put(entry.getKey(), jsonPrimitive.getAsBoolean());
                    } else if (jsonPrimitive.isString()) {
                        treeMap.put(entry.getKey(), jsonPrimitive.getAsString());
                    } else {
                        treeMap.put(entry.getKey(), jsonPrimitive.getAsString());
                    }
                } else {
                    treeMap.put(entry.getKey(), ot.getAsString());
                }
            }
            return treeMap;
        }
    }

    public static class ListJsonDeserializer implements JsonDeserializer<List<Object>> {

        public static final ListJsonDeserializer INSTANCE = new ListJsonDeserializer();

        @Override
        public List<Object> deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            List<Object> results = new ArrayList<>();
            JsonArray jsonArray = json.getAsJsonArray();
            for (JsonElement ot : jsonArray) {
                if (ot.isJsonArray()) {
                    results.add(deserialize(ot, typeOfT, context));
                } else if (ot.isJsonObject()) {
                    results.add(MapJsonDeserializer.INSTANCE.deserialize(ot, typeOfT, context));
                } else if (ot.isJsonNull()) {
                    results.add(null);
                } else if (ot.isJsonPrimitive()) {
                    JsonPrimitive jsonPrimitive = ot.getAsJsonPrimitive();
                    if (jsonPrimitive.isNumber()) {
                        double doubleNum = ot.getAsDouble();
                        // 判断数字是否为整数值
                        // 数字超过long的最大值，返回浮点类型
                        if (doubleNum > Long.MAX_VALUE) {
                            results.add(doubleNum);
                        } else {
                            // 判断数字是否为整数值
                            long longNum = (long) doubleNum;
                            if (doubleNum == longNum) {
                                if (longNum > Integer.MAX_VALUE) {
                                    results.add(longNum);
                                } else {
                                    results.add((int) longNum);
                                }
                            } else {
                                results.add(doubleNum);
                            }
                        }
                    } else if (jsonPrimitive.isBoolean()) {
                        results.add(jsonPrimitive.getAsBoolean());
                    } else if (jsonPrimitive.isString()) {
                        results.add(jsonPrimitive.getAsString());
                    } else {
                        results.add(jsonPrimitive.getAsString());
                    }
                } else {
                    results.add(ot.getAsString());;
                }
            }
            return results;
        }
    }

    public static class MapJsonSerializer implements JsonSerializer<Map<String, Object>> {
        @Override
        public JsonElement serialize(Map<String, Object> src, Type typeOfSrc, JsonSerializationContext context) {
            if (src == null || src.isEmpty()) {
                return null;
            }

            final JsonObject jsonObject = new JsonObject();

            for (final Map.Entry<String, ?> entry : src.entrySet()) {
                JsonElement element = context.serialize(entry.getValue());
                jsonObject.add(entry.getKey(), element);
            }
            return jsonObject;
        }
    }

    public static class NumberTypeAdapter extends TypeAdapter<Number> {
        @Override
        public Number read(JsonReader in) throws IOException {
            // 反序列化
            JsonToken token = in.peek();
            switch (token) {
                case NUMBER:
                    // 改写数字的处理逻辑，将数字值分为整型与浮点型。
                    double dbNum = in.nextDouble();

                    // 判断数字是否为整数值
                    // 数字超过long的最大值，返回浮点类型
                    if (dbNum > Long.MAX_VALUE) {
                        return dbNum;
                    }
                    // 判断数字是否为整数值
                    long lngNum = (long) dbNum;
                    if (dbNum == lngNum) {
                        if (lngNum > Integer.MAX_VALUE) {
                            return lngNum;
                        } else {
                            return (int) lngNum;
                        }
                    } else {
                        return dbNum;
                    }
                case NULL:
                    in.nextNull();
                    return null;

                default:
                    throw new IllegalStateException();
            }
        }

        @Override
        public void write(JsonWriter out, Number value) throws IOException {
            out.value(value);
        }
    }

    public static class Apple {
        private Map<String, Object> automationMap;

        public Map<String, Object> getAutomationMap() {
            return automationMap;
        }

        public void setAutomationMap(Map<String, Object> automationMap) {
            this.automationMap = automationMap;
        }

        @Override
        public String toString() {
            return "Apple{" +
                    "automationMap=" + automationMap +
                    '}';
        }
    }

    public static class Banana {
        private String name;

        public Banana() {

        }

        public Banana(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        @Override
        public String toString() {
            return "Banana{" +
                    "name='" + name + '\'' +
                    '}';
        }
    }

    public static void main(String[] args) {

//        Date date1 = new Date();
//        System.out.println("date1: " + date1);
//
//        String s = GSON.toJson(new Date());
//        System.out.println("s: " + s);
//
//        Date date2 = GSON.fromJson(s, Date.class);
//        System.out.println("date2: " + date2);

//        Apple apple = new Apple();
//        apple.setAutomationMap(MapUtil.builder(new HashMap<String, Object>())
//                .put("1", 1.1).put("2", 2).put("3", 0.3).put("4", 4L).put("5", new BigDecimal("5.0")).put("6", "6")
//                .put("7", new Banana("haha")).put("8", 0x7fffffff11L)
//                .getMap());
//        String s1 = GSON.toJson(apple);
//        System.out.println("s1: " + s1);
//
//        Apple apple1 = GSON.fromJson(s1, Apple.class);
//        System.out.println("apple1: " + apple1);
//
//        String s2 = GSON.toJson("123");
//        System.out.println("s2: " + s2);



//        List<Banana> bananas = new ArrayList<>();
//        bananas.add(new Banana("123"));
//        bananas.add(new Banana("456"));
//        bananas.add(new Banana("789"));
//        String bananasJson = GsonUtil.GSON.toJson(bananas);
//        List<Banana> bananas1 = GsonUtil.GSON.fromJson(bananasJson, new TypeToken<List<Banana>>() {}.getType());
//        System.out.println("over");

        Map<String, String> map = MapUtil.builder(new HashMap<String, String>()).put("key1", "value1").put("key2", "value2").getMap();
        String s = GSON.toJson(map);
        System.out.println("s: " + s);
    }
}

