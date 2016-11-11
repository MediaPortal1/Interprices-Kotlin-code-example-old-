package com.intprices

import android.os.AsyncTask
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.*
import com.intprices.api.ResultResponce
import com.intprices.api.model.*
import java.util.*


abstract class AbstractFiltersActivity : AbstractToolbarActivity(), AdapterView.OnItemSelectedListener {

    var responce = ResultResponce.instance
    protected lateinit var filterHolder: FiltersHolder
    protected lateinit var noInternetSnackBar: Snackbar

    protected var request: String = ""
    protected var category: String? = null
    protected var filterFormQuery: String? = null
    protected var priceFrom: String? = null
    protected var priceTo: String? = null
    protected var country: String? = null
    protected var dealType: String? = null
    protected var condition: String? = null
    protected var sortBy: String? = null
    protected var isFreeShipping: Boolean = false
    protected lateinit var requestMap: HashMap<String, String>

    abstract override fun setSettings()

    override fun initViews(state: Bundle?) {
        filterHolder = initFilterHolder()
        initFilter()
        initSnackBar()
    }

    abstract fun initFilterHolder(): FiltersHolder

    protected fun initSnackBar() {
        noInternetSnackBar = Snackbar
                .make(filterHolder.root, getString(R.string.no_internet), Snackbar.LENGTH_INDEFINITE)
                .setAction(getString(R.string.try_again), { snackbarAction() })
    }

    abstract fun snackbarAction()

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        val key = (parent?.adapter as SettingsSpinnerAdapter).getItem(position)["key"]
        if (!key.isNullOrBlank() || position == 0) {
            var adapter: SettingsSpinnerAdapter? = null
            when (parent) {
                filterHolder.category -> {
                    category = key
                    adapter = filterHolder.category.adapter as SettingsSpinnerAdapter
                }
                filterHolder.condition -> {
                    condition = key
                    adapter = filterHolder.condition.adapter as SettingsSpinnerAdapter
                }
                filterHolder.country -> {
                    country = key
                    adapter = filterHolder.country.adapter as SettingsSpinnerAdapter
                }
                filterHolder.type -> {
                    dealType = key
                    adapter = filterHolder.condition.adapter as SettingsSpinnerAdapter
                }
                filterHolder.sort -> {
                    sortBy = key
                    adapter = filterHolder.sort.adapter as SettingsSpinnerAdapter
                }
            }
            if (adapter != null) adapter.selectedItem = position
        }
    }

    abstract protected fun onSearchClick()

    override fun onNothingSelected(parent: AdapterView<*>?) {
        when (parent) {
            filterHolder.category -> category = null
            filterHolder.condition -> condition = null
            filterHolder.country -> country = null
            filterHolder.type -> dealType = null
            filterHolder.sort -> sortBy = null
        }
    }

    protected fun loadFilter() {
        if (isConnected()) {
            filterHolder.repeatBtn.visibility = View.INVISIBLE
            filterHolder.progressBar.visibility = View.VISIBLE
            LoadSettings().execute()
        } else {
            initSnackBar()
            noInternetSnackBar.show()
        }
    }

    private fun initFilter() {
        filterHolder.repeatBtn.setOnClickListener { v ->
            val repeatAnimation = AnimationUtils.loadAnimation(baseContext, R.anim.repeat_btn)
            repeatAnimation.setAnimationListener(object : Animation.AnimationListener {
                override fun onAnimationRepeat(p0: Animation?) {
                }

                override fun onAnimationStart(p0: Animation?) {
                }

                override fun onAnimationEnd(p0: Animation?) {
                    loadFilter()
                }
            })
            v.startAnimation(repeatAnimation)
        }

        if (!isConnected()) {
            filterHolder.repeatBtn.visibility = View.VISIBLE

            filterHolder.progressBar.visibility = View.INVISIBLE
        } else loadFilter()
    }

    protected abstract fun onFiltersLoaded()

    protected abstract fun initFirebaseAnalitics()

    protected fun makeSearchRequest(query: String = "", page: Int = 1): Bundle {
        val bundle = Bundle()

        if (!query.isNullOrBlank()) bundle.addNotNull("query", query)
        else if (!filterFormQuery.isNullOrEmpty())
            bundle.addNotNull("query", filterFormQuery)
        else bundle.addNotNull("query", request)

        bundle.addNotNull("priceFrom", priceFrom)
        bundle.addNotNull("priceTo", priceTo)
        bundle.addNotNull("country", country)
        bundle.addNotNull("type", dealType)
        bundle.addNotNull("condition", condition)
        bundle.addNotNull("category", category)
        bundle.addNotNull("sort", sortBy)
        bundle.addNotNull("page", page.toString())
        if (isFreeShipping) bundle.addNotNull("freeShipping", "1")

        return bundle
    }

    protected fun makeSearchMap(query: String = "", page: Int = 1) {
        requestMap = makeSearchRequest(query, page).loadFromBundle() as HashMap<String, String>
    }

    protected fun Bundle.addNotNull(key: String, value: String?) {
        if (!value.isNullOrBlank() && !value.equals("null"))
            this.putString(key, value)
    }

    protected fun Bundle.loadifNotNull(map: HashMap<String, String>, key: String): String {
        val value: String? = this.getString(key)
        if (!value.isNullOrBlank()) map[key] = value as String
        return value ?: ""
    }

    protected fun Bundle.loadFromBundle(): Map<String, String> {
        val map = HashMap<String, String>()
        filterFormQuery = this.loadifNotNull(map, "query")
        priceFrom = this.loadifNotNull(map, "priceFrom")
        priceTo = this.loadifNotNull(map, "priceTo")
        country = this.loadifNotNull(map, "country")
        dealType = this.loadifNotNull(map, "type")
        condition = this.loadifNotNull(map, "condition")
        sortBy = this.loadifNotNull(map, "sort")
        this.loadifNotNull(map, "page")
        category = this.loadifNotNull(map, "category")
        if (this.loadifNotNull(map, "freeShipping") == "true") isFreeShipping = true
        return map
    }

    protected fun Bundle.hasExtra(key: String) = if (get(key) != null && get(key).toString().isNotBlank()) true else false

    protected fun setSpinnersToFilter(bundle: Bundle) {
        if (bundle.hasExtra("query")) filterHolder.formSearch.setText(bundle.getString("query"))
        if (bundle.hasExtra("priceFrom")) filterHolder.priceFrom.setText(bundle.getString("priceFrom"))
        if (bundle.hasExtra("priceTo")) filterHolder.priceTo.setText(bundle.getString("priceTo"))
        var adapter: SettingsSpinnerAdapter?
        var position: Int
        if (bundle.hasExtra("country")) {
            adapter = filterHolder.country.adapter as SettingsSpinnerAdapter?
            position = adapter?.getPositionByKey(bundle.getString("country"))!!
            filterHolder.country.setSelection(position)
        }
        if (bundle.hasExtra("type")) {
            adapter = filterHolder.type.adapter as SettingsSpinnerAdapter?
            position = adapter?.getPositionByKey(bundle.getString("type"))!!
            filterHolder.type.setSelection(position)
        }
        if (bundle.hasExtra("condition")) {
            adapter = filterHolder.condition.adapter as SettingsSpinnerAdapter?
            position = adapter?.getPositionByKey(bundle.getString("condition"))!!
            filterHolder.condition.setSelection(position)
        }
        if (bundle.hasExtra("sort")) {
            adapter = filterHolder.sort.adapter as SettingsSpinnerAdapter?
            position = adapter?.getPositionByKey(bundle.getString("sort"))!!
            filterHolder.sort.setSelection(position)
        }
        if (bundle.hasExtra("category")) {
            adapter = filterHolder.category.adapter as SettingsSpinnerAdapter?
            position = adapter?.getPositionByKey(bundle.getString("category"))!!
            filterHolder.category.setSelection(position)
        }
        if (bundle.hasExtra("freeShipping")) filterHolder.freeShipping.isChecked = true
    }

    private fun initOnFilterSelected() {
        filterHolder.category.onItemSelectedListener = this
        filterHolder.country.onItemSelectedListener = this
        filterHolder.type.onItemSelectedListener = this
        filterHolder.sort.onItemSelectedListener = this
        filterHolder.condition.onItemSelectedListener = this
        filterHolder.priceFrom.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (!s?.toString().isNullOrBlank()) priceFrom = s.toString()
            }
        })

        filterHolder.priceTo.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (!s?.toString().isNullOrBlank()) priceTo = s.toString()
            }
        })
        filterHolder.formSearch.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (!s?.toString().isNullOrBlank()) filterFormQuery = s.toString()
            }
        })
        filterHolder.freeShipping.setOnCheckedChangeListener { compoundButton, b -> isFreeShipping = b }
    }

    /******SETTINGS ADAPTER*****/
    inner class SettingsSpinnerAdapter(val inflator: LayoutInflater, val items: List<SettingsItem>) : BaseAdapter() {
        val emptyMap = HashMap<String, String>()
        var selectedItem = 0

        init {
            emptyMap["key"] = ""
            emptyMap["name"] = ""
        }

        override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View? {
            var view = convertView
            if (view == null) {
                view = inflator.inflate(R.layout.spinner_item, parent, false)
            }
            val textview = view?.findViewById(R.id.spinnertext) as TextView
            val label = view?.findViewById(R.id.spinner_label) as TextView
            when (items[0]) {
                is Category -> label.text = getString(R.string.label_category)
                is Country -> label.text = getString(R.string.label_country)
                is Type -> label.text = getString(R.string.label_type)
                is Condition -> label.text = getString(R.string.label_condition)
                is Sort -> label.text = getString(R.string.label_sort_by)
            }
            if (position == 0) {
                when (items[position]) {
                    is Category -> textview.text = view?.context?.getString(R.string.form_category_placeholder)
                    is Condition -> textview.text = view?.context?.getString(R.string.form_condition_placeholder)
                    is Country -> textview.text = view?.context?.getString(R.string.form_country_placeholder)
                    is Type -> textview.text = view?.context?.getString(R.string.form_type_placeholder)
                    is Sort -> textview.text = items[0].getMap()["name"]
                }
            } else
                textview.text = items[if (items[0] !is Sort) position - 1 else position].getMap()["name"]
            return view
        }

        override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup?): View {
            var view = convertView
            if (view == null) {
                view = inflator.inflate(R.layout.spinner_dropdown_item, parent, false)
            }
            val textview = view?.findViewById(R.id.spinnertext) as TextView
            if (position == 0) {
                when (items[position]) {
                    is Category -> textview.text = view?.context?.getString(R.string.form_category_placeholder)
                    is Condition -> textview.text = view?.context?.getString(R.string.form_condition_placeholder)
                    is Country -> textview.text = view?.context?.getString(R.string.form_country_placeholder)
                    is Type -> textview.text = view?.context?.getString(R.string.form_type_placeholder)
                    is Sort -> textview.text = items[0].getMap()["name"]
                }
            } else
                textview.text = items[if (items[0] !is Sort) position - 1 else position].getMap()["name"]
            val radioBtn = view?.findViewById(R.id.spiiner_radio_btn) as RadioButton
            if (position == selectedItem) radioBtn.isChecked = true else radioBtn.isChecked = false

            return view!!
        }


        override fun getItem(position: Int): Map<String, String> = if (position != 0) items[position - 1].getMap() else emptyMap

        override fun getItemId(position: Int): Long {
            return position.toLong()
        }

        override fun getCount(): Int = if (items[0] !is Sort) items.size + 1 else items.size

        fun getPositionByKey(key: String): Int {
            for ((index, value) in items.withIndex()) {
                if (value.getMap()["key"] == key) return index + 1
            }
            return 0
        }
    }

    /******SETTING LOADER*****/
    private inner class LoadSettings() : AsyncTask<Void, Void, SettingsResponce>() {

        override fun onPreExecute() {
            super.onPreExecute()
        }

        override fun doInBackground(vararg params: Void?): SettingsResponce {
            return responce.getSettingsResult()!!
        }

        override fun onPostExecute(result: SettingsResponce?) {
            super.onPostExecute(result)
            if (result != null) {
                filterHolder.category.adapter = SettingsSpinnerAdapter(layoutInflater, result.categories)
                filterHolder.country.adapter = SettingsSpinnerAdapter(layoutInflater, result.countries)
                filterHolder.type.adapter = SettingsSpinnerAdapter(layoutInflater, result.types)
                filterHolder.sort.adapter = SettingsSpinnerAdapter(layoutInflater, result.sorts)
                filterHolder.condition.adapter = SettingsSpinnerAdapter(layoutInflater, result.conditions)
                filterHolder.root.visibility = View.VISIBLE
                filterHolder.repeatBtn.visibility = View.GONE
                filterHolder.progressBar.visibility = View.GONE
                initOnFilterSelected()
            }

            if (intent != null && intent.hasExtra(HomeActivity.Companion.SEARCH_REQUEST)) {
                setSpinnersToFilter(intent.getBundleExtra(HomeActivity.Companion.SEARCH_REQUEST))
            }
            onFiltersLoaded()
        }
    }

    class FiltersHolder(val root: ViewGroup,
                        val formSearch: EditText,
                        val category: Spinner,
                        val country: Spinner,
                        val type: Spinner,
                        val condition: Spinner,
                        val sort: Spinner,
                        val freeShipping: Switch,
                        val priceFrom: EditText,
                        val priceTo: EditText,
                        val repeatBtn: ImageButton,
                        val progressBar: ProgressBar)
}