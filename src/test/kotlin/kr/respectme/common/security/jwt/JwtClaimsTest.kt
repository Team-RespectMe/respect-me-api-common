package kr.respectme.common.security.jwt

import io.kotest.core.spec.style.AnnotationSpec
import io.kotest.matchers.shouldBe
import java.util.*

class JwtClaimsTest : AnnotationSpec() {

    @Test
    fun `test JwtClaims`() {
        // Given
        val id = UUID.randomUUID()
        val email = "test@email.com"
        val roles = listOf("ROLE_USER", "ROLE_ADMIN")
        val isActivated = true

        // When
        val claims = JwtClaims(
            memberId = id.toString(),
            email = email,
            roles = roles,
            isActivated = isActivated
        )

        // Then
        claims.memberId shouldBe id.toString()
        claims.email shouldBe email
        claims.roles shouldBe roles
        claims.isActivated shouldBe isActivated
    }
}