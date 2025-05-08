package kr.respectme.common.utility

import io.kotest.core.spec.style.AnnotationSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import jakarta.servlet.http.HttpServletRequest
import kr.respectme.common.annotation.CursorPagination
import kr.respectme.common.annotation.CursorParam
import org.springframework.core.MethodParameter
import org.springframework.mock.web.MockHttpServletRequest
import org.springframework.web.context.request.RequestContextHolder
import org.springframework.web.context.request.ServletRequestAttributes

class PaginationUtilityTest : AnnotationSpec() {
    class Item(
        val id: Long,
        val name: String,
    ) { }

    var items: List<Item> = listOf<Item>()
    val methodParameters: List<MethodParameter> = listOf(
        mockk<MethodParameter>(),
        mockk<MethodParameter>(),
    )

    @BeforeEach
    fun setup() {
        items = listOf(
            Item(1, "item1"),
            Item(2, "item2"),
            Item(3, "item3"),
        )
        mockkStatic(RequestContextHolder::class)
        val request = mockk<HttpServletRequest>(relaxed = true)
        val attributes = mockk<ServletRequestAttributes>(relaxed = true)

        every { RequestContextHolder.currentRequestAttributes() } returns attributes
        every { attributes.request } returns request
        every { request.requestURL } returns StringBuffer("http://localhost:8080/api/items")

        every { methodParameters.get(0).hasParameterAnnotation( CursorParam::class.java ) } returns true
        every { methodParameters.get(1).hasParameterAnnotation( CursorParam::class.java ) } returns false

        every { methodParameters.get(0).parameterName } returns "id"
        every { methodParameters.get(1).parameterName } returns "name"

        every { methodParameters.get(0).getParameterAnnotation(CursorParam::class.java) } returns CursorParam(key = "id", inherit = false)
    }

    @Test
    fun `toCursorList 테스트`() {
        val cursorList = PaginationUtility.toCursorList(
            data = items,
            parameters = methodParameters,
            queryMap = mapOf(
                "id" to arrayOf("1"),
                "size" to arrayOf("2"),
            )
        )

        cursorList.next shouldNotBe null
        cursorList.count shouldBe 2
        cursorList.next shouldBe "http://localhost:8080/api/items?id=3&size=2"
    }

    @AfterEach
    fun tearDown() {
        clearAllMocks();
        // Clean up any resources or state if needed
    }
}