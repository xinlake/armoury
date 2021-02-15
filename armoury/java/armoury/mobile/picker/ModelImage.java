package armoury.mobile.picker;

import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import armoury.common.XinText;


/**
 * @author XinLake
 * @version 2020.08
 */
public class ModelImage {
    private final Uri uri;
    private final String title;

    private final long size;
    private final long width;
    private final long height;

    private final String sizeText;
    private final String resolutionText;

    private ModelImage(@NonNull Uri uri, @NonNull String title, long size, long width, long height) {
        this.uri = uri;
        this.title = title;

        this.size = size;
        this.width = width;
        this.height = height;

        sizeText = XinText.formatSize1024(size);
        resolutionText = String.format(Locale.ENGLISH, "%dx%d", width, height);
    }

    @Override
    public boolean equals(Object object) {
        try {
            ModelImage image = (ModelImage) object;
            return uri.equals(image.uri);
        } catch (Exception ignored) {
            return false;
        }
    }

    public Uri getUri() {
        return uri;
    }

    public String getTitle() {
        return title;
    }

    public long getSize() {
        return size;
    }

    public long getWidth() {
        return width;
    }

    public long getHeight() {
        return height;
    }

    public String getSizeText() {
        return sizeText;
    }

    public String getResolutionText() {
        return resolutionText;
    }

    @NonNull
    public static List<ModelImage> loadLocalDB(Context appContext) {
        final String[] projection = new String[]{
            MediaStore.Images.Media._ID,
            MediaStore.Images.Media.TITLE,

            MediaStore.Images.Media.SIZE,
            MediaStore.Images.Media.WIDTH,
            MediaStore.Images.Media.HEIGHT,
        };

        List<ModelImage> imageList = new ArrayList<>();

        // query videos
        Cursor cursor = appContext.getContentResolver().query(
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI, projection,
            null, null, null);
        if (cursor != null) {
            int idColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media._ID);
            int titleColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.TITLE);
            int sizeColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.SIZE);
            int widthColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.WIDTH);
            int heightColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.HEIGHT);

            while (cursor.moveToNext()) {
                long id = cursor.getLong(idColumn);
                String title = cursor.getString(titleColumn);

                long size = cursor.getLong(sizeColumn);
                long width = cursor.getLong(widthColumn);
                long height = cursor.getLong(heightColumn);

                Uri contentUri = ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, id);
                ModelImage video = new ModelImage(contentUri, title, size, width, height);
                imageList.add(video);
            }
            cursor.close();
        }

        return imageList;
    }
}
