package chat.hola.com.app.Utilities;

import android.os.Environment;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import chat.hola.com.app.AppController;

public class FileHelper {

    public String convertByteArrayToFile(byte[] data, String name, String extension) {


        File file;


        String path = AppController.getInstance().getFilesDir() + ApiOnServer.CHAT_RECEIVED_THUMBNAILS_FOLDER;


        try {


            File folder = new File(path);


            if (!folder.exists() && !folder.isDirectory()) {
                folder.mkdirs();
            }


            file = new File(path, name + "." + extension);


            if (!file.exists()) {

                file.createNewFile();

            }


            FileOutputStream fos = new FileOutputStream(file);


            fos.write(data);
            fos.flush();
            fos.close();
        } catch (IOException e) {

        }


        return path + "/" + name + "." + extension;
    }
}
