package com.smartcode.web.utils;

import javax.servlet.http.Cookie;

public class CookieUtil {


    public static Cookie getCookie(Cookie[] cookies, String name) {
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals(name)) {
                return cookie;
            }
        }
        return null;
    }
}
