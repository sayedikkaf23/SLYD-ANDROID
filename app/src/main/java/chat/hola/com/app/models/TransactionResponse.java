package chat.hola.com.app.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

/**
 * <h1>TransactionResponse</h1>
 * <p></p>
 *
 * @author : Shaktisinh Jadeja
 * @version : 1.0
 * @company : 3Embed Software Technologies Pvt. Ltd.
 * @since : 05 Feb 2020
 */
public class TransactionResponse implements Serializable {
    @SerializedName("message")
    @Expose
    String message;
    @SerializedName("pageState")
    @Expose
    String pageState;
    @SerializedName("totalCount")
    @Expose
    String totalCount;
    @SerializedName("data")
    @Expose
    List<WalletTransactionData> transactionDataList;


    public String getPageState() {
        return pageState;
    }

    public void setPageState(String pageState) {
        this.pageState = pageState;
    }

    public String getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(String totalCount) {
        this.totalCount = totalCount;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<WalletTransactionData> getTransactionDataList() {
        return transactionDataList;
    }

    public void setTransactionDataList(List<WalletTransactionData> transactionDataList) {
        this.transactionDataList = transactionDataList;
    }


}
