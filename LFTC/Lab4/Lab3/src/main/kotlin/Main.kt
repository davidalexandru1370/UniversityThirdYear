import domain.FiniteAutomata
import tests.SymbolTableTests
import kotlin.assert

fun main(args: Array<String>) {
    SymbolTableTests();
    val menuOptions = mutableMapOf(
        1 to "1. Print integer automata",
        2 to "2. Print identifier automata",
        3 to "3. Print if identifier is accepted",
        4 to "4. Print if integer is accepted"
    )

    //val scanner: Scanner = Scanner("p3.txt")
    //scanner.printPIF()
    //scanner.printSymbolTable()

    while (true) {
        println("Choose an option:")
        menuOptions.forEach { (key, value) -> println(value) }
        val option = readLine()!!.toInt()
        when (option) {
            1 -> {
                println(FiniteAutomata("src/main/kotlin/input/fa_integers.txt").toString())
            }
            2 -> {
                println(FiniteAutomata("src/main/kotlin/input/fa_identifiers.txt").toString())
            }
            3-> {
                println("Enter identifier:")
                val identifier = readLine()!!
                println(FiniteAutomata("src/main/kotlin/input/fa_identifiers.txt").matchSequence(identifier) == identifier)
            }
            4 -> {
                println("Enter integer:")
                val integer = readLine()!!
                println(FiniteAutomata("src/main/kotlin/input/fa_integers.txt").matchSequence(integer) == integer)
            }
            else -> {
                println("Invalid option")
            }
        }
    }
}


