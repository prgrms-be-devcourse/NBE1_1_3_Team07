package com.develetter.develetter.userfilter.service

import com.develetter.develetter.userfilter.dto.UserFilterReqDto
import com.develetter.develetter.userfilter.entity.UserFilter

interface UserFilterService {
    fun getUserFilterByUserId(userId: Long): UserFilter
    fun registerUserFilter(userId: Long, userFilterReqDto: UserFilterReqDto)
}