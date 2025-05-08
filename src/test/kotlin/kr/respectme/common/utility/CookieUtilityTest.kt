package kr.respectme.common.utility

import io.kotest.core.spec.style.AnnotationSpec
import io.kotest.matchers.shouldBe
import jakarta.servlet.http.Cookie
import org.springframework.mock.web.MockHttpServletRequest
import org.springframework.mock.web.MockHttpServletResponse

class CookieUtilityTest : AnnotationSpec() {

    private var response = MockHttpServletResponse()
    private var request = MockHttpServletRequest()

    fun createCookie(name: String, value: String): Cookie {
        val cookie = Cookie(name, value)
        cookie.maxAge = 3600
        cookie.isHttpOnly = true
        cookie.secure = true
        cookie.setAttribute("SameSite", "Lax")
        cookie.path = "/"
        return cookie
    }

    @BeforeEach
    fun setup() {
        response = MockHttpServletResponse()
        request = MockHttpServletRequest()
        request.setCookies(
            createCookie("test", "value"),
        )
    }

    @Test
    fun `setCookie 테스트`() {
        CookieUtility.setCookie(response, "test", "value", 3600)
        response.cookies.get(0).let {
            "test" shouldBe it.name
            "value" shouldBe it.value
            3600 shouldBe it.maxAge
            true shouldBe it.isHttpOnly
            true shouldBe it.secure
            "/" shouldBe it.path
        }
    }

    @Test
    fun `getCookie 테스트`() {
        val cookie = CookieUtility.getCookie(request, "test")
        cookie?.let {
            "test" shouldBe it.name
            "value" shouldBe it.value
            3600 shouldBe it.maxAge
            true shouldBe it.isHttpOnly
            true shouldBe it.secure
            "/" shouldBe it.path
        }
    }

    @Test
    fun `delCookie 테스트`() {
        CookieUtility.setCookie(response, "test", "value", 3600)
        CookieUtility.delCookie(response, "test")

        response.cookies.size shouldBe 2
        response.cookies.last().name shouldBe "test"
        response.cookies.last().maxAge shouldBe 0
    }
}