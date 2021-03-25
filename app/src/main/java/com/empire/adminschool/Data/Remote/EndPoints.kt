package com.empire.adminschool.Data.Remote

import com.empire.adminschool.Models.*
import com.empire.adminschool.Models.attendence.AttendenceInResponse
import com.empire.adminschool.Models.attendence.AttendenceOutResponse
import com.empire.adminschool.Models.employee.EmployeeResponse
import retrofit2.Call
import retrofit2.http.*

interface EndPoints {

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

    @POST("api/addStudentSMSHistory")
    @FormUrlEncoded
    fun addStudentSmsHistory(
        @Field("school") school: String,
        @Field("student_name") student_name: String,
        @Field("mobile") mobile: String,
        @Field("message") message: String,
        @Field("format") format: String
    ): Call<SMSHistoryResponse>

    @POST("api/addEmployeeSMSHistory")
    @FormUrlEncoded
    fun addEmployeeSmsHistory(
        @Field("school") school: String,
        @Field("employee_name") employee_name: String,
        @Field("mobile") mobile: String,
        @Field("message") message: String,
        @Field("format") format: String
    ): Call<SMSHistoryResponse>

    @GET("api/attendance_in/{qr_code}/{school_id}")
    fun attendanceIn(
        @Path("qr_code") qr_code: String,
        @Path("school_id") school_id: String,
    ): Call<AttendenceInResponse>

    @GET("api/attendance_out/{qr_code}/{school_id}")
    fun attendanceOut(
            @Path("qr_code") qr_code: String,
            @Path("school_id") school_id: String,
    ): Call<AttendenceOutResponse>

}
