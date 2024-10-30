package com.develetter.develetter.blog.Util;

import java.util.Arrays;
import java.util.List;

public class BlogUtil {

    // 블로그 키워드 목록을 리스트로 변환하는 메서드
    public static List<String> getBlogKeywordsAsList(String blogKeywords) {
        return Arrays.asList(blogKeywords.split(",")); // 쉼표로 구분된 키워드를 리스트로 변환
    }

    public static boolean isBlogDetailPage(String link) {
        if (link.contains("toss.tech")) {
            return link.contains("/article/");
        }

        if (link.contains("techtopic.skplanet.com")) {
            return !link.equals("https://techtopic.skplanet.com/");
        }

        if (link.contains("tech.devsisters.com")) {
            return link.contains("/posts/");
        }

        if (link.contains("devocean.sk.com")) {
            return link.contains("techBoardDetail.do");
        }

        if (link.contains("tech.kakaopay.com")) {
            return link.contains("/post/");
        }

        if (link.contains("d2.naver.com")) {
            if (link.matches("https://d2\\.naver\\.com/(helloworld|news)(\\?.*)?$")) {
                return false;
            }
            if (link.matches("https://d2\\.naver\\.com/(helloworld|news)/\\d+")) {
                return true;
            }
        }

        if (link.contains("tech.kakao.com")) {
            return link.matches("https://tech\\.kakao\\.com/posts/\\d+");
        }

        if (link.contains("techblog.lycorp.co.jp")) {
            return link.matches("https://techblog\\.lycorp\\.co\\.jp/ko/.+");
        }

        if (link.contains("medium.com/musinsa-tech")) {
            if (link.equals("https://medium.com/musinsa-tech")) {
                return false;
            }
            if (link.matches("https://medium\\.com/musinsa-tech/.+")) {
                return true;
            }
        }

        if (link.contains("blog.wishket.com")) {
            if (link.matches("https://blog\\.wishket\\.com/condition-filtering-post(/\\?.*)?")) {
                return false; // 목록 페이지
            }
            if (link.matches("https://blog\\.wishket\\.com/.+")) {
                return true; // 상세 페이지
            }
        }

        if (link.contains("insight.infograb.net")) {
            if (link.matches("https://insight\\.infograb\\.net/blog(/page/\\d+)?")) {
                return false; // 목록 페이지
            }
            if (link.matches("https://insight\\.infograb\\.net/blog/\\d{4}/\\d{2}/\\d{2}/.+")) {
                return true; // 상세 페이지
            }
        }

        if (link.contains("techblog.woowahan.com")) {
            if (link.matches("https://techblog\\.woowahan\\.com(/\\?paged=\\d+(&pcat=.*)?)?")) {
                return false; // 목록 페이지
            }
            if (link.matches("https://techblog\\.woowahan\\.com/\\d+/")) {
                return true; // 상세 페이지
            }
        }

        if (link.contains("developers-kr.googleblog.com")) {
            if (link.matches("https://developers-kr\\.googleblog\\.com(/|/search/label/.+)?")) {
                return false; // 목록 페이지
            }
        }

        if (link.contains("blog.banksalad.com/tech")) {
            if (link.equals("https://blog.banksalad.com/tech") ||
                    link.matches("https://blog\\.banksalad\\.com/tech(/page/\\d+)?")) {
                return false;
            }
            return true;
        }
        return true;
    }
}


