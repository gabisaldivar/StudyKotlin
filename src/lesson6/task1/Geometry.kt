@file:Suppress("UNUSED_PARAMETER")

package lesson6.task1

import lesson1.task1.sqr

/**
 * Точка на плоскости
 */
data class Point(val x: Double, val y: Double) {
    /**
     * Пример
     *
     * Рассчитать (по известной формуле) расстояние между двумя точками
     */
    fun distance(other: Point): Double = Math.sqrt(sqr(x - other.x) + sqr(y - other.y))

    override fun hashCode(): Int = super.hashCode()

    override fun equals(other: Any?) =
            other is Point && (Math.abs(x - other.x) <= 10e-10) && (Math.abs(y - other.y) <= 10e-10)
}

/**
 * Треугольник, заданный тремя точками (a, b, c, см. constructor ниже).
 * Эти три точки хранятся в множестве points, их порядок не имеет значения.
 */
class Triangle private constructor(private val points: Set<Point>) {

    private val pointList = points.toList()

    val a: Point get() = pointList[0]

    val b: Point get() = pointList[1]

    val c: Point get() = pointList[2]

    constructor(a: Point, b: Point, c: Point) : this(linkedSetOf(a, b, c))

    /**
     * Пример: полупериметр
     */
    fun halfPerimeter() = (a.distance(b) + b.distance(c) + c.distance(a)) / 2.0

    /**
     * Пример: площадь
     */
    fun area(): Double {
        val p = halfPerimeter()
        return Math.sqrt(p * (p - a.distance(b)) * (p - b.distance(c)) * (p - c.distance(a)))
    }

    /**
     * Пример: треугольник содержит точку
     */
    fun contains(p: Point): Boolean {
        val abp = Triangle(a, b, p)
        val bcp = Triangle(b, c, p)
        val cap = Triangle(c, a, p)
        return abp.area() + bcp.area() + cap.area() <= area()
    }

    override fun equals(other: Any?) = other is Triangle && points == other.points

    override fun hashCode() = points.hashCode()

    override fun toString() = "Triangle(a = $a, b = $b, c = $c)"
}

/**
 * Окружность с заданным центром и радиусом
 */
data class Circle(val center: Point, val radius: Double) {
    /**
     * Простая
     *
     * Рассчитать расстояние между двумя окружностями.
     * Расстояние между непересекающимися окружностями рассчитывается как
     * расстояние между их центрами минус сумма их радиусов.
     * Расстояние между пересекающимися окружностями считать равным 0.0.
     */
    fun distance(other: Circle): Double =
            if (center.distance(other.center) - radius - other.radius <= 0) 0.0
            else center.distance(other.center) - radius - other.radius


    /**
     * Тривиальная
     *
     * Вернуть true, если и только если окружность содержит данную точку НА себе или ВНУТРИ себя
     */
    fun contains(p: Point): Boolean = center.distance(p) <= radius + 10e-10
}

/**
 * Отрезок между двумя точками
 */
data class Segment(val begin: Point, val end: Point) {
    fun center(): Point =
            Point(begin.x + (end.x - begin.x) / 2, begin.y + (end.y - begin.y) / 2)

    override fun equals(other: Any?) =
            other is Segment && (begin == other.begin && end == other.end || end == other.begin && begin == other.end)

    override fun hashCode() =
            begin.hashCode() + end.hashCode()
}

/**
 * Средняя
 *
 * Дано множество точек. Вернуть отрезок, соединяющий две наиболее удалённые из них.
 * Если в множестве менее двух точек, бросить IllegalArgumentException
 */
fun diameter(vararg points: Point): Segment {
    if (points.size < 2) throw IllegalArgumentException("")
    var maxDistance = 0.0
    var maxSegment = Segment(points[0], points[1])
    for (first in 0 until points.size)
        for (second in first + 1 until points.size) {
            val distBetweenPoints = points[first].distance(points[second])
            if (distBetweenPoints > maxDistance) {
                maxDistance = distBetweenPoints
                maxSegment = Segment(points[first], points[second])
            }
        }
    return maxSegment
}

/**
 * Простая
 *
 * Построить окружность по её диаметру, заданному двумя точками
 * Центр её должен находиться посередине между точками, а радиус составлять половину расстояния между ними
 */
fun circleByDiameter(diameter: Segment): Circle = Circle(Point((diameter.begin.x + diameter.end.x) / 2,
        (diameter.begin.y + diameter.end.y) / 2), diameter.begin.distance(diameter.end) / 2)

/**
 * Прямая, заданная точкой point и углом наклона angle (в радианах) по отношению к оси X.
 * Уравнение прямой: (y - point.y) * cos(angle) = (x - point.x) * sin(angle)
 * или: y * cos(angle) = x * sin(angle) + b, где b = point.y * cos(angle) - point.x * sin(angle).
 * Угол наклона обязан находиться в диапазоне от 0 (включительно) до PI (исключительно).
 */
class Line private constructor(val b: Double, val angle: Double) {
    init {
        assert(angle >= 0 && angle < Math.PI) { "Incorrect line angle: $angle" }
    }

    constructor(point: Point, angle: Double) : this(point.y * Math.cos(angle) - point.x * Math.sin(angle), angle)

    /**
     * Средняя
     *
     * Найти точку пересечения с другой линией.
     * Для этого необходимо составить и решить систему из двух уравнений (каждое для своей прямой)
     */
    fun crossPoint(other: Line): Point {
        val x = (other.b * Math.cos(angle) - b * Math.cos(other.angle)) / Math.sin(angle - other.angle)
        val y = (b * Math.sin(other.angle) - other.b * Math.sin(angle)) / Math.sin(other.angle - angle)
        return Point(x, y)
    }

    override fun equals(other: Any?) = other is Line && angle == other.angle && b == other.b

    override fun hashCode(): Int {
        var result = b.hashCode()
        result = 31 * result + angle.hashCode()
        return result
    }

    override fun toString() = "Line(${Math.cos(angle)} * y = ${Math.sin(angle)} * x + $b)"
}

/**
 * Средняя
 *
 * Построить прямую по отрезку
 */
fun lineBySegment(s: Segment): Line = lineByPoints(s.begin, s.end)

/**
 * Средняя
 *
 * Построить прямую по двум точкам
 */
fun lineByPoints(a: Point, b: Point): Line {
    val angle = Math.atan((a.y - b.y) / (a.x - b.x))
    return if (angle < 0.0) Line(a, Math.PI + angle)
    else Line(a, angle)
}


/**
 * Сложная
 *
 * Построить серединный перпендикуляр по отрезку или по двум точкам
 */
fun bisectorByPoints(a: Point, b: Point): Line {
    val line = lineByPoints(a, b).angle
    val segment = Segment(a, b).center()
    return if (line < Math.PI / 2) Line(segment, line + Math.PI / 2)
    else Line(segment, line - Math.PI / 2)
}

/**
 * Средняя
 *
 * Задан список из n окружностей на плоскости. Найти пару наименее удалённых из них.
 * Если в списке менее двух окружностей, бросить IllegalArgumentException
 */
fun findNearestCirclePair(vararg circles: Circle): Pair<Circle, Circle> {
    if (circles.size < 2) throw IllegalArgumentException("")
    var pairOfCircles = Pair(circles[0], circles[1])
    var minimumDistance =
            circles[0].distance(circles[1])
    for (first in 0 until circles.size)
        for (second in first + 1 until circles.size) {
            val distBetweenCircles =
                    circles[first].distance(circles[second])
            if (distBetweenCircles < minimumDistance) {
                minimumDistance = distBetweenCircles
                pairOfCircles = Pair(circles[first], circles[second])
            }
        }
    return pairOfCircles
}

/**
 * Сложная
 *
 * Дано три различные точки. Построить окружность, проходящую через них
 * (все три точки должны лежать НА, а не ВНУТРИ, окружности).
 * Описание алгоритмов см. в Интернете
 * (построить окружность по трём точкам, или
 * построить окружность, описанную вокруг треугольника - эквивалентная задача).
 */
fun circleByThreePoints(a: Point, b: Point, c: Point): Circle {
    val center = bisectorByPoints(a, b).crossPoint(bisectorByPoints(b, c))
    return Circle(center, center.distance(a))
}


/**
 * Очень сложная
 *
 * Дано множество точек на плоскости. Найти круг минимального радиуса,
 * содержащий все эти точки. Если множество пустое, бросить IllegalArgumentException.
 * Если множество содержит одну точку, вернуть круг нулевого радиуса с центром в данной точке.
 *
 * Примечание: в зависимости от ситуации, такая окружность может либо проходить через какие-либо
 * три точки данного множества, либо иметь своим диаметром отрезок,
 * соединяющий две самые удалённые точки в данном множестве.
 */
fun minContainingCircle(vararg points: Point): Circle =
        when {
            points.isEmpty() -> throw IllegalArgumentException()
            points.size == 1 -> Circle(points[0], 0.0)
            else -> {
                val diameter = diameter(*points)
                val minimumCircle = circleByDiameter(diameter)
                var furtherPoint = minimumCircle.center
                for (p in points)
                    if (!minimumCircle.contains(p) &&
                            minimumCircle.center.distance(p) > minimumCircle.center.distance(furtherPoint))
                        furtherPoint = p
                if (furtherPoint != minimumCircle.center)
                    circleByThreePoints(diameter.end, diameter.begin, furtherPoint)
                else minimumCircle
            }
        }