package com.sprve.Util;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONReader;
import com.alibaba.fastjson2.JSONWriter;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.SerializationException;

import java.nio.charset.Charset;
import java.util.AbstractMap;
import java.util.Map;

public class FastJson2RedisSerializer<T> implements RedisSerializer<T> {

    private final Class<T> clazz;
    //序列化.
    @Override
    public byte[] serialize(T t) throws SerializationException {
        if (t == null) {
            return new byte[0];
        }
        Map.Entry<String, T> entity = new AbstractMap.SimpleEntry<>(t.getClass().getName(), t);
        return JSON.toJSONString(entity, JSONWriter.Feature.WriteClassName).getBytes(Charset.defaultCharset());
    }
    //反序列化.
    @Override
    public T deserialize(byte[] bytes) throws SerializationException {
        if (bytes == null) {
            return null;
        }
        String str = new String(bytes, Charset.defaultCharset());
        int index = str.indexOf(":");
        String cls = str.substring(2, index - 1);
        String obj = str.substring(index + 1, str.length() - 1);
        return JSON.parseObject(
                obj,
                clazz,
                JSONReader.autoTypeFilter(
                        cls
                ),
                JSONReader.Feature.SupportClassForName);
    }

    public FastJson2RedisSerializer(Class<T> clazz) {
        super();
        this.clazz = clazz;
    }
}