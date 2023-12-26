package com.ensoft.lesson36

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.ensoft.lesson36.db.DatabaseContainer
import com.ensoft.lesson36.db.Person

class RvAdapter(var listOfItems:MutableList<Person>): RecyclerView.Adapter<RvAdapter.ViewHolder>() {

    lateinit var listener: OnItemClickListener

    inner class ViewHolder(itemview: View):RecyclerView.ViewHolder(itemview){
        val tvFirst = itemview.findViewById<TextView>(R.id.tv_first)
        val tvLast = itemview.findViewById<TextView>(R.id.tv_last)
        val tvAge = itemview.findViewById<TextView>(R.id.tv_age)

        init {
            itemview.setOnLongClickListener {
                if (adapterPosition != RecyclerView.NO_POSITION)
                    listener.onItemClickListener(adapterPosition)
                true
            }
        }


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.row_item,parent,false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return listOfItems.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val item = listOfItems[position]
        holder.tvFirst.text= item.fName
        holder.tvLast.text = item.lName
        holder.tvAge.text = item.age.toString()
    }
    fun setOnItemLongClickedListener(listener: OnItemClickListener){
        this.listener = listener

    }
    interface OnItemClickListener{
        fun onItemClickListener(position: Int)

    }
}