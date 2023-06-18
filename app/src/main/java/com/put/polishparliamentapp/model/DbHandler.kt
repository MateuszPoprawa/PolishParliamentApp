package com.put.polishparliamentapp.model

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DbHandler (context: Context, name: String?, factory: SQLiteDatabase.CursorFactory?, version: Int?) : SQLiteOpenHelper(context,
    DATABASE_NAME, factory, DATABASE_VERSION) {
    companion object {
        const val DATABASE_VERSION = 1
        const val DATABASE_NAME = "parliament.db"
        const val clubs_table = "clubs"
        const val members_table = "members"
        const val committees_table= "committees"
        const val processes_table = "processes"
    }

    override fun onCreate(db: SQLiteDatabase) {
        val createClubsTable = "CREATE TABLE IF NOT EXISTS $clubs_table (" +
                " id TEXT, name TEXT,  membersCount INT, image TEXT, term TEXT, PRIMARY KEY(id, term))"
        db.execSQL(createClubsTable)

        val createMembersTable = "CREATE TABLE IF NOT EXISTS $members_table (" +
                " id TEXT, firstName TEXT, lastName TEXT, birthDate TEXT, club TEXT, profession TEXT, email TEXT, districtName TEXT," +
                " photo TEXT, term TEXT, PRIMARY KEY(id, term))"
        db.execSQL(createMembersTable)

        val createCommitteesTable = "CREATE TABLE IF NOT EXISTS $committees_table (" +
                " id TEXT, name TEXT, phone TEXT, scope TEXT, term TEXT, PRIMARY KEY(id, term))"
        db.execSQL(createCommitteesTable)

        val createProcessesTable = "CREATE TABLE IF NOT EXISTS $processes_table (" +
                " id TEXT, title TEXT, description TEXT, documentDate TEXT, term TEXT, PRIMARY KEY(id, term) )"
        db.execSQL(createProcessesTable)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $clubs_table")
        db.execSQL("DROP TABLE IF EXISTS $members_table")
        db.execSQL("DROP TABLE IF EXISTS $committees_table")
        db.execSQL("DROP TABLE IF EXISTS $processes_table")
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
            list.add(Club(cursor.getString(0), cursor.getString(1), cursor.getInt(2), cursor.getString(3), cursor.getString(4)))
        }
        cursor.close()
        return list
    }

    fun selectClub(id: String, term:String): Club {
        val query = "SELECT * FROM $clubs_table WHERE id = '$id' AND term = '$term'"
        val db = this.writableDatabase
        val cursor = db.rawQuery(query, null)
        cursor.moveToFirst()
        val club = Club(cursor.getString(0), cursor.getString(1), cursor.getInt(2), cursor.getString(3), cursor.getString(4))
        cursor.close()
        return club
    }

    fun membersTotalCount(term: String): Int {
        val query = "SELECT COUNT(*) FROM $members_table WHERE term = '$term'"
        val db = this.writableDatabase
        val cursor = db.rawQuery(query, null)
        cursor.moveToFirst()
        val count = cursor.getInt(0)
        cursor.close()
        return count
    }

    private fun getMember(cursor: Cursor): Member {
        return  Member(cursor.getString(0),cursor.getString(1), cursor.getString(2), cursor.getString(3),
            cursor.getString(4), cursor.getString(5), cursor.getString(6), cursor.getString(7),
            cursor.getString(8), cursor.getString(9))
    }

    fun selectMember(id: String, term: String): Member {
        val query = "SELECT * FROM $members_table WHERE ID = '$id' AND term = '$term'"
        val db = this.writableDatabase
        val cursor = db.rawQuery(query, null)
        cursor.moveToFirst()
        val member = getMember(cursor)
        cursor.close()
        return member
    }

    fun selectMembers(club: String, term: String): List<Member> {
        val list: MutableList<Member> = mutableListOf()
        val query = "SELECT * FROM $members_table WHERE club = '$club' AND term = '$term'"
        val db = this.writableDatabase
        val cursor = db.rawQuery(query, null)
        while (cursor.moveToNext()){
            list.add(getMember(cursor))
        }
        cursor.close()
        return list
    }

    fun selectCommittee(id: String, term: String): Committee {
        val query = "SELECT * FROM $committees_table WHERE ID = '$id' AND term = '$term'"
        val db = this.writableDatabase
        val cursor = db.rawQuery(query, null)
        cursor.moveToFirst()
        val committee = Committee(cursor.getString(0), cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4))
        cursor.close()
        return committee
    }

    fun selectCommittees(term: String): MutableList<Committee> {
        val list: MutableList<Committee> = mutableListOf()
        val query = "SELECT * FROM $committees_table WHERE term = '$term'"
        val db = this.writableDatabase
        val cursor = db.rawQuery(query, null)
        while (cursor.moveToNext()){
            list.add(Committee(cursor.getString(0), cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4)))
        }
        cursor.close()
        return list
    }

    fun selectProcesses(term: String): MutableList<Processes> {
        val list: MutableList<Processes> = mutableListOf()
        val query = "SELECT * FROM $processes_table WHERE term = '$term'"
        val db = this.writableDatabase
        val cursor = db.rawQuery(query, null)
        while (cursor.moveToNext()){
            list.add(Processes(cursor.getString(0), cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4)))
        }
        cursor.close()
        return list
    }
}