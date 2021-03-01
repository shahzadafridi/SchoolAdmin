package com.brikmas.balochtransport.Data.Network

class RetrofitConstant {
    companion object{
        val BASE_URL: String = "https://school-admin.pk/"

        fun getEndPoints(): EndPoints {
            return RetrofitClient.getClient(BASE_URL)!!.create(EndPoints::class.java)
        }
    }
}