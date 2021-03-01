package com.brikmas.balochtransport.Data.Network

import com.empire.adminschool.Models.*
import com.empire.adminschool.Models.employee.EmployeeResponse
import retrofit2.Call
import retrofit2.http.*

interface EndPoints {

    /*
    @Headers(
        "api_token: ZW5jcnlwdHNvdWxAaG9tZXJlbWVkaWVz"
    )
    @POST("api/categories")
    fun getCategoires(): Call<Categories>
 http://masoodrehman.com/baloch/public/api/busService/seatHold
     */

    @POST("api/login")
    @FormUrlEncoded
    fun userLogin(
        @Field("username") username: String,
        @Field("password") password: String
    ): Call<LoginResponse>

    @GET("api/school/{school_id}")
    fun getSchool(
            @Path("school_id") school_id: String
    ): Call<School>

    @GET("api/classes/{school_id}")
    fun getClasses(
            @Path("school_id") school_id: String
    ): Call<ClassesResponse>

    @GET("api/class_students/{class}/{session}")
    fun getStudents(
            @Path("class") cls: String,
            @Path("session") session: String,
    ): Call<StudentResponse>

    @GET("api/current_employees/{school_id}")
    fun getCurrentEmpolyees(
        @Path("school_id") school_id: String,
    ): Call<EmployeeResponse>

    @GET("api/ex_employees/{school_id}")
    fun getExEmpolyees(
        @Path("school_id") school_id: String,
    ): Call<EmployeeResponse>


//    @GET("api/user/profile")
//    fun getUserProfile(@Query("id") id: String): Call<ProfileResponse>


}
