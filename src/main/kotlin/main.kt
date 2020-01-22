import java.awt.Color
import kotlin.math.sqrt

fun main() {
    val width = 1920
    val height = 1080
    val ns = 100
    val lookFrom = Vec3(13f, 2f, -3f)
    val lookAt = Vec3()
    val distFocus = 10f
    val aperture = 0.1f
    val world = scene()
    val camera = Camera(
        lookFrom,
        lookAt,
        Vec3(0f, 1f, 0f),
        20f,
        width.toFloat() / height.toFloat(),
        aperture,
        distFocus
    )

    val bbs = BasicBitmapStorage(width, height)
    for (y in 0 until height) {
        for (x in 0 until width) {
            val color = Vec3()

            for (s in 0 until ns) {
                val u = (x + Math.random()).toFloat() / width.toFloat()
                val v = 1 - ((y + Math.random()).toFloat() / height.toFloat())
                val ray = camera.getRay(u, v)
                color += getColor(ray, world, 0)
            }

            color /= ns.toFloat()
            color.set(sqrt(color.r), sqrt(color.g), sqrt(color.b))
            val ir = (color.r * 255.99).toInt().coerceIn(0..255)
            val ig = (color.g * 255.99).toInt().coerceIn(0..255)
            val ib = (color.b * 255.99).toInt().coerceIn(0..255)
            val c = Color(ir, ig, ib)
            bbs.setPixel(x, y, c)
        }
        println("$y / $height")
    }

    bbs.write(width, height)
}

fun scene(): Hitable {
    val list = arrayListOf<Hitable>()

    // 地面
    list.add(Sphere(Vec3(0f, -1000f, 0f), 1000f, Lambertian(Vec3(0.5f, 0.5f, 0.5f))))

    for (a in -10..10) {
        for (b in -10..10) {
            val chooseMaterial = Math.random()
            val radius = 0.2
            val center = Vec3(a + 0.9f * Math.random(), radius, b + 0.9f * Math.random())
            if ((center - Vec3(4f, 0.2f, 0f)).length() > 0.9) {
                when {
                    chooseMaterial < 0.8 -> { // Lambertian
                        list.add(
                            Sphere(
                                center,
                                radius.toFloat(),
                                Lambertian(
                                    Vec3(
                                        Math.random() * Math.random(),
                                        Math.random() * Math.random(),
                                        Math.random() * Math.random()
                                    )
                                )
                            )
                        )
                    }
                    chooseMaterial < 0.95 -> { // Metal
                        list.add(
                            Sphere(
                                center,
                                radius.toFloat(),
                                Metal(
                                    Vec3(
                                        0.5f * (1 + Math.random()),
                                        0.5f * (1 + Math.random()),
                                        0.5f * (1 + Math.random())
                                    ),
                                    0.5f * Math.random().toFloat()
                                )
                            )
                        )
                    }
                    else -> { // Dielectric
                        list.add(
                            Sphere(
                                center,
                                radius.toFloat(),
                                Dielectric(1.5f)
                            )
                        )
                    }
                }
            }
        }
    }

    list.add(Sphere(Vec3(-4f, 1f, 0f), 1f, Lambertian(Vec3(0.5f, 0.5f, 0.5f))))
    list.add(Sphere(Vec3(0f, 1f, 0f), 1f, Dielectric(1.5f)))
    list.add(Sphere(Vec3(4f, 1f, 0f), 1f, Metal(Vec3(0.7f, 0.6f, 0.5f))))

    return HitableList(list)
}

fun getColor(ray: Ray, world: Hitable, depth: Int): Vec3 {
    val (isHit, record) = world.hit(ray, 0.001f, Float.MAX_VALUE)
    return if (isHit) {
        val (result, attenuation, scattered) = record.material.scatter(ray, record)

        if (depth < 50 && result) {
            getColor(scattered, world, depth + 1) * attenuation
        } else {
            Vec3()
        }
    } else {
        val unit = ray.direction.toUnit()
        val t = 0.5f * (unit.y + 1f)
        Vec3(1f, 1f, 1f) * (1f - t) + Vec3(0.5f, 0.7f, 1f) * t
    }
}