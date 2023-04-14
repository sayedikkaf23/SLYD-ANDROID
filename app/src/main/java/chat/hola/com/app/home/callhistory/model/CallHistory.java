package chat.hola.com.app.home.callhistory.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

import chat.hola.com.app.home.stories.model.Viewer;

/**
 * <h1>CallHistory</h1>
 *
 * @author 3embed
 * @version 1.0
 * @since 9/08/2021
 */

public class CallHistory implements Serializable {
    @SerializedName("callInitiated")
    @Expose
    private Boolean callInitiated;
    @SerializedName("calltime")
    @Expose
    private String calltime;
    @SerializedName("callType")
    @Expose
    private String callType;
    @SerializedName("callId")
    @Expose
    private String callId;
    @SerializedName("callDuration")
    @Expose
    private String callDuration;
    @SerializedName("opponentUid")
    @Expose
    private String opponentUid;
    @SerializedName("opponentProfilePic")
    @Expose
    private String opponentProfilePic;
    @SerializedName("opponentNumber")
    @Expose
    private String opponentNumber;
    @SerializedName("isMissCalled")
    @Expose
    private Boolean isMissCalled;

    public Boolean getCallInitiated() {
        return callInitiated;
    }

    public void setCallInitiated(Boolean callInitiated) {
        this.callInitiated = callInitiated;
    }

    public String getCalltime() {
        return calltime;
    }

    public void setCalltime(String calltime) {
        this.calltime = calltime;
    }

    public String getCallType() {
        return callType;
    }

    public void setCallType(String callType) {
        this.callType = callType;
    }

    public String getCallId() {
        return callId;
    }

    public void setCallId(String callId) {
        this.callId = callId;
    }

    public String getCallDuration() {
        return callDuration;
    }

    public void setCallDuration(String callDuration) {
        this.callDuration = callDuration;
    }

    public String getOpponentUid() {
        return opponentUid;
    }

    public void setOpponentUid(String opponentUid) {
        this.opponentUid = opponentUid;
    }

    public String getOpponentProfilePic() {
        return opponentProfilePic;
    }

    public void setOpponentProfilePic(String opponentProfilePic) {
        this.opponentProfilePic = opponentProfilePic;
    }

    public String getOpponentNumber() {
        return opponentNumber;
    }

    public void setOpponentNumber(String opponentNumber) {
        this.opponentNumber = opponentNumber;
    }

    public Boolean getMissCalled() {
        return isMissCalled;
    }

    public void setMissCalled(Boolean missCalled) {
        isMissCalled = missCalled;
    }
}
