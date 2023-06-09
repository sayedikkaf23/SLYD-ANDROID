package chat.hola.com.app.DocumentPicker;



import java.util.ArrayList;

import chat.hola.com.app.DocumentPicker.Models.BaseFile;
import chat.hola.com.app.DocumentPicker.Models.FileType;
import chat.hola.com.app.DocumentPicker.Utils.Orientation;
import com.ezcall.android.R;

/**
 * Created by moda on 22/08/17.
 */

public class PickerManager {
    private static PickerManager ourInstance = new PickerManager();
    private int maxCount = FilePickerConst.DEFAULT_MAX_COUNT;
    private int currentCount;
    private boolean showImages = true;
    private int cameraDrawable = R.drawable.ic_camera_icon;

    public static PickerManager getInstance() {
        return ourInstance;
    }

    private ArrayList<String> mediaFiles;
    private ArrayList<String> docFiles;

    private ArrayList<FileType> fileTypes;

    private ArrayList<String> documentMimeTypes;


    private int theme = R.style.LibAppTheme;

    private boolean showVideos;

    private boolean showGif;

    private boolean docSupport = true;

    private boolean enableCamera = true;

    private Orientation orientation = Orientation.UNSPECIFIED;

    private boolean showFolderView = true;

    private String providerAuthorities;

    private PickerManager() {
        mediaFiles = new ArrayList<>();
        docFiles = new ArrayList<>();
        fileTypes = new ArrayList<>();


        documentMimeTypes = new ArrayList<>();
    }

    public void setMaxCount(int count) {
        clearSelections();
        this.maxCount = count;
    }

    public int getMaxCount() {
        return maxCount;
    }

    public int getCurrentCount() {
        return currentCount;
    }

    public void add(String path, int type) {
        if (path != null && shouldAdd()) {
            if (!mediaFiles.contains(path) && type == FilePickerConst.FILE_TYPE_MEDIA)
                mediaFiles.add(path);
            else if (type == FilePickerConst.FILE_TYPE_DOCUMENT)
                docFiles.add(path);
            else
                return;

            currentCount++;
        }
    }


    /**
     *
     */
    public void addDocumentAlongWithType(String path, int type, String documentMimeType) {
        if (path != null && shouldAdd()) {
            if (type == FilePickerConst.FILE_TYPE_DOCUMENT) {
                docFiles.add(path);
                documentMimeTypes.add(documentMimeType);


            } else
                return;

            currentCount++;
        }
    }


    public void add(ArrayList<String> paths, int type) {
        for (int index = 0; index < paths.size(); index++) {
            add(paths.get(index), type);
        }
    }


    public void addDocumentWithMimeType(ArrayList<String> paths, int type, String mimeType) {
        for (int index = 0; index < paths.size(); index++) {
            addDocumentAlongWithType(paths.get(index), type, mimeType);
        }
    }


    public void remove(String path, int type) {
        if ((type == FilePickerConst.FILE_TYPE_MEDIA) && mediaFiles.contains(path)) {
            mediaFiles.remove(path);
            currentCount--;

        } else if (type == FilePickerConst.FILE_TYPE_DOCUMENT) {


            for (int i = 0; i < docFiles.size(); i++) {


                if (docFiles.get(i).equals(path)) {

                    docFiles.remove(path);
                    documentMimeTypes.remove(i);
                    break;

                }


            }


            currentCount--;
        }
    }

    public boolean shouldAdd() {
        return currentCount < maxCount;
    }

    public ArrayList<String> getSelectedPhotos() {
        return mediaFiles;
    }

    public ArrayList<String> getSelectedFiles() {
        return docFiles;
    }

    public ArrayList<String> getSelectedMimeTypes() {
        return documentMimeTypes;
    }

    public ArrayList<String> getSelectedFileTypes() {
        return documentMimeTypes;
    }


    public ArrayList<String> getSelectedFilePaths(ArrayList<BaseFile> files) {
        ArrayList<String> paths = new ArrayList<>();
        for (int index = 0; index < files.size(); index++) {
            paths.add(files.get(index).getPath());
        }
        return paths;
    }

    public void clearSelections() {
        docFiles.clear();

        documentMimeTypes.clear();


        mediaFiles.clear();
        fileTypes.clear();
        currentCount = 0;
        maxCount = 0;
    }

    public int getTheme() {
        return theme;
    }

    public void setTheme(int theme) {
        this.theme = theme;
    }

    public boolean showVideo() {
        return showVideos;
    }

    public void setShowVideos(boolean showVideos) {
        this.showVideos = showVideos;
    }

    public boolean showImages() {
        return showImages;
    }

    public void setShowImages(boolean showImages) {
        this.showImages = showImages;
    }

    public boolean isShowGif() {
        return showGif;
    }

    public void setShowGif(boolean showGif) {
        this.showGif = showGif;
    }

    public boolean isShowFolderView() {
        return showFolderView;
    }

    public void setShowFolderView(boolean showFolderView) {
        this.showFolderView = showFolderView;
    }

    public void addFileType(FileType fileType) {
        fileTypes.add(fileType);
    }

    public void addDocTypes() {
        String[] pdfs = {"pdf"};
        fileTypes.add(new FileType(FilePickerConst.PDF, pdfs, R.drawable.ic_pdf));

        String[] docs = {"doc", "docx", "dot", "dotx"};
        fileTypes.add(new FileType(FilePickerConst.DOC, docs, R.drawable.ic_word));

        String[] ppts = {"ppt", "pptx"};
        fileTypes.add(new FileType(FilePickerConst.PPT, ppts, R.drawable.ic_ppt));

        String[] xlss = {"xls", "xlsx"};
        fileTypes.add(new FileType(FilePickerConst.XLS, xlss, R.drawable.ic_excel));

        String[] txts = {"txt"};
        fileTypes.add(new FileType(FilePickerConst.TXT, txts, R.drawable.ic_txt));
    }

    public ArrayList<FileType> getFileTypes() {
        return fileTypes;
    }

    public boolean isDocSupport() {
        return docSupport;
    }

    public void setDocSupport(boolean docSupport) {
        this.docSupport = docSupport;
    }

    public boolean isEnableCamera() {
        return enableCamera;
    }

    public void setEnableCamera(boolean enableCamera) {
        this.enableCamera = enableCamera;
    }

    public Orientation getOrientation() {
        return orientation;
    }

    public void setOrientation(Orientation orientation) {
        this.orientation = orientation;
    }

    public String getProviderAuthorities() {
        return providerAuthorities;
    }

    public void setProviderAuthorities(String providerAuthorities) {
        this.providerAuthorities = providerAuthorities;
    }

    public void setCameraDrawable(int drawable) {
        this.cameraDrawable = drawable;
    }

    public int getCameraDrawable() {
        return cameraDrawable;
    }
}