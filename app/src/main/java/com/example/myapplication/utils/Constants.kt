package com.example.myapplication.utils

object URLConstants {
    const val WIDE_IMAGE = "https://img.freepik.com/free-vector/night-ocean-landscape-full-moon-stars-shine_107791-7397.jpg?w=2000"
    const val SQUARE_IMAGE = "https://thenextscoop.com/wp-content/uploads/2018/03/5-Mind-Blowing-Facts-from-the-Google-Logo-Design-History.png"

    fun getList(size : Int = 10)= (1..size).map { WIDE_IMAGE }
}

