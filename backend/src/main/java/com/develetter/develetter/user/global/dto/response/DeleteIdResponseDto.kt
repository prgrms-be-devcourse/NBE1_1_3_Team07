
import com.develetter.develetter.user.global.common.ResponseCode
import com.develetter.develetter.user.global.common.ResponseMessage
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity

class DeleteIdResponseDto : LogInResponseDto() {

    companion object {
        fun idNotFound(): ResponseEntity<LogInResponseDto> {
            val responseBody = LogInResponseDto(ResponseCode.ID_NOT_FOUND, ResponseMessage.ID_NOT_FOUND)
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(responseBody)
        }

        fun idNotMatching(): ResponseEntity<LogInResponseDto> {
            val responseBody = LogInResponseDto(ResponseCode.ID_NOT_MATCHING, ResponseMessage.ID_NOT_MATCHING)
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(responseBody)
        }
    }
}