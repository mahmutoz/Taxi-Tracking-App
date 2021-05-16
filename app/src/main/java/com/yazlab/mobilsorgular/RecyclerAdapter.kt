package com.yazlab.mobilsorgular

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.rv_row.view.*

class RecyclerAdapter (val veriler : ArrayList<String>) : RecyclerView.Adapter<RecyclerAdapter.Tip2VH>() {
    class Tip2VH(itemView: View) : RecyclerView.ViewHolder (itemView) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Tip2VH {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.rv_row,parent,false)
        return Tip2VH(itemView)
    }

    override fun onBindViewHolder(holder: Tip2VH, position: Int) {
        holder.itemView.rvTextView.text = veriler.get(position)
    }

    override fun getItemCount(): Int {
        return veriler.size
    }


}