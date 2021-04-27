package com.fiqih.aplikasipencatatan

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.SQLException
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DatabaseHandler(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private val DATABASE_VERSION = 1
        private val DATABASE_NAME = "ActivityDatabase1s"

        private val TABLE_CONTACTS = "ActivityTable"

        private val KEY_ID = "_id"
        private val KEY_TIME = "time"
        private val KEY_DESCRIPTION = "description"

    }

    override fun onCreate(db: SQLiteDatabase?) {
        val CREATE_CONTACTS_TABLE = ("CREATE TABLE " + TABLE_CONTACTS + "("
                + KEY_ID + " INTEGER PRIMARY KEY,"
                + KEY_TIME + " TEXT,"
                + KEY_DESCRIPTION + " TEXT)")

        db?.execSQL(CREATE_CONTACTS_TABLE)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db!!.execSQL("DROP TABLE IF EXISTS $TABLE_CONTACTS")
        onCreate(db)
    }


    // Methot untuk memasukkan DATA / Record

    fun addActivity(emp: MyActivityModel): Long {
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(KEY_TIME, emp.time)
        contentValues.put(KEY_DESCRIPTION, emp.description)


        // Memasukkan detail karyawan menggunakan kueri sisipkan
        val success = db.insert(TABLE_CONTACTS, null, contentValues)
        db.close()
        return success
    }

    // metode untuk membaca catatan
    fun viewActivity(): ArrayList<MyActivityModel> {
        val empList: ArrayList<MyActivityModel> = ArrayList<MyActivityModel>()
        val selectQuery = "SELECT * FROM ${TABLE_CONTACTS}"

        val db = this.readableDatabase

        var cursor: Cursor? = null

        try {
            cursor = db.rawQuery(selectQuery, null)
        } catch (e: SQLException) {
            db.execSQL(selectQuery)
            return ArrayList()
        }
        var id: Int
        var time: String
        var description: String



        if (cursor.moveToFirst()) {
            do {
                id = cursor.getInt(cursor.getColumnIndex(KEY_ID))
                time = cursor.getString(cursor.getColumnIndex(KEY_TIME))
                description = cursor.getString(cursor.getColumnIndex(KEY_DESCRIPTION))

                val emp = MyActivityModel(id = id, time = time, description = description)
                empList.add(emp)
            } while (cursor.moveToNext())
        }
        return empList
    }

    // method untuk menghapus data/record dalam database
    fun deleteActivity(emp: MyActivityModel): Int {
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(KEY_ID, emp.id)

        val success = db.delete(TABLE_CONTACTS, KEY_ID + "=" + emp.id, null)
        db.close()
        return success
    }

    // method untuk mengupdate data/record
    fun updateActivity(emp: MyActivityModel): Int {
        val db = this.writableDatabase
        val contentvalues = ContentValues()
        contentvalues.put(KEY_TIME, emp.time)
        contentvalues.put(KEY_DESCRIPTION, emp.description)

        val success = db.update(TABLE_CONTACTS, contentvalues, KEY_ID + "=" + emp.id,null)
        db.close()
        return success
    }




}