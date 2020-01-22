import java.awt.Color
import java.awt.image.BufferedImage
import java.io.FileOutputStream

class BasicBitmapStorage(width: Int, height: Int) {
    private val image = BufferedImage(width, height, BufferedImage.TYPE_3BYTE_BGR)

    fun setPixel(x: Int, y: Int, c: Color) = image.setRGB(x, y, c.getRGB())

    fun getPixel(x: Int, y: Int) = Color(image.getRGB(x, y))

    fun write(width: Int, height: Int) {
        val fos = FileOutputStream("output.ppm")
        val buffer = ByteArray(width * 3)  // write one line at a time
        fos.use {
            val header = "P6\n$width $height\n255\n".toByteArray()
            with(it) {
                write(header)
                for (y in 0 until height) {
                    for (x in 0 until width) {
                        val c = getPixel(x, y)
                        buffer[x * 3] = c.red.toByte()
                        buffer[x * 3 + 1] = c.green.toByte()
                        buffer[x * 3 + 2] = c.blue.toByte()
                    }
                    write(buffer)
                }
            }
        }
    }
}