package com.hamilton.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.View
import android.widget.EditText
import android.widget.SearchView
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_main.*
import com.hamilton.R
import com.hamilton.adapters.CountriesAdapter
import com.hamilton.models.MyCountry
import com.hamilton.services.CountryService
import com.hamilton.services.ServiceBuilder
import com.hamilton.utils.Common
import com.hamilton.utils.RecyclerItemClickListenr
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {


    var dataList = ArrayList < MyCountry > ()
    var allDataList = ArrayList < MyCountry > ()
    lateinit
    var countriesAdapter: CountriesAdapter
    lateinit
    var recyclerView: RecyclerView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        recyclerView = findViewById(R.id.country_recycler);
        countriesAdapter = CountriesAdapter(dataList)
        recyclerView.adapter = countriesAdapter;
        recyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        recyclerView.addOnItemTouchListener(RecyclerItemClickListenr(this, recyclerView, object : RecyclerItemClickListenr.OnItemClickListener {

            override fun onItemClick(view: View, position: Int) {
                //do your work here..
                openCaseDetails(dataList.get(position))
            }
            override fun onItemLongClick(view: View?, position: Int) {
                TODO("do nothing")
            }
        }))

        loadCountries()
    }

    fun openCaseDetails(country: MyCountry) {

        val intent = Intent(this, CaseDetails::class.java).apply {
            putExtra(Common.COUNTRY_NAME, country.country)
            putExtra(Common.COUNTRY_FLAG_URL, country.countryInfo.flag)
            putExtra(Common.COUNTRY_TOTAL_CASES, country.cases.toString())
            putExtra(Common.COUNTRY_TOTAL_DEATHS, country.deaths.toString())
            putExtra(Common.COUNTRY_TODAY_DEATHS, country.todayDeaths.toString())
            putExtra(Common.COUNTRY_TODAY_CASES, country.todayCases.toString())
        }
        startActivity(intent)
    }


    private fun loadCountries() {
        //initiate the service
        val destinationService  = ServiceBuilder.buildService(CountryService::class.java)
        val requestCall =destinationService.getAffectedCountryList()
        //make network call asynchronously
        requestCall.enqueue(object : Callback<List<MyCountry>>{
            override fun onResponse(call: Call<List<MyCountry>>, response: Response<List<MyCountry>>) {
                Log.d("Response", "onResponse: ${response.body()}")
                if (response.isSuccessful){
                    val countryList  = response.body()!!
                    Log.d("Response", "countrylist size : ${countryList.size}")
                    dataList.addAll(response!!.body() !!)
                    allDataList.addAll(response!!.body() !!)
                    recyclerView.adapter!!.notifyDataSetChanged()
                    /*country_recycler.apply {
                        setHasFixedSize(true)
                        layoutManager = GridLayoutManager(this@MainActivity,2)
                        adapter = CountriesAdapter(response.body()!!)
                    }*/
                }else{
                    Toast.makeText(this@MainActivity, "Something went wrong ${response.message()}", Toast.LENGTH_SHORT).show()
                }
            }
            override fun onFailure(call: Call<List<MyCountry>>, t: Throwable) {
                Toast.makeText(this@MainActivity, "Something went wrong $t", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun filter(text: String) {

        if(text.length == 0){
            dataList.clear()
            dataList = allDataList;
            recyclerView.adapter!!.notifyDataSetChanged()
        }
        //new array list that will hold the filtered data
        val filteredNames = ArrayList < MyCountry > ()
        //looping through existing elements and adding the element to filtered list
        dataList!!.filterTo(filteredNames) {
            //if the existing elements contains the search input
            it.country.toLowerCase().contains(text.toLowerCase())
        }
        //calling a method of the adapter class and passing the filtered list
        if (filteredNames != null) {
            countriesAdapter!!.filterList(filteredNames)
            dataList = filteredNames;
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.options_menu, menu)
        val search = menu.findItem(R.id.appSearchBar)
        val searchView = search.actionView as SearchView
        searchView.queryHint = "Search"
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }
            override fun onQueryTextChange(newText: String?): Boolean {
                filter(newText.toString())
                return true
            }
        })

        return super.onCreateOptionsMenu(menu)
    }
}