package com.intprices

import android.content.Context
import android.view.ContextMenu
import android.view.View
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdRequest
import com.intprices.api.ResultResponce
import kotlinx.android.synthetic.main.activity_home.*
import org.junit.*


class IntpricesTest {
    lateinit var resp: ResultResponce


    @Before fun initRetrofit() {
        resp = ResultResponce.instance
    }

    @Test
    fun retrofitAPITest() {
        var res = resp.getSettingsResult()
        Assert.assertNotNull("Settings fine", res)
    }

}