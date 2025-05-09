package kr.respectme.common.security.jwt.adapter

import io.kotest.assertions.throwables.shouldNotThrowAny
import io.kotest.assertions.throwables.shouldThrowAny
import io.kotest.core.spec.style.AnnotationSpec
import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.mockk
import kr.respectme.common.response.ApiResult
import kr.respectme.common.security.jwt.adapter.dto.JwtAuthenticationRequirements
import kr.respectme.common.security.jwt.adapter.dto.JwtValidateRequest

class JwtAuthenticationAdapterTest: AnnotationSpec() {

    private lateinit var requirementsRequester: JwtAuthenticationRequirementsRequestAdapter
    private val serviceToken = "serviceToken"

    @BeforeEach
    fun setUp() {
        requirementsRequester = mockk(relaxed = true)
    }

    @Test
    fun `생성자 테스트`() {
        // Given
        val requirements = JwtAuthenticationRequirements(
            issuer = "testIssuer",
            secret = "testSecret"
        )

        // When
        every { requirementsRequester.request(serviceToken) } returns ApiResult(
            status = 200,
            message = "success",
            data = requirements
        )

        // Then
        shouldNotThrowAny {
            val jwtAuthenticationAdapter = JwtAuthenticationAdapter(
                jwtAuthenticationRequirementsRequestPort = requirementsRequester,
                serviceToken = serviceToken
            )
        }
    }

    @Test
    fun `정상 토큰 검증 테스트`() {
        // Given
        val requirements = JwtAuthenticationRequirements(
            issuer = "https://identification.respect-me.kr",
            secret = "test-secret"
        )
        val token = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJodHRwczovL2lkZW50aWZpY2F0aW9uLnJlc3BlY3QtbWUua3IiLCJzdWIiOiIwMTkxN2Y3YS1hZWViLTdkNjAtODkzYi1lNGM3YzYwZmY5YjkiLCJlbWFpbCI6Im1lbWJlcjFAcmVzcGVjdC1tZS5rciIsIm5pY2tuYW1lIjoibWVtYmVyMUByZXNwZWN0LW1lLmtyIiwicm9sZXMiOlsiUk9MRV9NRU1CRVIiXSwiaXNBY3RpdmF0ZWQiOnRydWUsImlhdCI6MTc0Njc5ODE4OCwiZXhwIjozNjM4OTU4MTg4fQ.2XjWIvaMeC7unJCYIJy_IgSo4XlbPPwj_x32DZftuRk"
        val jwtValidationRequest = JwtValidateRequest(type = "Bearer", accessToken = token)

        // When
        every { requirementsRequester.request(serviceToken) } returns ApiResult(
            status = 200,
            message = "success",
            data = requirements
        )

        // Then
        shouldNotThrowAny {
            val jwtAuthenticationAdapter = JwtAuthenticationAdapter(
                jwtAuthenticationRequirementsRequestPort = requirementsRequester,
                serviceToken = serviceToken
            )
            val result = jwtAuthenticationAdapter.verify(jwtValidationRequest)
        }

    }

    @Test
    fun `비정상 토큰 검증 테스트`() {
// Given
        val requirements = JwtAuthenticationRequirements(
            issuer = "https://identification.respect-me.kr",
            secret = "test-secret"
        )
        val token = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJodHRwczovL2lkZW50aWZpY2F0aW9uLnJlc3BlY3QtbWUua3IiLCJzdWIiOiIwMTkxN2Y3YS1hZWViLTdkNjAtODkzYi1lNGM3YzYwZmY5YjkiLCJlbWFpbCI6Im1lbWJlcjFAcmVzcGVjdC1tZS5rciIsIm5pY2tuYW1lIjoibWVtYmVyMUByZXNwZWN0LW1lLmtyIiwicm9sZXMiOlsiUk9MRV9NRU1CRVIiXSwiaXNBY3RpdmF0ZWQiOnRydWUsImlhdCI6MTc0Njc5ODE4OCwiZXhwIjozNjM4OTU4MTg4fQ.2XjWIvaMeC7unJCYIJy_IgSo4XlbPPwj_x32DZftuR1"
        val jwtValidationRequest = JwtValidateRequest(type = "Bearer", accessToken = token)

        // When
        every { requirementsRequester.request(serviceToken) } returns ApiResult(
            status = 200,
            message = "success",
            data = requirements
        )
        val jwtAuthenticationAdapter = JwtAuthenticationAdapter(
            jwtAuthenticationRequirementsRequestPort = requirementsRequester,
            serviceToken = serviceToken
        )
        // Then
        shouldThrowAny {
            jwtAuthenticationAdapter.verify(jwtValidationRequest)
        }
    }

    @AfterEach
    fun tearDown() {
        clearAllMocks()
    }
}