package chat.hola.com.app.coin.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

class CoinPackageResponse : Serializable {
    @SerializedName("message")
    @Expose
    var message: String? = null

    @SerializedName("data")
    @Expose
    var data: Data? = null

    class Data : Serializable{
        @SerializedName("data")
        @Expose
        lateinit var packages: ArrayList<CoinPackage>
    }

}