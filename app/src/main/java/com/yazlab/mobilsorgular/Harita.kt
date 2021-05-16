package com.yazlab.mobilsorgular

import android.location.Geocoder
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.annotation.RequiresApi

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import java.lang.Exception
import java.util.*
import kotlin.system.measureTimeMillis

class Harita : AppCompatActivity(), OnMapReadyCallback {
    // Veritabanı bağlantısı
    val database = Firebase.database
    val lokasyonlar = database.getReference("lokasyonlar")
    private lateinit var mMap: GoogleMap
    var DOLocation = 0
    var PULocation = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_harita)
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
                .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)


        val alici = intent
        val alinan = intent.getIntExtra("yollananVeri",0)
        DOLocation = alinan!!/1000
        PULocation = alinan%1000
        println(alinan.toString() + " " + DOLocation.toString() + " " + PULocation.toString())
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        // Add a marker in Sydney and move the camera
        locationFind(DOLocation,PULocation)
    }

    fun locationFind(veri1 : Int, veri2: Int) {
        val geocoder = Geocoder(applicationContext, Locale.US)
        val oku = object : ValueEventListener {
            @RequiresApi(Build.VERSION_CODES.N)
            override fun onDataChange(snapshot: DataSnapshot) {
                for (i in snapshot.children){
                    val locationID = i.child("LocationID").getValue().toString()
                    val zone = i.child("Zone").getValue().toString()
                    if(locationID.toInt() == veri1 || locationID.toInt() == veri2){
                        val adres1 = geocoder.getFromLocationName(zone,1)
                        if(adres1 != null){
                            val add1 = LatLng(adres1.get(0).latitude, adres1.get(0).longitude)
                            mMap.addMarker(MarkerOptions().position(add1).title(zone))
                            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(add1,6f))
                        }
                    }
                }
            }
            override fun onCancelled(error: DatabaseError) {
            }
        }
        lokasyonlar.addValueEventListener(oku)
    }
}