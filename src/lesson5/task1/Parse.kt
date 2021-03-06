@file:Suppress("UNUSED_PARAMETER")

package lesson5.task1

import java.util.*

/**
 * Пример
 *
 * Время представлено строкой вида "11:34:45", содержащей часы, минуты и секунды, разделённые двоеточием.
 * Разобрать эту строку и рассчитать количество секунд, прошедшее с начала дня.
 */
fun timeStrToSeconds(str: String): Int {
    val parts = str.split(":")
    var result = 0
    for (part in parts) {
        val number = part.toInt()
        result = result * 60 + number
    }
    return result
}

/**
 * Пример
 *
 * Дано число n от 0 до 99.
 * Вернуть его же в виде двухсимвольной строки, от "00" до "99"
 */
fun twoDigitStr(n: Int) = if (n in 0..9) "0$n" else "$n"

/**
 * Пример
 *
 * Дано seconds -- время в секундах, прошедшее с начала дня.
 * Вернуть текущее время в виде строки в формате "ЧЧ:ММ:СС".
 */
fun timeSecondsToStr(seconds: Int): String {
    val hour = seconds / 3600
    val minute = (seconds % 3600) / 60
    val second = seconds % 60
    return String.format("%02d:%02d:%02d", hour, minute, second)
}

/**
 * Пример: консольный ввод
 */
fun main(args: Array<String>) {
    println("Введите время в формате ЧЧ:ММ:СС")
    val line = readLine()
    if (line != null) {
        val seconds = timeStrToSeconds(line)
        if (seconds == -1) {
            println("Введённая строка $line не соответствует формату ЧЧ:ММ:СС")
        } else {
            println("Прошло секунд с начала суток: $seconds")
        }
    } else {
        println("Достигнут <конец файла> в процессе чтения строки. Программа прервана")
    }
}

/**
 * Средняя
 *
 * Дата представлена строкой вида "15 июля 2016".
 * Перевести её в цифровой формат "15.07.2016".
 * День и месяц всегда представлять двумя цифрами, например: 03.04.2011.
 * При неверном формате входной строки вернуть пустую строку
 */
fun dateStrToDigit(str: String): String {
    val months = listOf("января", "февраля", "марта", "апреля", "мая", "июня", "июля", "августа", "сентября", "октября", "ноября", "декабря")
    val date = str.split(" ")
    if (date.size == 3) {
        try {
            val day = date[0].toInt()
            if ((date[1] in months) && (day in 1..31)) {
                val month = months.indexOf(date[1]) + 1
                return String.format("%02d.%02d.%d", day, month, date[2].toInt())
            } else return ""
        } catch (e: NumberFormatException) {
            return ""
        }
    } else return ""
}

/**
 * Средняя
 *
 * Дата представлена строкой вида "15.07.2016".
 * Перевести её в строковый формат вида "15 июля 2016".
 * При неверном формате входной строки вернуть пустую строку
 */
fun dateDigitToStr(digital: String): String {
    val months = listOf("января", "февраля", "марта", "апреля", "мая", "июня", "июля", "августа", "сентября", "октября", "ноября", "декабря")
    try {
        val date = digital.split(".")
        if (date.size == 3) {
            val day = date[0].toInt()
            val month = date[1].toInt()
            if (day in 1..31 && month in 1..12)
                return ("$day ${months[month - 1]} ${date[2]}")
            else
                return ""
        } else return ""
    } catch (e: NumberFormatException) {
        return ""
    }
}

/**
 * Средняя
 *
 * Номер телефона задан строкой вида "+7 (921) 123-45-67".
 * Префикс (+7) может отсутствовать, код города (в скобках) также может отсутствовать.
 * Может присутствовать неограниченное количество пробелов и чёрточек,
 * например, номер 12 --  34- 5 -- 67 -98 тоже следует считать легальным.
 * Перевести номер в формат без скобок, пробелов и чёрточек (но с +), например,
 * "+79211234567" или "123456789" для приведённых примеров.
 * Все символы в номере, кроме цифр, пробелов и +-(), считать недопустимыми.
 * При неверном формате вернуть пустую строку
 */
val number = '0'..'9'
val whitelist = " ()-+"
fun flattenPhoneNumber(phone: String): String {
    if (phone.isEmpty() || phone.indexOf('+') > 0) return ""

    for (i in phone)
        if (i !in number && i !in whitelist) return ""
    var result = ""
    for (el in phone) {
        if (el in number || el == '+') result += el
        else if (el !in whitelist) return ""
    }
    return result
}

/**
 * Средняя
 *
 * Результаты спортсмена на соревнованиях в прыжках в длину представлены строкой вида
 * "706 - % 717 % 703".
 * В строке могут присутствовать числа, черточки - и знаки процента %, разделённые пробелами;
 * число соответствует удачному прыжку, - пропущенной попытке, % заступу.
 * Прочитать строку и вернуть максимальное присутствующее в ней число (717 в примере).
 * При нарушении формата входной строки или при отсутствии в ней чисел, вернуть -1.
 */
fun bestLongJump(jumps: String): Int {
    if (jumps.isEmpty()) return -1
    val list = jumps.trim().split(" ")
    var maxResult = -1
    for (el in list) {
        try {
            if (el.isNotEmpty() && el.toInt() > maxResult)
                maxResult = el.toInt()
        } catch (e: NumberFormatException) {
            if (el == " " || el == "-" || el == "%" || el == "") continue
            else return -1
        }
    }
    return maxResult
}

/**
 * Сложная
 *
 * Результаты спортсмена на соревнованиях в прыжках в высоту представлены строкой вида
 * "220 + 224 %+ 228 %- 230 + 232 %%- 234 %".
 * Здесь + соответствует удачной попытке, % неудачной, - пропущенной.
 * Высота и соответствующие ей попытки разделяются пробелом.
 * Прочитать строку и вернуть максимальную взятую высоту (230 в примере).
 * При нарушении формата входной строки вернуть -1.
 */
fun bestHighJump(jumps: String): Int {
    val parts = jumps.split(" ")
    var max = -1
    for (part in 1..parts.size - 1 step 2) {
        val maxOfParts = parts[part - 1].toInt()
        if ('+' in parts[part] && max < maxOfParts) max = maxOfParts
    }
    return max
}

/**
 * Сложная
 *
 * В строке представлено выражение вида "2 + 31 - 40 + 13",
 * использующее целые положительные числа, плюсы и минусы, разделённые пробелами.
 * Наличие двух знаков подряд "13 + + 10" или двух чисел подряд "1 2" не допускается.
 * Вернуть значение выражения (6 для примера).
 * Про нарушении формата входной строки бросить исключение IllegalArgumentException
 */
fun plusMinus(expression: String): Int {
    try {
        val parts = expression.split(" ")
        var result = parts[0].toInt()
        for (i in 2..parts.size step 2) {
            if (parts[i - 1] == "+") {
                result += parts[i].toInt()
            } else if (parts[i - 1] == "-") {
                result -= parts[i].toInt()
            }
            else throw IllegalArgumentException()
        }
        return result
    } catch (e: NumberFormatException) {
        throw  IllegalArgumentException(e)
    }
}

/**
 * Сложная
 *
 * Строка состоит из набора слов, отделённых друг от друга одним пробелом.
 * Определить, имеются ли в строке повторяющиеся слова, идущие друг за другом.
 * Слова, отличающиеся только регистром, считать совпадающими.
 * Вернуть индекс начала первого повторяющегося слова, или -1, если повторов нет.
 * Пример: "Он пошёл в в школу" => результат 9 (индекс первого 'в')
 */
fun firstDuplicateIndex(str: String): Int {
    val parts = str.toLowerCase().split(" ")
    var index = 0
    for (i in 0..parts.size - 2) {
        if (parts[i] == parts[i + 1]) return index
        index += parts[i].length + 1
    }
    return -1
}

/**
 * Сложная
 *
 * Строка содержит названия товаров и цены на них в формате вида
 * "Хлеб 39.9; Молоко 62.5; Курица 184.0; Конфеты 89.9".
 * То есть, название товара отделено от цены пробелом,
 * а цена отделена от названия следующего товара точкой с запятой и пробелом.
 * Вернуть название самого дорогого товара в списке (в примере это Курица),
 * или пустую строку при нарушении формата строки.
 * Все цены должны быть положительными
 */
fun mostExpensive(description: String): String {
    if (description.isEmpty()) return ""
    var maxCost = 0.0
    var nameMaxCost = ""
    try {
        val parts = description.split("; ")
        for (el in parts) {
            val product = el.split(" ")
            if (product.size != 2) return ""
            val maxOfParts = product[1].toDouble()
            if (maxOfParts >= maxCost) {
                maxCost = maxOfParts
                nameMaxCost = product[0]
            }
        }
    } catch (e: NumberFormatException) {
        return ""
    }
    return nameMaxCost
}

/**
 * Сложная
 *
 * Перевести число roman, заданное в римской системе счисления,
 * в десятичную систему и вернуть как результат.
 * Римские цифры: 1 = I, 4 = IV, 5 = V, 9 = IX, 10 = X, 40 = XL, 50 = L,
 * 90 = XC, 100 = C, 400 = CD, 500 = D, 900 = CM, 1000 = M.
 * Например: XXIII = 23, XLIV = 44, C = 100
 *
 * Вернуть -1, если roman не является корректным римским числом
 */
fun fromRoman(roman: String): Int {
    if (roman.isEmpty()) return -1
    val romNumb = listOf(1000, 900, 500, 400, 100, 90, 50, 40, 10, 9, 5, 4, 1)
    val romAbc = listOf("M", "CM", "D", "CD", "C", "XC", "L", "XL", "X", "IX", "V", "IV", "I")
    var number = 0
    var elNumb = 0
    while (elNumb < roman.length) {
        val el = roman[elNumb].toString()
        if (el in romAbc) {
            if (elNumb < roman.length - 1) {
                val nextEl = roman[elNumb + 1].toString()
                if (((el == "I" && nextEl == "V") ||
                        (el == "I" && nextEl == "X") ||
                        (el == "X" && nextEl == "L") ||
                        (el == "X" && nextEl == "C") ||
                        (el == "C" && nextEl == "D") ||
                        (el == "C" && nextEl == "M"))) {
                    number += romNumb[romAbc.indexOf(el + nextEl)]
                    elNumb++
                } else
                    number += romNumb[romAbc.indexOf(el)]
            } else
                number += romNumb[romAbc.indexOf(el)]
        } else return -1
        elNumb++
    }
    return number
}

/**
 * Очень сложная
 *
 * Имеется специальное устройство, представляющее собой
 * конвейер из cells ячеек (нумеруются от 0 до cells - 1 слева направо) и датчик, двигающийся над этим конвейером.
 * Строка commands содержит последовательность команд, выполняемых данным устройством, например +>+>+>+>+
 * Каждая команда кодируется одним специальным символом:
 *	> - сдвиг датчика вправо на 1 ячейку;
 *  < - сдвиг датчика влево на 1 ячейку;
 *	+ - увеличение значения в ячейке под датчиком на 1 ед.;
 *	- - уменьшение значения в ячейке под датчиком на 1 ед.;
 *	[ - если значение под датчиком равно 0, в качестве следующей команды следует воспринимать
 *  	не следующую по порядку, а идущую за соответствующей следующей командой ']' (с учётом вложенности);
 *	] - если значение под датчиком не равно 0, в качестве следующей команды следует воспринимать
 *  	не следующую по порядку, а идущую за соответствующей предыдущей командой '[' (с учётом вложенности);
 *      (комбинация [] имитирует цикл)
 *  пробел - пустая команда
 *
 * Изначально все ячейки заполнены значением 0 и датчик стоит на ячейке с номером N/2 (округлять вниз)
 *
 * После выполнения limit команд или всех команд из commands следует прекратить выполнение последовательности команд.
 * Учитываются все команды, в том числе несостоявшиеся переходы ("[" при значении под датчиком не равном 0 и "]" при
 * значении под датчиком равном 0) и пробелы.
 *
 * Вернуть список размера cells, содержащий элементы ячеек устройства после завершения выполнения последовательности.
 * Например, для 10 ячеек и командной строки +>+>+>+>+ результат должен быть 0,0,0,0,0,1,1,1,1,1
 *
 * Все прочие символы следует считать ошибочными и формировать исключение IllegalArgumentException.
 * То же исключение формируется, если у символов [ ] не оказывается пары.
 * Выход за границу конвейера также следует считать ошибкой и формировать исключение IllegalStateException.
 * Считать, что ошибочные символы и непарные скобки являются более приоритетной ошибкой чем выход за границу ленты,
 * то есть если в программе присутствует некорректный символ или непарная скобка, то должно быть выброшено
 * IllegalArgumentException.
 * IllegalArgumentException должен бросаться даже если ошибочная команда не была достигнута в ходе выполнения.
 *
 */

fun computeDeviceCells(cells: Int, commands: String, limit: Int): List<Int> {
    var brackets = 0
    for (i in 0 until commands.length) {
        if (commands[i] == '[') brackets++
        else if (commands[i] == ']') brackets--
        if (brackets < 0) throw IllegalArgumentException()
    }
    if (brackets != 0) throw IllegalArgumentException()
    val elements = MutableList(cells) { 0 }
    val listOfCycles = ArrayDeque<Int>()
    var operationsCount = 0
    var cell = cells / 2
    var counter = 0
    while (counter < commands.length && operationsCount != limit) {
        when (commands[counter]) {
            '>' -> cell++
            '<' -> cell--
            ' ' -> {
            }
            '+' -> elements[cell]++
            '-' -> elements[cell]--
            '[' -> {
                if (elements[cell] == 0) {
                    var openCycles = 0
                    while (counter != commands.length) {
                        counter++
                        if (commands[counter] == '[') openCycles++
                        if (commands[counter] == ']') openCycles--
                        if (openCycles == -1) break
                    }
                } else listOfCycles.addFirst(counter)
            }
            ']' -> {
                if (elements[cell] != 0) counter = listOfCycles.peekFirst()
                else listOfCycles.removeFirst()
            }
            else -> throw IllegalArgumentException()
        }
        if (cell >= cells || cell < 0) throw IllegalStateException()
        operationsCount++
        counter++
    }
    return elements
}