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

        if(key is String){
            value = hash(key as String);
        }
        else if(key is Int){
            value = hash(key as Int);
        }

        return value;
    }

    public fun insert(key: T): Pair<Int, T>{
        val hashValue: Int = computeHashValue(key);
        if(!this.items[hashValue].contains(key)){
            this.items[hashValue].add(key);
            return Pair(hashValue, key);
        }
        throw Exception("Key already exists");
    }

    public fun contains(key: T): Boolean{
        val hashValue = computeHashValue(key);
        return this.items[hashValue].contains(key);
    }

    public fun getPosition(key: T) : Pair<Int, T>{
        if(this.contains(key)){
            val hashValue = computeHashValue(key);
            return Pair(hashValue, key);
        }

        throw Exception("Key does not exist");
    }

    override fun toString(): String {
        return "HashTable(size=$size, items=$items)"
    }

}