import lombok.Getter
import java.io.File
import java.lang.StringBuilder

@Getter
class Grammar(filePath: String) {
    val productions: HashMap<String, LinkedHashSet<ArrayList<String>>> = HashMap()
    val terminals: HashSet<String> = HashSet()
    val nonTerminals: HashSet<String> = HashSet()
    var startSymbol: String = ""


    init {
        readFromFile(filePath)
    }

    fun checkIfContextFreeGrammar(): Boolean {
        var containsStartingSymbol = false
        this.productions.forEach { (from, _) ->
            if (!this.nonTerminals.contains(from)) {
                println("Symbol $from is not in the non-terminals")
                return false
            }

            if (from == this.startSymbol) {
                containsStartingSymbol = true
            }
        }

        if (!containsStartingSymbol) {
            println("Starting symbol ${this.startSymbol} is not in the non-terminals")
            return false
        }

        this.productions.values.forEach { p ->
            p.forEach { t ->
                t.forEach { s ->
                    if (!this.terminals.contains(s) && !this.nonTerminals.contains(s) && s != "epsilon") {
                        println("Symbol $s is not in the terminals or non-terminals")
                        return false
                    }
                }
            }
        }

        return true
    }

    fun getProductionsAsString(): String {
        val result: StringBuilder = StringBuilder()
        this.productions.forEach { (from, to) ->
            to.forEach { t ->
                result.append("$from -> ${t.joinToString(" ")}\n")
            }
        }
        return result.toString()
    }

    fun getTerminalsAsString(): String {
        return this.terminals.joinToString(" ")
    }

    fun getNonTerminalsAsString(): String {
        return this.nonTerminals.joinToString(" ")
    }

    fun getProductionsForGivenNonTerminal(nonTerminal: String): String {
        val result: StringBuilder = StringBuilder()

        if (!productions.containsKey(nonTerminal)) {
            result.append("No productions for $nonTerminal")
            return result.toString()
        }

        this.productions[nonTerminal]!!.forEach { t ->
            result.append("$nonTerminal -> ${t.joinToString(" ")}\n")
        }
        return result.toString()
    }

    fun getProductionsForGivenNonTerminalList(nonTerminal: String): LinkedHashSet<ArrayList<String>> {
        if (!productions.containsKey(nonTerminal)) {
            return LinkedHashSet()
        }

        return this.productions[nonTerminal]!!
    }

    fun getStartSymbolAsString(): String {
        return this.startSymbol
    }

    private fun readFromFile(filePath: String) {
        val delimiter: String = " "
        val lines: List<String> = File(filePath).readLines()

        lines[0].split(delimiter).forEach { t -> if (t.trim().isNotEmpty()) nonTerminals.add(t.trim()) }
        lines[1].split(delimiter).forEach { nt -> if (nt.trim().isNotEmpty()) terminals.add(nt.trim()) }
        startSymbol = lines[2].trim()
        lines.drop(3).forEach { p ->
            val production: List<String> = p.split("->")
            val from: String = production[0].trim()
            val to: ArrayList<String> = production[1].trim().split(Regex("[ \t]+")).toCollection(ArrayList());
            if (!productions.containsKey(from))
                productions[from] = LinkedHashSet(listOf(to))
            else {
                productions[from]!!.add(to)
            }

        }
    }
}
