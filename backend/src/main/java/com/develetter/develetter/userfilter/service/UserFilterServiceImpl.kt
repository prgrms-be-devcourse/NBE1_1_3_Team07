package com.develetter.develetter.userfilter.service;

import com.develetter.develetter.userfilter.dto.UserFilterReqDto;
import com.develetter.develetter.userfilter.entity.UserFilter;
import com.develetter.develetter.userfilter.repository.UserFilterRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserFilterServiceImpl implements UserFilterService{

    private final UserFilterRepository userFilterRepository;

    public UserFilter getUserFilterByUserId(Long userId){
        return userFilterRepository.findByUserId(userId).orElseThrow(EntityNotFoundException::new);
    }

    @Override
    @Transactional
    public void registerUserFilter(Long userId, UserFilterReqDto userFilterReqDto) {
        UserFilter userFilter = userFilterRepository.findByUserId(userId).orElse(null);
        if (userFilter == null) {
            userFilter = UserFilterReqDto.toEntity(userId, userFilterReqDto);
        } else {
            userFilter.update(userFilterReqDto);
        }

        userFilterRepository.save(userFilter);
    }

}
