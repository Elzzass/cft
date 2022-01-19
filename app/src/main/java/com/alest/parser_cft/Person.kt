package com.alest.parser_cft

import com.github.kittinunf.fuel.core.ResponseDeserializable
import com.google.gson.Gson

data class Person(
    val name: String,
    val email: String
){
    class Deserializer: ResponseDeserializable<Array<Person>> {
        override fun deserialize(content: String): Array<Person>? = Gson().fromJson(content, Array<Person>::class.java)
    }
}