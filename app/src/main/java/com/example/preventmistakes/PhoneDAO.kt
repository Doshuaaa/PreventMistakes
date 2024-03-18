package com.example.preventmistakes

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface PhoneDAO {

    @Query("SELECT * FROM table_phone")
    fun getAll(): LiveData<List<PhoneDirEntity>>

    @Query("SELECT EXISTS(SELECT phoneNumber FROM table_phone WHERE phoneNumber = :phoneNumber) ")
    // 1 : true, 0 : false
    fun isPhoneBlocked(phoneNumber: String): Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun blockPhone(phone: PhoneDirEntity)

    @Delete
    fun unblockPhone(phone: PhoneDirEntity)
}