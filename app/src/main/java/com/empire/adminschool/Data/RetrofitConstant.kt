package com.brikmas.balochtransport.Data.Network

class RetrofitConstant {
    companion object{
        val BASE_URL: String = "https://staging.balochtransport.com/"
        val IMG_BASE_URL: String = "https://staging.balochtransport.com/storage/"

        fun getEndPoints(): EndPoints {
            return RetrofitClient.getClient(BASE_URL)!!.create(EndPoints::class.java)
        }
    }
}