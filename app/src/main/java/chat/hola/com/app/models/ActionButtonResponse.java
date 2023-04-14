package chat.hola.com.app.models;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

/**
 * <h1>ActionButtonResponse</h1>
 *
 * @author DELL
 * @version 1.0
 * @since 09 September 2019
 */
public class ActionButtonResponse implements Serializable {
    @SerializedName("data")
    @Expose
    List<BizButton> dataList;

    public List<BizButton> getDataList() {
        return dataList;
    }

    public void setDataList(List<BizButton> dataList) {
        this.dataList = dataList;
    }

    public class BizButton implements Serializable {
        @SerializedName("buttonText")
        @Expose
        String buttonText;
        @SerializedName("buttonColor")
        @Expose
        String buttonColor;

        public String getButtonText() {
            return buttonText;
        }

        public void setButtonText(String buttonText) {
            this.buttonText = buttonText;
        }

        public String getButtonColor() {
            return buttonColor;
        }

        public void setButtonColor(String buttonColor) {
            this.buttonColor = buttonColor;
        }
    }
}
