package com.websarva.wings.android.roomlivedatasamplekotlin.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.websarva.wings.android.roomlivedatasamplekotlin.data.CocktailmemoRepository
import com.websarva.wings.android.roomlivedatasamplekotlin.data.local.Cocktailmemo
import kotlinx.coroutines.launch

class MainViewModel(application: Application) : AndroidViewModel(application) {
	private val _cocktailmemoRepository: CocktailmemoRepository
	val cocktailmemoLiveData = MutableLiveData<Cocktailmemo>()
	val cocktailmemoListLiveData: LiveData<MutableList<Cocktailmemo>>

	init {
		_cocktailmemoRepository = CocktailmemoRepository(application)
		val cocktailmemo = Cocktailmemo(-1, "未選択", 0)
		cocktailmemoLiveData.value = cocktailmemo
		cocktailmemoListLiveData = _cocktailmemoRepository.getCocktailmemoList()
	}

	fun prepareCocktailmemo(cocktailId: Long, cocktailName: String) {
		viewModelScope.launch {
			var cocktailmemo = _cocktailmemoRepository.getCocktailmemo(cocktailId)
			if(cocktailmemo == null) {
				cocktailmemo = Cocktailmemo(cocktailId, cocktailName, 0)
			}
			cocktailmemoLiveData.value = cocktailmemo
		}
	}

	fun saveCocktailmemo(priceStr: String) {
		val price = priceStr.toInt()
		var cocktailmemo = Cocktailmemo(cocktailmemoLiveData.value!!.id, cocktailmemoLiveData.value!!.name, price)
		viewModelScope.launch {
			val result = _cocktailmemoRepository.deleteInsertCocktailmemo(cocktailmemo)
			if(result) {
				cocktailmemo = Cocktailmemo(-1, "未選択", 0)
				cocktailmemoLiveData.value = cocktailmemo
			}
		}
	}
}
