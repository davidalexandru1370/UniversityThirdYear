fun main(args: Array<String>) {
    val grammar = Grammar("src/main/kotlin/g1.txt")
    val menu = mutableMapOf(
        "1" to "Check if the grammar is context free",
        "2" to "Get the productions",
        "3" to "Get the terminals",
        "4" to "Get the non-terminals",
        "5" to "Get the productions for a given non-terminal",
        "6" to "Exit"
    )

    while(true){
        println("Menu:")
        menu.forEach { (k, v) -> println("$k. $v") }
        print("Enter your choice: ")
        val choice = readLine()!!
        when(choice){
            "1" -> println(grammar.checkIfContextFreeGrammar())
            "2" -> println(grammar.getProductionsAsString())
            "3" -> println(grammar.getTerminalsAsString())
            "4" -> println(grammar.getNonTerminalsAsString())
            "5" -> {
                print("Enter the non-terminal: ")
                val nonTerminal = readLine()!!
                println(grammar.getProductionsForGivenNonTerminal(nonTerminal))
            }
            "6" -> return
            else -> println("Invalid choice")
        }
    }
}