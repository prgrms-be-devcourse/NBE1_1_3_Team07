import com.develetter.develetter.user.global.dto.request.CheckCertificationRequestDto
import com.develetter.develetter.user.global.dto.request.DeleteIdRequestDto
import com.develetter.develetter.user.global.dto.request.EmailCertificationRequestDto
import com.develetter.develetter.user.global.dto.request.IdCheckRequestDto
import com.develetter.develetter.user.global.dto.request.RegisterSubscribeRequestDto
import com.develetter.develetter.user.global.dto.request.SigninRequestDto
import com.develetter.develetter.user.global.dto.request.SignupRequestDto
import org.springframework.http.ResponseEntity


interface UserService {
    fun idCheck(dto: IdCheckRequestDto): ResponseEntity<out LogInResponseDto>
    fun emailCertification(dto: EmailCertificationRequestDto): ResponseEntity<out LogInResponseDto>
    fun checkCertification(dto: CheckCertificationRequestDto): ResponseEntity<out LogInResponseDto>
    fun signUp(dto: SignupRequestDto): ResponseEntity<out LogInResponseDto>
    fun signIn(dto: SigninRequestDto): ResponseEntity<out LogInResponseDto>
    fun deleteId(dto: DeleteIdRequestDto): ResponseEntity<out LogInResponseDto>
    fun registerSubscribe(dto: RegisterSubscribeRequestDto): ResponseEntity<out LogInResponseDto>
    fun getEmailByUserId(id: Long): String
    fun getAllUsers(): List<UserEntity>
}