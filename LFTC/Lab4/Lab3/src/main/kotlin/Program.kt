class Program {
    var index: Int = 0
    var lineIndex: Int  = 0
    var codeLength: Int = 0
    var code: String = ""

    constructor(code: String) {
        this.index = 0
        this.lineIndex = 0
        this.codeLength = code.length
        this.code = code
    }
}