package com.put.polishparliamentapp.model

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DbHandler (context: Context, name: String?, factory: SQLiteDatabase.CursorFactory?, version: Int?) : SQLiteOpenHelper(context,
    DATABASE_NAME, factory, DATABASE_VERSION) {
    companion object {
        const val DATABASE_VERSION = 5
        const val DATABASE_NAME = "parliament.db"
        const val clubs_table = "clubs"

    }

    override fun onCreate(db: SQLiteDatabase) {
        val createClubsTable = "CREATE TABLE IF NOT EXISTS $clubs_table (" +
                " id TEXT, name TEXT,  membersCount INT, image TEXT, term TEXT, PRIMARY KEY(id, term))"
        db.execSQL(createClubsTable)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $clubs_table")
        onCreate(db)
    }

    fun insert(tableName: String, values: ContentValues): Boolean {
        return if (!checkIfExists(tableName, values.get("id").toString(), values.get("term").toString())) {
            val db = this.writableDatabase
            db.insert(tableName, null, values)
            true
        } else false
    }

    private fun checkIfExists(tableName: String, id: String, term: String): Boolean {
        val query = "SELECT * FROM $tableName WHERE id = '$id' AND term = '$term'"
        val db = this.writableDatabase
        val cursor = db.rawQuery(query, null)
        val exists = cursor.count != 0
        cursor.close()
        return exists
    }

    fun selectClubs(term: String) :MutableList<Club> {
        val list: MutableList<Club> = mutableListOf()
        val query = "SELECT * FROM $clubs_table WHERE term = '$term' ORDER BY membersCount DESC"
        val db = this.writableDatabase
        val cursor = db.rawQuery(query, null)
        while (cursor.moveToNext()){
            list.add(Club(cursor.getString(0), cursor.getString(1), cursor.getInt(2), cursor.getString(3)))
        }
        cursor.close()
        return list
    }

}