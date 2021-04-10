package com.github.pettyfer.caas.global.provider.token;

import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.security.oauth2.provider.token.store.redis.StandardStringSerializationStrategy;

/**
 * Token储存自定义序列化
 * @author Petty
 */
public class JdkSerializationStrategy extends StandardStringSerializationStrategy {

    private final RedisSerializer defaultSerializer;

    public JdkSerializationStrategy(RedisSerializer<Object> redisSerializer) {
        this.defaultSerializer = redisSerializer;
    }

    @Override
    @SuppressWarnings("unchecked")
    protected <T> T deserializeInternal(byte[] bytes, Class<T> clazz) {
        return (T) defaultSerializer.deserialize(bytes);
    }

    @Override
    @SuppressWarnings("unchecked")
    protected byte[] serializeInternal(Object object) {
        return defaultSerializer.serialize(object);
    }
}
