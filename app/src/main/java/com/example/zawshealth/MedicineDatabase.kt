package com.example.zawshealth

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.database.Cursor

class MedicineDatabase (context: Context) : SQLiteOpenHelper (context, "admin", null, 1) {
    override fun onCreate(db: SQLiteDatabase?) {
        TODO("Not yet implemented")

//        db?.execSQL("CREATE TABLE medicines(record_id INTEGER PRIMARY KEY AUTOINCREMENT, medicine_name VARCHAR)")
//        db?.execSQL("INSERT INTO medicines(medicine_name) VALUES('acetazolamide')")
//        db?.execSQL("INSERT INTO medicines(medicine_name) VALUES('Paracetamol acetaminophen')")
//        db?.execSQL("INSERT INTO medicines(medicine_name) VALUES('timolol')")
//        db?.execSQL("INSERT INTO medicines(medicine_name) VALUES('timolol')")
    }

    override fun onUpgrade(p0: SQLiteDatabase?, p1: Int, p2: Int) {
        TODO("Not yet implemented")
    }

    fun createTable() {
        val db = this.writableDatabase
        db?.execSQL("CREATE TABLE IF NOT EXISTS medicines(record_id INTEGER PRIMARY KEY AUTOINCREMENT, medicine_name VARCHAR)")
    }

    fun addMedicine(medicineName: String) {
        val db = this.writableDatabase
        val insertData = ContentValues()

        insertData.put("medicine_name", medicineName)

        db.insert("medicines", null, insertData)
        db.close()
    }

    fun getAllMedicine(): Cursor? {
        val db = this.readableDatabase
        return db.rawQuery("SELECT * FROM medicines", null)
    }

    fun deleteAll() {
        val db = this.writableDatabase

        db.delete("medicines", null, null)
        db.close()
    }
}