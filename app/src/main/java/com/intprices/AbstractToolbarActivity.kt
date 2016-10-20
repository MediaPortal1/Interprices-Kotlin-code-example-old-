package com.intprices

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.os.Bundle
import android.support.annotation.IdRes
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.view.View
import kotlinx.android.synthetic.main.activity_toolbar.*
import kotlin.jvm.*


abstract class AbstractToolbarActivity : AppCompatActivity() {

    var activityTitle = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_toolbar)
        setSettings()
        initToolbar()
        initViews()
    }

    protected abstract fun setSettings()

    protected abstract fun initViews()


    protected fun setSettings(@IdRes title: Int, layout: Int) {
        activityTitle = title
        viewstub.layoutResource = layout
        viewstub.inflate()
    }
    protected fun setSettings(@IdRes title: Int, layout: Int,backToggle: Boolean=true) {
        activityTitle = title
        viewstub.layoutResource = layout
        viewstub.inflate()
        if(backToggle)setBackToggle()
    }

    private fun initToolbar() {
        setSupportActionBar(toolbar)
        if (activityTitle != 0) setTitle(activityTitle)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_options, menu)
        return true
    }

    override fun onOptionsMenuClosed(menu: Menu?) {
        super.onOptionsMenuClosed(menu)
        menu?.clear()
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.menu_options_aboutus -> {startActivity(Intent(baseContext, AboutActivity::class.java))}
            else -> return false
        }
        return true
    }

    private fun isConnected(): Boolean {
        val connMgr = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo = connMgr.getActiveNetworkInfo()
        return networkInfo?.isConnected() ?: false
    }

    protected fun setBackToggle() {
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }
    open protected fun onSearchClick(v: View){}
}
