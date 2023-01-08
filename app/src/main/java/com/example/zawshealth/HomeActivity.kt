package com.example.zawshealth

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.LinearLayout
import androidx.cardview.widget.CardView

class HomeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        val medicineList = findViewById<CardView>(R.id.medicine_list)
        val medicineInteraction = findViewById<CardView>(R.id.medicine_interaction)
        val source = findViewById<CardView>(R.id.source)

        medicineList.setOnClickListener {
            val intent = Intent(this, MedicineListActivity::class.java)
            startActivity(intent)
        }
        medicineInteraction.setOnClickListener {
            val intent = Intent(this, InteractionCheckerActivity::class.java)
            startActivity(intent)
        }

        source.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse("https://www.drugs.com")
            startActivity(intent)
        }
    }
}