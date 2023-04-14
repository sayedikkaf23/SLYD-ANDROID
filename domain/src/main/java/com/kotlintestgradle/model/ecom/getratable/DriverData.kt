package com.customer.domain.model.getratable
import com.kotlintestgradle.model.ecom.getratable.RatableAttributesData
import java.util.ArrayList

data class DriverData(
   val driverName:String,
   val profilePic:String,
   val driverId:String,
  val driverReview:String,
  val rating:String,
   val attribute: ArrayList<RatableAttributesData?>
)