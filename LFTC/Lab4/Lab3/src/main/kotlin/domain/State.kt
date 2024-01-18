package domain

class State {
    lateinit var name: String
    //from current state to next state on a given symbol
    var transitions: MutableList<Pair<String,String>> = mutableListOf()

    override fun toString(): String {
        val stringBuilder: StringBuilder = StringBuilder()
        for(transition in transitions){
            stringBuilder.append(transition.first)
            stringBuilder.append(" -> ")
            stringBuilder.append(transition.second)
            stringBuilder.append("\n")
        }

        return stringBuilder.toString()
    }
}