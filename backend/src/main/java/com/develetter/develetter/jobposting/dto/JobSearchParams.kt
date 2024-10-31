package com.develetter.develetter.jobposting.dto

import com.develetter.develetter.global.util.DtoUtil

data class JobSearchParams(
    val keywords: String? = null,
    val bbs_gb: Int? = null,
    val stock: String? = null,
    val sr: String? = null,
    val loc_cd: String? = null,
    val loc_mcd: String? = null,
    val loc_bcd: String? = null,
    val ind_cd: String? = null,
    val job_mid_cd: String? = null,
    val job_cd: String = "84+86+87",
    val job_type: String? = null,
    val edu_lv: String? = null,
    val fields: String = "posting-date+expiration-date+count",
    val published: String? = null,
    val published_min: String,
    val published_max: String? = null,
    val updated: String? = null,
    val updated_min: String? = null,
    val updated_max: String? = null,
    val deadline: String? = null,
    val start: Int,
    val count: Int = 100,
    val sort: String? = null
) {
    // Companion 객체를 반환하는 대신 직접 Map을 리턴하도록 변경
    companion object {
        fun defaultParams(start: Int, publishedMin: String): Map<String, Any?> {
            return JobSearchParams(
                start = start,
                published_min = publishedMin
            ).let { DtoUtil.toMap(it) }
        }
    }
}
