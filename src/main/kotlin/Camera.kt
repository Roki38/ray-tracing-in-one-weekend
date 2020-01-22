import kotlin.math.tan

class Camera(lookFrom: Vec3, lookAt: Vec3, vUp: Vec3, vFov: Float, aspect: Float, aspecture: Float, focusDist: Float) {
    private var lowerLeft = Vec3()
    private var horizontal = Vec3()
    private var vertical = Vec3()
    private var origin = Vec3()
    private var lensRadius: Float = 0.0f

    private fun randomInUnitDisk(): Vec3 {
        var p: Vec3
        do {
            p = Vec3(Math.random(), Math.random(), 0.0) * 2f - Vec3(1f, 1f, 0f)
        } while (dot(p, p) >= 1f)
        return p
    }


    init {
        lensRadius = aspecture / 2

        val theta = vFov * Math.PI / 180
        val halfHeight = tan(theta / 2).toFloat()
        val halfWidth = (aspect * halfHeight)
        origin = lookFrom


        val w = (lookFrom - lookAt).toUnit()
        val u = cross(vUp, w).toUnit()
        val v = cross(w, u)
        lowerLeft = origin - u * focusDist * halfWidth - v * focusDist * halfHeight - w * focusDist
        horizontal = u * 2f * halfWidth * focusDist
        vertical = v * 2f * halfHeight * focusDist
    }

    fun getRay(u: Float, v: Float): Ray {
        val rd = randomInUnitDisk() * lensRadius
        val offset = u * rd.x + v * rd.y
        val direction = lowerLeft + horizontal * u + vertical * v - origin - offset
        return Ray(origin + offset, direction)
    }
}

