package com.empire.adminschool.Data.Remote

class RetrofitConstant {
    companion object{
        var BASE_URL: String = "https://school-admin.pk/"

        fun getEndPoints(): EndPoints {
            return RetrofitClient.getClient(BASE_URL)!!.create(EndPoints::class.java)
        }
    }
}