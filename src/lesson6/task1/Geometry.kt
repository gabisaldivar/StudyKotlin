@file:Suppress("UNUSED_PARAMETER")

package lesson6.task1

import lesson1.task1.seconds
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
    fun distance(other: Circle): Double = if (center.distance(other.center) <= radius + other.radius) 0.0 else
        center.distance(other.center) - radius - other.radius

    /**
     * Тривиальная
     *
     * Вернуть true, если и только если окружность содержит данную точку НА себе или ВНУТРИ себя
     */
    fun contains(p: Point): Boolean = center.distance(p) <= radius
}

/**
 * Отрезок между двумя точками
 */
data class Segment(val begin: Point, val end: Point) {
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
    var maxSegment = Segment(points[0], points[0])
    for (first in 0 until points.size)
        for (second in 1 until points.size) {
            val distBetweenPoints = points[first].distance(points[second])
            if (first != second && distBetweenPoints > maxDistance) {
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
fun circleByDiameter(diameter: Segment): Circle = Circle(Point(diameter.begin.x / 2 + diameter.end.x / 2,
        diameter.begin.y / 2 + diameter.end.y / 2), diameter.begin.distance(diameter.end) / 2)

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
        val x = (b * Math.cos(other.angle) - other.b * Math.cos(angle)) / Math.sin(other.angle - angle)
        val y = (x * Math.sin(other.angle) + other.b) / Math.cos(other.angle)
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
fun lineBySegment(s: Segment): Line = if (s.begin.x - s.end.x == 0.0) Line(Point(s.begin.x, s.begin.y), Math.PI / 2)
else Line(Point(s.begin.x, s.begin.y), Math.atan((s.begin.y - s.end.y) / (s.begin.x - s.end.x)))


/**
 * Средняя
 *
 * Построить прямую по двум точкам
 */
fun lineByPoints(a: Point, b: Point): Line = lineBySegment(Segment(a, b))

/**
 * Сложная
 *
 * Построить серединный перпендикуляр по отрезку или по двум точкам
 */
fun bisectorByPoints(a: Point, b: Point): Line = Line(Point((a.x + b.x) / 2, (a.y + b.y) / 2),
        ((Math.atan((a.y - b.y) / (a.x - b.x)) + Math.PI / 2)) % Math.PI)

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
            Circle(circles[0].center, circles[0].radius)
                    .distance(Circle(circles[1].center, circles[1].radius))
    for (first in 0 until circles.size)
        for (second in first + 1 until circles.size) {
            val distBetweenCircles =
                    Circle(circles[first].center, circles[first].radius)
                            .distance(Circle(circles[second].center, circles[second].radius))
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
    val point = bisectorByPoints(a, b).crossPoint(bisectorByPoints(b, c))
    return Circle(point,point.distance(a))
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
fun minContainingCircle(vararg points: Point): Circle {
    if (points.isEmpty()) IllegalArgumentException("")
    if (points.size == 1) return Circle(points[0], 0.0)
    if (points.size == 2) return circleByDiameter(Segment(points[0], points[1]))
    var minimumCicle = circleByThreePoints(points[0], points[1], points[2])
    var radius = Double.MAX_VALUE
    var have: Boolean
    for (first in 0 until points.size)
        for (second in first + 1 until points.size)
            for (third in second + 1 until points.size)
                if ((first != second) && (second != third) && (first != third)) {
                    have = true
                    val cicle = circleByThreePoints(points[first], points[second], points[third])
                    for (i1 in 0 until points.size)
                        if (!cicle.contains(points[i1])) have = false
                    if ((radius > cicle.radius) && have) {
                        radius = cicle.radius
                        minimumCicle = cicle
                    }
                }
    return minimumCicle
}
