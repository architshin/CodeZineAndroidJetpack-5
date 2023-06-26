package com.websarva.wings.android.roomflowsample

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.activity.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.websarva.wings.android.roomflowsample.data.local.Cocktailmemo
import com.websarva.wings.android.roomflowsample.ui.MainViewModel
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
	private val _mainViewModel by viewModels<MainViewModel>()
	private lateinit var _cocktailListAdapter: CocktailListAdapter

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_main)
		lifecycleScope.launch {
			repeatOnLifecycle(Lifecycle.State.STARTED) {
				_mainViewModel.cocktailmemoFlow.collect(CocktailmemoCollector())
			}
		}
		lifecycleScope.launch {
			repeatOnLifecycle(Lifecycle.State.STARTED) {
				_mainViewModel.cocktailmemoListFlow.collect(CocktailmemoListCollector())
			}
		}

		val rvCocktail = findViewById<RecyclerView>(R.id.rvCocktail)
		val layout = LinearLayoutManager(this@MainActivity)
		rvCocktail.layoutManager = layout
		val decoration = DividerItemDecoration(this@MainActivity, layout.orientation)
		rvCocktail.addItemDecoration(decoration)
		_cocktailListAdapter = CocktailListAdapter()
		rvCocktail.adapter = _cocktailListAdapter

		val btnSave = findViewById<Button>(R.id.btnSave)
		btnSave.setOnClickListener(OnSaveButtonClick())
	}

	private inner class OnSaveButtonClick : View.OnClickListener {
		override fun onClick(view: View?) {
			val etPrice = findViewById<EditText>(R.id.etPrice)
			val price = etPrice.text.toString()
			_mainViewModel.saveCocktailmemo(price)
		}
	}

	private inner class CocktailmemoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
		val cocktailNameRow: TextView
		val cocktailPriceRow: TextView
		init {
			cocktailNameRow = itemView.findViewById(android.R.id.text1)
			cocktailPriceRow = itemView.findViewById(android.R.id.text2)
		}
	}

	private inner class CocktailListAdapter() : RecyclerView.Adapter<CocktailmemoViewHolder>() {
		private var _cocktailmemoList: MutableList<Cocktailmemo> = mutableListOf()

		override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CocktailmemoViewHolder {
			val inflater = LayoutInflater.from(this@MainActivity)
			val row = inflater.inflate(android.R.layout.simple_list_item_2, parent, false)
			row.setOnClickListener(ListItemClickListener())
			return CocktailmemoViewHolder(row)
		}

		override fun onBindViewHolder(holder: CocktailmemoViewHolder, position: Int) {
			val cocktailmemo = _cocktailmemoList.get(position)
			val cocktailNameRow = holder.cocktailNameRow
			cocktailNameRow.text = cocktailmemo.name
			cocktailNameRow.tag = cocktailmemo.id
			val cocktailPriceRow = holder.cocktailPriceRow
			cocktailPriceRow.text = "¥${cocktailmemo.price}"
		}

		override fun getItemCount(): Int {
			return _cocktailmemoList.size
		}

		fun changeCocktailmemoList(cocktailmemoList: MutableList<Cocktailmemo>) {
			_cocktailmemoList = cocktailmemoList
			notifyDataSetChanged()
		}
	}

	private inner class ListItemClickListener : View.OnClickListener {
		override fun onClick(view: View?) {
			view?.let {
				val cocktailNameRow = it.findViewById<TextView>(android.R.id.text1)
				val id = cocktailNameRow.tag as Long
				val cocktailName = cocktailNameRow.text.toString()
				_mainViewModel.prepareCocktailmemo(id, cocktailName)
			}
		}
	}

	private inner class CocktailmemoListCollector : FlowCollector<MutableList<Cocktailmemo>> {
		override suspend fun emit(cocktailmemos: MutableList<Cocktailmemo>) {
			_cocktailListAdapter.changeCocktailmemoList(cocktailmemos)
		}
	}

	private inner class CocktailmemoCollector : FlowCollector<Cocktailmemo> {
		override suspend fun emit(cocktailmemo: Cocktailmemo) {
			val tvCocktailName = findViewById<TextView>(R.id.tvCocktailName)
			tvCocktailName.text = cocktailmemo.name
			val etPrice = findViewById<EditText>(R.id.etPrice)
			etPrice.setText(cocktailmemo.price.toString())
			val btnSave = findViewById<Button>(R.id.btnSave)
			if(cocktailmemo.id == -1L) {
				etPrice.isEnabled = false
				btnSave.isEnabled = false
			}
			else {
				etPrice.isEnabled = true
				btnSave.isEnabled = true
			}
		}
	}
}
