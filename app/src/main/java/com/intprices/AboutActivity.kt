package com.intprices

import android.content.pm.PackageManager
import android.text.Html
import android.text.method.LinkMovementMethod
import android.view.Menu
import kotlinx.android.synthetic.main.activity_about.*

class AboutActivity : AbstractToolbarActivity() {

    override fun setSettings() {
        setSettings(R.string.title_activity_about, R.layout.activity_about)
    }

    override fun initViews() {
        var versionName = "0"
        try {
            versionName = packageManager.getPackageInfo(packageName, 0).versionName
        } catch (e: PackageManager.NameNotFoundException) {e.printStackTrace()}

        about_textview.setText(Html.fromHtml(getString(R.string.about_content,versionName)))
        about_textview.setMovementMethod(LinkMovementMethod.getInstance())
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menu?.clear()
        return false
    }
}