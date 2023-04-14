package chat.hola.com.app.coin.model

import com.android.billingclient.api.SkuDetails
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

class CoinPackage : Serializable {
    @SerializedName("_id")
    @Expose
    var id: String? = null

    @SerializedName("statusCode")
    @Expose
    var statusCode: String? = null

    @SerializedName("statusText")
    @Expose
    var statusText: String? = null

    @SerializedName("playstoreId")
    @Expose
    var playstoreId: String? = null

    @SerializedName("appstoreId")
    @Expose
    var appstoreId: String? = null

    @SerializedName("planCost")
    @Expose
    var planCost: String? = null

    @SerializedName("oldPlanCost")
    @Expose
    var oldPlanCost: String? = null

    @SerializedName("currency")
    @Expose
    var currency: String? = null

    @SerializedName("currencySymbol")
    @Expose
    var currencySymbol: String? = null

    @SerializedName("coins")
    @Expose
    var coins: String? = null

    @SerializedName("createdDate")
    @Expose
    var createdDate: String? = null

    @SerializedName("createdTs")
    @Expose
    var createdTs: String? = null

    @SerializedName("image")
    @Expose
    var image: String? = null

    @SerializedName("backgroundImage")
    @Expose
    var backgroundImage: String? = null

    @SerializedName("description")
    @Expose
    var description: String? = null

    @SerializedName("planTitle")
    @Expose
    var title: String? = null

    var skuDetails: SkuDetails? = null
//
//    class Title : Serializable {
//        @SerializedName("en")
//        @Expose
//        var english: String? = null
//
//        @SerializedName("hi")
//        @Expose
//        var hindi: String? = null
//    }
//
//    class Description : Serializable {
//        @SerializedName("en")
//        @Expose
//        var english: String? = null
//
//        @SerializedName("hi")
//        @Expose
//        var hindi: String? = null
//    }
}