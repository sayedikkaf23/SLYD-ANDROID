package chat.hola.com.app.home.home

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.kotlintestgradle.model.ecom.home.CategoryData
import com.kotlintestgradle.remote.model.response.ecom.homeSubCategory.CategoryDetails

data class ProductsResponse(
    @Expose
    @SerializedName("data")
    val data: ProductsList
)

data class ProductsList(
    @Expose
    @SerializedName("data")
    val data: ArrayList<CategoryDetails>
)