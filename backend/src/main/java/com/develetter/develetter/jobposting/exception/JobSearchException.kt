package com.develetter.develetter.jobposting.exception

class JobSearchException : RuntimeException {
    constructor(message: String?) : super(message)

    constructor(message: String?, cause: Throwable?) : super(message, cause)
}