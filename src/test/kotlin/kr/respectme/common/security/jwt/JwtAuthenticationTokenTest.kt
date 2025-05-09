package kr.respectme.common.security.jwt

import io.kotest.core.spec.style.AnnotationSpec
import io.kotest.matchers.shouldBe
import kr.respectme.common.support.jwt.JwtAuthenticationSupport


class JwtAuthenticationTokenTest: AnnotationSpec() {

    @Test
    fun `정상적인 JwtAuthentication이 넘어간 경우 테스트`() {
        // Given
        val authentication = JwtAuthenticationSupport.createJwtAuthentication()

        // When
        val token = JwtAuthenticationToken(authentication)

        // Then
        token.name shouldBe  authentication.email
        token.authorities.forEach { authority ->
            authentication.roles.contains(authority) shouldBe true
        }
        token.details shouldBe authentication.id
        token.isAuthenticated shouldBe true
        token.principal shouldBe authentication
        token.credentials shouldBe null
    }

    @Test
    fun `비활성화된 인증 정보인 경우`() {
        // Given
        val authentication = JwtAuthenticationSupport.createJwtAuthentication(isActivated = false)

        // When
        val token = JwtAuthenticationToken(authentication)

        // Then
        token.name shouldBe authentication.email
        token.authorities.forEach { authority ->
            authentication.roles.contains(authority) shouldBe true
        }
        token.details shouldBe authentication.id
        token.isAuthenticated shouldBe false
        token.principal shouldBe authentication
        token.credentials shouldBe null
    }
}