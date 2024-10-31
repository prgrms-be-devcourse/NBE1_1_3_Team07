package com.develetter.develetter.userfilter.service

import com.develetter.develetter.userfilter.dto.UserFilterReqDto
import com.develetter.develetter.userfilter.entity.UserFilter
import com.develetter.develetter.userfilter.repository.UserFilterRepository
import jakarta.persistence.EntityNotFoundException
import jakarta.transaction.Transactional
import org.springframework.stereotype.Service

@Service
class UserFilterServiceImpl(
    private val userFilterRepository: UserFilterRepository
) : UserFilterService {

    override fun getUserFilterByUserId(userId: Long): UserFilter {
        return userFilterRepository.findByUserId(userId).orElseThrow { EntityNotFoundException() }
    }

    @Transactional
    override fun registerUserFilter(userId: Long, userFilterReqDto: UserFilterReqDto) {
        var userFilter = userFilterRepository.findByUserId(userId).orElse(null)
        if (userFilter == null) {
            userFilter = UserFilterReqDto.toEntity(userId, userFilterReqDto)
        } else {
            userFilter.update(userFilterReqDto)
        }
        userFilterRepository.save(userFilter)
    }
}