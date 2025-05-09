package kr.respectme.common.security.jwt

import io.kotest.assertions.throwables.shouldNotThrowAny
import io.kotest.core.spec.style.AnnotationSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import io.mockk.every
import io.mockk.mockk
import kr.respectme.common.security.jwt.port.JwtAuthenticationPort
import kr.respectme.common.support.jwt.JwtAuthenticationSupport
import java.util.*

class JwtAuthenticationProviderTest: AnnotationSpec() {

    private lateinit var jwtAuthenticationPort: JwtAuthenticationPort

    @BeforeEach
    fun setUp() {
        // Mocking and setup code here
         jwtAuthenticationPort = mockk<JwtAuthenticationPort>()
    }

    @Test
    fun `생성자 호출 테스트`() {
        shouldNotThrowAny {
            val jwtAuthenticationProvider = JwtAuthenticationProvider(jwtAuthenticationPort)
        }
    }

    @Test
    fun `JwtAuthenticationToken 인증 테스트`() {
        // Given
        val jwtAuthenticationProvider = JwtAuthenticationProvider(jwtAuthenticationPort)
        val claims = JwtClaims(
            memberId = UUID.randomUUID().toString(),
            email = "test@email.com",
            roles = listOf("ROLE_USER", "ROLE_ADMIN"),
            isActivated = true
        )
        // When
        val jwtAuthentication = JwtAuthenticationToken("testToken;")
        every { jwtAuthenticationPort.verify(any()) } returns claims

        // Then
        val authentication = jwtAuthenticationProvider.authenticate(jwtAuthentication)
        authentication shouldNotBe null
        authentication?.name shouldBe claims.email
        authentication?.authorities?.forEach { authority ->
            claims.roles.contains(authority.authority) shouldBe true
        }
        authentication?.details.toString() shouldBe claims.memberId
        authentication?.isAuthenticated shouldBe true
    }

    @Test
    fun `authenticate with null 테스트`() {
        // Given
        val authentication = null

        // When
        val jwtAuthenticationProvider = JwtAuthenticationProvider(jwtAuthenticationPort)

        // Then
        jwtAuthenticationProvider.authenticate(authentication) shouldBe null
    }

    @Test
    fun `지원하지 않는 token supports 테스트`() {
        // Given
        val token = Any::class.java

        // When
        val jwtAuthenticationProvider = JwtAuthenticationProvider(jwtAuthenticationPort)

        // Then
        jwtAuthenticationProvider.supports(token) shouldBe false
    }

    @Test
    fun `JwtAuthenticationToken supports 테스트`() {

        // Given
        val clazz = JwtAuthenticationToken::class.java

        // When
        val jwtAuthenticationProvider = JwtAuthenticationProvider(jwtAuthenticationPort)

        // Then
        jwtAuthenticationProvider.supports(clazz) shouldBe  true
    }

    @AfterEach
    fun tearDown() {
        // Cleanup code here
    }
}