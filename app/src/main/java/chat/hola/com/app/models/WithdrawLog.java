package chat.hola.com.app.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class WithdrawLog implements Serializable {

    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("data")
    @Expose
    private DataResponse data;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public DataResponse getData() {
        return data;
    }

    public void setData(DataResponse data) {
        this.data = data;
    }

    public class DataResponse implements Serializable {

        @SerializedName("pageState")
        String pageState;
        @SerializedName("totalCount")
        String totalCount;
        @SerializedName("data")
        List<Data> data;

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

        public List<Data> getData() {
            return data;
        }

        public void setData(List<Data> data) {
            this.data = data;
        }

        public class Data implements Serializable {

            @SerializedName("updatedatTimestamp")
            @Expose
            private String updatedatTimestamp;
            @SerializedName("commontype")
            @Expose
            private String commontype;
            @SerializedName("withdrawid")
            @Expose
            private String withdrawid;
            @SerializedName("amount")
            @Expose
            private String amount;
            @SerializedName("fee")
            @Expose
            private String fee = "0";
            @SerializedName("approvedby")
            @Expose
            private String approvedby;
            @SerializedName("approvedon")
            @Expose
            private Object approvedon;
            @SerializedName("autopayout")
            @Expose
            private String autopayout;
            @SerializedName("bankid")
            @Expose
            private String bankid;
            @SerializedName("coins")
            @Expose
            private Integer coins;
            @SerializedName("createdat")
            @Expose
            private String createdat;
            @SerializedName("currency")
            @Expose
            private String currency;
            @SerializedName("notes")
            @Expose
            private String notes;
            @SerializedName("pgid")
            @Expose
            private String pgid;
            @SerializedName("pgname")
            @Expose
            private String pgname;
            @SerializedName("reason")
            @Expose
            private String reason;
            @SerializedName("status")
            @Expose
            private String status;
            @SerializedName("transferredby")
            @Expose
            private String transferredby;
            @SerializedName("transferredon")
            @Expose
            private Object transferredon;
            @SerializedName("updatedat")
            @Expose
            private String updatedat;
            @SerializedName("userid")
            @Expose
            private String userid;
            @SerializedName("usertype")
            @Expose
            private String usertype;
            private final static long serialVersionUID = -5438228217790910369L;

            public String getFee() {
                return fee;
            }

            public void setFee(String fee) {
                this.fee = fee;
            }

            public String getCommontype() {
                return commontype;
            }

            public void setCommontype(String commontype) {
                this.commontype = commontype;
            }

            public String getWithdrawid() {
                return withdrawid;
            }

            public void setWithdrawid(String withdrawid) {
                this.withdrawid = withdrawid;
            }

            public String getAmount() {
                return amount;
            }

            public void setAmount(String amount) {
                this.amount = amount;
            }

            public String getApprovedby() {
                return approvedby;
            }

            public void setApprovedby(String approvedby) {
                this.approvedby = approvedby;
            }

            public Object getApprovedon() {
                return approvedon;
            }

            public void setApprovedon(Object approvedon) {
                this.approvedon = approvedon;
            }

            public String getAutopayout() {
                return autopayout;
            }

            public void setAutopayout(String autopayout) {
                this.autopayout = autopayout;
            }

            public String getBankid() {
                return bankid;
            }

            public void setBankid(String bankid) {
                this.bankid = bankid;
            }

            public Integer getCoins() {
                return coins;
            }

            public void setCoins(Integer coins) {
                this.coins = coins;
            }

            public String getCreatedat() {
                return createdat;
            }

            public void setCreatedat(String createdat) {
                this.createdat = createdat;
            }

            public String getCurrency() {
                return currency;
            }

            public void setCurrency(String currency) {
                this.currency = currency;
            }

            public String getNotes() {
                return notes;
            }

            public void setNotes(String notes) {
                this.notes = notes;
            }

            public String getPgid() {
                return pgid;
            }

            public void setPgid(String pgid) {
                this.pgid = pgid;
            }

            public String getPgname() {
                return pgname;
            }

            public void setPgname(String pgname) {
                this.pgname = pgname;
            }

            public String getReason() {
                return reason;
            }

            public void setReason(String reason) {
                this.reason = reason;
            }

            public String getStatus() {
                return status;
            }

            public void setStatus(String status) {
                this.status = status;
            }

            public String getTransferredby() {
                return transferredby;
            }

            public void setTransferredby(String transferredby) {
                this.transferredby = transferredby;
            }

            public Object getTransferredon() {
                return transferredon;
            }

            public void setTransferredon(Object transferredon) {
                this.transferredon = transferredon;
            }

            public String getUpdatedat() {
                return updatedat;
            }

            public void setUpdatedat(String updatedat) {
                this.updatedat = updatedat;
            }

            public String getUserid() {
                return userid;
            }

            public void setUserid(String userid) {
                this.userid = userid;
            }

            public String getUsertype() {
                return usertype;
            }

            public void setUsertype(String usertype) {
                this.usertype = usertype;
            }

            public String getUpdatedatTimestamp() {
                return updatedatTimestamp;
            }

            public void setUpdatedatTimestamp(String updatedatTimestamp) {
                this.updatedatTimestamp = updatedatTimestamp;
            }
        }
    }


}
