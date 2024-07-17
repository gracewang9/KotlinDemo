import imageprocessor.crop
import imageprocessor.loadImage
import org.junit.jupiter.api.Test
import java.io.File


class TestImageProcessor {
    private val TEST_BASE_PATH="/Users/wanghong/IdeaProjects/KotlinDemo/src/main/resources/images"
    @Test
    fun testFlipHorizontal(){
        val image= loadImage(File("${TEST_BASE_PATH}image.webp"))
        val height=image.height()/2
        val width=image.width()/2
        val target= loadImage(File("${TEST_BASE_PATH}image_up.png"))
        val crop=image.crop(0,0,width,height)

    }
    @Test
    fun testFlipVertical(){}
    @Test
    fun testCrop(){}
}