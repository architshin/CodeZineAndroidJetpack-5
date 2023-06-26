package com.websarva.wings.android.roomlivedatasamplekotlin.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.websarva.wings.android.roomlivedatasamplekotlin.data.local.Cocktailmemo

@Database(entities = [Cocktailmemo::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
	companion object {
		private var _instance: AppDatabase? = null

		fun getDatabase(context: Context): AppDatabase {
			if (_instance == null) {
				_instance = Room.databaseBuilder(context.applicationContext, AppDatabase::class.java, "cocktailmemo_db").createFromAsset("database/cocktailmemo_db.db").build()
			}
			return _instance!!
		}
	}

	abstract fun createCocktailmemoDAO(): CocktailmemoDAO
}
