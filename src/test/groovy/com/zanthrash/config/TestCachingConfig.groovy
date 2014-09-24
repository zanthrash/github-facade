package com.zanthrash.config

import org.springframework.cache.Cache
import org.springframework.cache.CacheManager
import org.springframework.cache.annotation.CachingConfigurer
import org.springframework.cache.concurrent.ConcurrentMapCache
import org.springframework.cache.interceptor.KeyGenerator
import org.springframework.cache.interceptor.SimpleKeyGenerator
import org.springframework.cache.support.SimpleCacheManager
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class TestCachingConfig implements CachingConfigurer{

    @Bean
    @Override
    CacheManager cacheManager() {
        SimpleCacheManager cacheManager = new SimpleCacheManager()

        List<Cache> caches = new ArrayList<Cache>()
        caches.add(new ConcurrentMapCache('orgReposByPullRequests'))
        cacheManager.setCaches(caches)

        return cacheManager
    }

    @Bean
    @Override
    KeyGenerator keyGenerator() {
        return new SimpleKeyGenerator()
    }
}
