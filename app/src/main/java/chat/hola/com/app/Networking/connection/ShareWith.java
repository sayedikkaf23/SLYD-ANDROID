package chat.hola.com.app.Networking.connection;

public class ShareWith {
    private boolean facebook;
    private boolean twitter;
    private boolean instagram;

    public ShareWith(boolean facebook, boolean twitter, boolean instagram) {
        this.facebook = facebook;
        this.twitter = twitter;
        this.instagram = instagram;
    }

    public boolean isFacebook() {
        return facebook;
    }

    public boolean isTwitter() {
        return twitter;
    }

    public boolean isInstagram() {
        return instagram;
    }
}
