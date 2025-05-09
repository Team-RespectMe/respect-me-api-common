package kr.respectme.common.security.jwt

import com.fasterxml.jackson.databind.ObjectMapper
import io.kotest.assertions.throwables.shouldNotThrowAny
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.AnnotationSpec
import io.kotest.matchers.nulls.shouldNotBeNull
import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import jakarta.servlet.FilterChain
import org.springframework.mock.web.MockHttpServletRequest
import org.springframework.mock.web.MockHttpServletResponse
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.web.util.matcher.RequestMatcher

class JwtAuthenticationFilterTest : AnnotationSpec(){

    private lateinit var mockkObjectMapper: ObjectMapper
    private lateinit var mockkRequestMatcher: RequestMatcher
    private lateinit var mockkJwtAuthenticationProvider: JwtAuthenticationProvider

    @BeforeEach
    fun setUp() {
        mockkObjectMapper = mockk()
        mockkRequestMatcher = mockk()
        mockkJwtAuthenticationProvider = mockk()
    }

    @Test
    fun `인증 불필요 URL인 경우 필터를 통과한다`() {
        // Given
        val request = MockHttpServletRequest()
        val response = MockHttpServletResponse()
        val filterChain = mockk<FilterChain>(relaxed = true)

        // When
        every { mockkRequestMatcher.matches(request) } returns true
        every { filterChain.doFilter(request, response) } returns Unit
        val filter = JwtAuthenticationFilter(
            exclusiveRequestMatcher = mockkRequestMatcher,
            jwtAuthenticationProvider = mockkJwtAuthenticationProvider,
            objectMapper = mockkObjectMapper
        )

        // Then
        shouldNotThrowAny {
            filter.doFilter(request, response, filterChain)
        }
    }

    fun `인증이 필요한 URL에 정상적인 토큰이 주어지면 Authentication이 세팅된다`() {
        // Given
        val tokens = "Bearer valid-token-string"
        val request = MockHttpServletRequest()
        val response = MockHttpServletResponse()
        val filterChain = mockk<FilterChain>(relaxed = true)
        val authentication = mockk<JwtAuthenticationToken>(relaxed = true)
        mockkStatic(SecurityContextHolder::class)

        // When
        every { mockkRequestMatcher.matches(request) } returns false
        every { request.getHeader("Authorization") } returns tokens
        every { mockkJwtAuthenticationProvider.authenticate(any()) } returns authentication
        every { filterChain.doFilter(request, response) } returns Unit
        every {SecurityContextHolder.getContext().authentication } returns authentication

        val filter = JwtAuthenticationFilter(
            exclusiveRequestMatcher = mockkRequestMatcher,
            jwtAuthenticationProvider = mockkJwtAuthenticationProvider,
            objectMapper = mockkObjectMapper
        )

        // Then
        shouldNotThrowAny {
            filter.doFilter(request, response, filterChain)
        }
    }

    fun `인증이 필요한 URL에 비정상적인 토큰이 주어지면 예외가 발생한다`() {
        // Given
        val tokens = "Bearer invalid-token-string"
        val request = MockHttpServletRequest()
        val response = MockHttpServletResponse()
        val filterChain = mockk<FilterChain>(relaxed = true)
        val authentication = mockk<JwtAuthenticationToken>(relaxed = true)

        // When
        every { mockkRequestMatcher.matches(request) } returns false
        every { request.getHeader("Authorization") } returns tokens
        every { mockkJwtAuthenticationProvider.authenticate(any()) } throws JwtAuthenticationException("Invalid token")
        val filter = JwtAuthenticationFilter(
            exclusiveRequestMatcher = mockkRequestMatcher,
            jwtAuthenticationProvider = mockkJwtAuthenticationProvider,
            objectMapper = mockkObjectMapper
        )

        shouldThrow<JwtAuthenticationException> {
            filter.doFilter(request, response, filterChain)
        }
    }

    @Test
    fun `생성자 테스트`() {
        // Given
        val requestMatcher = mockk<RequestMatcher>()
        val provider = mockk<JwtAuthenticationProvider>()
        val objectMapper = mockk<ObjectMapper>()

        // When
        val filter = JwtAuthenticationFilter(
            exclusiveRequestMatcher = requestMatcher,
            jwtAuthenticationProvider = provider,
            objectMapper = objectMapper
        )

        // Then
        shouldNotBeNull {
            filter
        }
    }

    @AfterEach
    fun tearDown() {
        clearAllMocks()
    }
}