package com.example.zawshealth

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.ListView

class MedicinesListActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_medicines_list)

        var medicines = arrayOf(
            "paracetamol",
            "acetaminophen",
            "brinzolamide",
            "timolol",
            "acetazolamide"
        )

        val medicineListView = findViewById<ListView>(R.id.medicines_list_container)

        medicineListView.adapter = ArrayAdapter(this, R.layout.activity_medicines_list_view_item, medicines)
    }
}