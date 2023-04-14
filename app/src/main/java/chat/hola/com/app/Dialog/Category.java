package chat.hola.com.app.Dialog;

/**
 * Created by ankit on 20/2/18.
 */

public class Category {
    private int mImgRes;
    private String CategoryTitle ="";
    private boolean isChecked =false;

    public Category(int mImgRes, String categoryTitle, boolean isChecked) {
        this.mImgRes = mImgRes;
        this.CategoryTitle = categoryTitle;
        this.isChecked = isChecked;
    }

    //setter
    public void setmImgRes(int mImgRes) {
        this.mImgRes = mImgRes;
    }

    public void setCategoryTitle(String categoryStr) {
        CategoryTitle = categoryStr;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    //getter
    public int getmImgRes() {
        return mImgRes;
    }

    public String getCategoryTitle() {
        return CategoryTitle;
    }

    public boolean isChecked() {
        return isChecked;
    }
}
