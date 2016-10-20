package com.intprices

import android.os.AsyncTask
import com.intprices.adapter.ResultsRecyclerAdapter
import com.intprices.api.ResultResponce
import com.intprices.api.model.Product
import kotlinx.android.synthetic.main.activity_result.*

class SearchResultActivity : AbstractToolbarActivity() {

    private var request: String = ""

    override fun setSettings() {
        setSettings(R.string.title_activity_search, R.layout.activity_result, true)
    }

    override fun initViews() {

        if (intent?.hasExtra(HomeActivity.Companion.SEARCH_REQUEST)!!) {
            request = intent.getStringExtra(HomeActivity.Companion.SEARCH_REQUEST)
            query.setText(request)
            loadResult()
        }
    }

    private fun loadResult() {
        LoadResults().execute()
    }

    inner class LoadResults() : AsyncTask<Int, Void, List<Product>>() {

        override fun onPreExecute() {
            super.onPreExecute()
        }

        override fun doInBackground(vararg params: Int?): List<Product> {
            return ResultResponce.instance.getSearchResult(request)?.products!!
        }

        override fun onPostExecute(result: List<Product>?) {
            val adapter = ResultsRecyclerAdapter(result!!, recyclerview)
            recyclerview.adapter = adapter
            super.onPostExecute(result)

        }
    }
}