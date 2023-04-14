package chat.hola.com.app.ViewHolders;

/**
 * Created by moda on 08/08/17.
 */

import androidx.recyclerview.widget.RecyclerView;
import android.view.View;

import com.ezcall.android.R;
import chat.hola.com.app.Utilities.SlackLoadingView;


/**
 * View holder for the loading more results item in recycler view
 */

public class ViewHolderLoading extends RecyclerView.ViewHolder {


    public SlackLoadingView slack;


    public ViewHolderLoading(View view) {
        super(view);


        slack = (SlackLoadingView) view.findViewById(R.id.slack);


    }
}
