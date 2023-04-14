package chat.hola.com.app.home.stories.model;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Build;
import android.os.Bundle;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import chat.hola.com.app.cameraActivities.manager.CameraOutputModel;

/**
 * <h1>StoryModel</h1>
 *
 * @author 3Embed
 * @version 1.0
 * @since 4/24/2018.
 */

public class StoryModel {

    @Inject
    List<StoryPost> storyPosts;
    @Inject
    List<StoryData> storyData;

    @Inject
    StoriesAdapter adapter;

    private Map<String, Object> parameters;

    @Inject
    StoryModel() {
        parameters = new HashMap<>();
    }

    public Map<String, Object> getParameters(String type, String urlPath, boolean isPrivate) {
        parameters.put("type", type);
        parameters.put("urlPath", urlPath);
        parameters.put("thumbnail", urlPath);
        parameters.put("isPrivate", isPrivate);

        return parameters;
    }

    public Intent launchImahePicker() {
        Intent intent;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
            intent.addCategory(Intent.CATEGORY_DEFAULT);
            intent.setType("*/*");
        } else {
            intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
        }
        intent.putExtra("android.intent.extra.MIME_TYPES", new String[]{"image/*", "video/*"});
        return intent;
    }

    public Bundle getCameraOutputModel(CameraOutputModel cameraOutputModel) {
        Bundle bundle = new Bundle();
        bundle.putString("path", cameraOutputModel.getPath());
        bundle.putInt("type", cameraOutputModel.getType());
        bundle.putBoolean("isPrivate", false);
        return bundle;
    }

    public Bitmap getCircleBitmap(Bitmap bitmap) {
        try {
            final Bitmap circuleBitmap = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getWidth(), Bitmap.Config.ARGB_8888);
            final Canvas canvas = new Canvas(circuleBitmap);

            final int color = Color.GRAY;
            final Paint paint = new Paint();
            final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getWidth());
            final RectF rectF = new RectF(rect);

            paint.setAntiAlias(true);
            canvas.drawARGB(0, 0, 0, 0);
            paint.setColor(color);
            canvas.drawOval(rectF, paint);

            paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
            canvas.drawBitmap(bitmap, rect, rect, paint);

            return circuleBitmap;
        } catch (Exception e) {
            return null;
        }
    }

    public boolean myStories(List<StoryPost> posts) {
        storyPosts.clear();
        storyPosts.addAll(posts);
        return true;
    }

    public void stories(List<StoryData> data) {
        List<StoryData> recent = new ArrayList<>();
        List<StoryData> viewed = new ArrayList<>();
        for (StoryData story : data)
            if (story.isViewedAll()) viewed.add(story);
            else recent.add(story);

        storyData.clear();

        if (!recent.isEmpty()) {
            StoryData story = new StoryData();
            story.setHeader(true);
            story.setHeaderTitle("Recent updates");
            storyData.add(story);
            storyData.addAll(recent);
        }

        if (!viewed.isEmpty()) {
            StoryData story = new StoryData();
            story.setHeader(true);
            story.setHeaderTitle("Viewed updates");
            storyData.add(story);
            storyData.addAll(viewed);
        }
        adapter.notifyDataSetChanged();
    }

    public void addMystory(StoryData data) {
        storyData.add(data);
    }

    public void addStory(StoryPost data) {
        storyPosts.add(data);
        adapter.notifyDataSetChanged();
    }

    public StoryData getStoryData(int position) {
        return storyData.get(position);
    }

    public List<StoryPost> getStoryPosts() {
        return storyPosts;
    }

    public List<StoryPost> getStoryPosts(int position) {
        return storyData.get(position).getPosts();
    }

    public List<StoryData> getAllStoryData(){
        return storyData;
    }

    public void updateData(int position) {
        storyPosts.get(position).setViewed(true);
        adapter.notifyDataSetChanged();
    }

    public void updateAll() {
        for (StoryPost post: storyPosts)
            post.setViewed(true);
        adapter.notifyDataSetChanged();
    }
}
