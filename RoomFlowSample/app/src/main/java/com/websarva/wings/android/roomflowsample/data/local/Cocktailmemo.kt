package com.websarva.wings.android.roomflowsample.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "cocktailmemos")
data class Cocktailmemo (
	@PrimaryKey(autoGenerate = true) val id: Long,
	val name: String,
	val price: Int
)
