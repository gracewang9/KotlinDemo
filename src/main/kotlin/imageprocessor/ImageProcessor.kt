package imageprocessor

import coroutines.logX
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import java.awt.Color
import java.awt.image.BufferedImage
import java.io.File
import java.net.HttpURLConnection
import java.util.concurrent.TimeUnit
import javax.imageio.ImageIO

class ImageProcessor {
}

const val BASE_PATH = "./src/main/resources/images/"

//fun main() {
//    val image = loadImage(File("${BASE_PATH}image.webp"))
//    image.crop(0, 100, 0, 100)
//    println("Width=${image}")
//}
//fun main() = runBlocking {
//    val url = "https://raw.githubusercontent.com/chaxiu/ImageProcessor/main/src/main/resources/images/android.png"
//    val path = "${BASE_PATH}downloaded.png"
//    // 调用挂起函数
//    downloadImage(url, File(path))
//    val image = loadImage(File(path))
//    println("Width = ${image.width()};Height = ${image.height()}")
//}
fun main() = runBlocking {
    val url = "http://xxxx.jpg"
    val path = "${BASE_PATH}downloaded.png"
    downloadImage(url, File(path))
    loadImage(File(path))
        .flipVertical()
        .writeToFile(File("${BASE_PATH}download_flip_vertical.png"))
    logX("Done")
}

// 将内存图片保存到硬盘
fun Image.writeToFile(outputFile: File): Boolean {
    return try {
        val width = width()
        val height = height()
        val image = BufferedImage(width, height, BufferedImage.TYPE_INT_RGB)
        for (x in 0 until width) {
            for (y in 0 until height) {
                val awtColor = getPixel(y, x)
                image.setRGB(x, y, awtColor.rgb)
            }
        }
        ImageIO.write(image, "png", outputFile)
        true
    } catch (e: Exception) {
        println(e)
        false
    }
}

fun loadImage(file: File) = ImageIO.read(file).let { it ->
    Array(it.height) { y ->
        Array(it.width) { x ->
            Color(it.getRGB(x, y))
        }
    }.let { list ->
        Image(list)
    }
}


fun Image.flipHorizontal(): Image {
    val pixels = Array(height()) { y ->
        Array(width()) { x ->
            getPixel(y, width() - 1 - x)
        }
    }
    return Image(pixels)
}

fun Image.flipVertical(): Image {
    val pixels = Array(height()) { y ->
        Array(width()) { x ->
            getPixel(height() - 1 - y, x)
        }
    }
    return Image(pixels)
}

fun Image.crop(startY: Int, startX: Int, width: Int, height: Int): Image {
    val pixels = Array(height) { y ->
        Array(width) { x ->
            getPixel(startY + y, startX + x)
        }
    }
    return Image(pixels)
}

/**
 * 挂起函数，http的方式下载图片，保存到本地
 *
 */
suspend fun downloadImage(url: String, outputFile: File): Boolean {
    return withContext(Dispatchers.IO) {
        try {
            downloadSync(url, outputFile)

        } catch (e: java.lang.Exception) {
            println(e)
            return@withContext false
        }
        return@withContext true
    }
}


fun downloadSync(url: String, outputFile: File) {
    logX("Download start!")
    val okHttpClient = OkHttpClient().newBuilder()
        .connectTimeout(10L, TimeUnit.SECONDS)
        .readTimeout(10L, TimeUnit.SECONDS).build()
    val request = Request.Builder().url(url).build()
    val response = okHttpClient.newCall(request).execute()
    val body = response.body()
    val responseCode = response.code()
    if (responseCode >= HttpURLConnection.HTTP_OK &&
        responseCode < HttpURLConnection.HTTP_MULT_CHOICE &&
        body != null
    ) {
        body.byteStream().apply {
            outputFile.outputStream().use { fileOut ->
                copyTo(fileOut)
            }
        }
    }
    logX("Download finish!")
}


private fun checkImageSame(picture:Image,expected:Image){
}










