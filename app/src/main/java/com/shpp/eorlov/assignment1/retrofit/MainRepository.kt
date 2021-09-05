package com.shpp.eorlov.assignment1.retrofit

class MainRepository(private val retrofitService: RetrofitService) {

    suspend fun registerUser(registrationBody: RegistrationBody) = retrofitService.registerUser(registrationBody)
}