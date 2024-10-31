package com.develetter.develetter.user.global.common

interface ResponseMessage {
    companion object {
        const val SUCCESS = "Success"
        const val VALIDATION_FAIL = "Validation failed"
        const val DUPLICATE_ID = "Duplicate ID"
        const val SIGN_IN_FAIL = "Login information mismatch"
        const val CERTIFICATION_FAIL = "Certification failed"
        const val MAIL_FAIL = "Failed to send email"
        const val DATABASE_ERROR = "Database error"
        const val ID_NOT_FOUND = "ID Not found"
        const val ID_NOT_MATCHING = "ID does not match"
        const val WRONG_ROLE = "Wrong role."
    }
}