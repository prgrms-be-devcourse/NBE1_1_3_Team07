package com.develetter.develetter.jobposting.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class FilteredJobPostingCacheService {

    private final RedisTemplate<String, Object> redisTemplate;

    @Autowired
    public FilteredJobPostingCacheService(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public void addJobPostingToCache(Long userId, Long jobPostingId) {
        String cacheKey = "filteredJobPosting:" + userId;
        redisTemplate.opsForSet().add(cacheKey, jobPostingId.toString());
    }

    public Set<Object> getJobPostingsFromCache(Long userId) {
        String cacheKey = "filteredJobPosting:" + userId;
        return redisTemplate.opsForSet().members(cacheKey);
    }

    public void clearCache(Long userId) {
        String cacheKey = "filteredJobPosting:" + userId;
        redisTemplate.delete(cacheKey);
    }
}
