package com.intprices

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.os.AsyncTask
import android.os.Bundle
import android.support.annotation.IdRes
import android.support.v4.view.GravityCompat
import android.support.v7.app.AppCompatActivity
import android.view.*
import android.widget.*
import com.intprices.api.ResultResponce
import com.intprices.api.model.Category
import com.intprices.api.model.Country
import com.intprices.api.model.SettingsItem
import com.intprices.api.model.SettingsResponce
import kotlinx.android.synthetic.main.activity_toolbar.*
import kotlinx.android.synthetic.main.activity_toolbar.view.*
import kotlin.jvm.*


abstract class AbstractToolbarActivity : AppCompatActivity(), AdapterView.OnItemClickListener, TextView.OnEditorActionListener {

    var activityTitle = 0
    protected var responce = ResultResponce.instance
    var category: String? = null
    var filterquery: String? = null
    var pricefrom: String? = null
    var priceto: String? = null
    var country: String? = null
    var dealtype: String? = null
    var condition: String? = null
    var sortby: String? = null
    var freeshipping: Boolean=false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_toolbar)
        setSettings()
        initToolbar()
        initViews(savedInstanceState)
        initFilter()
    }

    protected abstract fun setSettings()

    protected abstract fun initViews(state: Bundle?)


    protected fun setSettings(@IdRes title: Int, layout: Int, backToggle: Boolean = true, drawer: Boolean = false) {
        activityTitle = title
        viewstub.layoutResource = layout
        viewstub.inflate()
        if (backToggle) setBackToggle()
        if (!drawer) drawerlayout.visibility = View.GONE
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
            R.id.menu_options_aboutus -> {
                startActivity(Intent(baseContext, AboutActivity::class.java))
            }
            R.id.menu_filter -> {
                openDrawer()
            }
            else -> return false
        }
        return true
    }

    override fun onItemClick(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        val key = (parent?.adapter as SettingsSpinnerAdapter).getItem(position)["key"]
        when (view?.id) {
            R.id.settings_category -> category = key
            R.id.settings_condition -> condition = key
            R.id.settings_country -> country = key
            R.id.settings_type -> dealtype = key
            R.id.settings_sort -> sortby = key
        }
    }

    override fun onEditorAction(v: TextView?, actionId: Int, event: KeyEvent?): Boolean {
        if (v?.text != null && v?.text!!.equals("")) {
            when (v?.id) {
                R.id.settings_query -> filterquery = v?.text.toString()
                R.id.settings_edit_from_price -> pricefrom = v?.text.toString()
                R.id.settings_edit_to_price -> priceto = v?.text.toString()
            }
            return true
        } else return false
    }

    private fun openDrawer() {
        if (drawerlayout?.isDrawerOpen(Gravity.END)!!)
            drawerlayout.openDrawer(Gravity.RIGHT)
        else drawerlayout.closeDrawer(GravityCompat.END)
    }

    protected fun isConnected(): Boolean {
        val connMgr = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo = connMgr.getActiveNetworkInfo()
        return networkInfo?.isConnected() ?: false
    }

    protected fun setBackToggle() {
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }


    open protected fun onSearchClick(v: View) {
    }

    private fun initFilter() {
        LoadSettings().execute()
        settings_category.onItemClickListener = this
        settings_country.onItemClickListener = this
        settings_type.onItemClickListener = this
        settings_sort.onItemClickListener = this
        settings_condition.onItemClickListener = this
        settings_edit_from_price.setOnEditorActionListener(this)
        settings_edit_to_price.setOnEditorActionListener(this)
        settings_query.setOnEditorActionListener(this)
        settings_checkbox.setOnCheckedChangeListener { button, b -> freeshipping=b }

    }

    protected inner class LoadSettings() : AsyncTask<Void, Void, SettingsResponce>() {

        override fun onPreExecute() {
            super.onPreExecute()

        }

        override fun doInBackground(vararg params: Void?): SettingsResponce {
            return responce.getSettingsResult()!!
        }

        override fun onPostExecute(result: SettingsResponce?) {
            super.onPostExecute(result)
            if (result != null) {
                settings_category.adapter = SettingsSpinnerAdapter(layoutInflater, result?.categories!!)
                settings_country.adapter = SettingsSpinnerAdapter(layoutInflater, result?.countries!!)
                settings_type.adapter = SettingsSpinnerAdapter(layoutInflater, result?.types!!)
                settings_sort.adapter = SettingsSpinnerAdapter(layoutInflater, result?.sorts!!)
                settings_condition.adapter = SettingsSpinnerAdapter(layoutInflater, result?.conditions!!)
            }
        }
    }

    class SettingsSpinnerAdapter(val inflator: LayoutInflater, val items: List<SettingsItem>) : BaseAdapter() {

        override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View? {
            var view = convertView
            if (view == null) {
                view = inflator?.inflate(R.layout.spinner_item, parent, false)
            }
            val textview = view?.findViewById(R.id.spinnertext) as TextView
            textview.text = items[position].getMap()["name"]
            return view
        }

        override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup?): View {
            var view = convertView
            if (view == null) {
                view = inflator?.inflate(R.layout.spinner_item, parent, false)
            }
            val textview = view?.findViewById(R.id.spinnertext) as TextView
            textview.text = items[position].getMap()["name"]
            return view!!
        }

        override fun getItem(position: Int): Map<String, String> = items[position].getMap()

        override fun getItemId(position: Int): Long {
            return position.toLong()
        }

        override fun getCount(): Int = items.size

    }
}
