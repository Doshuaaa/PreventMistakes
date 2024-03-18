package com.example.preventmistakes

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [PhoneDirEntity::class], version = 1)
abstract class PhoneDataBase : RoomDatabase() {

    abstract fun phoneDao(): PhoneDAO

    companion object {

        private var instance: PhoneDataBase? = null

        @Synchronized
        fun getInstance(context: Context): PhoneDataBase? {
            if(instance == null) {

                synchronized(PhoneDataBase::class) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        PhoneDataBase::class.java,
                        "phone.db"
                    ).build()
                }
            }
            return instance
        }

        fun destroyInstance() {
            instance = null
        }

    }
}