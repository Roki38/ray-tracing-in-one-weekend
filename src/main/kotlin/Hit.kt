data class HitRecord(var t: Float = 0f,
                     var p: Vec3 = Vec3(),
                     var normal: Vec3 = Vec3(),
                     val material: Material = Lambertian(Vec3(0.5f, 0.5f, 0.5f)))

interface Hitable {
    fun hit(ray: Ray, tMin: Float, tMax: Float): Pair<Boolean, HitRecord>
}

class HitableList(private val list: List<Hitable>): Hitable {
    override fun hit(ray: Ray, tMin: Float, tMax: Float): Pair<Boolean, HitRecord> {
        var tempRecord = HitRecord()
        var hitAnything = false
        var closestSoFar = tMax
        list.forEach {
            val (isHit, record) = it.hit(ray, tMin, closestSoFar)
            if (isHit) {
                hitAnything = true
                closestSoFar = record.t
                tempRecord = record
            }
        }

        return hitAnything to tempRecord
    }
}