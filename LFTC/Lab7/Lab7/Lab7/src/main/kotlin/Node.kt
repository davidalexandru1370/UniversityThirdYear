class Node(
    var parent: Int, var value: String, var index: Int, var sibling: Int, var hasRight: Boolean
) {
    override fun toString(): String {
        return "$index '$value' $parent $sibling"
    }
}