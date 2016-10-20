package com.intprices.adapter

import android.databinding.DataBindingUtil
import android.support.v7.widget.RecyclerView
import android.text.Layout
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.intprices.R
import com.intprices.api.model.Product
import com.intprices.databinding.ListItemProductBinding


class ResultsRecyclerAdapter(val productlist: List<Product>, recycler: RecyclerView) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    init {

    }

    private var loading = false

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder?, position: Int) {
        if (holder is ProductHolder)
            holder.binder.product =productlist[position] //IGNOR ERROR, COMPILE FINE
    }


    override fun getItemCount() = if (loading) productlist.size else productlist.size + 1


    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int) =
            if (!loading){
                val inflator= LayoutInflater.from(parent?.context)
                val binding=ListItemProductBinding.inflate(inflator,parent,false)
                ProductHolder(binding.root)
            }
            else LoadHolder(LayoutInflater.from(parent?.context).inflate(R.layout.list_item_load, parent, false))

    //    Product holder
    class ProductHolder(itemView: View?) : RecyclerView.ViewHolder(itemView) {
        val binder: ListItemProductBinding=DataBindingUtil.bind(itemView)
    }

    //    Load holder
    class LoadHolder(itemView: View?) : RecyclerView.ViewHolder(itemView)


}