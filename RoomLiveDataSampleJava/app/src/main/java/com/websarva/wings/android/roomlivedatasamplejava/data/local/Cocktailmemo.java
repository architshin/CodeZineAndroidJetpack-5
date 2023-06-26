package com.websarva.wings.android.roomlivedatasamplejava.data.local;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "cocktailmemos")
public class Cocktailmemo {
	@PrimaryKey(autoGenerate = true)
	public long id;
	@NonNull
	public String name;
	@NonNull
	public int price;
}
