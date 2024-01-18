class SymbolTable(private var size: Int = 97) {

    private var identifiers: HashTable<String> = HashTable(size);
    private var intConstants: HashTable<Int> = HashTable(size);
    private var stringConstants : HashTable<String> = HashTable(size);

    public fun addIntConstant(value: Int): Pair<Int, Int> {
        return intConstants.insert(value);
    }

    public fun addIdentifier(value: String): Pair<Int, Int> {
        return identifiers.insert(value);
    }

    public fun addStringConstant(value: String): Pair<Int, Int> {
        return stringConstants.insert(value);
    }

    public fun hasStringIdentifier(string: String): Boolean = stringConstants.contains(string);

    public fun hasIntIdentifier(int: Int): Boolean = intConstants.contains(int);

    public fun hasIdentifier(identifier: String): Boolean = identifiers.contains(identifier);

    public fun getIdentifierPosition(identifier: String): Pair<Int, Int> = identifiers.getPosition(identifier);

    public fun getIntIdentifierPosition(int: Int): Pair<Int, Int> = intConstants.getPosition(int);

    public fun getStringIdentifierPosition(string: String): Pair<Int, Int> = stringConstants.getPosition(string);

    public fun getIntByPosition(position: Pair<Int, Int>): Int = intConstants.getByPosition(position);

    public fun getStringByPosition(position: Pair<Int, Int>): String = stringConstants.getByPosition(position);

    public fun getIdentifierByPosition(position: Pair<Int, Int>): String = identifiers.getByPosition(position);

    public override fun toString(): String {
        var stringBuilder: StringBuilder = StringBuilder();
        stringBuilder.append("Identifiers:\n");
        stringBuilder.append(identifiers.toString());
        stringBuilder.append("Int Constants:\n");
        stringBuilder.append(intConstants.toString());
        stringBuilder.append("String Constants:\n");
        stringBuilder.append(stringConstants.toString());

        return stringBuilder.toString()
    }
}