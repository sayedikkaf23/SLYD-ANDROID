package chat.hola.com.app.profileScreen.business.post.actionbutton;


import java.util.List;

import javax.inject.Inject;

import chat.hola.com.app.models.ActionButtonResponse;

/**
 * <h1>ActionButtonModel</h1>
 *
 * @author DELL
 * @version 1.0
 * @since 09 September 2019
 */
public class ActionButtonModel {

    @Inject
    List<ActionButtonResponse.BizButton> dataList;
    @Inject
    ActionButtonAdapter adapter;

    @Inject
    public ActionButtonModel() {
    }

    public void setData(List<ActionButtonResponse.BizButton> dataList) {
        this.dataList.clear();
        this.dataList = dataList;
        int pos = 0;
        for (int i = 0; i < dataList.size(); i++) {
            if (ActionButtonFragment.buttonText.equalsIgnoreCase(dataList.get(i).getButtonText())) {
                pos = i;
                break;
            }
        }
        adapter.setData(dataList, pos);
    }
}
