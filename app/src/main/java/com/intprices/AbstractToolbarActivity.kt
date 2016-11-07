package com.intprices

import android.content.Context
import android.net.ConnectivityManager
import android.os.Bundle
import android.support.annotation.IdRes
import android.support.annotation.LayoutRes
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import com.intprices.api.ResultResponce
import kotlinx.android.synthetic.main.app_bar_layout.*
import java.util.*


abstract class AbstractToolbarActivity : AppCompatActivity() {

    var activityTitle = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setSettings()
        initViews(savedInstanceState)
    }

    protected abstract fun setSettings()

    protected abstract fun initViews(state: Bundle?)

    open protected fun setSettings(@IdRes title: Int, @LayoutRes layoutContentId: Int,@LayoutRes layoutContainerId: Int=R.layout.activity_toolbar,backToggle: Boolean = false) {
        activityTitle = title
        setContentLayout(layoutContainerId)
        initViewStub(layoutContentId)
        initToolbar(backToggle)
    }

    open protected fun setContentLayout(@LayoutRes activityLayout: Int){
        setContentView(activityLayout)
    }
    protected abstract fun initViewStub(@LayoutRes layoutId: Int)

    private fun initToolbar(backToggle: Boolean) {
        setSupportActionBar(toolbar)
        if (activityTitle != 0) setTitle(activityTitle)
        if (backToggle) setBackToggle()
    }

    override fun onOptionsMenuClosed(menu: Menu?) {
        super.onOptionsMenuClosed(menu)
        menu?.clear()
    }

    protected fun isConnected(): Boolean {
        val connMgr = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo = connMgr.activeNetworkInfo
        return networkInfo.isConnected
    }

    protected fun setBackToggle() {
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDefaultDisplayHomeAsUpEnabled(true)
    }

}

