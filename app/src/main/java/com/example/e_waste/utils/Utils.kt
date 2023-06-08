package com.example.e_waste.utils

fun verifyDataFromUser(username: String, email: String, contact: String, password: String, password2: String): Boolean{
    return (username.isNotEmpty() && email.isNotEmpty() && contact.isNotEmpty() && password.isNotEmpty() && password2.isNotEmpty()) && (password == password2)
}

fun verifyLoginDataFromUser(email: String, password: String): Boolean{
    return (email.isNotEmpty() && password.isNotEmpty())
}