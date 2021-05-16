package com.yazlab.mobilsorgular

import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.fragment_frg_tip2.*

class frgTip2 : Fragment() {

    // Veritabanı bağlantısı
    val database = Firebase.database
    val yolcuBilgileri = database.getReference("yolcuBilgileri")
    val lokasyonlar = database.getReference("lokasyonlar")

    // Veriler
    var veriler = ArrayList<String>()
    private lateinit var adapter : RecyclerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sorguTip2()
        //veriler.add("Test")
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_frg_tip2, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        println(veriler.size)

        val layoutManager = LinearLayoutManager(context)
        recyclerView.layoutManager = layoutManager

        adapter = RecyclerAdapter(veriler)
        recyclerView.adapter = adapter

    }

    fun sorguTip2(){
        var total : Float = 0f
        var count : Int = 1
        var ort : Float = 0f
        var temp : String
        val tip2Map = hashMapOf<String,Float>()
        var seferSayisi : Int = 0

        val oku = object : ValueEventListener {
            @RequiresApi(Build.VERSION_CODES.N)
            override fun onDataChange(snapshot: DataSnapshot) {
                for (i in snapshot.children){
                    val tpep_dropoff_datetime = i.child("tpep_dropoff_datetime").getValue()
                    val total_amount = i.child("total_amount").getValue()
                    temp = total_amount.toString()
                    if(tpep_dropoff_datetime.toString().contains("12/${count.toInt()}/20")){
                        if (total_amount != null) { total = total.plus(temp.toFloat()) }
                        seferSayisi++;
                    } else if (count != 24){
                        var veri = "0"
                        if (count >= 10)
                            veri = count.toString()
                        else
                            veri += count.toString()
                        ort = total / seferSayisi

                        tip2Map.put("12/${veri}/20",ort)
                        seferSayisi =  0
                        total = 0f
                        ort = 0f
                        count++;
                    } else {
                        break
                    }
                }

                val result = tip2Map.toList().sortedBy { (_, value) -> value }.toMap() // value leri küçükten büyüğe sıralama
                var two : Int = 0
                var kucukTarih : String
                var buyukTarih : String
                var tarihTemp : String = ""

                println("********* ORTALAMA ALINAN TÜM ÜCRETLER (Küçükten Büyüğe) ***********") // !! ortalama alınan tüm ücretleri de yazdıralım ekrana hoca kontrol ederken uğraşmasın
                for (i in result) {
                    println(i.key + " Tarihinde alınan ortalama ücret= " + i.value + " $\'dır.")
                }

                println("********* EN AZ ÜCRET ALINAN 2 TARİH ***********")
                for (i in result) {
                    if(two == 0){
                        tarihTemp = i.key
                        two++
                    } else if(two == 1){
                        if( tarihTemp < i.key){
                            kucukTarih = tarihTemp
                            buyukTarih = i.key
                        } else {
                            kucukTarih = i.key
                            buyukTarih = tarihTemp
                        }
                        println("\n ######### En az ucret alınan iki tarih arasindaki günlük alınan ortalama ucretler ##########\n")
                        println("Kücük Tarih= " + kucukTarih + " Büyük Tarih= " + buyukTarih)
                        tarihAraligi.text = "Kücük Tarih= " + kucukTarih + " - Büyük Tarih= " + buyukTarih
                        val temp2 = tip2Map.toList().sortedBy { (key, value) -> key }.toMap() // keyleri küçükten büyüğe sıralama
                        for (j in temp2){
                            if ( j.key >= kucukTarih && j.key <= buyukTarih){

                                println(j.key + " tarihinde alınan ortalama ücret= " + j.value + " $\'dır.")
                                veriler.add(j.key + " tarihinde alınan ortalama ücret= " + "%.2f".format(j.value) + "$\'dır.")
                                //println(veriler.size)
                            //   12/12/2020
                            }
                        }
                        adapter.notifyDataSetChanged()
                        two++
                    } else {
                        break
                    }
                }
            }
            override fun onCancelled(error: DatabaseError) {
            }
        }
        yolcuBilgileri.addValueEventListener(oku)
        //
    }
}