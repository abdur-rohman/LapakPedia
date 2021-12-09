package dev.rohman.lapakpedia.repos.local.databases

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import dev.rohman.lapakpedia.repos.local.daos.CartDao
import dev.rohman.lapakpedia.repos.local.entities.CartEntity
import dev.rohman.lapakpedia.repos.local.entities.SellerEntity

@Database(entities = [CartEntity::class, SellerEntity::class], version = 1, exportSchema = false)
abstract class LocalDatabase : RoomDatabase() {
    abstract fun cartDao(): CartDao

    companion object {
        private const val DATABASE_NAME = "local_database.db"

        fun instance(context: Context): LocalDatabase {
            return Room.databaseBuilder(context.applicationContext, LocalDatabase::class.java, DATABASE_NAME).build()
        }
    }
}