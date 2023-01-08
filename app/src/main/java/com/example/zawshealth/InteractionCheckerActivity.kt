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

class InteractionCheckerActivity : AppCompatActivity() {
    val serverAddress = "http://192.168.204.30:8100"
    var medicineId = arrayOf<String>()
    var medicineName = arrayOf<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_interaction_checker)

        val backIcon = findViewById<ImageView>(R.id.back_icon)
        val medicineSearchInput = findViewById<EditText>(R.id.medicine_search_input)
        val medicineSearchButton = findViewById<Button>(R.id.medicine_search_button)
        val medicineSearchResultContainer = findViewById<CardView>(R.id.medicine_search_result_container)
        val medicineSearchResultCloseButton = findViewById<ImageView>(R.id.medicine_search_result_close_button)
        val checkInteractionButton = findViewById<Button>(R.id.check_interaction_button)
        val removeAllMedicineButton = findViewById<Button>(R.id.remove_all_medicine_button)
        val interactionCheckResultCloseButton = findViewById<Button>(R.id.interaction_check_result_close_button)

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

        checkInteractionButton.setOnClickListener {
            checkInteraction()
        }

        removeAllMedicineButton.setOnClickListener {
            removeAllMedicineFromList()
        }

        interactionCheckResultCloseButton.setOnClickListener {
            closeInteractionCheckResult()
        }

        showMedicineList()
    }

    fun showMedicineList() {
        val medicineList = findViewById<ListView>(R.id.medicine_list)
        val checkInteractionButton = findViewById<Button>(R.id.check_interaction_button)
        val removeAllMedicineButton = findViewById<Button>(R.id.remove_all_medicine_button)

        for (i in 0 until this.medicineId.size) {
            println(this.medicineId[i])
        }

        if (this.medicineName.size > 0) {
            removeAllMedicineButton.visibility = View.VISIBLE
        } else {
            removeAllMedicineButton.visibility = View.GONE
        }

        if (this.medicineName.size > 1) {
            checkInteractionButton.visibility = View.VISIBLE
        } else {
            checkInteractionButton.visibility = View.GONE
        }

        medicineList.adapter = ArrayAdapter(this, R.layout.activity_medicine_list_item, this.medicineName)
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
                var medicineId = arrayOf<String>()
                var medicineName = arrayOf<String>()

                if (jsonArray.length() > 0) {
                    for (i in 0 until jsonArray.length()) {
                        medicineId += jsonArray.getJSONObject(i).getString("id")
                        medicineName += jsonArray.getJSONObject(i).getString("name")
                    }
                } else {
                    medicineId += "-"
                    medicineName += "Obat tidak ditemukan"
                }

                medicineSearchResultContainer.visibility = View.VISIBLE
                medicineSearchResultList.adapter = ArrayAdapter(this, R.layout.activity_medicine_search_item, medicineName)

                medicineSearchResultList.setOnItemClickListener { parent, view, position, id ->
                    addMedicineToList(medicineId[position], parent.getItemAtPosition(position) as String)
                }
            },
            { error ->
                Toast.makeText(this, "Terjadi kesalahan saat menghubungi peladen", Toast.LENGTH_LONG).show()
            }
        )

        volleyQueue.add(jsonArrayRequest)
    }

    fun addMedicineToList(medicineId: String, medicineName: String) {
        val medicineSearchResultContainer = findViewById<CardView>(R.id.medicine_search_result_container)

        if (this.medicineName.size >= 3) {
            Toast.makeText(this, "Hanya dapat memeriksa interaksi dari maksimal 3 jenis obat", Toast.LENGTH_LONG).show()
            return
        }

        if (this.medicineName.contains(medicineName) == false) {
            this.medicineId += medicineId
            this.medicineName += medicineName

            Toast.makeText(this, medicineName + " berhasil ditambahkan ke daftar obat", Toast.LENGTH_LONG).show()

            medicineSearchResultContainer.visibility = View.INVISIBLE
            showMedicineList()
        } else {
            Toast.makeText(this, medicineName + " sudah ada di daftar obat", Toast.LENGTH_LONG).show()
        }
    }

    fun checkInteraction() {
        val interactionCheckResultContainer = findViewById<CardView>(R.id.interaction_check_result_container)
        val interactionCheckResultList = findViewById<ListView>(R.id.interaction_check_result_list)
        val volleyQueue = Volley.newRequestQueue(this)
        var medicineId = ""

        for (i in 0 until this.medicineId.size) {
            if (i != this.medicineId.size - 1) {
                medicineId += this.medicineId[i] + ","
            } else {
                medicineId += this.medicineId[i]
            }
        }

        println(medicineId)

        val jsonArrayRequest = JsonArrayRequest(
            Request.Method.GET,
            this.serverAddress + "/check_interaction?drugs_id=" + medicineId,
            null,
            { response ->
                val jsonArray = JSONTokener(response.toString()).nextValue() as JSONArray
                var interactions = arrayOf<String>()

                if (jsonArray.length() > 0) {
                    for (i in 0 until jsonArray.length()) {
                        interactions += jsonArray[i].toString()
                    }
                } else {
                    interactions += "Tidak ada interaksi yang ditemukan"
                }

                interactionCheckResultContainer.visibility = View.VISIBLE
                interactionCheckResultList.adapter = ArrayAdapter(this, R.layout.activity_interaction_check_result_item, interactions)
            },
            { error ->
                Toast.makeText(this, "Terjadi kesalahan saat menghubungi peladen", Toast.LENGTH_LONG).show()
            }
        )

        volleyQueue.add(jsonArrayRequest)
    }

    fun removeAllMedicineFromList() {
        this.medicineId = arrayOf<String>()
        this.medicineName = arrayOf<String>()
        showMedicineList()
    }

    fun closeInteractionCheckResult() {
        val interactionCheckResultContainer = findViewById<CardView>(R.id.interaction_check_result_container)
        interactionCheckResultContainer.visibility = View.GONE
    }
}