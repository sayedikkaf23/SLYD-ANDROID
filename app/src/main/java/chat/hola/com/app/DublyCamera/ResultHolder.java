package chat.hola.com.app.DublyCamera;

import java.io.File;

public class ResultHolder {

    private static File video;


    private static String path;
    private static String type;
    private static String call;
    private static Long duration;
    private static String musicId;


//    public static void setVideo(@Nullable File video) {
//        ResultHolder.video = video;
//    }
//
//    @Nullable
//    public static File getVideo() {
//        return video;
//    }


    public static void dispose() {


//        setVideo(null);
        setPath(null);
        setType(null);
        setCall(null);
        setDuration(0L);
        setMusicId(null);
    }

    public static String getPath() {
        return path;
    }

    public static void setPath(String path) {
        ResultHolder.path = path;
    }

    public static void setType(String type) {
        ResultHolder.type = type;
    }

    public static String getType() {
        return type;
    }

    public static String getCall() {
        return call;
    }

    public static void setCall(String call) {
        ResultHolder.call = call;
    }

    public static Long getDuration() {
        return duration;
    }

    public static void setDuration(Long duration) {
        ResultHolder.duration = duration;
    }

    public static void setMusicId(String musicId) {
        ResultHolder.musicId = musicId;
    }
}
