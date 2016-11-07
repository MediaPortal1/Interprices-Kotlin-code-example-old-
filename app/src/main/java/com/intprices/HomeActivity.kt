package com.intprices
import android.content.Intent
import android.os.Bundle
import android.view.KeyEvent
import android.view.MenuItem
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.Filter
import android.widget.TextView
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.activity_home.view.*
import kotlinx.android.synthetic.main.activity_toolbar.*

class HomeActivity: AbstractFiltersActivity() {


    companion object{
        val SEARCH_REQUEST="intprices_search_request"
    }

    override fun setSettings() {
        setSettings(R.string.title_activity_home,R.layout.activity_home,R.layout.activity_toolbar,false)
    }

    override fun initFilterHolder() =  FiltersHolder(home_filter_root,form_home_query,home_filter_category,
                home_filter_country,home_filter_type,
                home_filter_condition, home_filter_sort,
                home_filter_checkbox, home_filter_edit_from_price,
                home_filter_price_to,home_filter_search_btn,
                home_filter_repeat, home_filter_progressbar)


    override fun initViewStub(layoutId: Int) {
        toolbar_viewstub.layoutResource=layoutId
        toolbar_viewstub.inflate()
    }

    override fun initViews(state: Bundle?) {
        super.initViews(state)
        form_home_query.imeOptions=EditorInfo.IME_ACTION_SEARCH
        form_home_query.setOnEditorActionListener (TextView.OnEditorActionListener { v, actionId, event ->
            if(actionId==EditorInfo.IME_ACTION_SEARCH){
                startResultActivity()
                return@OnEditorActionListener true
            }
            false
        })
    }

    override fun onFiltersLoaded() {

    }

    override fun onSearchClick(){
        startResultActivity()
    }
    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.menu_options_aboutus -> startActivity(Intent(baseContext, AboutActivity::class.java))
            else -> return false
        }
        return true
    }

    private fun startResultActivity(querytext:String= form_home_query.text.toString()){
        val intent=Intent(this,SearchResultActivity::class.java)
        intent.putExtra(HomeActivity.Companion.SEARCH_REQUEST,makeSearchRequest(querytext))
        startActivity(intent)
    }

}