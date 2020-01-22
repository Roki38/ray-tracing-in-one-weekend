import kotlin.math.sqrt

class Sphere(private val center: Vec3, private val radius: Float, private val material: Material): Hitable {
    override fun hit(ray: Ray, tMin: Float, tMax: Float): Pair<Boolean, HitRecord> {
        val oc = ray.origin - center
        val a = dot(ray.direction, ray.direction)
        val b = dot(oc, ray.direction)
        val c = dot(oc, oc) - radius * radius
        val discriminant = b * b - a * c
        if (discriminant > 0) {
            var temp = (-b - sqrt(discriminant)) / a
            if (temp < tMax && temp > tMin) {
                val t = temp
                val p = ray.at(t)
                val normal = (p - center) / radius
                return true to HitRecord(t, p, normal, material)
            }

            temp = (-b + sqrt(discriminant)) / a
            if (temp < tMax && temp > tMin) {
                val t = temp
                val p = ray.at(t)
                val normal = (p - center) / radius
                return true to HitRecord(t, p, normal, material)
            }
        }

        return false to HitRecord()
    }
}