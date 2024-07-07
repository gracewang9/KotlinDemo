import org.junit.jupiter.api.Test
import processor.TextProcessor
import kotlin.test.assertEquals

class TestProcessorV1 {
    @Test
    fun testProcessor(){
        val text="Kotlin is my Kotlin Java,I love Kotlin"
        val result=TextProcessor()
        val processors = result.processText(text)
        assertEquals(3,processors[0].frequency)
        assertEquals("Kotlin",processors[0].word)
    }
}