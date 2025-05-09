package kr.respectme.common.security.jwt

import io.kotest.core.spec.style.AnnotationSpec
import io.kotest.matchers.shouldBe
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import java.util.UUID

class JwtAuthenticationTest : AnnotationSpec(){

    @Test
    fun `생성자 테스트`() {
        // Given
        val id = UUID.randomUUID()
        val email = "test@email.com"
        val roles = mutableListOf<GrantedAuthority>(SimpleGrantedAuthority("ROLE_MEMBER"))
        val isActivated = true

        // When
        val authentication = JwtAuthentication(
            id = id,
            email = email,
            roles = roles,
            isActivated = isActivated
        )

        // Then
        authentication.id shouldBe id
        authentication.email shouldBe email
        authentication.roles shouldBe roles
        authentication.isActivated shouldBe isActivated
        authentication.name shouldBe email
    }
}