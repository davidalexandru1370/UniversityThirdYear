fun main(args: Array<String>) {

    testAddStringIdentifier_shouldReturnTheAddedString_returnsAddedString();
    testAddIntIdentifier_shouldReturnTheAddedInt_returnsAddedInt();
    testAddIdentifier_shouldThrowNotFoundException_throwNotFoundException()
}


fun testAddStringIdentifier_shouldReturnTheAddedString_returnsAddedString(){
    val symbolTable : SymbolTable = SymbolTable();

    symbolTable.addStringConstant("asd");
    val value: Pair<Int, String> = symbolTable.getStringIdentifierPosition("asd");
    assert(value.second == "asd");
}

fun testAddIntIdentifier_shouldReturnTheAddedInt_returnsAddedInt(){
    val symbolTable : SymbolTable = SymbolTable();

    symbolTable.addIntConstant(3);
    val value: Pair<Int, Int> = symbolTable.getIntIdentifierPosition(3)
    assert(value.second == 3);
}

fun testAddIdentifier_shouldThrowNotFoundException_throwNotFoundException(){
    val symbolTable : SymbolTable = SymbolTable();

    symbolTable.addIdentifier("asd");
    try{
        val value: Pair<Int, String> = symbolTable.getIdentifierPosition("a");
    }
    catch (e: Exception){
        return
    }
    assert(false)
}