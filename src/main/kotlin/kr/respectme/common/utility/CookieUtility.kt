package kr.respectme.common.utility

import jakarta.servlet.http.Cookie
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.util.SerializationUtils
import java.util.*

class CookieUtility {

    companion object {
        fun getCookie(request: HttpServletRequest, field: String): Cookie? {
            val cookies = request.cookies
            return cookies?.let{it.find { cookie -> cookie.name == field }}
        }

        fun setCookie(response: HttpServletResponse, field: String, value: String, maxAge: Int) {
            val cookie = Cookie(field, value)
            cookie.maxAge = maxAge
            cookie.isHttpOnly = true
            cookie.secure = true
            cookie.setAttribute("SameSite", "Lax");
            cookie.path="/"
            response.addCookie(cookie)
        }

        fun delCookie(response: HttpServletResponse, field: String) {
            setCookie(response, field, "", 0)
        }
    }
}