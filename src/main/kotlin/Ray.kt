class Ray(val origin: Vec3, val direction: Vec3) {
    fun at(t: Float): Vec3 {
        return origin + (direction * t)
    }
}