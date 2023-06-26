package com.websarva.wings.android.roomflowsample.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface CocktailmemoDAO {
	@Query("SELECT * FROM cocktailmemos")
	fun findAll(): Flow<MutableList<Cocktailmemo>>

	@Query("SELECT * FROM cocktailmemos WHERE id = :id")
	suspend fun findByPK(id: Long): Cocktailmemo?

	@Insert
	suspend fun insert(cocktailmemo: Cocktailmemo): Long

	@Delete
	suspend fun delete(cocktailmemo: Cocktailmemo): Int
}
