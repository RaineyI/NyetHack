package com.bignerdranch.nyethack

import java.nio.file.Files.move
import kotlin.system.exitProcess

lateinit var player: Player


 fun main() {
    narrate("Welcome to NyetHack!")
    val playerName = promptHeroName()
    player = Player(playerName)
    // changeNarratorMood()

    println()

    Game.play()
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

object Game {
    private val worldMap = listOf(
        listOf(TownSquare(), Tavern(), Room("Back Room")),
        listOf(Room("A Long Corridor"), Room("A Generic Room")),
        listOf(Room("The Dungeon"))
    )
    var gameMarker = true

    private var currentRoom: Room = worldMap[0][0]
    private var currentPosition = Coordinate(0, 0)

    init {
        narrate("Welcome, adventurer")
        val mortality = if (player.isImmortal) "an immortal" else "a mortal"
        narrate("${player.name}, $mortality, has ${player.healthPoints} health points")
    }

    fun play() {
        while (gameMarker) {
            narrate("${player.name} of ${player.hometown}, ${player.title}, is in ${currentRoom.description()}")
            currentRoom.enterRoom()
            print("> Enter your command: ")
            GameInput(readLine()).processCommand()
        }
    }

            fun move(direction: Direction) {
                val newPosition = direction.updateCoordinate(currentPosition)
                val newRoom = worldMap.getOrNull(newPosition.y)?.getOrNull(newPosition.x)
                if (newRoom != null) {
                    narrate("The hero moves ${direction.name}")
                    currentPosition = newPosition
                    currentRoom = newRoom
                } else {
                    narrate("You cannot move ${direction.name}")
                }
            }

    private class GameInput(arg:String?) {
        private val input = arg ?: ""
        val command = input.split(" ")[0]
        val argument = input.split(" ").getOrElse(1) { "" }

        fun processCommand() =  when (command.lowercase()) {
            "move" -> {
                val direction = Direction.values()
                    .firstOrNull { it.name.equals(argument, ignoreCase = true) }
                if (direction != null) {
                    move(direction)
                } else {
                    narrate("I don't know what direction that is")
                }
            }
            "cast" ->
                when(argument.lowercase()) {
                "fireball" -> player.castFireball()
                    else -> narrate("I don't know what direction that is")}

            "prophesize" -> {
                player.prophesize()
            }
            "ring" -> {
                if (currentRoom is TownSquare) {
                    val ts = TownSquare()
                    ts.ringBell()
                    }
                else {println()}
            }
            "map" -> {
                val ourMap =
                    when(currentRoom) {
                     is TownSquare -> """X00
                         |00
                         |0
                     """.trimMargin()
                        is Tavern -> """0X0
                         |00
                         |0
                     """.trimMargin()
                        Room("Back Room") -> """00X
                         |00
                         |0
                     """.trimMargin()
                        Room("A Long Corridor") -> """000
                         |X0
                         |0
                     """.trimMargin()
                        Room("A Generic Room") -> """000
                         |0X
                         |0
                     """.trimMargin()
                        else -> """000
                            |00
                            |X
                        """.trimMargin()
                    }
                println(ourMap)
                println()
            }

            "quit" -> {
                narrate("You are leaving the game. See you soon")
                exitProcess(0)
            }
            "exit" -> {
                narrate("You are leaving the game. See you soon")
                gameMarker = false
            }
            else -> narrate("I'm not sure what you're trying to do")
        }
    }
}

