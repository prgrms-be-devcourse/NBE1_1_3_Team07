package com.develetter.develetter.jobposting.service

import org.springframework.data.redis.core.RedisTemplate
import org.springframework.stereotype.Service

@Service
class FilteredJobPostingCacheService(
    private val redisTemplate: RedisTemplate<String, Any>
) {

    fun addJobPostingToCache(userId: Long, jobPostingId: Long) {
        val cacheKey = "filteredJobPosting:$userId"
        redisTemplate.opsForSet().add(cacheKey, jobPostingId.toString())
    }

    fun getJobPostingsFromCache(userId: Long): Set<Any>? {
        val cacheKey = "filteredJobPosting:$userId"
        return redisTemplate.opsForSet().members(cacheKey)
    }

    fun clearCache(userId: Long) {
        val cacheKey = "filteredJobPosting:\$userId"
        redisTemplate.delete(cacheKey)
    }
}
