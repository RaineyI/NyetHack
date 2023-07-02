package com.bignerdranch.nyethack

import kotlin.random.Random
import kotlin.random.nextInt

var narrationModifier: (String) -> String = { it }

inline fun narrate(
    message: String,
    modifier: (String) -> String = { narrationModifier(it) },
) {
    println(modifier(message))
}
fun changeNarratorMood() {
    val mood: String
    val modifier: (String) -> String
    when (Random.nextInt(1..7)) {
        1 -> {
            mood = "loud"
            modifier = { message ->
                val numExclamationPoints = 3
                message.uppercase() + "!".repeat(numExclamationPoints)
            }
        }
        2 -> {
            mood = "tired"
            modifier = { message ->
                message.lowercase().replace(" ", "... ")
            }
        }
        3 -> {
            mood = "unsure"
            modifier = { message ->
                "$message?"
            }
        }
        4 -> {
            var narrationsGiven = 0
            mood = "like sending an itemized bill"
            modifier = { message ->
                narrationsGiven++
                "$message.\n(I have narrated $narrationsGiven things)"
            }
        }
        5 -> {
            mood = "lazy"
            modifier = { message ->
            message.take(25)
            }
        }

        6 -> {
            mood = "mysterious"
            modifier = {message ->
              message.replace("l", "1").replace("o", "0")
                  .replace("e", "3").replace("t", "7")
            }
                }

//        7 -> {
//            mood = "poetic"
//            modifier = {message ->
//                message.replace("${message.Random.equals(" ")}","\n")
//
////                ("${message.random(" ")}","\n")
//            }
//            }

//when(Random.nextInt(1..1)) {
    // 1 -> message.replace(" ", "\n")
    //else -> message
        else -> {
            mood = "professional"
            modifier = { message ->
                "$message."
            }
        }
    }
    narrationModifier = modifier
    narrate("The narrator begins to feel $mood")
}


