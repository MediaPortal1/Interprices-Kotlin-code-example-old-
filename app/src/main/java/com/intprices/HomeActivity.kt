package com.intprices

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdRequest
import com.google.firebase.analytics.FirebaseAnalytics
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.activity_toolbar.*


class HomeActivity : AbstractFiltersActivity() {


    companion object {
        val SEARCH_REQUEST = "intprices_search_request"
    }

    override fun setSettings() {
        setSettings(R.string.title_activity_home, R.layout.activity_home, R.layout.activity_toolbar, false)
    }

    override fun initFilterHolder() = FiltersHolder(home_filter_root, form_home_query, home_filter_category,
            home_filter_country, home_filter_type,
            home_filter_condition, home_filter_sort,
            home_filter_freeshipping_switch, home_filter_edit_from_price,
            home_filter_price_to, home_filter_repeat, home_filter_progressbar)


    override fun initViewStub(layoutId: Int) {
        toolbar_viewstub.layoutResource = layoutId
        toolbar_viewstub.inflate()
    }

    override fun initViews(state: Bundle?) {
        super.initViews(state)
        form_home_query.imeOptions = EditorInfo.IME_ACTION_SEARCH or EditorInfo.IME_FLAG_NO_FULLSCREEN
        form_home_query.setOnEditorActionListener(TextView.OnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                startResultActivity()
                return@OnEditorActionListener true
            }
            false
        })
        home_filter_search_btn.setOnClickListener { onSearchClick() }
        initFirebaseAnalitics()
    }

    override fun snackBarAction() {
        loadFilter()
    }

    private fun initAds() {
        val adRequest = AdRequest.Builder()
                .addKeyword("aliexpress")
                .addKeyword("ebay")
                .addKeyword("amazon")
                .addKeyword("taobao")
                .build()
        home_adView.adListener = object : AdListener() {
            override fun onAdLoaded() {
                home_adView.visibility = View.VISIBLE
            }
        }
        home_adView.loadAd(adRequest)
    }

    override fun initFirebaseAnalitics() {
        val firebaseAnalytics = FirebaseAnalytics.getInstance(this)
        val params = Bundle()
        params.putString("name", HomeActivity::class.java.simpleName)
        firebaseAnalytics.logEvent("activity", params)
    }

    override fun onFiltersLoaded() {
        initAds()
    }

    override fun onSearchClick() {
        startResultActivity()
    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_options, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.menu_options_aboutus -> startActivity(Intent(baseContext, AboutActivity::class.java))
            else -> return false
        }
        return true
    }

    private fun startResultActivity(queryText: String = form_home_query.text.toString()) {
        val intent = Intent(this, SearchResultActivity::class.java)
        intent.putExtra(HomeActivity.Companion.SEARCH_REQUEST, makeSearchRequest(queryText))
        startActivity(intent)
    }

}