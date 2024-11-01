import com.develetter.develetter.user.global.common.Role
import com.develetter.develetter.user.global.dto.response.*
import com.develetter.develetter.user.global.dto.request.*
import com.develetter.develetter.user.global.entity.CertificationEntity
import com.develetter.develetter.user.provider.CertificationNumberProvider
import com.develetter.develetter.user.provider.EmailProvider
import com.develetter.develetter.user.provider.JwtProvider
import com.develetter.develetter.user.repository.CertificationRepository
import com.develetter.develetter.user.repository.UserRepository
import jakarta.transaction.Transactional
import org.slf4j.LoggerFactory
import org.springframework.http.ResponseEntity
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import java.time.LocalDateTime

@Service
class UserServiceImpl(
    private val userRepository: UserRepository,
    private val certificationRepository: CertificationRepository,
    private val jwtProvider: JwtProvider,
    private val emailProvider: EmailProvider
) : UserService {

    private val passwordEncoder: PasswordEncoder = BCryptPasswordEncoder()
    private val log = LoggerFactory.getLogger(UserServiceImpl::class.java)

    override fun idCheck(dto: IdCheckRequestDto): ResponseEntity<out LogInResponseDto> {
        return try {
            val accountId = dto.email
            if (userRepository.existsByAccountId(accountId)) {
                IdCheckResponseDto.duplicateId()
            } else {
                LogInResponseDto.success()
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
            if (userRepository.existsByAccountId(accountId)) {
                return EmailCertificationResponseDto.duplicateId()
            }

            val certificationNumber = CertificationNumberProvider.generateNumber()
            if (!emailProvider.sendVerificationEmail(email, certificationNumber)) {
                return EmailCertificationResponseDto.mailSendFail()
            }

            val certificationEntity = CertificationEntity(
                accountId = accountId,
                email = email,
                certificationNumber = certificationNumber
            )
            certificationRepository.save(certificationEntity)
            LogInResponseDto.success()
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

            val isMatch = certificationEntity.email == dto.email &&
                    certificationEntity.certificationNumber == dto.certificationNumber

            if (!isMatch) CheckCertificationResponseDto.certificationFail() else LogInResponseDto.success()
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
                return SignupResponseDto.duplicateId()
            }

            val certificationEntity = certificationRepository.findByAccountId(accountId)
                ?: return SignupResponseDto.certificationFail()

            val isMatched = certificationEntity.email == dto.email &&
                    certificationEntity.certificationNumber == dto.certificationNumber

            if (!isMatched) return SignupResponseDto.certificationFail()

            val encodedPassword = passwordEncoder.encode(dto.password)
            val userEntity = UserEntity(
                accountId = accountId,
                password = encodedPassword,
                email = dto.email,
                type = "general",
                role = Role.USER,
                subscription = "NO",
            )
            userRepository.save(userEntity)
            certificationRepository.deleteByAccountId(accountId)
            SignupResponseDto.success(Role.USER)
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

            if (!passwordEncoder.matches(dto.password, userEntity.password)) {
                return SigninResponseDto.signInFail()
            }

            val token = userEntity.id?.let { jwtProvider.create(it, userEntity.role) }
            SigninResponseDto.success(token,userEntity.role)
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

            if (!passwordEncoder.matches(dto.password, userEntity.password)) {
                return DeleteIdResponseDto.idNotMatching()
            }

            userRepository.delete(userEntity)
            certificationRepository.deleteByAccountId(accountId)
            LogInResponseDto.success()
        } catch (e: Exception) {
            log.info("회원 삭제 실패: {}", e.message)
            LogInResponseDto.databaseError()
        }
    }

    override fun registerSubscribe(dto: RegisterSubscribeRequestDto): ResponseEntity<out LogInResponseDto> {
        return try {
            val userEntity = userRepository.findById(dto.userId)
            val updateUserEntity = UserEntity(
                accountId = userEntity.accountId,
                password = userEntity.password,
                email = userEntity.email,
                type = userEntity.type,
                role = userEntity.role,
                subscription = dto.subscribeType,
            )
            userRepository.save(updateUserEntity)
            LogInResponseDto.success()
        } catch (e: Exception) {
            log.info("구독 등록 실패: {}", e.message)
            LogInResponseDto.databaseError()
        }
    }

    override fun getEmailByUserId(id: Long): String {
        val user = userRepository.findById(id)
        return user.email
    }

    override fun getAllUsers(): List<UserEntity> {
        return userRepository.findAll()
    }
}