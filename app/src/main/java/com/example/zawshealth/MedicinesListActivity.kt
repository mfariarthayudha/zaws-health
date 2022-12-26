package com.example.zawshealth

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.ListView

class MedicinesListActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_medicines_list)

        showMedicines()
    }

    fun showMedicines() {
        val medicineDatabase = MedicineDatabase(applicationContext)
        var cursor = medicineDatabase.getAllMedicine()
        var medicines = arrayOf<String>()

        while(cursor!!.moveToNext()) {
            medicines += (cursor.getString(2))
        }

        val medicineListContainer = findViewById<ListView>(R.id.medicines_list_container)
        medicineListContainer.adapter = ArrayAdapter(this, R.layout.activity_medicines_list_view_item, medicines)
    }
}