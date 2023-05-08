import java.io.File
import java.util.*
import kotlin.random.Random
import kotlin.random.nextInt

private const val TAVERN_MASTER = "Taernyl"
private const val TAVERN_NAME = "$TAVERN_MASTER's Folly"

private val firstNames = setOf("Alex", "Mordoc", "Sophie", "Tariq")
private val lastNames = setOf("Ironfoot", "Fernsworth", "Baggins", "Downstrider")

private val menuData = File("data/tavern-menu-data.txt")
    .readText()
    .split("\n")
    .map { it.split(",") }

private val menuItems = menuData.map { (_, name, _) -> name }

private val menuItemPrices = menuData.associate { (_, name, price) ->
    name to price.toDouble()
}

private val menuItemTypes = menuData.associate { (type, name, _) ->
    name to type
}

private val priceItems = menuData.map { (_, _, price) -> price }

fun printMenu2 (name: List<String>, price: List<String>) {
    println()
    val hello = "*** Welcome to $TAVERN_MASTER's Folly ***"
    val cnt = hello.count()
    println(hello)
    // Создаем изменяемый список с видами элементов меню
    val typeList = mutableListOf<String>()
    menuData.forEach { t ->
        // "Разбирем" строку меню на элементы
        val (type, _, _) = t
        // Если такого элемента нет...
        if (typeList.contains(type) == false) {
            // ... добавляем его в список
            typeList.add(type)
            //Формируем и выводим заголовок вида
            val titleType = "        ~[" + type + "]~"
            println(titleType)
            // Ищем все записи c таким видом
            menuData.forEach { m ->
                //"Разбирем" строку меню на элементы
                val (type2, name, price) = m
                // Если вид текущей записи совпадает с выбранным...
                if (type == type2) {
                    // ... выводим его в меню
                    // Делаем первую букву названия заглавной
                    val nameOut = name.replaceFirstChar {
                        if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString()
                    }
                    //Определяем, сколько символов после десятичной точки
                    val pos = price.indexOf('.')
                    // Если он один, то добавляем 0
                    val priceOut = if (price.count() - pos == 2) {
                        price + '0'
                    } else {
                        price
                    }
                    // Дополняем название пункта меню нужным количество точек
                    val nameWithDots = nameOut.padEnd(cnt - priceOut.count(), '.')
                    // Печатаем пункт меню с точками, дополненный ценой
                    println(nameWithDots + priceOut)
                }
            }
        }
    }
    println()
}

fun visitTavern() {
    narrate("$heroName enters $TAVERN_NAME")
    narrate("There are several items for sale:")
    printMenu2(menuItems, priceItems)

    //val patrons = mutableListOf("Eli", "Mordoc", "Sophie")
//    val patrons: MutableSet<String> = mutableSetOf()
    val patrons: MutableSet<String> = firstNames.shuffled()
        .zip(lastNames.shuffled()) { firstName, lastName -> "$firstName $lastName"
        }
        .toMutableSet()

    val patronGold = mutableMapOf(
        TAVERN_MASTER to 86.00,
        heroName to 4.50,
        *patrons.map { it to 6.00 }.toTypedArray(),
    )


    narrate("$heroName sees several patrons in the tavern:")
    narrate(patrons.joinToString())

    val itemOfDay = patrons.flatMap { getFavoriteMenuItems(it) }.random()
    narrate("The item of the day is the $itemOfDay")


    repeat(3) {
        placeOrder(patrons.random(), menuItems.random(), patronGold)
    }
    displayPatronBalances(patronGold)

    val departingPatrons: List<String> = patrons
        .filter { patron -> patronGold.getOrDefault(patron, 0.0) < 4.0 }
        patrons -= departingPatrons
        patronGold -= departingPatrons
    departingPatrons.forEach { patron ->
        narrate("$heroName sees $patron departing the tavern")
    }

    narrate("There are still some patrons in the tavern")
    narrate(patrons.joinToString())

    println()
    val gradesByStudent = mapOf("Josh" to 4.0, "Alex" to 2.0, "Jane" to 3.0)
    println(gradesByStudent)
    println(gradesByStudent.values)
    println(gradesByStudent.keys)

val newMap = flipValues(gradesByStudent)
   println(newMap)
}

private fun getFavoriteMenuItems(patron: String): List<String> {
    return when (patron) {
        "Alex Ironfoot" -> menuItems.filter { menuItem ->
            menuItemTypes[menuItem]?.contains("dessert") == true
        }
        else -> menuItems.shuffled().take(Random.nextInt(1..2))
    }
}

private fun placeOrder(
    patronName: String,
    menuItemName: String,
    patronGold: MutableMap<String, Double>
) {
    val itemPrice = menuItemPrices.getValue(menuItemName)
    narrate("$patronName speaks with $TAVERN_MASTER to place an order")
    if (itemPrice <= patronGold.getOrDefault(patronName, 0.0)) {
        val action = when (menuItemTypes[menuItemName]) {
            "shandy", "elixir" -> "pours"
            "meal" -> "serves"
            else -> "hands"
        }
        narrate("$TAVERN_MASTER $action $patronName a $menuItemName")
        narrate("$patronName pays $TAVERN_MASTER $itemPrice gold")
        patronGold[patronName] = patronGold.getValue(patronName) - itemPrice
        patronGold[TAVERN_MASTER] = patronGold.getValue(TAVERN_MASTER) + itemPrice
    } else {
        narrate("$TAVERN_MASTER says, \"You need more coin for a $menuItemName\"")
    }
}

private fun displayPatronBalances(patronGold: Map<String, Double>) {
    narrate("$heroName intuitively knows how much money each patron has")
    patronGold.forEach { (patron, balance) ->
        narrate("$patron has ${"%.2f".format(balance)} gold")
    }
}

private fun flipValues(map: Map<String, Double>) : Map<Double, String> {
    return map.entries
        .associate { (name, mark) -> mark to name }

//    map.entries
//        .associate {(name, mark) -> mark to name}


//    val name = listOf(gradesByStudent.values)
//    val mark = listOf(gradesByStudent.keys)
//    gradesByStudent.values.associate {mark ->
//        mark to name
//    }


//    gradesByStudent.values.associate { gradesByStudent.keys  to gradesByStudent.values}
    //  println(gradesByStudent)

    //gradesByStudent.values.associateWith { gradesByStudent.keys } //.also { gradesByStudent = it }

//    private var newGradesByStudent: Map<String, Double> = List(gradesByStudent.size) {index ->
//
//        mark.toDouble() to name
//    }.toMap()

//gradesByStudent. {}
}