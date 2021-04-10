package com.github.pettyfer.caas.global.provider.repository;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ScanOptions;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.security.web.authentication.rememberme.PersistentRememberMeToken;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * @author Petty
 */
@Slf4j
public class RedisTokenRepository implements PersistentTokenRepository {

    private static final Long TOKEN_VALID_DAYS = 10L;
    private static final String REDIS_REMEMBER_ME_TOKEN_KEY = "spring:security:remember-me:token:";

    private final RedisTemplate<Object, Object> redisTemplate;

    public RedisTokenRepository(RedisTemplate<Object, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }


    @Override
    public void createNewToken(PersistentRememberMeToken token) {
        if (log.isDebugEnabled()) {
            log.debug("token create seriesId: [{}]", token.getSeries());
        }
        String key = generateKey(token.getSeries());
        HashMap<String, String> map = new HashMap<>(4);
        map.put("username", token.getUsername());
        map.put("tokenValue", token.getTokenValue());
        map.put("date", String.valueOf(token.getDate().getTime()));
        redisTemplate.opsForHash().putAll(key, map);
        redisTemplate.expire(key, TOKEN_VALID_DAYS, TimeUnit.DAYS);
    }

    @Override
    public void updateToken(String series, String tokenValue, Date lastUsed) {
        String key = generateKey(series);
        HashMap<String, String> map = new HashMap<>(4);
        map.put("tokenValue", tokenValue);
        map.put("date", String.valueOf(lastUsed.getTime()));
        redisTemplate.opsForHash().putAll(key, map);
        redisTemplate.expire(key, TOKEN_VALID_DAYS, TimeUnit.DAYS);
    }

    @Override
    public PersistentRememberMeToken getTokenForSeries(String seriesId) {
        String key = generateKey(seriesId);
        List<Object> hashKeys = new ArrayList<>();
        hashKeys.add("username");
        hashKeys.add("tokenValue");
        hashKeys.add("date");
        List<Object> hashValues = redisTemplate.opsForHash().multiGet(key, hashKeys);
        String username = (String) hashValues.get(0);
        String tokenValue = (String) hashValues.get(1);
        String date = (String) hashValues.get(2);
        if (null == username || null == tokenValue || null == date) {
            return null;
        }
        Long timestamp = Long.valueOf(date);
        Date time = new Date(timestamp);
        return new PersistentRememberMeToken(username, seriesId, tokenValue, time);
    }

    @Override
    public void removeUserTokens(String username) {
        if (log.isDebugEnabled()) {
            log.debug("token remove username: [{}]", username);
        }
        byte[] hashKey = new StringRedisSerializer().serialize("username");
        RedisConnection redisConnection = Objects.requireNonNull(redisTemplate.getConnectionFactory()).getConnection();
        try (Cursor<byte[]> cursor = redisConnection.scan(ScanOptions.scanOptions().match(generateKey("*")).count(1024).build())) {
            while (cursor.hasNext()) {
                byte[] key = cursor.next();
                assert hashKey != null;
                byte[] hashValue = redisConnection.hGet(key, hashKey);
                String storeName = new StringRedisSerializer().deserialize(hashValue);
                if (username.equals(storeName)) {
                    redisConnection.expire(key, 0L);
                    return;
                }
            }
        } catch (IOException ex) {
            log.warn("token remove exception", ex);
        }
    }

    /**
     * 生成key
     *
     * @param series
     * @return
     */
    private String generateKey(String series) {
        return REDIS_REMEMBER_ME_TOKEN_KEY + series;
    }
}
