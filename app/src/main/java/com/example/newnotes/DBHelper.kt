package com.example.newnotes

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteException
import android.database.sqlite.SQLiteOpenHelper

class DBHelper(context: Context, factory: SQLiteDatabase.CursorFactory?) :
    SQLiteOpenHelper(context, DATABASE_NAME, factory, DATABASE_VERSION) {
    companion object {
        const val DATABASE_NAME = "db"
        const val DATABASE_VERSION = 1
        const val TABLE_NAME = "notes_table"
        const val KEY_ID = "id"
        const val KEY_NOTE = "note"
        const val KEY_DATE = "date"
    }

    override fun onCreate(db: SQLiteDatabase) {
        val query = ("CREATE TABLE " + TABLE_NAME + " (" +
                KEY_ID + " INTEGER PRIMARY KEY, " +
                KEY_NOTE + " TEXT, " +
                KEY_DATE + " TEXT" + ")")
        db.execSQL(query)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
    }

    fun addNote(notes: Notes) {
        val values = ContentValues()
        values.put(KEY_ID, notes.id)
        values.put(KEY_NOTE, notes.note)
        values.put(KEY_DATE, notes.date)
        val db = writableDatabase
        db.insert(TABLE_NAME, null, values)
        db.close()
    }

    @SuppressLint("Range", "Recycle")
    fun getInfo(): MutableList<Notes> {
        val notesList: MutableList<Notes> = mutableListOf()
        val selectQuery = "SELECT * FROM $TABLE_NAME"
        val db = this.readableDatabase
        var cursor: Cursor? = null
        try {
            cursor = db.rawQuery(selectQuery, null)
        } catch (e: SQLiteException) {
            db.execSQL(selectQuery)
            return notesList
        }
        if (cursor.moveToFirst()) {
            do {
                val id = cursor.getInt(cursor.getColumnIndex("id"))
                val note = cursor.getString(cursor.getColumnIndex("note"))
                val date = cursor.getString(cursor.getColumnIndex("date"))
                val notes = Notes(id, note, date)
                notesList.add(notes)
            } while (cursor.moveToNext())
        }
        return notesList
    }

    fun removeDB() {
        val db = this.writableDatabase
        db.delete(TABLE_NAME, null, null)
    }

    fun deleteNote(notes: Notes) {
        val db = writableDatabase
        val contentValues = ContentValues()
        contentValues.put(KEY_ID, notes.id)
        db.delete(TABLE_NAME, "id=" + notes.id, null)
        db.close()
    }

    fun isIdExists(id: Int): Boolean {
        val db = this.readableDatabase
        val cursor = db.rawQuery(
            "SELECT COUNT(*) FROM $TABLE_NAME WHERE $KEY_ID = ?",
            arrayOf(id.toString())
        )
        cursor.use {
            if (it.moveToFirst()) {
                return it.getInt(0) > 0
            }
        }
        return false
    }
}