package com.example.zawshealth

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.database.Cursor

class MedicineDatabase (context: Context) : SQLiteOpenHelper (context, "admin", null, 1) {
    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL("CREATE TABLE medicines(record_id INTEGER PRIMARY KEY AUTOINCREMENT, medicine_id VARCHAR, medicine_name VARCHAR)")
        db?.execSQL("INSERT INTO medicines(medicine_id, medicine_name) VALUES('11-2744', 'acetazolamide')")
        db?.execSQL("INSERT INTO medicines(medicine_id, medicine_name) VALUES('86-0', 'Paracetamol acetaminophen')")
        db?.execSQL("INSERT INTO medicines(medicine_id, medicine_name) VALUES('2196-0', 'timolol')")
        db?.execSQL("INSERT INTO medicines(medicine_id, medicine_name) VALUES('2196-0', 'timolol')")
    }

    override fun onUpgrade(p0: SQLiteDatabase?, p1: Int, p2: Int) {
        TODO("Not yet implemented")
    }

    fun createTable() {
        val db = this.writableDatabase
        db?.execSQL("CREATE TABLE medicines(record_id INTEGER PRIMARY KEY AUTOINCREMENT, medicine_id VARCHAR, medicine_name VARCHAR)")
    }

    fun getAllMedicine(): Cursor? {
        val db = this.readableDatabase
        return db.rawQuery("SELECT * FROM medicines", null)
    }
}