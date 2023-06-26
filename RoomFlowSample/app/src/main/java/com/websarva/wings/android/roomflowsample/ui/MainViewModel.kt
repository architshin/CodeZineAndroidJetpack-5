package com.websarva.wings.android.roomflowsample.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.websarva.wings.android.roomflowsample.data.CocktailmemoRepository
import com.websarva.wings.android.roomflowsample.data.local.Cocktailmemo
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class MainViewModel(application: Application) : AndroidViewModel(application) {
	private val _cocktailmemoRepository: CocktailmemoRepository
	val cocktailmemoFlow: MutableStateFlow<Cocktailmemo>
	val cocktailmemoListFlow: Flow<MutableList<Cocktailmemo>>

	init {
		_cocktailmemoRepository = CocktailmemoRepository(application)
		val cocktailmemo = Cocktailmemo(-1, "未選択", 0)
		cocktailmemoFlow = MutableStateFlow(cocktailmemo)
		cocktailmemoListFlow = _cocktailmemoRepository.getCocktailmemoList()
	}

	fun prepareCocktailmemo(cocktailId: Long, cocktailName: String) {
		viewModelScope.launch {
			var cocktailmemo = _cocktailmemoRepository.getCocktailmemo(cocktailId)
			if(cocktailmemo == null) {
				cocktailmemo = Cocktailmemo(cocktailId, cocktailName, 0)
			}
			cocktailmemoFlow.value = cocktailmemo
		}
	}

	fun saveCocktailmemo(priceStr: String) {
		val price = priceStr.toInt()
		var cocktailmemo = Cocktailmemo(cocktailmemoFlow.value.id, cocktailmemoFlow.value.name, price)
		viewModelScope.launch {
			val result = _cocktailmemoRepository.deleteInsertCocktailmemo(cocktailmemo)
			if(result) {
				cocktailmemo = Cocktailmemo(-1, "未選択", 0)
				cocktailmemoFlow.value = cocktailmemo
			}
		}
	}
}
