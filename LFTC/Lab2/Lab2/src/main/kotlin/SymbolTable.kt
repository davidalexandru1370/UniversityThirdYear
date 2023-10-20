class SymbolTable(private var size: Int = 97) {

    private var identifiers: HashTable<String> = HashTable(size);
    private var intConstants: HashTable<Int> = HashTable(size);
    private var stringConstants : HashTable<String> = HashTable(size);

    public fun addIntConstant(value: Int): Pair<Int, Int> {
        return intConstants.insert(value);
    }

    public fun addIdentifier(value: String): Pair<Int, String> {
        return identifiers.insert(value);
    }

    public fun addStringConstant(value: String): Pair<Int, String> {
        return stringConstants.insert(value);
    }

    public fun hasStringIdentifier(string: String): Boolean = identifiers.contains(string);

    public fun hasIntIdentifier(int: Int): Boolean = intConstants.contains(int);

    public fun hasIdentifier(identifier: String): Boolean = identifiers.contains(identifier);

    public fun getIdentifierPosition(identifier: String): Pair<Int, String> = identifiers.getPosition(identifier);

    public fun getIntIdentifierPosition(int: Int): Pair<Int, Int> = intConstants.getPosition(int);

    public fun getStringIdentifierPosition(string: String): Pair<Int, String> = stringConstants.getPosition(string);
}