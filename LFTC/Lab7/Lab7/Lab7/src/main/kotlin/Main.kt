fun main(args: Array<String>) {
    //run_all_tests()
    val grammar = Grammar("src/main/kotlin/g1.txt")
    val menu = mutableMapOf(
        "1" to "Check if the grammar is context free",
        "2" to "Get the productions",
        "3" to "Get the terminals",
        "4" to "Get the non-terminals",
        "5" to "Get the productions for a given non-terminal",
        "6" to "Exit"
    )
    val parser: Parser = Parser(grammar)
    val parserOutput: ParserOutput = ParserOutput(parser, "a*(a+a)","src/main/kotlin/out.txt")
    while (true) {
        println("Menu:")
        menu.forEach { (k, v) -> println("$k. $v") }
        print("Enter your choice: ")
        val choice = readLine()!!
        when (choice) {
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


fun run_all_tests() {
    println("Running parsing tests")
    `test with g1`()
    `test with g3`()
    println("All tests passed")
}

fun `test with g3`(){
    val grammar = Grammar("src/main/kotlin/g3.txt")
    val parser: Parser = Parser(grammar)
     val firstTable: HashMap<String, HashSet<String>> = parser.getFirstTable()

    assert(firstTable["R"]!!.contains("<>"))
    assert(firstTable["R"]!!.contains("="))
    assert(firstTable["R"]!!.size == 2)

    assert(firstTable["S"]!!.contains("if"))
    assert(firstTable["S"]!!.size == 1)

    assert(firstTable["C"]!!.contains("const"))
    assert(firstTable["C"]!!.contains("id"))
    assert(firstTable["C"]!!.size == 2)

    assert(firstTable["T"]!!.contains("epsilon"))
    assert(firstTable["T"]!!.contains("else"))
    assert(firstTable["T"]!!.size == 2)

    assert(firstTable["E"]!!.contains("const"))
    assert(firstTable["E"]!!.contains("id"))
    assert(firstTable["E"]!!.size == 2)

    assert(firstTable["I"]!!.contains("id"))
    assert(firstTable["I"]!!.size == 1)
}

fun `test with g1`() {
    val grammar = Grammar("src/main/kotlin/g1.txt")
    val parser: Parser = Parser(grammar)
    val firstTable: HashMap<String, HashSet<String>> = parser.getFirstTable()

    assert(firstTable["S"]!!.contains("a"))
    assert(firstTable["S"]!!.contains("("))
    assert(firstTable["S"]!!.size == 2)

    assert(firstTable["A"]!!.contains("+"))
    assert(firstTable["A"]!!.contains("epsilon"))
    assert(firstTable["A"]!!.size == 2)

    assert(firstTable["B"]!!.contains("a"))
    assert(firstTable["B"]!!.contains("("))
    assert(firstTable["B"]!!.size == 2)

    assert(firstTable["C"]!!.contains("*"))
    assert(firstTable["C"]!!.contains("epsilon"))
    assert(firstTable["C"]!!.size == 2)

    assert(firstTable["D"]!!.contains("a"))
    assert(firstTable["D"]!!.contains("("))
    assert(firstTable["D"]!!.size == 2)

    val followTable: HashMap<String, HashSet<String>> = parser.getFollowTable()

    assert(followTable["S"]!!.contains("epsilon"))
    assert(followTable["S"]!!.contains(")"))
    assert(followTable["S"]!!.size == 2)

    assert(followTable["A"]!!.contains(")"))
    assert(followTable["A"]!!.contains("epsilon"))
    assert(followTable["A"]!!.size == 2)

    assert(followTable["B"]!!.contains(")"))
    assert(followTable["B"]!!.contains("epsilon"))
    assert(followTable["B"]!!.contains("+"))
    assert(followTable["B"]!!.size == 3)

    assert(followTable["C"]!!.contains(")"))
    assert(followTable["C"]!!.contains("epsilon"))
    assert(followTable["C"]!!.contains("+"))
    assert(followTable["C"]!!.size == 3)

    assert(followTable["D"]!!.contains(")"))
    assert(followTable["D"]!!.contains("epsilon"))
    assert(followTable["D"]!!.contains("+"))
    assert(followTable["D"]!!.contains("*"))
    assert(followTable["D"]!!.size == 4)
}