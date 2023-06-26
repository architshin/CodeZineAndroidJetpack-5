package com.websarva.wings.android.roomlivedatasamplejava.data.local;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {Cocktailmemo.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
	private static AppDatabase _instance;

	public static AppDatabase getDatabase(Context context) {
		if (_instance == null) {
			_instance = Room.databaseBuilder(context.getApplicationContext(), AppDatabase.class, "cocktailmemo_db").createFromAsset("database/cocktailmemo_db.db").build();
		}
		return _instance;
	}

	public abstract CocktailmemoDAO createCocktailmemoDAO();
}
