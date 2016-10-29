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
import android.view.View
import android.widget.AutoCompleteTextView
import com.intprices.adapter.OnLoadProducts
import com.intprices.adapter.OnPageChange
import com.intprices.adapter.ResultsRecyclerAdapter
import com.intprices.api.ResultResponce
import com.intprices.api.model.Product
import kotlinx.android.synthetic.main.activity_result.*
import java.util.*

class SearchResultActivity : AbstractToolbarActivity(), OnLoadProducts, OnPageChange {

    private var page = 1
    private var productlist = ArrayList<Product>()
    private var adapter: ResultsRecyclerAdapter? = null
    private var isLoaded = false
    private val listkey = "PRODUCT_LIST"
    private lateinit var requestMap:HashMap<String,String>

    override fun setSettings() {
        setSettings(R.string.title_activity_search, R.layout.activity_result, true,true)
    }

    override fun initViews(state: Bundle?) {
        initProductList()

        if (state != null && state?.containsKey(listkey)!!) {
            productlist.addAll(state?.getParcelableArrayList<Product>(listkey)!!)
            isLoaded = true
        }

        if (intent?.hasExtra(HomeActivity.Companion.SEARCH_REQUEST)!!) {
            requestMap = intent.getBundleExtra(HomeActivity.Companion.SEARCH_REQUEST).loadFromBundle()  as HashMap<String, String>
            if (!isLoaded) tryToLoadResults() else {
                setLoading(false, true, false)
                adapter?.notifyDataSetChanged()
            }
        }
        root_result.setOnRefreshListener(SwipeRefreshLayout.OnRefreshListener {onClearResults(); tryToLoadResults()})
        root_result.setColorSchemeResources(R.color.primaryColorAccent)
    }


    private fun setLoading(loading: Boolean, connection: Boolean, itemsnull: Boolean) {
        when {
            connection && !itemsnull -> {
                recyclerview.visibility = if (!loading) View.VISIBLE else View.INVISIBLE
                progressbar.visibility = if (!loading) View.INVISIBLE else View.VISIBLE
                textview_noitems.visibility = View.INVISIBLE
            }
            connection && itemsnull -> {
                recyclerview.visibility = View.INVISIBLE
                progressbar.visibility = View.INVISIBLE
                textview_noitems.visibility = View.VISIBLE
                textview_noitems.setText(R.string.search_result_empty)
            }
            !connection -> {
                recyclerview.visibility = View.INVISIBLE
                progressbar.visibility = View.INVISIBLE
                textview_noitems.visibility = View.VISIBLE
                textview_noitems.setText(R.string.no_internet)
            }
        }
    }

        private fun initProductList() {
            recyclerview.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
            adapter = ResultsRecyclerAdapter(productlist, recyclerview, this, this)
            recyclerview.adapter = adapter
        }

        private fun tryToLoadResults() {
            if (isConnected()) loadProducts()
            else noInternet()
        }

        private fun noInternet() {
            Snackbar.make(root_result, getString(R.string.no_internet), Snackbar.LENGTH_LONG).setAction(getString(R.string.try_again), View.OnClickListener { tryToLoadResults() }).show()
            setLoading(false,false,true)
        }


        override fun loadProducts() {
            if(isLoaded)makeSearchRequest(request,page)
            LoadResults().execute()

        }

        override fun incrementPage() {
            page++
            requestMap["page"]="$page"
        }

    override fun onSearchClick(v: View) {
        makeSearchRequest()
        onClearResults()
        tryToLoadResults()
    }

    override fun isLastPage() = false

        override fun onSaveInstanceState(outState: Bundle?) {
            super.onSaveInstanceState(outState)
            outState?.putParcelableArrayList(listkey, productlist)

        }
        private fun onClearResults(){
            productlist.clear()
            isLoaded = false
            page = 1
            setLoading(true,isConnected(),false)
        }
        override fun onCreateOptionsMenu(menu: Menu?): Boolean {
            menuInflater.inflate(R.menu.search_menu, menu)
            val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
            val searchItem = menu?.findItem(R.id.menu_searchview)
            val searchView: SearchView? = searchItem?.actionView as SearchView
            if (searchView != null) {
                searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()))
                searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                    override fun onQueryTextSubmit(query: String?): Boolean {
                        makeSearchRequest(query!!)
                        onClearResults()
                        tryToLoadResults()
                        return true
                    }

                    override fun onQueryTextChange(newText: String?): Boolean {
                        return true
                    }
                })
            }
            return true
        }

        inner class LoadResults() : AsyncTask<Int, Void, List<Product>>() {

            override fun onPreExecute() {
                super.onPreExecute()
                if (productlist.size == 0) {
                    setLoading(true, true, false)
                }
            }

            override fun doInBackground(vararg params: Int?): List<Product>? {
                return responce.getSearchResult(requestMap)!!.products
            }

            override fun onPostExecute(result: List<Product>?) {
                result?.let {
                    productlist.addAll(it)
                    adapter?.notifyDataSetChanged()
                }
                if (productlist.size == 0) {
                    setLoading(false, true, true)
                } else setLoading(false, true, false)

                if (productlist.size != 0) isLoaded = true
                root_result.isRefreshing=false
                super.onPostExecute(result)
            }
        }
    }

