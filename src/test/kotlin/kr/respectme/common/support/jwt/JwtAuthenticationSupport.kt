package kr.respectme.common.support.jwt

import kr.respectme.common.security.jwt.JwtAuthentication
import org.springframework.security.core.authority.SimpleGrantedAuthority
import java.util.UUID

class JwtAuthenticationSupport {

    companion object {

        fun createJwtAuthentication(
            id: UUID = UUID.randomUUID(),
            email: String = "test@email.com",
            roles: List<String> = listOf("ROLE_MEMBER"),
            isActivated: Boolean = true
        ): JwtAuthentication {
            return JwtAuthentication(
                id = id,
                email = email,
                roles = roles.map{ role -> SimpleGrantedAuthority(role) }.toMutableList(),
                isActivated = isActivated
            )
        }
    }
}