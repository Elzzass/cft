package com.alest.parser_cft

import java.util.*

data class Valute(
        val ID: String,
        val NumCode: String = "999",
        val CharCode: String,
        val Nominal: Int,
        val Name: String ="AD",
        val Value: Float,
        val Previous: Float

)