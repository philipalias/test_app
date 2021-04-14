package com.hamilton.activities

import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.hamilton.R
import com.hamilton.utils.Common
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_details.*

class CaseDetails : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_details)
        // Get the Intent that started this activity and extract the string
        val countryName = intent.getStringExtra(Common.COUNTRY_NAME)
        val totalCases = intent.getStringExtra(Common.COUNTRY_TOTAL_CASES)
        val todaysCases = intent.getStringExtra(Common.COUNTRY_TODAY_CASES)
        val totalDeaths = intent.getStringExtra(Common.COUNTRY_TOTAL_DEATHS)
        val todaysDeath = intent.getStringExtra(Common.COUNTRY_TODAY_DEATHS)
        val countryFlag = intent.getStringExtra(Common.COUNTRY_FLAG_URL)

        // Capture the layout's TextView and set the string as its text
        val countryNameTxt = findViewById<TextView>(R.id.countryNameTxt).apply {
            text = countryName
        }
        val totalCasesTxt = findViewById<TextView>(R.id.totalCasesTxt).apply {
            text =  "Total cases :${totalCases}"
        }
        val todayCasesTxt = findViewById<TextView>(R.id.todayCasesTxt).apply {
            text =  "Today's cases :${todaysCases}"
        }
        val totalDeathTxt = findViewById<TextView>(R.id.totalDeathTxt).apply {
            text =  "Total deaths :${totalDeaths}"
        }
        val todayDeathTxt = findViewById<TextView>(R.id.todayDeathTxt).apply {
            text =  "Today's deaths :${todaysDeath}"
        }
        val bannerImage = findViewById<ImageView>(R.id.bannerImage).apply {
            Picasso.get().load(countryFlag).into(bannerImage)
        }
    }
}