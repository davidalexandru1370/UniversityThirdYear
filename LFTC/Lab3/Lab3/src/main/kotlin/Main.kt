import tests.SymbolTableTests
import kotlin.assert

fun main(args: Array<String>) {
   SymbolTableTests();
   val scanner: Scanner = Scanner("p1wrong.txt")
   scanner.printPIF()
   scanner.printSymbolTable()
}


