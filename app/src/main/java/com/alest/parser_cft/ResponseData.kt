package com.alest.parser_cft

import com.github.kittinunf.fuel.core.ResponseDeserializable
import com.google.gson.Gson

data class ResponseData(
//    val name: String,
//    val email: String
val Date: String,
val PreviousDate: String,
val PreviousURL: String,
val Timestamp: String,
val Valute:MutableMap<String,Valute>
){
    class Deserializer: ResponseDeserializable<ResponseData> {
//        override fun deserialize(content: String): Array<Person>? = Gson().fromJson(content, Array<Person>::class.java)
        override fun deserialize(content: String): ResponseData? = Gson().fromJson(content, ResponseData::class.java)
    }

    override fun toString(): String {
        return "Date: $Date PreviousDate: $PreviousDate valute: ${Valute.values.toList()}"
//        return "name: $name email: $email"
     }
}