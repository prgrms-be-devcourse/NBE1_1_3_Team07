package com.develetter.develetter.blog.Util

import java.util.*

object BlogUtil {
    // 블로그 키워드 목록을 리스트로 변환하는 메서드
    fun getBlogKeywordsAsList(blogKeywords: String?): List<String> {
        return blogKeywords?.split(",")?.map { it.trim() }?.filter { it.isNotEmpty() } ?: emptyList()
    }

    fun isBlogDetailPage(link: String): Boolean {
        if (link.contains("toss.tech")) {
            return link.contains("/article/")
        }
        if (link.contains("techtopic.skplanet.com")) {
            return link != "https://techtopic.skplanet.com/"
        }
        if (link.contains("tech.devsisters.com")) {
            return link.contains("/posts/")
        }
        if (link.contains("devocean.sk.com")) {
            return link.contains("techBoardDetail.do")
        }
        if (link.contains("tech.kakaopay.com")) {
            return link.contains("/post/")
        }
        if (link.contains("d2.naver.com")) {
            if (link.matches("https://d2\\.naver\\.com/(helloworld|news)(\\?.*)?$".toRegex())) {
                return false
            }
            if (link.matches("https://d2\\.naver\\.com/(helloworld|news)/\\d+".toRegex())) {
                return true
            }
        }
        if (link.contains("tech.kakao.com")) {
            return link.matches("https://tech\\.kakao\\.com/posts/\\d+".toRegex())
        }
        if (link.contains("techblog.lycorp.co.jp")) {
            return link.matches("https://techblog\\.lycorp\\.co\\.jp/ko/.+".toRegex())
        }
        if (link.contains("medium.com/musinsa-tech")) {
            if (link == "https://medium.com/musinsa-tech") {
                return false
            }
            if (link.matches("https://medium\\.com/musinsa-tech/.+".toRegex())) {
                return true
            }
        }
        if (link.contains("blog.wishket.com")) {
            if (link.matches("https://blog\\.wishket\\.com/condition-filtering-post(/\\?.*)?".toRegex())) {
                return false // 목록 페이지
            }
            if (link.matches("https://blog\\.wishket\\.com/.+".toRegex())) {
                return true // 상세 페이지
            }
        }
        if (link.contains("insight.infograb.net")) {
            if (link.matches("https://insight\\.infograb\\.net/blog(/page/\\d+)?".toRegex())) {
                return false // 목록 페이지
            }
            if (link.matches("https://insight\\.infograb\\.net/blog/\\d{4}/\\d{2}/\\d{2}/.+".toRegex())) {
                return true // 상세 페이지
            }
        }
        if (link.contains("techblog.woowahan.com")) {
            if (link.matches("https://techblog\\.woowahan\\.com(/\\?paged=\\d+(&pcat=.*)?)?".toRegex())) {
                return false // 목록 페이지
            }
            if (link.matches("https://techblog\\.woowahan\\.com/\\d+/".toRegex())) {
                return true // 상세 페이지
            }
        }
        if (link.contains("developers-kr.googleblog.com")) {
            if (link.matches("https://developers-kr\\.googleblog\\.com(/|/search/label/.+)?".toRegex())) {
                return false // 목록 페이지
            }
        }
        return if (link.contains("blog.banksalad.com/tech")) {
            if (link == "https://blog.banksalad.com/tech" ||
                link.matches("https://blog\\.banksalad\\.com/tech(/page/\\d+)?".toRegex())
            ) {
                false
            } else true
        } else true
    }
}
