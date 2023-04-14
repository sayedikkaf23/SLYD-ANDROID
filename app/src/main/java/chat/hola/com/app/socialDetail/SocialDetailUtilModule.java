package chat.hola.com.app.socialDetail;

import androidx.appcompat.app.AlertDialog;
import android.widget.ArrayAdapter;

import com.ezcall.android.R;

import java.util.ArrayList;
import java.util.List;

import chat.hola.com.app.dagger.ActivityScoped;
import chat.hola.com.app.home.model.Data;
import dagger.Module;
import dagger.Provides;

/**
 * <h1>SocialDetailUtilModule</h1>
 *
 * @author 3Embed
 * @version 1.0
 * @since 23/3/18.
 */

//@ActivityScoped
@Module
public class SocialDetailUtilModule {

    @Provides
    @ActivityScoped
    List<Data> dataList(){
        return new ArrayList<Data>();
    }

    @ActivityScoped
    @Provides
    AlertDialog.Builder builder(SocialDetailActivity activity) {
        return new AlertDialog.Builder(activity);
    }

    @ActivityScoped
    @Provides
    ArrayAdapter<String> reportReasons(SocialDetailActivity activity) {
        return new ArrayAdapter<String>(activity, R.layout.custom_select_dialog_singlechoice);
    }


}
