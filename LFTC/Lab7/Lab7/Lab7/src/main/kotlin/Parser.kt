import java.util.Stack

class Parser(private var grammar: Grammar) {
    private val EPSILON: String = "epsilon"
    private val NULL: String = "null"
    private var firstTable: HashMap<String, HashSet<String>> = HashMap()
    private var followTable: HashMap<String, HashSet<String>> = HashMap()
    private var parseTable: HashMap<Pair<Any, Any>, Pair<Any, Any>> = HashMap()
    private var allProductionsAtIndex: ArrayList<ArrayList<String>> = ArrayList()

    init {
        first()
        follow()
        parseTable()
        //printParseTable()
        //var indicies = parseSequence("a*(a+a)")
        //println(indicies)
    }

    private fun first() {
        for (nonTerminal in grammar.nonTerminals) {
            val f0 = getF0(nonTerminal)
            firstTable[nonTerminal] = f0
        }
        firstTable.forEach { (k, v) -> println("${k} -> ${v}") }

        val allFirstValues: ArrayList<HashSet<String>> = ArrayList(firstTable.entries.map { it.value }.toList())
        var noMoreChanges: Boolean = true

        do {
            val nextColumn: HashMap<String, HashSet<String>> = HashMap()
            for (nonTerminal in grammar.nonTerminals) {
                val productionsForNonTerminal: HashSet<ArrayList<String>> =
                    grammar.getProductionsForGivenNonTerminalList(nonTerminal)
                val previousValues: HashSet<String> = HashSet(firstTable[nonTerminal]!!)
                for (production in productionsForNonTerminal) {
                    val rightNonTerminals: ArrayList<String> = ArrayList()
                    var rightTerminal: String = ""
                    for (symbol in production) {
                        if (grammar.nonTerminals.contains(symbol)) {
                            rightNonTerminals.add(symbol)
                        } else {
                            rightTerminal = symbol
                            break
                        }
                    }
                    val concatenationResult: ArrayList<String> = concatenation(rightTerminal, rightNonTerminals)
                    previousValues.addAll(concatenationResult)
                }

                nextColumn[nonTerminal] = previousValues
            }
            if (nextColumn.entries.containsAll(firstTable.entries)) {
                noMoreChanges = false
            }

            println("------------------")
            nextColumn.forEach { (k, v) -> println("$k -> $v") }
            println("------------------")

            firstTable = nextColumn

        } while (noMoreChanges)

    }

    private fun concatenation(rightTerminal: String, rightNonTerminals: ArrayList<String>): ArrayList<String> {
        if (rightNonTerminals.size == 0) {
            return arrayListOf<String>()
        }
        if (rightNonTerminals.size == 1) {
            return firstTable[rightNonTerminals[0]]!!.toMutableList() as ArrayList<String>
        }

        val concatenationResult: ArrayList<String> = ArrayList()
        var allEpsilon: Boolean = true

        for (nonTerminal in rightNonTerminals) {
            if (!firstTable[nonTerminal]!!.contains(EPSILON)) {
                allEpsilon = false
            }
        }

        if (allEpsilon) {
            concatenationResult.add(if (rightTerminal == "") EPSILON else rightTerminal)
        }

        for (index in 0..<rightNonTerminals.size) {
            var containsEpsilon: Boolean = false
            val productions: HashSet<String> = firstTable[rightNonTerminals[index]]!!
            for (production in productions) {
                if (production == EPSILON) {
                    containsEpsilon = true
                } else {
                    concatenationResult.add(production)
                }
            }
            if (!containsEpsilon) {
                break
            }
        }

        return concatenationResult
    }

    private fun getF0(nonTerminal: String): HashSet<String> {
        val f0: HashSet<String> = HashSet()
        for (production in grammar.productions) {
            if (production.key == nonTerminal) {
                for (productionList in production.value) {
                    var firstSymbol: String = productionList.iterator().next()
                    if (grammar.terminals.contains(firstSymbol) || firstSymbol == this.EPSILON) {
                        f0.add(productionList.iterator().next())
                    }
                }
            }
        }
        return f0
    }

    private fun follow() {
        for (nonTerminal in grammar.nonTerminals) {
            followTable[nonTerminal] = HashSet()
        }
        followTable[grammar.startSymbol]!!.add(this.EPSILON)
        var noMoreChanges: Boolean = true
        println("Follow table:")
        followTable.forEach { (k, v) -> println("$k -> $v") }

        do {
            val nextColumn: HashMap<String, HashSet<String>> = HashMap()
            for (nonTerminal in this.grammar.nonTerminals) {
                nextColumn[nonTerminal] = HashSet<String>()
                val productionsWithGivenNonTerminalInRight: HashMap<String, HashSet<ArrayList<String>>> = HashMap()
                for (production in grammar.productions) {
                    val rightProductions = production.value
                    val leftProduction = production.key
                    for (rightProduction in rightProductions) {
                        if (rightProduction.contains(nonTerminal)) {
                            if (leftProduction !in productionsWithGivenNonTerminalInRight) {
                                productionsWithGivenNonTerminalInRight[leftProduction] = HashSet()
                            }
                            productionsWithGivenNonTerminalInRight[leftProduction]!!.add(rightProduction)
                        }
                    }
                }

                val nextValues: HashSet<String> = HashSet()
                nextValues.addAll(followTable[nonTerminal]!!)

                for (productionWithGivenNonTerminalInRight in productionsWithGivenNonTerminalInRight.entries) {
                    val key = productionWithGivenNonTerminalInRight.key
                    for (production in productionWithGivenNonTerminalInRight.value) {
                        for (index in 0..<production.size) {
                            if (production[index] == nonTerminal) {
                                if (index + 1 == production.size) {
                                    nextValues.addAll(this.followTable[key]!!)
                                } else {
                                    val nextSymbol: String = production[index + 1]
                                    if (grammar.terminals.contains(nextSymbol)) {
                                        nextValues.add(nextSymbol)
                                    } else {
                                        for (symbol in firstTable[nextSymbol]!!) {
                                            if (symbol == this.EPSILON) {
                                                nextValues.addAll(followTable[key]!!)
                                            } else {
                                                nextValues.addAll(firstTable[nextSymbol]!!)
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
                nextColumn[nonTerminal] = nextValues
            }
            if (nextColumn.entries.equals(followTable.entries)) {
                noMoreChanges = false
            }
            followTable = nextColumn

            println("------------------")
            nextColumn.forEach { (k, v) -> println("$k -> $v") }
            println("------------------")

        } while (noMoreChanges)
    }

    private fun parseTable() {
        val rows: ArrayList<String> =
            this.grammar.nonTerminals.toList().union(this.grammar.terminals.toList()).toList() as ArrayList
        rows.add("$")

        val columns: ArrayList<String> = ArrayList(this.grammar.terminals)
        columns.add("$")

        rows.forEach { row ->
            columns.forEach { col ->
                parseTable.put(Pair<String, String>(row, col), Pair<String, Int>("error", -1))
            }
        }

        columns.forEach { col ->
            parseTable.put(Pair<String, String>(col, col), Pair<String, Int>("pop", -1))
        }

        parseTable.put(Pair<String, String>("$", "$"), Pair<String, Int>("accept", -1))

        var allProductions: ArrayList<ArrayList<String>> = ArrayList()
        for (production in this.grammar.productions) {
            for (p in production.value) {
                if (p[0] != this.EPSILON) {
                    allProductions.add(p)
                } else {
                    allProductions.add(ArrayList(listOf(this.EPSILON, production.key)))
                }
            }
        }

        this.allProductionsAtIndex = allProductions

        for (production in this.grammar.productions) {
            var key: String = production.key
            for (p in production.value) {
                var firstSymbol: String = p.first()
                if (grammar.terminals.contains(firstSymbol)) {
                    if (parseTable.get(Pair(key, firstSymbol))!!.first == "error") {
                        parseTable.put(
                            Pair(key, firstSymbol),
                            Pair(p.joinToString(separator = ","), allProductions.indexOf(p) + 1)
                        )
                    } else {
                        print("conflict in parse table: pair $key , $firstSymbol")
                        return
                    }
                } else if (grammar.nonTerminals.contains(firstSymbol)) {
                    if (p.size == 1) {
                        for (symbol in this.firstTable[firstSymbol]!!) {
                            var symbol2 = symbol
                            if (symbol2 == this.EPSILON) {
                                symbol2 = "$"
                            }
                            if (parseTable.get(Pair(key, symbol2))!!.first == "error") {
                                parseTable.put(
                                    Pair(key, symbol2),
                                    Pair(p.joinToString(separator = ","), allProductions.indexOf(p) + 1)
                                )
                            } else {
                                print("conflict in parse table: pair $key , $symbol")
                                return
                            }
                        }
                    } else {
                        var index: Int = 1
                        var nextSymbol: String = p[index]
                        val firstSetForProduction = this.firstTable.get(firstSymbol)!!

                        while (index < p.size && grammar.nonTerminals.contains(nextSymbol)) {
                            val nextSymbolsFromFirstTable = firstTable.get(nextSymbol)!!
                            if (firstSetForProduction.contains(this.EPSILON)) {
                                firstSetForProduction.remove(this.EPSILON)
                                firstSetForProduction.addAll(nextSymbolsFromFirstTable)
                            }
                            index += 1
                            if (index < p.size) {
                                nextSymbol = p[index]
                            }
                        }

                        for (symbol in firstSetForProduction) {
                            var symbol2 = symbol
                            if (symbol2 == this.EPSILON) {
                                symbol2 = "$"
                            }
                            if (parseTable.get(Pair(key, symbol2))!!.first == "error") {
                                parseTable.put(
                                    Pair(key, symbol2),
                                    Pair(p.joinToString(separator = ","), allProductions.indexOf(p) + 1)
                                )
                            } else {
                                println("conflict in parse table: pair $key , $symbol")
                                return
                            }
                        }
                    }
                } else {
                    var followLeft = followTable.get(key)!!
                    for (symbol in followLeft) {
                        if (symbol == this.EPSILON) {
                            if (parseTable.get(Pair(key, "$"))!!.first == "error") {
                                var prod = ArrayList(listOf(this.EPSILON, key))
                                parseTable.put(Pair(key, "$"), Pair(this.EPSILON, allProductions.indexOf(prod) + 1))
                            } else {
                                println("conflict in parse table: pair $key , $symbol")
                                return
                            }
                        } else if (parseTable.get(Pair(key, symbol))!!.first == "error") {
                            val prod = ArrayList(listOf(this.EPSILON, key))
                            parseTable[Pair(key, symbol)] = Pair(this.EPSILON, allProductions.indexOf(prod) + 1)
                        } else {
                            println("conflict in parse table: pair $key , $symbol")
                            return
                        }
                    }
                }
            }
        }
    }

    public fun parseSequence(sequence: String): List<Int> {
        val alpha: Stack<String> = Stack()
        val beta: Stack<String> = Stack()
        val resultIndices: ArrayList<Int> = ArrayList()

        alpha.push("$")

        sequence.reversed().forEach { c ->
            alpha.push(c.toString())
        }

        beta.push("$")
        beta.push(grammar.startSymbol)

        while (!(alpha.peek() == "$" && beta.peek() == "$")) {
            val alphaTop: String = alpha.peek()
            val betaTop: String = beta.peek()

            val key: Pair<String, String> = Pair(betaTop, alphaTop)
            val value: Pair<String, Int> = (parseTable.get(key) as Pair<String, Int>?)!!
            if (value.first != "error") {
                if (value.first == "pop") {
                    alpha.pop()
                    beta.pop()
                } else {
                    beta.pop()

                    if (value.first != this.EPSILON) {
                        val value: List<String> = value.first.toString().split(",")
                        for (i in value.size - 1 downTo 0) {
                            beta.push(value[i])
                        }
                    }
                    resultIndices.add(value.second)
                }
            } else {
                println("Error for key ${key}")
                println("alpha: ${alpha}")
                println("beta: ${beta}")
                return resultIndices
            }
        }

        return resultIndices
    }

    private fun printParseTable() {
        var stringBuilder = StringBuilder()
        parseTable.entries.forEach {
            stringBuilder.append(it.key.toString() + " -> " + it.value.toString() + "\n")
        }
        println(stringBuilder.toString())
    }

    public fun getFirstTable(): HashMap<String, HashSet<String>> {
        return this.firstTable
    }

    public fun getFollowTable(): HashMap<String, HashSet<String>> {
        return this.followTable
    }

    public fun getGrammar(): Grammar {
        return this.grammar
    }

    public fun getProductionByItsNumber(productionNumber: Int): List<String> {
        val production = this.allProductionsAtIndex.toList()[productionNumber - 1]
        if (production.contains(this.EPSILON)) {
            return listOf(this.EPSILON)
        }
        return production
    }
}