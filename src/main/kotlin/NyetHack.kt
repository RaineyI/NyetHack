var heroName: String = ""

fun main() {
    heroName = promptHeroName()

   // changeNarratorMood()

    narrate("$heroName, ${createTitle(heroName)}, heads to the town square")

    visitTavern()
}

private fun promptHeroName(): String {
    narrate("A hero enters the town of Kronstadt. What is their name?") { message ->
        // Выводит message желтым цветом
        "\u001b[33;1m$message\u001b[0m"
    }
    /*val input = readLine()
    require(input != null && input.isNotEmpty()) {
    "The hero must have a name."
    }
    return input*/
    println("Madrigal")
    return "Madrigal"
}

private fun createTitle(name: String): String {
    return when {
        name.all { it.isDigit() } -> "The Identifiable" //все числа
        name.none { it.isLetter() } -> "The Witness Protection Member" //нет букв
        name.count { it.lowercase() in "aeiou" } > 4 -> "The Master of Vowel" //много гласных
        name.all { it.isUpperCase() } -> "The Outstanding" //все буквы в его имени записаны в верхнем регистре
        name.count { it.isLetter() } > 10 -> "The Voluminous" //много букв
        name.equals(name.reversed(), true) -> "The Palindrome carrier" //реверсия имени совпадает с оригиналом
    else -> "The Renowned Hero"
    }
}

