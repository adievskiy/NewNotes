package com.example.newnotes

class IdGenerator(private val dbHelper: DBHelper) {
    private val existingIds: MutableSet<String> = mutableSetOf()

    fun addId(): Int {
        val newId = generateNewId()
        existingIds.add(newId.toString())
        return newId
    }

    private fun generateNewId(): Int {
        var nextId = 1
        while (dbHelper.isIdExists(nextId)) {
            nextId++
        }
        return nextId
    }
}
