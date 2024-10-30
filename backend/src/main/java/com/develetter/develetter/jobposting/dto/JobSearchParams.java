package com.develetter.develetter.jobposting.dto;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public record JobSearchParams(
        String keywords,
        Integer bbs_gb,
        String stock,
        String sr,
        String loc_cd,
        String loc_mcd,
        String loc_bcd,
        String ind_cd,
        String job_mid_cd,
        String job_cd,
        String job_type,
        String edu_lv,
        String fields,
        String published,
        String published_min,
        String published_max,
        String updated,
        String updated_min,
        String updated_max,
        String deadline,
        Integer start,
        Integer count,
        String sort
) {
    public static JobSearchParams defaultParams(int start, String published_min) {

        return new JobSearchParams(
                null, null, null, null, null, null, null, null,
                null, "84+86+87", null, null, "posting-date+expiration-date+count", null, published_min, null,
                null, null, null, null, start, 100, null);
    }
}
