class ProgramInternalForm {
    private var entries: List<ProgramInternalFormEntry> = ArrayList<ProgramInternalFormEntry>();

    public fun addEntry(entry: ProgramInternalFormEntry) {
        (entries as ArrayList<ProgramInternalFormEntry>).add(entry);
    }

    override fun toString(): String {
        var stringBuilder: StringBuilder = StringBuilder();
        for (entry in entries) {
            stringBuilder.append(entry.name + " " + entry.position.first + " " + entry.position.second + "\n");
        }

        return stringBuilder.toString();
    }
}

data class ProgramInternalFormEntry(
    var name: String,
    var position: Pair<Int, Int> = Pair(-1, -1),
)

enum class DataType {
    INT,
    STRING,
    IDENTIFIER
}