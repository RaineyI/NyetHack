package com.bignerdranch.nyethack

class Player (
    initialName: String,
    val hometown: String  = "Neversummer",
    var healthPoints: Int,
    val isImmortal: Boolean ) {
    var name = "madrigal"
        get() = field.replaceFirstChar { it.uppercase() }
        private set(value) {
            field = value.trim()
        }

    val title: String
        get() = when {
            name.all { it.isDigit() } -> "The Identifiable" //все числа
            name.none { it.isLetter() } -> "The Witness Protection Member" //нет букв
            name.count { it.lowercase() in "aeiou" } > 4 -> "The Master of Vowel" //много гласных
            name.all { it.isUpperCase() } -> "The Outstanding" //все буквы в его имени записаны в верхнем регистре
            name.count { it.isLetter() } > 10 -> "The Voluminous" //много букв
            name.equals(name.reversed(), true) -> "The Palindrome carrier" //реверсия имени совпадает с оригиналом
            else -> "The Renowned Hero"
        }

    val prophecy by lazy {
        narrate("$name embarks on an arduous quest to locate a fortune teller")
        Thread.sleep(3000)
        narrate("The fortune teller bestows a prophecy upon $name")
        "An intrepid hero from $hometown shall some day " + listOf(
            "form an unlikely bond between two warring factions",
            "take possession of an otherworldly blade",
            "bring the gift of creation back to the world",
            "best the world-eater"
        ).random()
    }


    init {
        require(healthPoints > 0) { "healthPoints must be greater than zero" }
        require(name.isNotBlank()) { "Player must have a name" }
    }
    constructor(name: String) : this(
        initialName = name,
        healthPoints = 100,
        isImmortal = false
    ) {
    if (name.equals("Jason", ignoreCase = true)) {
        healthPoints = 500
    } }


    var weapon: Weapon? = Weapon("Mjolnir")
    fun printWeaponName() {
        weapon?.let {
            println(it.name)
        }
    }

    fun castFireball(numFireballs: Int = 2) {
        narrate("A glass of Fireball springs into existence (x$numFireballs)")
    }

    fun changeName(newName: String) {
        narrate("$name legally changes their name to $newName")
        name = newName
}

    fun prophesize() {
        narrate("$name thinks about their future")
        narrate("A fortune teller told Madrigal, \"$prophecy\"")
    }
}

class Weapon(val name: String)