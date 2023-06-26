package com.websarva.wings.android.roomlivedatasamplejava.ui;

import android.app.Application;

import com.websarva.wings.android.roomlivedatasamplejava.data.CocktailmemoRepository;
import com.websarva.wings.android.roomlivedatasamplejava.data.local.Cocktailmemo;

import java.util.List;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

public class MainViewModel extends AndroidViewModel {
	private CocktailmemoRepository _cocktailmemoRepository;
	private MutableLiveData<Cocktailmemo> _cocktailmemoLiveData = new MutableLiveData<>();
	private LiveData<List<Cocktailmemo>> _cocktailmemoListLiveData;

	public MainViewModel(Application application) {
		super(application);
		_cocktailmemoRepository = new CocktailmemoRepository(application);
		Cocktailmemo cocktailmemo = new Cocktailmemo();
		cocktailmemo.id = -1;
		cocktailmemo.name = "未選択";
		cocktailmemo.price = 0;
		_cocktailmemoLiveData.setValue(cocktailmemo);
		_cocktailmemoListLiveData = _cocktailmemoRepository.getCocktailmemoList();
	}

	public void prepareCocktailmemo(long cocktailId, String cocktailName) {
		Cocktailmemo cocktailmemo = _cocktailmemoRepository.getCocktailmemo(cocktailId);
		if(cocktailmemo == null) {
			cocktailmemo = new Cocktailmemo();
			cocktailmemo.id = cocktailId;
			cocktailmemo.name = cocktailName;
			cocktailmemo.price = 0;
		}
		_cocktailmemoLiveData.setValue(cocktailmemo);
	}

	public void saveCocktailmemo(String priceStr) {
		int price = Integer.valueOf(priceStr);
		Cocktailmemo cocktailmemo = _cocktailmemoLiveData.getValue();
		cocktailmemo.price = price;
		boolean result = _cocktailmemoRepository.deleteInsertCocktailmemo(cocktailmemo);
		if(result) {
			cocktailmemo.id = -1;
			cocktailmemo.name = "未選択";
			cocktailmemo.price = 0;
			_cocktailmemoLiveData.setValue(cocktailmemo);
		}
	}

	public LiveData<Cocktailmemo> getCocktailmemoLiveData() {
		return _cocktailmemoLiveData;
	}

	public LiveData<List<Cocktailmemo>> getCocktailmemoListLiveData() {
		return _cocktailmemoListLiveData;
	}
}
