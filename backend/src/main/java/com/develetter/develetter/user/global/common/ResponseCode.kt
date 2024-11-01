package com.develetter.develetter.user.global.common

interface ResponseCode {
    companion object {
        const val SUCCESS = "SU"
        const val VALIDATION_FAIL = "VF"
        const val DUPLICATE_ID = "DI"
        const val SIGN_IN_FAIL = "SF"
        const val CERTIFICATION_FAIL = "CF"
        const val MAIL_FAIL = "MF"
        const val DATABASE_ERROR = "DBE"
        const val ID_NOT_FOUND = "IDNF"
        const val ID_NOT_MATCHING = "IDNM"
        const val WRONG_ROLE = "WR"
    }
}