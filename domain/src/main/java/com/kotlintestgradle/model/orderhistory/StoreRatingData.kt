package com.kotlintestgradle.model.orderhistory

data class StoreRatingData(
  val rating : String,
  val reviewTitle : String,
  val isRated : Boolean,
  val reviewDescription : String
)