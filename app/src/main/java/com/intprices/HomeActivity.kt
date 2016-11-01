package com.intprices
import android.content.Intent
import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.activity_toolbar.*

class HomeActivity: AbstractToolbarActivity() {

    companion object{
        val SEARCH_REQUEST="intprices_search_request"
    }

    override fun setSettings() {
        setSettings(R.string.title_activity_home,R.layout.activity_home,false,true)
    }

    override fun initViews(state: Bundle?) {
        form_home_query.imeOptions=EditorInfo.IME_ACTION_SEARCH
        form_home_query.setOnEditorActionListener (TextView.OnEditorActionListener { v, actionId, event ->
            if(actionId==EditorInfo.IME_ACTION_SEARCH){
                startResultActivity()
                return@OnEditorActionListener true
            }
            false
        })
    }

    override fun onSearchClick(v: View){
        when(v.id){
        R.id.settings_btn -> startResultActivity(settings_query.text.toString())
        R.id.home_search_btn -> startResultActivity()
        }
    }

    private fun startResultActivity(querytext:String= form_home_query.text.toString()){
        val intent=Intent(this,SearchResultActivity::class.java)
        intent.putExtra(HomeActivity.Companion.SEARCH_REQUEST,makeSearchRequest(querytext))
        startActivity(intent)
    }

}