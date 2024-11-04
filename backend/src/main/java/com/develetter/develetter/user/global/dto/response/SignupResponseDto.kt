
import com.develetter.develetter.user.global.common.ResponseCode
import com.develetter.develetter.user.global.common.ResponseMessage
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity

class SignupResponseDto(
    val role: String
) : LogInResponseDto() {

    companion object {
        fun duplicateId(): ResponseEntity<LogInResponseDto> {
            val responseBody = LogInResponseDto(ResponseCode.DUPLICATE_ID, ResponseMessage.DUPLICATE_ID)
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseBody)
        }

        fun certificationFail(): ResponseEntity<LogInResponseDto> {
            val responseBody = LogInResponseDto(ResponseCode.CERTIFICATION_FAIL, ResponseMessage.CERTIFICATION_FAIL)
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(responseBody)
        }

        fun wrongRole(): ResponseEntity<LogInResponseDto> {
            val responseBody = LogInResponseDto(ResponseCode.WRONG_ROLE, ResponseMessage.WRONG_ROLE)
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseBody)
        }

        fun success(role: String): ResponseEntity<SignupResponseDto> {
            val responseBody = SignupResponseDto(role)
            return ResponseEntity.status(HttpStatus.OK).body(responseBody)
        }
    }
}