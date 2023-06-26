package com.websarva.wings.android.roomlivedatasamplekotlin.data.local

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface CocktailmemoDAO {
	@Query("SELECT * FROM cocktailmemos")
	fun findAll(): LiveData<MutableList<Cocktailmemo>>

	@Query("SELECT * FROM cocktailmemos WHERE id = :id")
	suspend fun findByPK(id: Long): Cocktailmemo?

	@Insert
	suspend fun insert(cocktailmemo: Cocktailmemo): Long

	@Delete
	suspend fun delete(cocktailmemo: Cocktailmemo): Int
}
