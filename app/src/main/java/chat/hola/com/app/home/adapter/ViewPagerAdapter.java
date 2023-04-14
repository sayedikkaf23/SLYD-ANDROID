package chat.hola.com.app.home.adapter;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import chat.hola.com.app.home.comment.CommentFragment;

public class ViewPagerAdapter extends FragmentPagerAdapter {
    private String postId;
    private int commentCount;

    public ViewPagerAdapter(FragmentManager fm, String postId, int commentCount) {
        super(fm);
        this.postId = postId;
        this.commentCount = commentCount;
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment = null;
        if (position == 0) {
            fragment = new CommentFragment(postId, commentCount);
        }
        return fragment;
    }

    @Override
    public int getCount() {
        return 1;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        String title = null;
        if (position == 0) {
            title = "Likes";
        }
        return title;
    }
}
