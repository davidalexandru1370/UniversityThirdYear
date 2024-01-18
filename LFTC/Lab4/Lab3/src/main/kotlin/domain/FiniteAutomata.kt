package domain

class FiniteAutomata() {
    lateinit var initialState: String
    var states: MutableList<String> = mutableListOf()
    var finalStates: MutableList<String> = mutableListOf()
    var alphabet: MutableList<String> = mutableListOf()
    val transitions: HashMap<Pair<String, String>, HashSet<String>> = HashMap()

    constructor(filePath: String) : this() {
        initialiseFromFile(filePath)
    }

    fun matchSequence(sequence: String): String {
        if (sequence.length == 0) {
            return ""
        }

        var result: StringBuilder = StringBuilder()
        var currentState: String = this.initialState

        var index = 0
        while (index < sequence.length) {
            val symbol = sequence[index].toString()
            var newState = ""

            val nextTransition: Pair<String, String> = Pair(currentState, symbol)
            if(!this.transitions.containsKey(nextTransition)) {
                newState = ""
            }
            else {
                newState = this.transitions.get(nextTransition)!!.iterator().next()
                result.append(sequence[index])
            }

            if(newState == ""){
                if(!this.finalStates.contains(currentState)){
                    return ""
                }
                else {
                    return result.toString()
                }
            }
            currentState = newState
            index += 1

        }

        return result.toString()
    }

    override fun toString(): String {
        val stringBuilder: StringBuilder = StringBuilder()
        stringBuilder.append("Initial state:\n")
        stringBuilder.append(initialState)
        stringBuilder.append("\n")

        stringBuilder.append("States:\n")
        stringBuilder.append(getStatesAsString())

        stringBuilder.append("Final states:\n")
        stringBuilder.append(
            stringBuilder.append(
                finalStates.joinToString(",", postfix = "\n")
            )
        )

        stringBuilder.append("Alphabet:\n")
        stringBuilder.append(getAlphabetAsString())

        stringBuilder.append("Transitions:\n")
        stringBuilder.append(getTransitionsAsString())

        return stringBuilder.toString()
    }

    private fun getTransitionsAsString(delimiterBetweenStates: String = "\n"): String {
        val stringBuilder: StringBuilder = StringBuilder()

        for (transition in transitions) {
            stringBuilder.append(" ".repeat(transition.key.first.length + 2))
            stringBuilder.append(transition.key.second)
            stringBuilder.append(delimiterBetweenStates)
            stringBuilder.append(transition.key.first)
            stringBuilder.append(" -> ")
            stringBuilder.append(transition.value.iterator().next().toString())
            stringBuilder.append(delimiterBetweenStates)
            stringBuilder.append(delimiterBetweenStates)

        }

        return stringBuilder.toString()
    }

    private fun getAlphabetAsString(delimiterBetweenStates: String = "\n"): String {
        val stringBuilder: StringBuilder = StringBuilder()

        for (letter in alphabet) {
            stringBuilder.append(letter)
            stringBuilder.append(delimiterBetweenStates)
        }

        return stringBuilder.toString()
    }

    private fun initialiseFromFile(filePath: String) {
        val file = filePath
        val delimiter = " "
        val lines = java.io.File(file).readLines()
        this.states = lines[0].split(delimiter).toMutableList()
        this.initialState = lines[1]
        this.alphabet = lines[2].split(delimiter).toMutableList()
        this.finalStates = lines[3].split(delimiter).toMutableList()
        for (i in 4 until lines.size) {
            val elements: List<String> = lines[i].split(" ")
            val fromState: String = elements[0]
            val toState: String = elements[2]
            val withValue: String = elements[1]

            val transition = Pair(fromState, withValue)

            if (!this.transitions.containsKey(transition)) {
                this.transitions[transition] = hashSetOf(toState)
            } else {
                this.transitions[transition]!!.add(toState)
            }

        }
    }

    private fun getStatesAsString(delimiterBetweenStates: String = "\n"): String {
        val stringBuilder: StringBuilder = StringBuilder()

        for (state in states) {
            stringBuilder.append(state)
            stringBuilder.append(delimiterBetweenStates)
        }

        return stringBuilder.toString()
    }


}