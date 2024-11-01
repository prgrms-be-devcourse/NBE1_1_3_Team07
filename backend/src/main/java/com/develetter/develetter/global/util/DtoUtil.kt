package com.develetter.develetter.global.util

import org.slf4j.LoggerFactory
import java.lang.reflect.Field
import kotlin.reflect.full.declaredMemberProperties
import kotlin.reflect.jvm.isAccessible

object DtoUtil {
    private val log = LoggerFactory.getLogger(DtoUtil::class.java)

    fun toMap(obj: Any): Map<String, Any?> {
        return obj::class.declaredMemberProperties
            .associate { prop ->
                prop.isAccessible = true
                prop.name to prop.call(obj)
            }
    }

}
