package chat.hola.com.app.motionView.filters.helperClasses;

import android.content.Context;

/**
 * Created by moda on 20/05/17.
 */

public abstract class CancelHandler {
    private Context context;

    public abstract void execute();

    public CancelHandler(Context ctx) {
        this.context = ctx;
    }

    public void handleResponse() {
        execute();

    }
}