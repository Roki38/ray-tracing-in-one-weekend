import kotlin.math.pow
import kotlin.math.sqrt

interface Material {
    fun scatter(ray: Ray, record: HitRecord): Triple<Boolean, Vec3, Ray>
}

class Lambertian(private val albedo: Vec3) : Material {
    override fun scatter(ray: Ray, record: HitRecord): Triple<Boolean, Vec3, Ray> {
        val target = record.p + record.normal + randomInUnitSphere()
        val scattered = Ray(record.p, target - record.p)
        val attenuation = albedo
        return Triple(true, attenuation, scattered)
    }
}

fun reflect(v: Vec3, n: Vec3): Vec3 {
    return v - n * (2 * dot(v, n))
}

fun refract(v: Vec3, n: Vec3, niOverNt: Float): Pair<Boolean, Vec3> {
    val uv = v.toUnit()
    val dt = dot(uv, n)
    val discriminant = 1f - niOverNt * niOverNt * (1 - dt * dt)
    return if (discriminant > 0) {
        val refracted = (uv - n * dt) * niOverNt - n * sqrt(discriminant)
        true to refracted
    } else {
        false to Vec3()
    }
}

fun schlick(cosine: Float, index: Float): Float {
    var r0 = (1 - index) / (1 + index)
    r0 *= r0
    return r0 + (1 - r0) * (1.0 - cosine).pow(5).toFloat()
}

class Metal(private val albedo: Vec3, f: Float = 0f) : Material {
    private val fuzziness: Float = if (f < 1) { f } else { 1f }

    override fun scatter(ray: Ray, record: HitRecord): Triple<Boolean, Vec3, Ray> {
        val reflected = reflect(ray.direction.toUnit(), record.normal)
        val scattered = Ray(record.p, reflected + randomInUnitSphere() * fuzziness)
        val attenuation = albedo
        val result = dot(scattered.direction, record.normal) > 0
        return Triple(result, attenuation, scattered)
    }
}

class Dielectric(private val index: Float) : Material {
    override fun scatter(ray: Ray, record: HitRecord): Triple<Boolean, Vec3, Ray> {
        val reflected = reflect(ray.direction, record.normal)
        val attenuation = Vec3(1f, 1f, 1f)
        val outWardNormal: Vec3
        val niOverNt: Float
        val cosine: Float
        if (dot(ray.direction, record.normal) > 0) {
            outWardNormal = record.normal * -1f
            niOverNt = index
            cosine = index * dot(ray.direction, record.normal) / ray.direction.length()
        } else {
            outWardNormal = record.normal
            niOverNt = 1f / index
            cosine = -dot(ray.direction, record.normal) / ray.direction.length()
        }

        val (isRefracted, refracted) = refract(ray.direction, outWardNormal, niOverNt)
        val reflectProb = if (isRefracted) {
            schlick(cosine, index)
        } else {
            1f
        }

        return if (Math.random() < reflectProb) {
            Triple(true, attenuation, Ray(record.p, reflected))
        } else {
            Triple(true, attenuation, Ray(record.p, refracted))
        }
    }
}