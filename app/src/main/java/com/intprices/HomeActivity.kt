package com.intprices
import android.content.Intent
import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import kotlinx.android.synthetic.main.activity_home.*

class HomeActivity: AbstractToolbarActivity() {

    companion object{
        val SEARCH_REQUEST="intprices_search_request"
    }

    override fun setSettings() {
        setSettings(R.string.title_activity_home,R.layout.activity_home,false,true)
    }

    override fun initViews(state: Bundle?) {
        query.imeOptions=EditorInfo.IME_ACTION_SEARCH
        query.setOnEditorActionListener (object : TextView.OnEditorActionListener{
            override fun onEditorAction(v: TextView?, actionId: Int, event: KeyEvent?): Boolean {
                if(actionId==EditorInfo.IME_ACTION_SEARCH){
                    startResultActivity()
                    return true
                }
                return false
            }
        })
    }

    override fun onSearchClick(v: View){
        startResultActivity()
    }

    private fun startResultActivity(){
        val intent=Intent(this,SearchResultActivity::class.java)
        intent.putExtra(HomeActivity.Companion.SEARCH_REQUEST,query.text.toString())
        startActivity(intent)
    }

}