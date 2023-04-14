package chat.hola.com.app.Utilities;

import android.webkit.MimeTypeMap;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;

/**
 * Created by ankit on 12/3/18.
 */

public class FileUtil {


    public static String getMimeType(String url){
        String type = null;
        String extension = MimeTypeMap.getFileExtensionFromUrl(url);
        if(extension != null)
            type = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension);
        return type;
    }

    public static byte[] getFileToByteArray(String path) throws IOException {
        FileInputStream fis = new FileInputStream(path);
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        byte[] b = new byte[1024];

        for (int readNum; (readNum = fis.read(b)) != -1;) {
            bos.write(b, 0, readNum);
        }
        byte[] bytes = bos.toByteArray();
        return bytes;
    }

}
