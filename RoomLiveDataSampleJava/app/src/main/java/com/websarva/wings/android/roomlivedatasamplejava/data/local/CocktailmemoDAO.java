package com.websarva.wings.android.roomlivedatasamplejava.data.local;

import com.google.common.util.concurrent.ListenableFuture;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

@Dao
public interface CocktailmemoDAO {
	@Query("SELECT * FROM cocktailmemos")
	LiveData<List<Cocktailmemo>> findAll();

	@Query("SELECT * FROM cocktailmemos WHERE id = :id")
	ListenableFuture<Cocktailmemo> findByPK(long id);

	@Insert
	ListenableFuture<Long> insert(Cocktailmemo cocktailmemo);

	@Delete
	ListenableFuture<Integer> delete(Cocktailmemo cocktailmemo);
}
