import java.io.File
import java.util.Stack

class ParserOutput {
    private var EPSILON: String = "epsilon"
    private lateinit var parser: Parser
    private lateinit var productionList: List<Int>
    private lateinit var nodeList: ArrayList<Node>
    private var nodeIndex: Int = 1
    private lateinit var root: Node
    private lateinit var outputFile: String
    private lateinit var sequence: String

    constructor(parser: Parser, sequence: String, outputFile: String) {
        this.parser = parser
        this.sequence = sequence
        this.outputFile = outputFile
        this.nodeList = ArrayList()
        productionList = parser.parseSequence(sequence)
        if (productionList.contains(-1)) {
            throw Exception("Grammar is not LL1 or something")
        }
        computeTree()
        displayTree()
    }

    private fun computeTree() {
        val nodeStack: Stack<Node> = Stack()
        var productionsIndex: Int = 0

        this.root = Node(0, this.parser.getGrammar().startSymbol, nodeIndex, 0, false)
        this.nodeIndex += 1

        nodeStack.push(root)
        nodeList.add(root)

        while (productionsIndex < productionList.size && nodeStack.isNotEmpty()) {
            var topNode: Node = nodeStack.peek()
            if (parser.getGrammar().terminals.contains(topNode.value) || topNode.value.contains(this.EPSILON)) {
                while (nodeStack.isNotEmpty() && !nodeStack.peek().hasRight) {
                    nodeStack.pop()
                }
                if (nodeStack.size > 0) {
                    nodeStack.pop()
                } else {
                    break
                }
                continue
            }

            var production = parser.getProductionByItsNumber(productionList[productionsIndex])
            nodeIndex += production.size - 1
            for (i in production.size - 1 downTo 0) {
                val sibling = if (i == 0) 0 else this.nodeIndex - 1
                var child: Node =
                    Node(topNode.index, production[i], this.nodeIndex, sibling, i != production.size - 1)
                this.nodeIndex -= 1
                nodeStack.push(child)
                nodeList.add(child)
            }
            this.nodeIndex += production.size + 1
            productionsIndex += 1
        }
    }

    public fun displayTree() {
        this.nodeList = this.nodeList.sortedBy { it.index }.toList() as ArrayList<Node>

        val stringBuilder: StringBuilder = StringBuilder()
        stringBuilder.append("Index " + "Value " + "Parent " + "Sibling " + "\n")

        nodeList.forEach { node ->
            stringBuilder.append(node)
            stringBuilder.append("\n")
        }

        val file: File = File(outputFile)
        file.writeText(stringBuilder.toString())
        println("------------Tree-------------")
        println(stringBuilder.toString())
    }


}