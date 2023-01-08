package com.example.zawshealth

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import com.android.volley.Request
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.Volley
import org.json.JSONArray
import org.json.JSONTokener

class MedicineListActivity : AppCompatActivity() {
    val serverAddress = "http://192.168.204.30:8100"
    var medicines = arrayOf<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_medicine_list)

        val backIcon = findViewById<ImageView>(R.id.back_icon)
        val medicineSearchInput = findViewById<EditText>(R.id.medicine_search_input)
        val medicineSearchButton = findViewById<Button>(R.id.medicine_search_button)
        val medicineSearchResultContainer = findViewById<CardView>(R.id.medicine_search_result_container)
        val medicineSearchResultCloseButton = findViewById<ImageView>(R.id.medicine_search_result_close_button)
        val removeAllMedicineButton = findViewById<Button>(R.id.remove_all_medicine_button)

        backIcon.setOnClickListener {
            val intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)
        }

        medicineSearchButton.setOnClickListener {
            val searchKey = medicineSearchInput.text.toString()
            searchMedicine(searchKey)
        }

        medicineSearchResultCloseButton.setOnClickListener {
            medicineSearchResultContainer.visibility = View.INVISIBLE
        }

        removeAllMedicineButton.setOnClickListener {
            removeAllMedicineFromList()
        }

        showMedicineList()
    }

    fun showMedicineList() {
        val medicineList = findViewById<ListView>(R.id.medicine_list)
        val removeAllMedicineButton = findViewById<Button>(R.id.remove_all_medicine_button)

        this.medicines = arrayOf<String>()

        val medicineDatabase = MedicineDatabase(applicationContext)
        var cursor = medicineDatabase.getAllMedicine()

        while(cursor!!.moveToNext()) {
            this.medicines += (cursor.getString(1))
        }

        if (this.medicines.size > 0) {
            removeAllMedicineButton.visibility = View.VISIBLE
        } else {
            removeAllMedicineButton.visibility = View.GONE
        }

        medicineList.adapter = ArrayAdapter(this, R.layout.activity_medicine_list_item, this.medicines)
    }

    fun removeAllMedicineFromList() {
        val medicineDatabase = MedicineDatabase(applicationContext)
        medicineDatabase.deleteAll()

        showMedicineList()
    }

    fun addMedicineToList(medicineName: String) {
        val medicineSearchResultContainer = findViewById<CardView>(R.id.medicine_search_result_container)

        if (this.medicines.contains(medicineName) == false) {
            val medicineDatabase = MedicineDatabase(applicationContext)
            medicineDatabase.addMedicine(medicineName)

            Toast.makeText(this, medicineName + " berhasil ditambahkan ke daftar obat", Toast.LENGTH_LONG).show()

            medicineSearchResultContainer.visibility = View.INVISIBLE
            showMedicineList()
        } else {
            Toast.makeText(this, medicineName + " sudah ada di daftar obat", Toast.LENGTH_LONG).show()
        }
    }

    fun searchMedicine(searchKey: String) {
        val medicineSearchResultContainer = findViewById<CardView>(R.id.medicine_search_result_container)
        val medicineSearchResultList = findViewById<ListView>(R.id.medicine_search_result_list)
        val volleyQueue = Volley.newRequestQueue(this)

        val jsonArrayRequest = JsonArrayRequest(
            Request.Method.GET,
            this.serverAddress + "/search_drug?drug_name=" + searchKey,
            null,
            { response ->
                val jsonArray = JSONTokener(response.toString()).nextValue() as JSONArray
                var searchResults = arrayOf<String>()

                if (jsonArray.length() > 0) {
                    for (i in 0 until jsonArray.length()) {
                        val medicineName = jsonArray.getJSONObject(i).getString("name")
                        searchResults += medicineName
                    }
                } else {
                    searchResults += "Obat tidak ditemukan"
                }

                medicineSearchResultContainer.visibility = View.VISIBLE
                medicineSearchResultList.adapter = ArrayAdapter(this, R.layout.activity_medicine_search_item, searchResults)
            },
            { error ->
                Toast.makeText(this, "Terjadi kesalahan saat menghubungi peladen", Toast.LENGTH_LONG).show()
            }
        )

        volleyQueue.add(jsonArrayRequest)
        medicineSearchResultList.setOnItemClickListener { parent, view, position, id ->
            val medicineName = parent.getItemAtPosition(position) as String
            addMedicineToList(medicineName)
        }
    }
}