package kr.respectme.common.security.jwt

import io.kotest.assertions.throwables.shouldNotThrowAny
import io.kotest.core.spec.style.AnnotationSpec
import io.kotest.matchers.shouldNotBe
import io.mockk.mockk
import kr.respectme.common.security.jwt.port.JwtAuthenticationPort
import kr.respectme.common.security.jwt.port.JwtAuthenticationRequirementsRequestPort

class DefaultJwtAuthenticationConfigTest : AnnotationSpec() {

    @Test
    fun `DefaultJwtAuthenticationConfig 생성자 테스트`() {
        // Given
        val authApiUrl = "http://localhost:8080"
        val serviceToken = ""

        // When
        val defaultJwtAuthenticationConfig = DefaultJwtAuthenticationConfig(
            authApiUrl = authApiUrl,
            serviceToken = serviceToken
        )

        // Then
        defaultJwtAuthenticationConfig shouldNotBe  null
    }

    @Test
    fun `jwtAuthenticationPort Bean 생성 메서드 호출`() {
        // Given
        val authApiUrl = "http://localhost:8080"
        val serviceToken = ""
        val jwtAuthenticationRequirementsRequestPort = mockk<JwtAuthenticationRequirementsRequestPort>()

        // When
        val defaultJwtAuthenticationConfig = DefaultJwtAuthenticationConfig(
            authApiUrl = authApiUrl,
            serviceToken = serviceToken
        )

        // Then
        val jwtAuthenticationPort = shouldNotThrowAny {
            defaultJwtAuthenticationConfig.jwtAuthenticationPort(jwtAuthenticationRequirementsRequestPort)
        }
    }

    @Test
    fun `jwtAuthenticationProvider Bean 생성 메서드 호출`() {
        // Given
        val authApiUrl = "http://localhost:8080"
        val serviceToken = ""
        val jwtAuthenticationPort = mockk<JwtAuthenticationPort>()

        // When
        val defaultJwtAuthenticationConfig = DefaultJwtAuthenticationConfig(
            authApiUrl = authApiUrl,
            serviceToken = serviceToken
        )

        // Then
        val jwtAuthenticationProvider = shouldNotThrowAny {
            defaultJwtAuthenticationConfig.jwtAuthenticationProvider(jwtAuthenticationPort)
        }
    }
}