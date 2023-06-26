package com.websarva.wings.android.roomlivedatasamplejava;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.websarva.wings.android.roomlivedatasamplejava.data.local.Cocktailmemo;
import com.websarva.wings.android.roomlivedatasamplejava.ui.MainViewModel;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
	private MainViewModel _mainViewModel;
	private CocktailListAdapter _cocktailListAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		ViewModelProvider provider = new ViewModelProvider(MainActivity.this);
		_mainViewModel = provider.get(MainViewModel.class);
		LiveData<Cocktailmemo> cocktailmemoLiveData = _mainViewModel.getCocktailmemoLiveData();
		cocktailmemoLiveData.observe(MainActivity.this, new CocktailmemoObserver());
		LiveData<List<Cocktailmemo>> cocktailmemoListLiveData = _mainViewModel.getCocktailmemoListLiveData();
		cocktailmemoListLiveData.observe(MainActivity.this, new CocktailmemoListObserver());

		RecyclerView rvCocktail = findViewById(R.id.rvCocktail);
		LinearLayoutManager layout = new LinearLayoutManager(MainActivity.this);
		rvCocktail.setLayoutManager(layout);
		DividerItemDecoration decoration = new DividerItemDecoration(MainActivity.this, layout.getOrientation());
		rvCocktail.addItemDecoration(decoration);
		_cocktailListAdapter = new CocktailListAdapter();
		rvCocktail.setAdapter(_cocktailListAdapter);

		Button btnSave = findViewById(R.id.btnSave);
		btnSave.setOnClickListener(new OnSaveButtonClick());
	}

	private class OnSaveButtonClick implements View.OnClickListener {
		@Override
		public void onClick(View view) {
			EditText etPrice = findViewById(R.id.etPrice);
			String price = etPrice.getText().toString();
			_mainViewModel.saveCocktailmemo(price);

		}
	}

	private class CocktailmemoViewHolder extends RecyclerView.ViewHolder {
		private final TextView _cocktailNameRow;
		private final TextView _cocktailPriceRow;
		public CocktailmemoViewHolder(View itemView) {
			super(itemView);
			_cocktailNameRow = itemView.findViewById(android.R.id.text1);
			_cocktailPriceRow = itemView.findViewById(android.R.id.text2);
		}

		public TextView getCocktailNameRow() {
			return _cocktailNameRow;
		}

		public TextView getCocktailPriceRow() {
			return _cocktailPriceRow;
		}
	}

	private class CocktailListAdapter extends RecyclerView.Adapter<CocktailmemoViewHolder> {
		private List<Cocktailmemo> _cocktailmemoList = new ArrayList<>();

		@NonNull
		@Override
		public CocktailmemoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
			LayoutInflater inflater = LayoutInflater.from(MainActivity.this);
			View row = inflater.inflate(android.R.layout.simple_list_item_2, parent, false);
			row.setOnClickListener(new ListItemClickListener());
			CocktailmemoViewHolder holder = new CocktailmemoViewHolder(row);
			return holder;
		}

		@Override
		public void onBindViewHolder(@NonNull CocktailmemoViewHolder holder, int position) {
			Cocktailmemo cocktailmemo = _cocktailmemoList.get(position);
			TextView cocktailNameRow = holder.getCocktailNameRow();
			cocktailNameRow.setText(cocktailmemo.name);
			cocktailNameRow.setTag(cocktailmemo.id);
			TextView cocktailPriceRow = holder.getCocktailPriceRow();
			cocktailPriceRow.setText("¥" + cocktailmemo.price);
		}

		@Override
		public int getItemCount() {
			return _cocktailmemoList.size();
		}

		public void changeCocktailmemoList(List<Cocktailmemo> cocktailmemoList) {
			_cocktailmemoList = cocktailmemoList;
			notifyDataSetChanged();
		}
	}

	private class ListItemClickListener implements View.OnClickListener {
		@Override
		public void onClick(View view) {
			TextView cocktailNameRow = view.findViewById(android.R.id.text1);
			long id = (long) cocktailNameRow.getTag();
			String cocktailName = cocktailNameRow.getText().toString();
			_mainViewModel.prepareCocktailmemo(id, cocktailName);
		}
	}

	private class CocktailmemoListObserver implements Observer<List<Cocktailmemo>> {
		@Override
		public void onChanged(@NonNull List<Cocktailmemo> cocktailmemos) {
			_cocktailListAdapter.changeCocktailmemoList(cocktailmemos);
		}
	}

	private class CocktailmemoObserver implements Observer<Cocktailmemo> {
		@Override
		public void onChanged(@NonNull Cocktailmemo cocktailmemo) {
			TextView tvCocktailName = findViewById(R.id.tvCocktailName);
			tvCocktailName.setText(cocktailmemo.name);
			EditText etPrice = findViewById(R.id.etPrice);
			etPrice.setText(String.valueOf(cocktailmemo.price));
			Button btnSave = findViewById(R.id.btnSave);
			if(cocktailmemo.id == -1) {
				etPrice.setEnabled(false);
				btnSave.setEnabled(false);
			}
			else {
				etPrice.setEnabled(true);
				btnSave.setEnabled(true);
			}
		}
	}
}
