package com.github.pettyfer.caas.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

/**
 * SpringApplicationContext工具类
 *
 * @author Petty
 */
@Slf4j
@Service
@Lazy(value = false)
public class SpringContextHolder implements ApplicationContextAware, DisposableBean {

    public static ApplicationContext applicationContext;

    @Override
    public void destroy() throws Exception {
        clearContext();
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        SpringContextHolder.applicationContext = applicationContext;
    }

    private void clearContext() {
        if (log.isDebugEnabled()) {
            log.debug("ApplicationContext clear");
        }
        applicationContext = null;
    }

    public static ApplicationContext getApplicationContext() {
        return applicationContext;
    }

    public static Object getBean(String name) {
        return applicationContext.getBean(name);
    }

    public static <T> T getBean(Class<T> clazz) {
        return applicationContext.getBean(clazz);
    }

    public static <T> T getBean(String name, Class<T> clazz) {
        return getApplicationContext().getBean(name, clazz);
    }
}
