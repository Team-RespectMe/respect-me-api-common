package kr.respectme.common.utility

import io.kotest.core.spec.style.AnnotationSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import java.util.*

class UUIDV7GeneratorTest: AnnotationSpec() {

    var oldId: UUID = UUID.randomUUID()

    @BeforeEach
    fun setUp() {
        // Reset the UUIDV7Generator state if needed
        oldId = UUIDV7Generator.generate()
    }

    @Test
    fun `UUIDV7Generator should be singleton`() {
        // given
        val generator1 = UUIDV7Generator

        // when
        val generator2 = UUIDV7Generator

        // then
        generator1 shouldBe generator2
    }

    @Test
    fun `UUIDV7Generator should generate UUIDv7`() {
        // given
        val generator = UUIDV7Generator

        // when
        val uuid = generator.generate()

        // then
        uuid.version() shouldBe  7
        uuid.variant() shouldBe 2
        uuid.toString().length shouldBe 36
    }

    @Test
    fun `UUIDV7Generator should generate different UUIDs`() {
        // given
        val generator = UUIDV7Generator

        // when
        val uuid1 = generator.generate()
        val uuid2 = generator.generate()

        // then
        uuid1 shouldNotBe uuid2
    }

    @Test
    fun `서로 다른 시점에 생성된 UUID는 정렬이 가능해야 한다`() {
        val uuid1 = UUIDV7Generator.generate()
        Thread.sleep(1000)
        val uuid2 = UUIDV7Generator.generate()

        val uuids = listOf(uuid2, uuid1)
        Collections.sort(uuids)

        uuids[0] shouldBe uuid1
        uuids[1] shouldBe uuid2
    }
}