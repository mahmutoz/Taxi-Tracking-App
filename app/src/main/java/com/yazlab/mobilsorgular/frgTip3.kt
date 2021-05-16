package com.yazlab.mobilsorgular

import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.fragment_frg_tip1.*
import kotlinx.android.synthetic.main.fragment_frg_tip3.*

class frgTip3 : Fragment() {

    // Veritabanı bağlantısı
    val database = Firebase.database
    val yolcuBilgileri = database.getReference("yolcuBilgileri")

    var tempDOLoc : Int = 0
    var tempPULoc : Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_frg_tip3, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        haritaci.setOnClickListener{
            val gunDegeri = gun.text.toString().toIntOrNull()
            var gecerMi = 0

            if (gunDegeri == null)
            {
                Toast.makeText(context,"Gün girdisi yapın.",Toast.LENGTH_LONG).show()
                gecerMi=0
            }
            else if (gunDegeri<23 && gunDegeri > 0){
                sorguTip3(gunDegeri)
                gecerMi=1
            }
            else
            {
                Toast.makeText(context,"1-22 aralığında bir gün girin.",Toast.LENGTH_LONG).show()
                gecerMi=0
            }

            if (gecerMi==1)
            {

            }


        }
    }



    fun sorguTip3(gunDegeri : Int?){
        var tempDistance : Float = 0f

        val oku = object : ValueEventListener {
            @RequiresApi(Build.VERSION_CODES.N)
            override fun onDataChange(snapshot: DataSnapshot) {
                for (i in snapshot.children){
                    val tpep_dropoff_datetime = i.child("tpep_dropoff_datetime").getValue().toString()
                    val trip_distance = i.child("trip_distance").getValue().toString()
                    val DOLocationID = i.child("DOLocationID").getValue().toString()
                    val PULocationID = i.child("PULocationID").getValue().toString()
                    if(tpep_dropoff_datetime.contains("12/$gunDegeri/20")){
                        if(trip_distance.toFloat() > tempDistance){
                            tempDistance = trip_distance.toFloat()
                            tempDOLoc = DOLocationID.toInt()
                            tempPULoc = PULocationID.toInt()
                        }
                    }
                }

                println(tempDOLoc)
                println(tempPULoc)

                val degistirici = Intent(getActivity(), Harita::class.java)
                var deger = tempDOLoc*1000 + tempPULoc
                degistirici.putExtra("yollananVeri",deger)
                startActivity(degistirici)





            }
            override fun onCancelled(error: DatabaseError) {
            }
        }
        yolcuBilgileri.addValueEventListener(oku)
    }
}


















