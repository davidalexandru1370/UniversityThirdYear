import exceptions.DuplicateEntryException
import exceptions.NotFoundException

class HashTable<T>(private var size: Int) {
    private var items: ArrayList<ArrayList<T>> = ArrayList(size);

    init {
        for(i in 0..size){
            this.items.add(ArrayList<T>());
        }
    }

    fun getSize(): Int {
        return this.size;
    }

    private fun hash(key: Int) : Int{
        return key % size;
    }

    private fun hash(key: String): Int {
        var hash = 0;
        for (element in key) {
            hash += element.code;
        }

        return hash % size;
    }

    private fun hash(key: Char): Int{
        return key.code % size;
    }

    private fun computeHashValue(key: T): Int{
        var value: Int = -1;

        when (key) {
            is Char -> {
                value = hash(key as Char);
            }

            is String -> {
                value = hash(key as String);
            }

            is Int -> {
                value = hash(key as Int);
            }
        }

        return value;
    }

    public fun insert(key: T): Pair<Int, Int>{
        val hashValue: Int = computeHashValue(key);
        if(!this.items[hashValue].contains(key)){
            this.items[hashValue].add(key);
            return Pair(hashValue, items[hashValue].size - 1);
        }
        throw DuplicateEntryException("Key already exists");
    }

    public fun contains(key: T): Boolean{
        val hashValue = computeHashValue(key);
        return this.items[hashValue].contains(key);
    }

    public fun getPosition(key: T) : Pair<Int, Int>{
        if(this.contains(key)){
            val hashValue = computeHashValue(key);
            return Pair(hashValue, this.items[hashValue].indexOf(key));
        }

        throw NotFoundException("Key does not exist");
    }

    public fun getByPosition(position: Pair<Int, Int>): T{
        if(position.first >= this.size || position.second >= this.items[position.first].size){
            throw IndexOutOfBoundsException("Position is out of bounds");
        }

        return this.items[position.first][position.second];
    }

    override fun toString(): String {
        val stringBuilder: StringBuilder = StringBuilder();

        for (list: ArrayList<T> in this.items) {
            stringBuilder.append("[")
            for (element: T in list) {
                stringBuilder.append(element.toString() + ",");
            }
            stringBuilder.removePrefix(",")
            stringBuilder.append("]\n")
        }

        return stringBuilder.toString()
    }

}