package com.intprices

import android.app.SearchManager
import android.content.Context
import android.graphics.PorterDuff
import android.os.AsyncTask
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.SearchView
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.AutoCompleteTextView
import com.intprices.adapter.OnLoadProducts
import com.intprices.adapter.OnPageChange
import com.intprices.adapter.ResultsRecyclerAdapter
import com.intprices.api.ResultResponce
import com.intprices.api.model.Product
import kotlinx.android.synthetic.main.activity_result.*
import kotlinx.android.synthetic.main.activity_drawer.*
import java.util.*

class SearchResultActivity : AbstractFiltersActivity(), OnLoadProducts, OnPageChange {


    private var page = 1
    private var productList = ArrayList<Product>()
    private var adapter: ResultsRecyclerAdapter? = null
    private var isLoaded = false
    private val listKey = "PRODUCT_LIST"

    private val NO_INTERNET = 1
    private val NO_PRODUCTS = 2
    private val LOADING = 3
    private val RESULT = 4


    override fun setSettings() {
        setSettings(R.string.title_activity_search, R.layout.activity_result, R.layout.activity_drawer,false)
    }

    override fun initFilterHolder() = FiltersHolder(drawer_filter_root,drawer_filter_form_query,
            drawer_filter_category,drawer_filter_country,
            drawer_filter_type,drawer_filter_condition,
            drawer_filter_sort,drawer_filter_checkbox,
            drawer_filter_edit_from_price,drawer_filter_price_to,
            drawer_filter_search_btn,drawer_filter_repeat,
            drawer_filter_progressbar)

    override fun initViewStub(layoutId: Int) {
        drawer_viewstub.layoutResource = layoutId
        drawer_viewstub.inflate()
    }

    override fun initViews(state: Bundle?) {
        super.initViews(state)
        initProductList()

        if (state != null && state.containsKey(listKey)) {
            val savedList = state.getParcelableArrayList<Product>(listKey)
            if (savedList.isNotEmpty()) {
                productList.addAll(state.getParcelableArrayList<Product>(listKey)!!)
                isLoaded = true
            }
        }

        if (intent.hasExtra(HomeActivity.Companion.SEARCH_REQUEST)) {

            requestMap = intent.getBundleExtra(HomeActivity.Companion.SEARCH_REQUEST).loadFromBundle() as HashMap<String, String>

            if (!isLoaded) tryToLoadResults() else {
                setLoading(RESULT)
                adapter?.notifyDataSetChanged()
            }
        }
        root_result.setOnRefreshListener({ onClearResults(); tryToLoadResults() })
        root_result.setColorSchemeResources(R.color.primaryColorAccent)
    }

    private fun openDrawer(status: Boolean = false) {
        if (status)
            drawerlayout.openDrawer(navigationview)
        else drawerlayout.closeDrawers()
    }

    private fun setLoading(status: Int) {
        when (status) {
            LOADING, RESULT -> {
                recyclerview.visibility = if (status == RESULT) View.VISIBLE else View.INVISIBLE
                progressbar.visibility = if (status == RESULT) View.INVISIBLE else View.VISIBLE
                textview_noitems.visibility = View.INVISIBLE
            }
            NO_PRODUCTS -> {
                recyclerview.visibility = View.INVISIBLE
                progressbar.visibility = View.INVISIBLE
                textview_noitems.visibility = View.VISIBLE
                textview_noitems.setText(R.string.search_result_empty)
            }
            NO_INTERNET -> {
                recyclerview.visibility = View.INVISIBLE
                progressbar.visibility = View.INVISIBLE
                textview_noitems.visibility = View.VISIBLE
                textview_noitems.setText(R.string.no_internet)
            }
        }
    }

    private fun initProductList() {
        recyclerview.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        adapter = ResultsRecyclerAdapter(productList, recyclerview, this, this)
        recyclerview.adapter = adapter
    }

    private fun tryToLoadResults() {
        if (isConnected()) loadProducts()
        else noInternet()
    }

    private fun noInternet() {
        Snackbar.make(root_result, getString(R.string.no_internet), Snackbar.LENGTH_LONG)
                .setAction(getString(R.string.try_again), { tryToLoadResults() }).show()
        setLoading(NO_INTERNET)
    }


    override fun loadProducts() {
        if (isLoaded) makeSearchMap(request, page)
        LoadResults().execute()

    }

    override fun incrementPage() {
        page++
        requestMap["page"] = page.toString()
    }

    override fun onSearchClick() {
        makeSearchMap()
        onClearResults()
        tryToLoadResults()
    }

    override fun isLastPage() = false

    override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)
        outState?.putParcelableArrayList(listKey, productList)

    }

    private fun onClearResults() {
        productList.clear()
        isLoaded = false
        page = 1
        requestMap["page"] = page.toString()
        setLoading(LOADING)
        initProductList()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.search_menu, menu)
        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        val searchItem = menu?.findItem(R.id.menu_searchview)
        val searchView: SearchView? = searchItem?.actionView as SearchView
        if (searchView != null) {
            searchView.setSearchableInfo(searchManager.getSearchableInfo(componentName))
            searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean {
                    makeSearchMap(query.toString())
                    onClearResults()
                    tryToLoadResults()
                    return true
                }

                override fun onQueryTextChange(newText: String?): Boolean = true
            })
        }
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when(item?.itemId){
            R.id.menu_filter -> openDrawer(!drawerlayout.isDrawerOpen(navigationview))
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onFiltersLoaded() {
        openDrawer(true)
    }

    inner class LoadResults() : AsyncTask<Int, Void, List<Product>>() {

        override fun onPreExecute() {
            super.onPreExecute()
            if (productList.size == 0) {
                setLoading(LOADING)
            }
        }

        override fun doInBackground(vararg params: Int?): List<Product>? {
            return responce.getSearchResult(requestMap)?.products
        }

        override fun onPostExecute(result: List<Product>?) {
            result?.let {
                productList.addAll(it)
                adapter?.notifyDataSetChanged()
            }
            if (productList.size == 0) {
                setLoading(NO_PRODUCTS)
            } else {
                setLoading(RESULT)
                isLoaded = true
                root_result.isRefreshing = false
                openDrawer(false)
            }
            super.onPostExecute(result)
        }
    }
}

