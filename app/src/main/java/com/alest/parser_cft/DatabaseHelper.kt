package com.alest.parser_cft

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log

class DatabaseHelper(context: Context): SQLiteOpenHelper(context, DATABASE_NAME, null, SCHEMA) {
    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL("CREATE TABLE $TABLE ($COLUMN_ID TEXT, $COLUMN_NUMCODE TEXT, $COLUMN_CHARCODE TEXT, $COLUMN_NOMINAL INTEGER, $COLUMN_NAME TEXT, $COLUMN_VALUE REAL, $COLUMN_PREVIOUS REAl)")
    }
    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("DROP TABLE IF EXISTS $TABLE")
        onCreate(db)
    }

    fun getAllFromDB(): ArrayList<Valute> {
        val arrayList: ArrayList<Valute> = ArrayList()
        val selectAllQuery = "SELECT *FROM $TABLE"
        val db: SQLiteDatabase = this.getWritableDatabase()
        val cursor = db.rawQuery(selectAllQuery, null)
        Log.d("Tag", "getAllFromDB() cursor.columnCount: ${cursor.columnCount}")
        Log.d("Tag", "getAllFromDB() cursor.moveToFirst(): ${cursor.moveToFirst()}")
        if (cursor.moveToFirst()) {
            do {
                val valuteModel = Valute(
                        cursor.getString(0),
                        cursor.getString(1),
                        cursor.getString(2),
                        cursor.getInt(3),
                        cursor.getString(4),
                        cursor.getFloat(5),
                        cursor.getFloat(6),)
                arrayList.add(valuteModel)
            } while (cursor.moveToNext())
            cursor.close()
        }
        return arrayList
    }

    fun addValuta(valuta: Valute) {
        val db = this.writableDatabase
        val values = ContentValues()
        values.put("COLUMN_ID", valuta.ID)
        values.put("NUMCODE", valuta.NumCode)
        values.put("CHARCODE", valuta.CharCode)
        values.put("NOMINAL", valuta.Nominal)
        values.put("NAME", valuta.Name)
        values.put("VALUE", valuta.Value)
        values.put("PREVIOUS", valuta.Previous)

        Log.d("Tag", "insert valute.ID: ${valuta.ID}")
        Log.d("Tag", "insert valute.NUMCODE: ${valuta.NumCode}")
        Log.d("Tag", "insert valute.CharCode: ${valuta.CharCode}")
        Log.d("Tag", "insert valute.NOMINAL: ${valuta.Nominal}")
        Log.d("Tag", "insert valute.NAME: ${valuta.Name}")
        Log.d("Tag", "insert valute.VALUE: ${valuta.Value}")
        Log.d("Tag", "insert valute.PREVIOUS: ${valuta.Previous}")

        var ci = db.insert(TABLE, null, values)
        Log.d("Tag", "insert ci: ${ci}")
        Log.d("Tag", "insert values: ${values}")
        Log.d("Tag", "after insert getAllFromDB(): ${getAllFromDB()}")

        db.close()
    }

    fun deleteDB() {
        val db = this.writableDatabase
        db.delete(TABLE, null, null)
        db.close()
    }


    companion object {
        private const val DATABASE_NAME = "valuta.db" //имя базы данных
        private const val SCHEMA = 1 //версия базы данных

        const val TABLE = "valutes" //название таблицы
        const val COLUMN_ID = "COLUMN_ID" //имя столбца с id
        const val COLUMN_NUMCODE = "numcode"
        const val COLUMN_CHARCODE = "charcode"
        const val COLUMN_NOMINAL = "nominal"
        const val COLUMN_NAME = "name" //имя столбца с именем
        const val COLUMN_VALUE = "value"
        const val COLUMN_PREVIOUS = "previous"
    }
}