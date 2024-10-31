package com.develetter.develetter.user.service.implement

import com.develetter.develetter.user.global.common.Role
import com.develetter.develetter.user.global.dto.request.*
import com.develetter.develetter.user.global.entity.CertificationEntity
import com.develetter.develetter.user.provider.CertificationNumberProvider
import com.develetter.develetter.user.provider.EmailProvider
import com.develetter.develetter.user.provider.JwtProvider
import com.develetter.develetter.user.repository.CertificationRepository
import com.develetter.develetter.user.repository.UserRepository
import com.develetter.develetter.user.service.UserService
import com.example.demo.user.global.dto.LogInResponseDto
import com.example.demo.user.global.dto.response.CheckCertificationResponseDto
import com.example.demo.user.global.dto.response.DeleteIdResponseDto
import com.example.demo.user.global.dto.response.IdCheckResponseDto
import com.example.demo.user.global.dto.response.RegisterSubscribeResponseDto
import com.example.demo.user.global.dto.response.SigninResponseDto
import com.example.demo.user.global.dto.response.SignupResponseDto
import com.example.demo.user.global.entity.UserEntity
import org.slf4j.LoggerFactory
import org.springframework.http.ResponseEntity
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime

@Service
open class UserServiceImpl(
    private val userRepository: UserRepository,
    private val certificationRepository: CertificationRepository,
    private val jwtProvider: JwtProvider,
    private val emailProvider: EmailProvider
) : UserService {

    private val log = LoggerFactory.getLogger(UserServiceImpl::class.java)
    private val passwordEncoder: PasswordEncoder = BCryptPasswordEncoder()

    override fun idCheck(dto: IdCheckRequestDto): ResponseEntity<out LogInResponseDto> {
        return try {
            val accountId = dto.email
            if (userRepository.existsByAccountId(accountId)) {
                IdCheckResponseDto.duplicateId()
            } else {
                IdCheckResponseDto.success()
            }
        } catch (e: Exception) {
            log.info("ID 중복 체크 실패: {}", e.message)
            LogInResponseDto.databaseError()
        }
    }

    override fun emailCertification(dto: EmailCertificationRequestDto): ResponseEntity<out LogInResponseDto> {
        return try {
            val email = dto.email
            val accountId = email

            // 인증 번호 생성 및 이메일 전송
            val certificationNumber = CertificationNumberProvider.generateNumber()
            if (!emailProvider.sendVerificationEmail(email, certificationNumber)) {
                EmailCertificationResponseDto.mailSendFail()
            } else {
                val certificationEntity = CertificationEntity(accountId = accountId, email = email, certificationNumber = certificationNumber)
                certificationRepository.save(certificationEntity)
                EmailCertificationResponseDto.success()
            }
        } catch (e: Exception) {
            log.info("이메일 인증 실패: {}", e.message)
            LogInResponseDto.databaseError()
        }
    }

    override fun checkCertification(dto: CheckCertificationRequestDto): ResponseEntity<out LogInResponseDto> {
        return try {
            val accountId = dto.email
            val certificationEntity = certificationRepository.findTopByAccountIdOrderByCreatedAtDesc(accountId)
                ?: return CheckCertificationResponseDto.certificationFail()

            val isMatch = certificationEntity.email == dto.email && certificationEntity.certificationNumber == dto.certificationNumber
            if (isMatch) {
                CheckCertificationResponseDto.success()
            } else {
                CheckCertificationResponseDto.certificationFail()
            }
        } catch (e: Exception) {
            log.info("인증 번호 확인 실패: {}", e.message)
            LogInResponseDto.databaseError()
        }
    }

    @Transactional
    override fun signUp(dto: SignupRequestDto): ResponseEntity<out LogInResponseDto> {
        return try {
            val accountId = dto.email
            if (userRepository.existsByAccountId(accountId)) {
                SignupResponseDto.duplicateId()
            } else {
                val certificationEntity = certificationRepository.findByAccountId(accountId)
                    ?: return SignupResponseDto.certificationFail()

                val isMatched = certificationEntity.email == dto.email &&
                        certificationEntity.certificationNumber == dto.certificationNumber

                if (!isMatched) {
                    return SignupResponseDto.certificationFail()
                }

                // 비밀번호 암호화 후 저장
                val encodedPassword = passwordEncoder.encode(dto.password)
                val userEntity = UserEntity(
                    accountId = accountId,
                    password = encodedPassword,
                    email = dto.email,
                    type = "general",
                    role = Role.USER,
                    subscription = "NO",
                    createdAt = LocalDateTime.now(),
                    updatedAt = LocalDateTime.now()
                )
                userRepository.save(userEntity)
                certificationRepository.deleteByAccountId(accountId)
                SignupResponseDto.success("ROLE_USER")
            }
        } catch (e: Exception) {
            log.info("회원 가입 실패: {}", e.message)
            LogInResponseDto.databaseError()
        }
    }

    override fun signIn(dto: SigninRequestDto): ResponseEntity<out LogInResponseDto> {
        return try {
            val accountId = dto.email
            val userEntity = userRepository.findByAccountId(accountId)
                ?: return SigninResponseDto.signInFail()

            val isMatched = passwordEncoder.matches(dto.password, userEntity.password)
            if (!isMatched) return SigninResponseDto.signInFail()

            val token = jwtProvider.create(accountId, userEntity.role)
            SigninResponseDto.success(token, userEntity.role, userEntity.email, userEntity.subscription)
        } catch (e: Exception) {
            log.info("로그인 실패: {}", e.message)
            SigninResponseDto.signInFail()
        }
    }

    override fun deleteId(dto: DeleteIdRequestDto): ResponseEntity<out LogInResponseDto> {
        return try {
            val accountId = dto.email
            val userEntity = userRepository.findByAccountId(accountId)
                ?: return DeleteIdResponseDto.idNotFound()

            val isMatched = passwordEncoder.matches(dto.password, userEntity.password)
            if (!isMatched) return DeleteIdResponseDto.idNotMatching()

            userRepository.delete(userEntity)
            certificationRepository.deleteByAccountId(accountId)
            DeleteIdResponseDto.success()
        } catch (e: Exception) {
            log.info("회원 삭제 실패: {}", e.message)
            LogInResponseDto.databaseError()
        }
    }


    override fun registerSubscribe(dto: RegisterSubscribeRequestDto): ResponseEntity<out LogInResponseDto> {
        return try {
            val userId = dto.userId
            val subscribeType = dto.subscribeType
            val userEntity = userRepository.findById(userId)
                ?: return LogInResponseDto.databaseError() // 사용자 없음 오류 반환

            // 구독 정보를 업데이트할 새로운 엔티티 생성
            val updateUserEntity = UserEntity(
                id = userId,
                accountId = userEntity.accountId,
                password = userEntity.password,
                email = userEntity.email,
                type = userEntity.type,
                role = userEntity.role,
                createdAt = LocalDateTime.now(),
                updatedAt = LocalDateTime.now(),
                subscription = subscribeType
            )

            userRepository.save(updateUserEntity)
            RegisterSubscribeResponseDto.success()  // 구독 등록 성공 응답
        } catch (e: Exception) {
            log.info("구독 등록 실패: {}", e.message)
            LogInResponseDto.databaseError()  // 데이터베이스 오류 응답
        } as ResponseEntity<out LogInResponseDto>
    }

    override fun getEmailByUserId(id: Long): String {
        return userRepository.findById(id)?.email ?: ""
    }

    override fun getAllUsers(): List<UserEntity> {
        return userRepository.findAll()
    }
}