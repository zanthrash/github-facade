package com.zanthrash.config

import com.zanthrash.utils.CacheNames
import net.sf.ehcache.config.CacheConfiguration
import org.springframework.cache.CacheManager
import org.springframework.cache.annotation.CachingConfigurer
import org.springframework.cache.annotation.EnableCaching
import org.springframework.cache.ehcache.EhCacheCacheManager
import org.springframework.cache.interceptor.DefaultKeyGenerator
import org.springframework.cache.interceptor.KeyGenerator
import org.springframework.cache.interceptor.SimpleKeyGenerator
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class CachingConfig implements CachingConfigurer{

    @Bean
    @Override
    CacheManager cacheManager() {
        return new EhCacheCacheManager(ehCacheManager())
    }

    @Bean(destroyMethod = "shutdown")
    public net.sf.ehcache.CacheManager ehCacheManager() {

        net.sf.ehcache.config.Configuration config = new net.sf.ehcache.config.Configuration()

        config.addCache(createPullRequestCacheConfig())

        return net.sf.ehcache.CacheManager.newInstance(config)
    }

    private CacheConfiguration createPullRequestCacheConfig() {
        CacheConfiguration cacheConfig = new CacheConfiguration()
        cacheConfig.setName(CacheNames.ORG_REPOS_BY_PULL_REQUEST.name)
        cacheConfig.setMemoryStoreEvictionPolicy("LRU")
        cacheConfig.setMaxEntriesLocalHeap(100L)
        cacheConfig.setEternal(false)
        cacheConfig.setTimeToIdleSeconds(30L)
        cacheConfig.setTimeToLiveSeconds(30L)
        cacheConfig
    }

    @Bean
    @Override
    KeyGenerator keyGenerator() {
        return new SimpleKeyGenerator()
    }
}
