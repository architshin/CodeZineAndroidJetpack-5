package com.websarva.wings.android.roomflowsample.data

import android.app.Application
import com.websarva.wings.android.roomflowsample.data.local.AppDatabase
import com.websarva.wings.android.roomflowsample.data.local.Cocktailmemo
import kotlinx.coroutines.flow.Flow

class CocktailmemoRepository(application: Application) {
	private val _db: AppDatabase

	init {
		_db = AppDatabase.getDatabase(application)
	}

	fun getCocktailmemoList(): Flow<MutableList<Cocktailmemo>> {
		val cocktailmemoDAO = _db.createCocktailmemoDAO()
		return cocktailmemoDAO.findAll()
	}

	suspend fun getCocktailmemo(cocktailId: Long): Cocktailmemo? {
		val cocktailmemoDAO = _db.createCocktailmemoDAO()
		return cocktailmemoDAO.findByPK(cocktailId)
	}

	suspend fun deleteInsertCocktailmemo(cocktailmemo: Cocktailmemo): Boolean {
		val cocktailmemoDAO = _db.createCocktailmemoDAO()
		var result = false
		val resultDelete = cocktailmemoDAO.delete(cocktailmemo)
		if(resultDelete >= 0) {
			val resultInsert = cocktailmemoDAO.insert(cocktailmemo)
			if(resultInsert >= 0) {
				result = true
			}
		}
		return result
	}
}
