class Parser(private var grammar: Grammar) {
    private val EPSILON: String = "epsilon"
    private val NULL: String = "null"
    private var firstTable: HashMap<String, HashSet<String>> = HashMap()
    private var followTable: HashMap<String, HashSet<String>> = HashMap()


    init {
        first()
        follow()
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

    public fun getFirstTable(): HashMap<String, HashSet<String>> {
        return this.firstTable
    }

    public fun getFollowTable(): HashMap<String, HashSet<String>> {
        return this.followTable
    }
}