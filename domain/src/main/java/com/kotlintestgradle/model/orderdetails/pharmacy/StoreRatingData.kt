package com.customer.domain.model.orderdetails.pharmacy

data class StoreRatingData(
  val rating : String,
  val reviewTitle : String,
  val isRated : Boolean,
  val reviewDescription : String
)