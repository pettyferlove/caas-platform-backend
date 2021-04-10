package com.github.pettyfer.caas.configuration;

import com.github.pettyfer.caas.global.provider.repository.RedisTokenRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.web.authentication.rememberme.InMemoryTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;

/**
 * @author Petty
 */

@Slf4j
@Configuration
public class RedisTokenRepositoryConfiguration {

    @Bean("tokenRepository")
    @Primary
    @ConditionalOnBean(name = "redisTemplate")
    public PersistentTokenRepository redisTokenRepository(RedisTemplate<Object, Object> redisTemplate) {
        return new RedisTokenRepository(redisTemplate);
    }

    @Bean("tokenRepository")
    @Primary
    @ConditionalOnMissingBean(name = "redisTemplate")
    public PersistentTokenRepository tokenRepository() {
        return new InMemoryTokenRepositoryImpl();
    }

}
