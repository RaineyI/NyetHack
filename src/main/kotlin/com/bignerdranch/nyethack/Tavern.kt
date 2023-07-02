package com.bignerdranch.nyethack

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

class Tavern : Room(TAVERN_NAME) {
    val patrons: MutableSet<String> = firstNames.shuffled()
        .zip(lastNames.shuffled()) { firstName, lastName -> "$firstName $lastName"
        }
        .toMutableSet()

    val patronGold: MutableMap<String, Double> = mutableMapOf(
        TAVERN_MASTER to 86.00,
        player.name to 4.50,
        *patrons.map { it to 6.00 }.toTypedArray()
    )
    val itemOfDay = patrons.flatMap { getFavoriteMenuItems(it) }
        .groupingBy {it}.eachCount()
        .maxBy { it.value }
        .key //ключ + значение
    override val status = "Busy"
    override fun enterRoom() {
        narrate("${player.name} enters $TAVERN_NAME")
        narrate("There are several items for sale:")
        //narrate(menuItems.joinToString())
        printMenu2(menuItems, priceItems)
        narrate("The item of the day is the $itemOfDay")
        narrate("${player.name} sees several patrons in the tavern:")
        narrate(patrons.joinToString())
        placeOrder(patrons.random(), menuItems.random())

    }

    private fun placeOrder(
        patronName: String,
        menuItemName: String
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
            patronGold[TAVERN_MASTER] = patronGold.getValue(TAVERN_MASTER) +
                    itemPrice
        } else {
            narrate("$TAVERN_MASTER says, \"You need more coin for a $menuItemName\"")
        }
    }
}

private fun getFavoriteMenuItems(patron: String): List<String> {
    return when (patron) {
        "Alex Ironfoot" -> menuItems.filter { menuItem ->
            menuItemTypes[menuItem]?.contains("dessert") == true
        }
        else -> menuItems.shuffled().take(Random.nextInt(1..2))
    }
}