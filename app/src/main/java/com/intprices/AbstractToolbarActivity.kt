package com.intprices

import android.content.Context
import android.content.Intent
import android.databinding.adapters.TextViewBindingAdapter
import android.net.ConnectivityManager
import android.os.AsyncTask
import android.os.Bundle
import android.support.annotation.IdRes
import android.support.v4.view.GravityCompat
import android.support.v7.app.AppCompatActivity
import android.text.Editable
import android.text.TextWatcher
import android.view.*
import android.widget.*
import com.intprices.api.ResultResponce
import com.intprices.api.model.*
import kotlinx.android.synthetic.main.activity_result.*
import kotlinx.android.synthetic.main.activity_toolbar.*
import kotlinx.android.synthetic.main.activity_toolbar.view.*
import java.util.*
import kotlin.jvm.*


abstract class AbstractToolbarActivity : AppCompatActivity(), AdapterView.OnItemSelectedListener {

    var activityTitle = 0
    var responce = ResultResponce.instance
    var drawerstatus=true

    protected var request: String = ""
    protected var category: String? = null
    protected var filterquery: String? = null
    protected var pricefrom: String? = null
    protected var priceto: String? = null
    protected var country: String? = null
    protected var dealtype: String? = null
    protected var condition: String? = null
    protected var sortby: String? = null
    protected var freeshipping: Boolean=false
    protected lateinit var requestMap:HashMap<String,String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_toolbar)
        setSettings()
        initToolbar()
        initFilter()
        initViews(savedInstanceState)
    }

    protected abstract fun setSettings()

    protected abstract fun initViews(state: Bundle?)


    protected fun setSettings(@IdRes title: Int, layout: Int, backToggle: Boolean = true, drawer: Boolean = false) {
        activityTitle = title
        viewstub.layoutResource = layout
        viewstub.inflate()
        if (backToggle) setBackToggle()
        if (!drawer) {
            drawerstatus=false
            drawerlayout.removeView(navigationview)
        }
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

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        val key = (parent?.adapter as SettingsSpinnerAdapter).getItem(position)["key"]
        if(key!=null || position==0) {
            when (parent?.id) {
                R.id.settings_category -> category = key
                R.id.settings_condition -> condition = key
                R.id.settings_country -> country = key
                R.id.settings_type -> dealtype = key
                R.id.settings_sort -> sortby = key
            }
        }
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {
        when (parent?.id) {
            R.id.settings_category -> category = null
            R.id.settings_condition -> condition = null
            R.id.settings_country -> country = null
            R.id.settings_type -> dealtype = null
            R.id.settings_sort -> sortby = null
        }
    }




    protected fun openDrawer(status: Boolean=false) {
        if (status)
            drawerlayout.openDrawer(navigationview)
        else drawerlayout.closeDrawers()
    }

    protected fun isConnected(): Boolean {
        val connMgr = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo = connMgr.getActiveNetworkInfo()
        return networkInfo?.isConnected() ?: false
    }

    protected fun setBackToggle() {
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDefaultDisplayHomeAsUpEnabled(true)
    }


    open protected fun onSearchClick(v: View) {
    }

    protected fun makeSearchRequest(query:String="",page:Int=1): Bundle {
        var bundle=Bundle()

        if(!query.equals("")) bundle.addNotNull("query",query)
        else if(filterquery!=null && !filterquery.equals(""))
        bundle.addNotNull("query",filterquery)
            else bundle.addNotNull("query",request)

        bundle.addNotNull("priceFrom",pricefrom)
        bundle.addNotNull("priceTo",priceto)
        bundle.addNotNull("country",country)
        bundle.addNotNull("type",dealtype)
        bundle.addNotNull("condition",condition)
        bundle.addNotNull("category",category)
        bundle.addNotNull("sort",sortby)
        bundle.addNotNull("page",page.toString())
        if(freeshipping)bundle.addNotNull("freeShipping","1")

        return bundle
    }
    protected fun makeSearchMap(query:String="",page:Int=1){
        requestMap= makeSearchRequest(query,page).loadFromBundle() as HashMap<String, String>
    }
    protected fun Bundle.addNotNull(key:String,value:String?){
        if(value!=null && !value.equals("null") &&!value.equals(""))
            this.putString(key,value)
    }
    protected fun Bundle.loadifNotNull(map: HashMap<String,String>,key:String): String?{
        val value:String?=this.getString(key)
        if(value!=null && !value.equals("")) map.set(key,value)
        return value.toString()
    }

    protected fun Bundle.loadFromBundle(): Map<String, String> {
        val map=HashMap<String,String>()
        filterquery=this.loadifNotNull(map,"query")
        pricefrom=this.loadifNotNull(map,"priceFrom")
        priceto=this.loadifNotNull(map,"priceTo")
        country=this.loadifNotNull(map,"country")
        dealtype=this.loadifNotNull(map,"type")
        condition=this.loadifNotNull(map,"condition")
        sortby=this.loadifNotNull(map,"sort")
        this.loadifNotNull(map,"page")
        category=this.loadifNotNull(map,"category")
        if(this.loadifNotNull(map,"freeShipping").equals("true"))freeshipping=true
        return map
    }
    protected fun Bundle.hasExtra(key: String)= if(get(key)!=null && !get(key).equals("")) true else false

    protected fun setSpinnersToFilter(bundle: Bundle){
        if(bundle.hasExtra("query")) settings_query.setText(bundle.getString("query"))
        if(bundle.hasExtra("priceFrom"))settings_edit_from_price.setText(bundle.getString("priceFrom"))
        if(bundle.hasExtra("priceTo"))settings_edit_to_price.setText(bundle.getString("priceTo"))
        var adapter: SettingsSpinnerAdapter?=null
        var position=0
        if(bundle.hasExtra("country")){
            adapter= settings_country.adapter as SettingsSpinnerAdapter?
            position= adapter?.getPositionByKey(bundle.getString("country"))!!
            settings_country.setSelection(position)
        }
        if(bundle.hasExtra("type")){
            adapter= settings_type.adapter as SettingsSpinnerAdapter?
            position= adapter?.getPositionByKey(bundle.getString("type"))!!
            settings_type.setSelection(position)
        }
        if(bundle.hasExtra("condition")){
            adapter= settings_condition.adapter as SettingsSpinnerAdapter?
            position= adapter?.getPositionByKey(bundle.getString("condition"))!!
            settings_condition.setSelection(position)
        }
        if(bundle.hasExtra("sort")){
            adapter= settings_sort.adapter as SettingsSpinnerAdapter?
            position= adapter?.getPositionByKey(bundle.getString("sort"))!!
            settings_sort.setSelection(position)
        }
        if(bundle.hasExtra("category")){
            adapter= settings_category.adapter as SettingsSpinnerAdapter?
            position= adapter?.getPositionByKey(bundle.getString("category"))!!
            settings_category.setSelection(position)
        }
        if(bundle.hasExtra("freeShipping"))settings_checkbox.isChecked=true
    }
    private fun initFilter() {
        if(drawerstatus) {
            if (!isConnected()) {
                settings_repeat.visibility = View.VISIBLE
                settings_repeat.setOnClickListener { v -> loadFilter() }
                settings_progressbar.visibility = View.INVISIBLE
            } else loadFilter()
        }
    }
    private fun loadFilter(){
        if(isConnected()){
            settings_progressbar.visibility=View.VISIBLE
            LoadSettings().execute()
        }
    }
    private fun initOnFilterSelected(){
        settings_category.onItemSelectedListener = this
        settings_country.onItemSelectedListener = this
        settings_type.onItemSelectedListener = this
        settings_sort.onItemSelectedListener = this
        settings_condition.onItemSelectedListener = this
        settings_edit_from_price.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {}
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if(!s?.toString().equals("")) pricefrom = s?.toString()}})

        settings_edit_to_price.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {}
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (!s?.toString().equals("")) priceto = s?.toString()}})
        settings_query.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {}
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (!s?.toString().equals("")) filterquery = s?.toString()}})
        settings_checkbox.setOnCheckedChangeListener { button, b -> freeshipping=b }
        settings_btn.setOnClickListener { v->  onSearchClick(v)}
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
                settings_root.visibility=View.VISIBLE
                settings_repeat.visibility=View.GONE
                settings_progressbar.visibility=View.GONE
                initOnFilterSelected()
            }
            if(intent!=null && intent.hasExtra(HomeActivity.Companion.SEARCH_REQUEST))setSpinnersToFilter(intent.getBundleExtra(HomeActivity.Companion.SEARCH_REQUEST))
            openDrawer(true)
        }
    }

    class SettingsSpinnerAdapter(val inflator: LayoutInflater, val items: List<SettingsItem>) : BaseAdapter() {
        val emptymap =HashMap<String,String>()
        init{
            emptymap["key"]=""
            emptymap["name"]=""
        }

        override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View? {
            var view = convertView
            if (view == null) {
                view = inflator?.inflate(R.layout.spinner_item, parent, false)
            }
            val textview = view?.findViewById(R.id.spinnertext) as TextView
            if(position==0){
                when(items[position]){
                    is Category -> textview.text=view?.context?.getString(R.string.form_category_placeholder)
                    is Condition -> textview.text=view?.context?.getString(R.string.form_condition_placeholder)
                    is Country -> textview.text=view?.context?.getString(R.string.form_country_placeholder)
                    is Type -> textview.text=view?.context?.getString(R.string.form_type_placeholder)
                    is Sort -> textview.text = items[0].getMap()["name"]
                }
            }else
                 textview.text = items[if(items[0] !is Sort) position-1 else position].getMap()["name"]
            return view
        }

        override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup?): View {
            var view = convertView
            if (view == null) {
                view = inflator?.inflate(R.layout.spinner_item, parent, false)
            }
            val textview = view?.findViewById(R.id.spinnertext) as TextView
            if(position==0){
                when(items[position]){
                    is Category -> textview.text=view?.context?.getString(R.string.form_category_placeholder)
                    is Condition -> textview.text=view?.context?.getString(R.string.form_condition_placeholder)
                    is Country -> textview.text=view?.context?.getString(R.string.form_country_placeholder)
                    is Type -> textview.text=view?.context?.getString(R.string.form_type_placeholder)
                    is Sort -> textview.text = items[0].getMap()["name"]
                }
            }else
                textview.text = items[if(items[0] !is Sort) position-1 else position].getMap()["name"]
            return view!!
        }

        override fun getItem(position: Int): Map<String, String> = if(position!=0)items[position-1].getMap() else emptymap

        override fun getItemId(position: Int): Long {
            return position.toLong()
        }

        override fun getCount(): Int = if (items[0] !is Sort) items.size+1 else items.size

        fun getPositionByKey(key:String): Int{
            for((index,value) in items.withIndex()){
                if(value.getMap()["key"]==key)return index+1
            }
            return 0
        }
    }
}
