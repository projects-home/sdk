package com.x.sdk.util;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.x.sdk.serialize.TypeGetter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class JsonUtil {
    private static transient final Logger log = LoggerFactory.getLogger(JsonUtil.class);
    private final static ObjectMapper mapper = new ObjectMapper();

    static {
        init();
    }

    private JsonUtil() {
        // 禁止私有化
    }

    private static void init() {
        // 这个特性，决定了解析器是否将自动关闭那些不属于parser自己的输入源。
        // 如果禁止，则调用应用不得不分别去关闭那些被用来创建parser的基础输入流InputStream和reader；
        // 默认是true
        mapper.configure(JsonParser.Feature.AUTO_CLOSE_SOURCE, true);
        // 是否允许解析使用Java/C++ 样式的注释（包括'/'+'*' 和'//' 变量）
        mapper.configure(JsonParser.Feature.ALLOW_COMMENTS, true);

        // 设置为true时，属性名称不带双引号
        mapper.configure(JsonGenerator.Feature.QUOTE_FIELD_NAMES, false);
        // 反序列化是是否允许属性名称不带双引号
        mapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);

        // 是否允许单引号来包住属性名称和字符串值
        mapper.configure(JsonParser.Feature.ALLOW_SINGLE_QUOTES, true);

        // 是否允许JSON字符串包含非引号控制字符（值小于32的ASCII字符，包含制表符和换行符）
        mapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_CONTROL_CHARS, true);

        // 是否允许JSON整数以多个0开始
        mapper.configure(JsonParser.Feature.ALLOW_NUMERIC_LEADING_ZEROS, true);

        // null的属性不序列化
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);

        // 按字母顺序排序属性,默认false
        mapper.configure(MapperFeature.SORT_PROPERTIES_ALPHABETICALLY, true);

        // 是否以类名作为根元素，可以通过@JsonRootName来自定义根元素名称,默认false
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);

        // 是否缩放排列输出,默认false
        mapper.configure(SerializationFeature.INDENT_OUTPUT, false);

        // 序列化Date日期时以timestamps输出，默认true
        mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, true);
        // 设置JSON时间格式
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        mapper.setDateFormat(df);

        // 序列化枚举是否以toString()来输出，默认false，即默认以name()来输出
        mapper.configure(SerializationFeature.WRITE_ENUMS_USING_TO_STRING, true);

        // 序列化枚举是否以ordinal()来输出，默认false
        mapper.configure(SerializationFeature.WRITE_ENUMS_USING_INDEX, false);

        // 序列化单元素数组时不以数组来输出，默认false
        mapper.configure(SerializationFeature.WRITE_ENUMS_USING_TO_STRING, true);

        // 序列化Map时对key进行排序操作，默认false
        mapper.configure(SerializationFeature.ORDER_MAP_ENTRIES_BY_KEYS, true);

        // 序列化char[]时以json数组输出，默认false
        mapper.configure(SerializationFeature.WRITE_CHAR_ARRAYS_AS_JSON_ARRAYS, true);

    }

    /**
     * 序列化为json对象
     *
     * @param obj
     * @return
     * @throws Exception
     */
    public static String toJson(Object obj) throws Exception {
        String json = mapper.writeValueAsString(obj);
        if (log.isInfoEnabled()) {
            log.info(obj + " trasform into json:" + json);
        }
        return json;
    }

    /**
     * 从json字符串变回对象 如果是Map，List等请使用另外一个方法
     *
     * @param json
     * @param clazz
     * @return
     * @throws Exception
     */
    public static <T> T fromJson(String json, Class<T> clazz) throws Exception {
        T t = mapper.readValue(json, clazz);
        if (log.isInfoEnabled()) {
            log.info(json + " trasform into class:" + clazz + ",object:" + t);
        }
        return t;
    }

    /**
     * 根据类型进行json的反序列化，主要用于系列化为Map/List等之类含有类型定义的new TypeGetter<Map<String,
     * List<String>>>() { }
     *
     * @param json
     * @param type
     * @return
     * @throws Exception
     */
    public static <T> T fromJson(String json, TypeGetter<T> type) throws Exception {
        T t = mapper.readValue(json, type);
        if (log.isInfoEnabled()) {
            log.info(json + " trasform into class:" + type.getType() + ",object:" + t);
        }
        return t;
    }

    public static void main(String[] args) throws Exception {
        Map<String, String> map = new HashMap<>();
        map.put("test", "123456");
        map.put("123456", "hello");
        String json = JsonUtil.toJson(map);
        System.out.println(json);
        Date now = new Date();
        System.out.println(JsonUtil.toJson(now));
        Map<String, String> m = JsonUtil.fromJson(json, new TypeGetter<Map<String, String>>() {
        });
        System.out.println(m);
    }
}

