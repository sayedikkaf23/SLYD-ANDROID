package chat.hola.com.app.models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class StreamStats implements Serializable {

    @SerializedName("message")
    String message;

    @SerializedName("data")
    Stats stats;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Stats getStats() {
        return stats;
    }

    public void setStats(Stats stats) {
        this.stats = stats;
    }

    public class Stats implements Serializable {
        @SerializedName("viwers")
        String viewers;

        @SerializedName("gifts")
        String gifts;

        @SerializedName("durationInSeconds")
        String durationInSeconds;

        @SerializedName("totalEarning")
        String totalEarning;

        @SerializedName("MoneyEarn")
        double moneyEarned = 0;

        public double getMoneyEarned() {
            return moneyEarned;
        }

        public void setMoneyEarned(double moneyEarned) {
            this.moneyEarned = moneyEarned;
        }

        public String getViewers() {
            return viewers;
        }

        public void setViewers(String viewers) {
            this.viewers = viewers;
        }

        public String getGifts() {
            return gifts;
        }

        public void setGifts(String gifts) {
            this.gifts = gifts;
        }

        public String getDurationInSeconds() {
            return durationInSeconds;
        }

        public void setDurationInSeconds(String durationInSeconds) {
            this.durationInSeconds = durationInSeconds;
        }

        public String getTotalEarning() {
            return totalEarning;
        }

        public void setTotalEarning(String totalEarning) {
            this.totalEarning = totalEarning;
        }
    }
}
