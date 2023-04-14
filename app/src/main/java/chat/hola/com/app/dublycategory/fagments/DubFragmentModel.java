package chat.hola.com.app.dublycategory.fagments;

import chat.hola.com.app.dubly.Dub;
import chat.hola.com.app.dublycategory.modules.DubListAdapter;
import java.util.List;
import javax.inject.Inject;
import javax.inject.Named;

/**
 * <h1>DubCategoryModel</h1>
 *
 * @author 3Embed
 * @since 4/10/2018.
 */

public class DubFragmentModel {

  @Inject
  @Named("default")
  List<Dub> dubs;
  @Inject
  DubListAdapter adapter;

  @Inject
  DubFragmentModel() {
  }

  public void setData(List<Dub> data) {
    if (data != null) {
      this.dubs.addAll(data);
      adapter.notifyDataSetChanged();
    }
  }

  public void clearList() {
    this.dubs.clear();
  }

  public void getData(int position) {
    List<Dub> temp = dubs;
    dubs.clear();
    switch (position) {
      case 1:
        for (Dub dub : temp) {
          if (dub.isMyFavourite() == 1) dubs.add(dub);
        }
        break;
      case 2:
        break;
    }
    adapter.notifyDataSetChanged();
  }

  public void setPlaying(int position, boolean isPlaying) {
    try {
      dubs.get(position).setPlaying(isPlaying);
      if (isPlaying) {
        for (int i = 0; i < dubs.size(); i++)
          if (i != position) dubs.get(i).setPlaying(false);
      }
    }catch (ArrayIndexOutOfBoundsException e){
      e.printStackTrace();
    }
    adapter.notifyDataSetChanged();
  }

  public String getPath(int position) {
    return dubs.get(position).getPath();
  }

  public String getAudio(int position) {
    return dubs.get(position).getPath();
  }

  public void setFavourite(int position, boolean flag) {
    dubs.get(position).setMyFavourite(flag ? 1 : 0);
    adapter.notifyDataSetChanged();
  }

  public String getMusicId(int position) {
    return dubs.get(position).getId();
  }

  public String getName(int position) {
    return dubs.get(position).getName();
  }

  public String getDuration(int position) {
    return dubs.get(position).getDuration();
  }
}
