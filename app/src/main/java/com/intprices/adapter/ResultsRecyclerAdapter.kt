package com.intprices.adapter

import android.databinding.DataBindingUtil
import android.os.CountDownTimer
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.intprices.R
import com.intprices.api.model.Product
import com.intprices.databinding.ListItemProductBinding
import java.util.*


class ResultsRecyclerAdapter(val productlist: List<Product>,
                             recyclerView: RecyclerView,
                             pageChangeListener: OnPageChange,
                             loadMoreListener: OnLoadProducts) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var isLoading = true
    private val VIEW_TYPE_ITEM = 0
    private val VIEW_TYPE_LOADING = 1
    var mCounters = HashMap<TextView, CountDownTimer>()

    init {
        val linearLayoutManager = recyclerView.getLayoutManager() as LinearLayoutManager
        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            private var previousTotal = 0
            private val visibleThreshold = 4
            internal var firstVisibleItem: Int = 0
            internal var visibleItemCount: Int = 0
            internal var totalItemCount: Int = 0

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                visibleItemCount = recyclerView.childCount
                totalItemCount = linearLayoutManager.getItemCount()
                firstVisibleItem = linearLayoutManager.findFirstVisibleItemPosition()

                if (pageChangeListener.isLastPage()) {
                    return
                }
                if (totalItemCount !in 0..1) {
                    if (isLoading) {
                        if (totalItemCount > previousTotal) {
                            isLoading = false
                            previousTotal = totalItemCount
                            pageChangeListener.incrementPage()
                        }
                    }
                    if (!isLoading && firstVisibleItem + visibleThreshold >= totalItemCount - visibleItemCount) {
                        loadMoreListener.loadProducts()
                        isLoading = true
                    }
                }
            }
        })
    }


    override fun onBindViewHolder(holder: RecyclerView.ViewHolder?, position: Int) {
        if (holder is ProductHolder)
            holder.binding.product = productlist[position]//IGNOR ERROR, COMPILE FINE

    }


    override fun getItemCount() = if (!isLoading) productlist.size else productlist.size + 1

    override fun getItemViewType(position: Int): Int {
        return if (position === productlist.size) VIEW_TYPE_LOADING else VIEW_TYPE_ITEM
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int) =
            if (viewType == VIEW_TYPE_ITEM) {
                ProductHolder(DataBindingUtil.inflate<ListItemProductBinding>(LayoutInflater.from(parent?.context), R.layout.list_item_product, parent, false)
                )
            } else if (viewType == VIEW_TYPE_LOADING) LoadHolder(LayoutInflater.from(parent?.context).inflate(R.layout.list_item_load, parent, false))
            else null

    //    Product holder
    class ProductHolder(val binding: ListItemProductBinding) : RecyclerView.ViewHolder(binding.root)

    //    Load holder
    class LoadHolder(itemView: View?) : RecyclerView.ViewHolder(itemView)


}