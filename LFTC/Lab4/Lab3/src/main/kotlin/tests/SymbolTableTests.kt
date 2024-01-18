package tests

import SymbolTable
import exceptions.NotFoundException

class SymbolTableTests {

    init {
        println("Start executing tests")
        testAddStringIdentifier_shouldReturnTheAddedString_returnsAddedString()
        testAddIntIdentifier_shouldReturnTheAddedInt_returnsAddedInt()
        testAddIdentifier_shouldThrowNotFoundException_throwNotFoundException()
        testHasStringIdentifier_ValidIdentifier_shouldReturnTrue()
        testHasStringIdentifier_InvalidIdentifier_shouldReturnFalse()
        testHasIntIdentifier_ValidIdentifier_shouldReturnTrue()
        testHasIntIdentifier_InvalidIdentifier_shouldReturnFalse()
        testHasIdentifier_ValidIdentifier_shouldReturnTrue()
        testHasIdentifier_InvalidIdentifier_shouldReturnFalse()
        println("Executed all tests successfully")
    }


    private fun testAddStringIdentifier_shouldReturnTheAddedString_returnsAddedString(){
        val symbolTable : SymbolTable = SymbolTable();

        symbolTable.addStringConstant("asd");
        val position: Pair<Int, Int> = symbolTable.getStringIdentifierPosition("asd");

        assert(position.second == 0);

        val value = symbolTable.getStringByPosition(position)

        assert(value == "asd")
    }

    private fun testAddIntIdentifier_shouldReturnTheAddedInt_returnsAddedInt(){
        val symbolTable : SymbolTable = SymbolTable();

        symbolTable.addIntConstant(3);
        val position: Pair<Int, Int> = symbolTable.getIntIdentifierPosition(3)
        assert(position.second == 0);

        val number = symbolTable.getIntByPosition(position)

        assert(number == 3)
    }

    private fun testAddIdentifier_shouldThrowNotFoundException_throwNotFoundException(){
        val symbolTable : SymbolTable = SymbolTable();

        symbolTable.addIdentifier("asd");
        try{
            val value: Pair<Int, Int> = symbolTable.getIdentifierPosition("a");
        }
        catch (e: NotFoundException){
            return
        }
        assert(false)
    }

    private fun testHasStringIdentifier_ValidIdentifier_shouldReturnTrue(){
        val symbolTable : SymbolTable = SymbolTable();

        symbolTable.addStringConstant("asd");
        val value: Boolean = symbolTable.hasStringIdentifier("asd");
        assert(value == true);
    }

    private fun testHasStringIdentifier_InvalidIdentifier_shouldReturnFalse(){
        val symbolTable : SymbolTable = SymbolTable();

        val value: Boolean = symbolTable.hasStringIdentifier("asd");
        assert(value == false);
    }

    private fun testHasIntIdentifier_ValidIdentifier_shouldReturnTrue(){
        val symbolTable : SymbolTable = SymbolTable();
        val intConstant = 2
        symbolTable.addIntConstant(intConstant)
        val value: Boolean = symbolTable.hasIntIdentifier(intConstant);
        assert(value == true);
    }

    private fun testHasIntIdentifier_InvalidIdentifier_shouldReturnFalse(){
        val symbolTable : SymbolTable = SymbolTable();

        val intConstant = 2
        val value: Boolean = symbolTable.hasIntIdentifier(intConstant);
        assert(value == false);
    }

    private fun testHasIdentifier_ValidIdentifier_shouldReturnTrue(){
        val symbolTable : SymbolTable = SymbolTable();
        val identifier: String = "asd"

        symbolTable.addIdentifier("asd")

        assert(symbolTable.hasIdentifier(identifier) == true);
    }

    private fun testHasIdentifier_InvalidIdentifier_shouldReturnFalse(){
        val symbolTable : SymbolTable = SymbolTable();
        val identifier: String = "asd"

        assert(symbolTable.hasIdentifier(identifier) == false);
    }
}