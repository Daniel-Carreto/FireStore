package com.danycarreto.firestore.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.danycarreto.firestore.R

class EstadosAdapter(val items:List<Estado>) : BaseAdapter() {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View? {
        val viewHolder:ViewHolder? = null
        val estado = items.get(position);

        val view: View?
        val vh: ViewHolder
        if (convertView == null) {
            view = LayoutInflater.from(parent?.context).inflate(R.layout.item_capital, parent, false)
            vh = ViewHolder(view)
            view.tag = vh
        } else {
            view = convertView
            vh = view.tag as ViewHolder
        }

        vh.itemName.text = items[position].name
        vh.itemDescription.text = items[position].capital
        return view
    }

    private class ViewHolder(view:View) {
        val itemName:TextView
        val itemDescription: TextView

        init {
            itemName = view.findViewById(R.id.tvEstado) as TextView
            itemDescription =  view.findViewById(R.id.tvCapital) as TextView
        }
    }

    override fun getItem(position: Int): Any {
        return items[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getCount(): Int {
        return items.size
    }
}

data class Estado(
    var name:String,
    var capital:String
)