package com.yazlab.mobilsorgular

import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.RequiresApi
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.*
import kotlinx.android.synthetic.main.fragment_frg_tip1.*

class frgTip1 : Fragment() {
    // Veritabanı bağlantısı
    val database = Firebase.database
    val yolcuBilgileri = database.getReference("yolcuBilgileri")
    val lokasyonlar = database.getReference("lokasyonlar")


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        println("Tarih SeferSayısı YolcuSayısı")
        sorguTip1()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_frg_tip1, container, false)

    }

    fun sorguTip1(){
        var total : Int = 0
        var count : Int = 1
        var temp : String
        val tip1Map = hashMapOf<String,Int>()
        var seferSayisi : Int = 0

        val oku = object : ValueEventListener {
            @RequiresApi(Build.VERSION_CODES.N)
            override fun onDataChange(snapshot: DataSnapshot) {
                for (i in snapshot.children){
                    val tpep_dropoff_datetime = i.child("tpep_dropoff_datetime").getValue()
                    val passenger_count = i.child("passenger_count").getValue()
                    temp = passenger_count.toString() // Direk int şeklinde alındığında hata veriyor o yüzden stringe çevirip sonra int e çevirdim.
                    if(tpep_dropoff_datetime.toString().contains("12/${count.toInt()}/20")){
                        if (passenger_count != null) { total = total.plus(temp.toInt()) }
                        seferSayisi++;
                    } else if (count != 24){ // toplam 23 gün olduğu için
                        tip1Map.put("12/${count}/20\t$seferSayisi",total) // Hashmape kaydetme
                        seferSayisi =  0
                        total = 0
                        count++;
                    } else {
                        break
                    }
                }

                val result = tip1Map.toList().sortedByDescending { (_, value) -> value }.toMap() // value leri büyükten küçüğe sıralama
                var five : Int = 0
                for (i in result) {
                    println(i.key + "\t" + i.value)
                    //stringArrayi.add("örnek")
                    //yolcuArrayi[five+1].setText("örnek")
                    if(five==0)
                    {
                        val list = i.key.split("\t")
                        tip1tarih1.text=list[0]
                        tip1sefer1.text=list[1]+"        "
                        tip1yolcu1.text="${i.value}"
                    }
                    else if(five==1)
                    {
                        val list = i.key.split("\t")
                        tip1tarih2.text=list[0]
                        tip1sefer2.text=list[1]+"        "
                        tip1yolcu2.text="${i.value}"
                    }
                    else if(five==2)
                    {
                        val list = i.key.split("\t")
                        tip1tarih3.text=list[0]
                        tip1sefer3.text=list[1]+"        "
                        tip1yolcu3.text="${i.value}"
                    }
                    else if(five==3)
                    {
                        val list = i.key.split("\t")
                        tip1tarih4.text=list[0]
                        tip1sefer4.text=list[1]+"        "
                        tip1yolcu4.text="${i.value}"
                    }
                    else if(five==4)
                    {
                        val list = i.key.split("\t")
                        tip1tarih5.text=list[0]
                        tip1sefer5.text=list[1]+"        "
                        tip1yolcu5.text="${i.value}"
                    }
                    five++;
                }
            }
            override fun onCancelled(error: DatabaseError) {
            }
        }
        yolcuBilgileri.addValueEventListener(oku)
    }
}