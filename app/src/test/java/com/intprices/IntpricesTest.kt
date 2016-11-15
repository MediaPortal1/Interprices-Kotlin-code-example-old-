package com.intprices

import android.content.Context
import android.view.ContextMenu
import android.view.View
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdRequest
import com.intprices.api.ResultResponse
import kotlinx.android.synthetic.main.activity_home.*
import org.junit.*
import java.util.*


class IntpricesTest {
    lateinit var resp: ResultResponse


    @Before fun initRetrofit() {
        resp = ResultResponse.instance
    }

    @Test
    fun retrofitAPITest() {
        val map=HashMap<String,String>()
        map.put("query","iphone")
        map.put("page","1")
        var res = resp.getSearchResult(map)?.totalPages
        Assert.assertNotNull("Settings fine", res)
    }

}