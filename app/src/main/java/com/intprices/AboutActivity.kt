package com.intprices

import android.content.pm.PackageManager
import android.os.Bundle
import android.text.Html
import android.text.method.LinkMovementMethod
import android.view.Menu
import kotlinx.android.synthetic.main.activity_about.*
import kotlinx.android.synthetic.main.activity_toolbar.*

class AboutActivity : AbstractToolbarActivity() {

    override fun setSettings() {
        setSettings(R.string.title_activity_about, R.layout.activity_about)
    }

    override fun initViewStub(layoutId: Int) {
        toolbar_viewstub.layoutResource=layoutId
        toolbar_viewstub.inflate()

    }

    override fun initViews(state: Bundle?) {
        var versionName = "0"
        try {
            versionName = packageManager.getPackageInfo(packageName, 0).versionName
        } catch (e: PackageManager.NameNotFoundException) {e.printStackTrace()}

        about_textview.text = Html.fromHtml(getString(R.string.about_content,versionName))
        about_textview.movementMethod = LinkMovementMethod.getInstance()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menu?.clear()
        return false
    }
}