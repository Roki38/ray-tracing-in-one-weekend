import kotlin.math.sqrt

class Vec3(var x: Float = 0f, var y: Float = 0f, var z: Float = 0f) {

    constructor(x: Double, y: Double, z: Double) : this(x = x.toFloat(), y = y.toFloat(), z = z.toFloat())

    operator fun plus(other: Vec3): Vec3 = Vec3(this.x + other.x, this.y + other.y, this.z + other.z)
    operator fun plus(scalar: Float): Vec3 = Vec3(this.x + scalar, this.y + scalar, this.z + scalar)
    operator fun plusAssign(other: Vec3) = add(other)
    operator fun plusAssign(other: Float) = add(other)
    operator fun minus(other: Vec3): Vec3 = Vec3(this.x - other.x, this.y - other.y, this.z - other.z)
    operator fun minus(scalar: Float): Vec3 = Vec3(this.x - scalar, this.y - scalar, this.z - scalar)

    operator fun times(other: Vec3): Vec3 = Vec3(this.x * other.x, this.y * other.y, this.z * other.z)
    operator fun times(scalar: Float): Vec3 = Vec3(this.x * scalar, this.y * scalar, this.z * scalar)
    operator fun div(scalar: Float): Vec3 = Vec3(this.x / scalar, this.y / scalar, this.z / scalar)
    operator fun divAssign(scalar: Float) = divide(scalar)

    val r
        get() = x
    val g
        get() = y
    val b
        get() = z

    fun toUnit(): Vec3 = this / length()

    fun length(): Float = sqrt(spheredLength())
    fun spheredLength(): Float = (x * x + y * y + z * z)

    fun inc() = set(this.x + 1, this.y + 1, this.z + 1)
    fun dec() = set(this.x - 1, this.y - 1, this.z - 1)

    private fun add(vector: Vec3) = add(vector.x, vector.y, vector.z)
    private fun add(scalar: Float) = add(scalar, scalar, scalar)
    private fun add(x: Float, y: Float, z: Float) = this.set(this.x + x, this.y + y, this.z + z)

    private fun divide(scalar: Float) = divide(scalar, scalar, scalar)
    private fun divide(x: Float, y: Float, z: Float) = this.set(this.x / x, this.y / y, this.z / z)

    fun set(x: Float, y: Float, z: Float) {
        this.x = x
        this.y = y
        this.z = z
    }
}


fun dot(vector1: Vec3, vector2: Vec3): Float = vector1.x * vector2.x + vector1.y * vector2.y + vector1.z * vector2.z
fun cross(vector1: Vec3, vector2: Vec3): Vec3 = Vec3(vector1.y * vector2.z - vector1.z * vector2.y, vector1.z * vector2.x - vector1.x * vector2.z, vector1.x * vector2.y - vector1.y * vector2.x)

fun randomInUnitSphere(): Vec3 {
    var p: Vec3
    do {
        p = Vec3(Math.random(), Math.random(), Math.random()) * 2f
        p.dec()
    } while (p.spheredLength() >= 1f)
    return p
}