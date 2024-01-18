import exceptions.DuplicateEntryException
import exceptions.ScannerException
import java.io.File
import java.nio.file.Files
import java.nio.file.Path
import kotlin.io.path.Path
import kotlin.math.max

class Scanner {
    private var delimiters = listOf("{", "}", "(", ")", "[", "]", ":", ";", " ", "\t", ",", "\n", "\r")
    private var operators = listOf("+", "-", "*", "/", "%", "=", "==", "!=", "<", "<=", ">", ">=", "&&", "||", "!")
    private var reservedWords =
        listOf(
            "read",
            "readInt",
            "if",
            "else",
            "display",
            "while",
            "def",
            "return",
            "const",
            "program",
            "begin",
            "end"
        )
    private var fileName: String = ""
    private lateinit var programInternalForm: ProgramInternalForm
    private val symbolTable: SymbolTable
    private lateinit var program: Program

    constructor(fileName: String) {
        this.fileName = fileName;
        this.symbolTable = SymbolTable();
        this.programInternalForm = ProgramInternalForm()
        scan()
    }


    private fun nextToken() {
        fun overcomeSpaces() {
            while (program.index < program.codeLength && program.code[program.index].toString()
                    .matches(Regex("[ \t\r\n]"))
            ) {
                if (program.code[program.index] == '\n') {
                    program.lineIndex++
                }
                program.index++
            }
        }

        fun overcomeComments() {
            var substr = program.code.substring(program.index)
            while (program.index < program.codeLength && Regex("^(//)").find(substr) != null) {

                while (program.index < program.codeLength && program.code[program.index] != '\n') {
                    program.index++
                }
                substr = program.code.substring(program.index)
                program.lineIndex += 1
            }
        }
        overcomeSpaces()
        overcomeComments()
        overcomeSpaces()

    }

    private fun parseNewIdentifier(): Boolean {
        var identifierRegex: Regex = Regex("^(int|string|char)\\s+[a-zA-Z][a-zA-Z0-9]*")
        var substr = program.code.substring(program.index)
        var match = identifierRegex.find(substr)

        if (match == null) {
            return false
        }

        try {
            val name = match.value.split(" ")[1]
//            if (reservedWords.contains(name) || delimiters.contains(name) || operators.contains(name)) {
//                throw ScannerException("Invalid identifier name at line ${program.lineIndex} at index ${program.index}")
//            }
            val location = this.symbolTable.addIdentifier(name)
            this.programInternalForm.addEntry(ProgramInternalFormEntry(match.value.split(' ')[0]))
            this.programInternalForm.addEntry(ProgramInternalFormEntry(name, location))
            for(entry in match.value.split(" ").subList(2, match.value.split(" ").size)){
                this.programInternalForm.addEntry(ProgramInternalFormEntry(name))
            }
        } catch (duplicateKeyException: DuplicateEntryException) {
            throw ScannerException(duplicateKeyException.message!!)
        }

        program.index += match.value.length

        return true
    }

    private fun parseExistingIdentifier(): Boolean {
        var identifierRegex = Regex("^[a-zA-Z][a-zA-Z0-9]*")
        var substr = program.code.substring(program.index)
        var match = identifierRegex.find(substr)

        if (match == null || reservedWords.find { it == match.value } != null) {
            return false
        }


        if (!symbolTable.hasIdentifier(match.value)) {
            return false
        }

        programInternalForm.addEntry(
            ProgramInternalFormEntry(
                match.value,
                symbolTable.getIdentifierPosition(match.value)
            )
        )
        this.program.index += match.value.length
        return true
    }

    private fun parseSymbol(): Boolean {
        val possibleSymbol = program.code
            .substring(program.index)
            .split(Regex("[ \t\r\n]", RegexOption.IGNORE_CASE))
            .filter { it.isNotEmpty() }
            .first()[0].toString()

        val allSymbols = delimiters.union(operators);
        if ((possibleSymbol in allSymbols) == false) {
            return false
        }
        if (possibleSymbol == "\n") {
            program.lineIndex += 1
        }

        this.programInternalForm.addEntry(ProgramInternalFormEntry(possibleSymbol))

        program.index += possibleSymbol.length
        return true
    }

    private fun parseReservedWord(): Boolean {
        var possibleToken = program.code
            .substring(program.index)
            .split(Regex("[ \t\r\n]", RegexOption.IGNORE_CASE))
            .filter { it.isNotEmpty() }
            .first()

        var hasMatch: Boolean = false
        var length: Int = 0
        var reservedWord = ""
        for (token in reservedWords) {
            if (possibleToken.startsWith(token)) {
                hasMatch = true
                if (token.length > reservedWord.length) {
                    reservedWord = token
                    length = max(length, token.length)
                }
            }
        }

        if (hasMatch == false) {
            return false
        }

        this.programInternalForm.addEntry(ProgramInternalFormEntry(reservedWord))

        program.index += length
        return true
    }

    private fun parseStringConstant(): Boolean {
        var stringRegex = Regex("^(\").*(\")", RegexOption.CANON_EQ)
        var substr = program.code.substring(program.index)
        var match = stringRegex.find(substr)

        if (match == null) {
            return false
        }

        var location: Pair<Int, Int>;

        if (!symbolTable.hasStringIdentifier(match.value)) {
            location = symbolTable.addStringConstant(match.value)
        } else {
            location = symbolTable.getStringIdentifierPosition(match.value)
        }

        this.programInternalForm.addEntry(ProgramInternalFormEntry(match.value, location))

        program.index += match.value.length
        return true
    }

    private fun parseIntIdentifier(): Boolean {
        var intRegex = Regex("-?\\d+")
        var substr = program.code.substring(program.index)
        var match = intRegex.find(substr)

        if (match == null) {
            return false
        }

        var location: Pair<Int, Int>;

        if (!symbolTable.hasIntIdentifier(match.value.toInt())) {
            location = symbolTable.addIntConstant(match.value.toInt())
        } else {
            location = symbolTable.getIntIdentifierPosition(match.value.toInt())
        }

        this.programInternalForm.addEntry(ProgramInternalFormEntry(match.value, location))

        program.index += match.value.length

        return true
    }

    private fun parseCharIdentifier(): Boolean {
        var stringRegex = Regex("^(\').(\')", RegexOption.CANON_EQ)
        var substr = program.code.substring(program.index)
        var match = stringRegex.find(substr)

        if (match == null) {
            return false
        }

        var location: Pair<Int, Int>;

        if (!symbolTable.hasStringIdentifier(match.value)) {
            location = symbolTable.addStringConstant(match.value)
        } else {
            location = symbolTable.getStringIdentifierPosition(match.value)
        }

        this.programInternalForm.addEntry(ProgramInternalFormEntry(match.value, location))

        program.index += match.value.length
        return true
    }

    private fun parseToken() {
        nextToken()
        if (program.index >= program.codeLength) {
            return
        }

        if (parseNewIdentifier()) {
            return
        } else if (parseReservedWord()) {
            return
        } else if (parseExistingIdentifier()) {
            return
        } else if (parseSymbol()) {
            return
        } else if (parseStringConstant()) {
            return
        } else if (parseIntIdentifier()) {
            return
        } else if (parseCharIdentifier()) {
            return
        }

        throw ScannerException("Invalid token at line ${program.lineIndex} at index ${program.index}")

    }

    fun scan(): ProgramInternalForm {
        val file: Path = Path("src/main/kotlin/input/" + fileName)
        this.program = Program(Files.readString(file))
        while (program.index < program.codeLength) {
            parseToken()
        }

        return this.programInternalForm
    }

    fun printPIF() {
        val pif = this.programInternalForm.toString()
        File("src/main/kotlin/output/${fileName}_pif.txt").writeText(pif)
    }

    fun printSymbolTable() {
        val symbolTable = this.symbolTable.toString()
        File("src/main/kotlin/output/${fileName}_symbol_table.txt").writeText(symbolTable)
    }
}
