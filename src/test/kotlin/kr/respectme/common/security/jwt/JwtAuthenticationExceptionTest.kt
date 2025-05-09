package kr.respectme.common.security.jwt

import io.kotest.core.spec.style.AnnotationSpec
import io.kotest.matchers.shouldBe


class JwtAuthenticationExceptionTest: AnnotationSpec() {

    @Test
    fun `test JwtAuthenticationException`() {
        // Given
        val message = "Unauthorized access"

        // When
        val exception = JwtAuthenticationException(message)

        // Then
        exception.message shouldBe  message
    }
}