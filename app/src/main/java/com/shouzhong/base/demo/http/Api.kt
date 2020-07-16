package com.shouzhong.base.demo.http

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface Api {
    @GET("validateAndCacheCardInfo.json")
    suspend fun bankCardInfo(
        @Query("cardNo") cardNo: String,
        @Query("_input_charset") charset: String = "utf-8",
        @Query("cardBinCheck") check: String = "true"
    ) : Call<BankCardBean>
}