package com.ensoft.lesson36.db

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.provider.BaseColumns
import android.provider.BaseColumns._ID

val DATABASENAME = "user_database"
val DATABASE_VERSION = 1
class DatabaseHelper(context: Context) :SQLiteOpenHelper(context, DATABASENAME, null, DATABASE_VERSION) {
    override fun onCreate(db: SQLiteDatabase?) {
        val createTable = "CREATE TABLE ${DatabaseContainer.TABLE_NAME}(${BaseColumns._ID} INTEGER PRIMARY KEY AUTOINCREMENT," +
                "${DatabaseContainer.FIRST_NAME_COL} TEXT, ${DatabaseContainer.LAST_NAME_COL} TEXT," +
                "${DatabaseContainer.AGE} INTEGER)"

        db!!.execSQL(createTable)

    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("DROP TABLE IF EXISTS ${DatabaseContainer.TABLE_NAME}")
    }
    //Insert
    fun insertData(fName: String, lName: String, age: Int): Boolean{
        val db: SQLiteDatabase = this.writableDatabase
        val contentValues = ContentValues()
        with(contentValues){
            put(DatabaseContainer.FIRST_NAME_COL, fName)
            put(DatabaseContainer.LAST_NAME_COL, lName)
            put(DatabaseContainer.AGE, age)
        }
        val isInserted = db.insert(DatabaseContainer.TABLE_NAME, null, contentValues)
        db.close()

        return !isInserted.equals(-1)
    }
    //Read
    fun readData(): Cursor{
        val db = this.writableDatabase
        val read = db.rawQuery(/* sql = */ "SELECT * FROM ${DatabaseContainer.TABLE_NAME}", /* selectionArgs = */
            null)
        return read
    }
    fun deleteData(id: String): Boolean{
        val db = this.writableDatabase
        val isDeleted = db.delete(DatabaseContainer.TABLE_NAME, "$_ID=?", arrayOf(id))
        return !isDeleted.equals(-1)
    }
    //update
    fun updateData(id: String, fName: String, lName: String, age: Int): Boolean{
        val db = this.writableDatabase
        val contentValues = ContentValues()
        with(contentValues){
            put(DatabaseContainer.FIRST_NAME_COL, fName)
            put(DatabaseContainer.LAST_NAME_COL, lName)
            put(DatabaseContainer.AGE, age)
        }
        val isUpdated = db.update(DatabaseContainer.TABLE_NAME, contentValues, "$_ID=?", arrayOf(id))

        return isUpdated != -1
    }

}