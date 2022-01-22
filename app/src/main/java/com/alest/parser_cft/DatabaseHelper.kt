package com.alest.parser_cft

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log

/*val ID: String,
val NumCode: String = "999",
val CharCode: String,
val Nominal: Int,
val Name: String ="AD",
val Value: Float,
val Previous: Float*/
//создаем класс DatabaseHelper принимающий в себя Context и наследующийся от класса SQLiteOpenHelper
class DatabaseHelper(context: Context): SQLiteOpenHelper(context, DATABASE_NAME, null, SCHEMA) {
    override fun onCreate(db: SQLiteDatabase?) {
        //создаем таблицу в нашей базе данных
        db?.execSQL("CREATE TABLE $TABLE ($COLUMN_ID TEXT, $COLUMN_NUMCODE TEXT, $COLUMN_CHARCODE TEXT, $COLUMN_NOMINAL INTEGER, $COLUMN_NAME TEXT, $COLUMN_VALUE REAL, $COLUMN_PREVIOUS REAl)")
        //добавляем в базу данных одну строку
//        db?.execSQL("INSERT INTO $TABLE ($COLUMN_ID, $COLUMN_NUMCODE, $COLUMN_CHARCODE, $COLUMN_NOMINAL, $COLUMN_NAME, $COLUMN_VALUE, $COLUMN_PREVIOUS) VALUES ('R01010+', '036+', 'AUD+', 1, 'Австралийский доллар+', 55.5555, 55.0000)")
    }

    //функция обновления базы данных, например при изменении версии базы данных
    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        //удаляем таблицу если она существует
        db?.execSQL("DROP TABLE IF EXISTS $TABLE")
        //создаем таблицу вновь
        onCreate(db)
    }

    //get the all notes
    fun getAllFromDB(): ArrayList<Valute>? {
        val arrayList: ArrayList<Valute> = ArrayList()

        // select all query
        val select_query = "SELECT *FROM $TABLE"
        val db: SQLiteDatabase = this.getWritableDatabase()
//        val db: SQLiteDatabase = this.readableDatabase
//        val db = this.readableDatabase
        val cursor = db.rawQuery(select_query, null)
        Log.d("Tag", "getAllFromDB() cursor.columnCount: ${cursor.columnCount}")
        Log.d("Tag", "getAllFromDB() cursor.moveToFirst(): ${cursor.moveToFirst()}")

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                val valuteModel = Valute(
                        cursor.getString(0),
                        cursor.getString(1),
                        cursor.getString(2),
                        cursor.getInt(3),
                        cursor.getString(4),
                        cursor.getFloat(5),
                        cursor.getFloat(6),

                        )
                Log.d("Tag", "getAllFromDB() cursor: ${cursor}")

                /*val ID: String,
                val NumCode: String = "999",
                val CharCode: String,
                val Nominal: Int,
                val Name: String ="AD",
                val Value: Float,
                val Previous: Float*/

                arrayList.add(valuteModel)
            } while (cursor.moveToNext())
            cursor.close()
        }
        return arrayList
    }

    //add the new note
    /*fun addValuta(id: String,
                 numCode: String,
                 charCode: String,
                 nominal: Int,
                 name: String,
                 value: Float,
                 previous: Float
    ) */
    fun addValuta(valute: Valute)
    {
        val db = this.writableDatabase

        val values = ContentValues()

//        val values = Valute(id, numCode, charCode, nominal, name, value, previous)
        values.put("COLUMN_ID", valute.ID)
        values.put("NUMCODE", valute.NumCode)
        values.put("CHARCODE", valute.CharCode)
        values.put("NOMINAL", valute.Nominal)
        values.put("NAME", valute.Name)
        values.put("VALUE", valute.Value)
        values.put("PREVIOUS", valute.Previous)
//        values.put("Description", des)
        Log.d("Tag", "insert valute.ID: ${valute.ID}")
        Log.d("Tag", "insert valute.NUMCODE: ${valute.NumCode}")
        Log.d("Tag", "insert valute.CharCode: ${valute.CharCode}")
        Log.d("Tag", "insert valute.NOMINAL: ${valute.Nominal}")
        Log.d("Tag", "insert valute.NAME: ${valute.Name}")
        Log.d("Tag", "insert valute.VALUE: ${valute.Value}")
        Log.d("Tag", "insert valute.PREVIOUS: ${valute.Previous}")

        //inserting new row
        var ci = db.insert(TABLE, null, values)
        Log.d("Tag", "insert ci: ${ci}")
        Log.d("Tag", "insert values: ${values}")
        Log.d("Tag", "after insert getAllFromDB(): ${getAllFromDB()}")

        //close database connection
        db.close()
    }

    fun deleteDB() {
        val db = this.writableDatabase
        //deleting rows
        db.delete(TABLE, null, null)
        db.close()
    }
   /* val ID: String,
    val NumCode: String = "999",
    val CharCode: String,
    val Nominal: Int,
    val Name: String ="AD",
    val Value: Float,
    val Previous: Float*/

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