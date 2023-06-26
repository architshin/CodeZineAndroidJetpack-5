package com.websarva.wings.android.roomlivedatasamplejava.data;

import android.app.Application;

import com.google.common.util.concurrent.ListenableFuture;
import com.websarva.wings.android.roomlivedatasamplejava.data.local.AppDatabase;
import com.websarva.wings.android.roomlivedatasamplejava.data.local.Cocktailmemo;
import com.websarva.wings.android.roomlivedatasamplejava.data.local.CocktailmemoDAO;

import java.util.List;
import java.util.concurrent.ExecutionException;

import androidx.lifecycle.LiveData;

public class CocktailmemoRepository {
	private AppDatabase _db;
	public CocktailmemoRepository(Application application) {
		_db = AppDatabase.getDatabase(application);
	}

	public LiveData<List<Cocktailmemo>> getCocktailmemoList() {
		CocktailmemoDAO cocktailmemoDAO = _db.createCocktailmemoDAO();
		return cocktailmemoDAO.findAll();
	}

	public Cocktailmemo getCocktailmemo(long cocktailId) {
		CocktailmemoDAO cocktailmemoDAO = _db.createCocktailmemoDAO();
		ListenableFuture<Cocktailmemo> future = cocktailmemoDAO.findByPK(cocktailId);
		Cocktailmemo cocktailmemo = null;
		try {
			cocktailmemo = future.get();
		}
		catch(ExecutionException ex) {
			ex.printStackTrace();
		}
		catch(InterruptedException ex) {
			ex.printStackTrace();
		}
		return cocktailmemo;
	}

	public boolean deleteInsertCocktailmemo(Cocktailmemo cocktailmemo) {
		CocktailmemoDAO cocktailmemoDAO = _db.createCocktailmemoDAO();
		ListenableFuture<Integer> futureDelete = cocktailmemoDAO.delete(cocktailmemo);
		boolean result = false;
		try {
			int resultDelete = futureDelete.get();
			if(resultDelete >= 0) {
				ListenableFuture<Long> futureInsert = cocktailmemoDAO.insert(cocktailmemo);
				long resultInsert = futureInsert.get();
				if(resultInsert >= 0) {
					result = true;
				}
			}
		}
		catch(ExecutionException ex) {
			ex.printStackTrace();
		}
		catch(InterruptedException ex) {
			ex.printStackTrace();
		}
		return result;
	}
}
