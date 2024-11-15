package com.example.shine

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.widget.Toast

class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "shinevroom.db"
        private const val DATABASE_VERSION = 1
        const val TABLE_USERS = "users"
        const val COLUMN_ID = "id"
        const val COLUMN_EMAIL = "email"
        const val COLUMN_USERNAME = "username"
        const val COLUMN_PASSWORD = "password"

        const val TABLE_APPOINTMENTS = "appointments"
        const val COLUMN_APPOINTMENT_ID = "appointment_id"
        const val COLUMN_USER_ID = "user_id"
        const val COLUMN_SERVICE = "service"
        const val COLUMN_DATE = "date"
        const val COLUMN_TIME = "time"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        val createUsersTable = ("CREATE TABLE " + TABLE_USERS + " ("
                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COLUMN_EMAIL + " TEXT, "
                + COLUMN_USERNAME + " TEXT, "
                + COLUMN_PASSWORD + " TEXT)")
        db?.execSQL(createUsersTable)

        val createAppointmentsTable = ("CREATE TABLE " + TABLE_APPOINTMENTS + " ("
                + COLUMN_APPOINTMENT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COLUMN_USER_ID + " INTEGER, "
                + COLUMN_SERVICE + " TEXT, "
                + COLUMN_DATE + " TEXT, "
                + COLUMN_TIME + " TEXT, "
                + "FOREIGN KEY(" + COLUMN_USER_ID + ") REFERENCES " + TABLE_USERS + "(" + COLUMN_ID + "))")
        db?.execSQL(createAppointmentsTable)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("DROP TABLE IF EXISTS $TABLE_USERS")
        db?.execSQL("DROP TABLE IF EXISTS $TABLE_APPOINTMENTS")
        onCreate(db)
    }

    fun addUser(email: String, username: String, password: String): Boolean {
        val db = this.writableDatabase
        val values = ContentValues()
        values.put(COLUMN_EMAIL, email)
        values.put(COLUMN_USERNAME, username)
        values.put(COLUMN_PASSWORD, password)

        val result = db.insert(TABLE_USERS, null, values)
        db.close()
        return (result != -1L)
    }

    fun checkUser(username: String, password: String): Boolean {
        val db = this.readableDatabase
        val query = "SELECT * FROM $TABLE_USERS WHERE $COLUMN_USERNAME = ? AND $COLUMN_PASSWORD = ?"
        val cursor = db.rawQuery(query, arrayOf(username, password))
        val exists = cursor.count > 0
        cursor.close()
        db.close()
        return exists
    }

    @SuppressLint("Range")
    fun getUserId(username: String): Int {
        val db = this.readableDatabase
        val query = "SELECT $COLUMN_ID FROM $TABLE_USERS WHERE $COLUMN_USERNAME = ?"
        val cursor = db.rawQuery(query, arrayOf(username))
        var userId = -1
        if (cursor.moveToFirst()) {
            userId = cursor.getInt(cursor.getColumnIndex(COLUMN_ID))
        }
        cursor.close()
        db.close()
        return userId
    }

    fun addAppointment(userId: Int, service: String, date: String, time: String): Boolean {
        val db = this.writableDatabase
        val values = ContentValues()
        values.put(COLUMN_USER_ID, userId)
        values.put(COLUMN_SERVICE, service)
        values.put(COLUMN_DATE, date)
        values.put(COLUMN_TIME, time)

        val result = db.insert(TABLE_APPOINTMENTS, null, values)
        db.close()
        return (result != -1L)
    }

    @SuppressLint("Range")
    fun getAppointments(): List<String> {
        val appointments = mutableListOf<String>()
        val db = this.readableDatabase
        val query = "SELECT * FROM $TABLE_APPOINTMENTS"
        val cursor = db.rawQuery(query, null)

        if (cursor.moveToFirst()) {
            do {
                val service = cursor.getString(cursor.getColumnIndex(COLUMN_SERVICE))
                val date = cursor.getString(cursor.getColumnIndex(COLUMN_DATE))
                val time = cursor.getString(cursor.getColumnIndex(COLUMN_TIME))
                appointments.add("Servicio: $service\nFecha seleccionada: $date\nHora seleccionada: $time")
            } while (cursor.moveToNext())
        }
        cursor.close()
        db.close()
        return appointments
    }

    @SuppressLint("Range")
    fun getAppointmentsByUser(username: String): List<String> {
        val appointments = mutableListOf<String>()
        val db = this.readableDatabase
        val userIdQuery = "SELECT $COLUMN_ID FROM $TABLE_USERS WHERE $COLUMN_USERNAME = ?"
        val cursorUser = db.rawQuery(userIdQuery, arrayOf(username))
        if (cursorUser.moveToFirst()) {
            val userId = cursorUser.getInt(cursorUser.getColumnIndex(COLUMN_ID))
            val query = "SELECT * FROM $TABLE_APPOINTMENTS WHERE $COLUMN_USER_ID = ?"
            val cursor = db.rawQuery(query, arrayOf(userId.toString()))

            if (cursor.moveToFirst()) {
                do {
                    val service = cursor.getString(cursor.getColumnIndex(COLUMN_SERVICE))
                    val date = cursor.getString(cursor.getColumnIndex(COLUMN_DATE))
                    val time = cursor.getString(cursor.getColumnIndex(COLUMN_TIME))
                    appointments.add("Servicio: $service\nFecha seleccionada: $date\nHora seleccionada: $time")
                } while (cursor.moveToNext())
            }
            cursor.close()
        }
        cursorUser.close()
        db.close()
        return appointments
    }

    @SuppressLint("Range")
    fun deleteAppointmentsByUser(username: String): Boolean {
        val db = this.writableDatabase
        val userIdQuery = "SELECT $COLUMN_ID FROM $TABLE_USERS WHERE $COLUMN_USERNAME = ?"
        val cursorUser = db.rawQuery(userIdQuery, arrayOf(username))
        var isDeleted = false
        if (cursorUser.moveToFirst()) {
            val userId = cursorUser.getInt(cursorUser.getColumnIndex(COLUMN_ID))
            val result = db.delete(TABLE_APPOINTMENTS, "$COLUMN_USER_ID = ?", arrayOf(userId.toString()))
            isDeleted = result > 0
        }
        cursorUser.close()
        db.close()
        return isDeleted
    }

    fun deleteAppointmentById(appointmentId: Int): Boolean {
        val db = this.writableDatabase
        val result = db.delete(TABLE_APPOINTMENTS, "$COLUMN_APPOINTMENT_ID = ?", arrayOf(appointmentId.toString()))
        db.close()
        return result > 0
    }

    fun deleteAllAppointments(): Boolean {
        val db = this.writableDatabase
        val result = db.delete(TABLE_APPOINTMENTS, null, null)
        db.close()
        return result > 0
    }
}
