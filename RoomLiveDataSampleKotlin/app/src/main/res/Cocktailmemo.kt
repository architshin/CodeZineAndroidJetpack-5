package com.websarva.wings.android.repositorysamplekotlin.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "cocktailmemos")
data class Cocktailmemo (
	@PrimaryKey val id: Int,
	val name: String,
	val note: String?
)
