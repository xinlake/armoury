package armoury.library;

import android.content.Context;
import android.content.res.AssetManager;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;


/**
 * @author XinLake
 * @version 2020.08
 */
public final class XinFile {
    private static final String Tag = XinFile.class.getSimpleName();

    /**
     * No additional permissions are required for the calling app to read or write files under the returned path.
     * @param assetFile The name of the asset to open. This name can be hierarchical.
     * @return Return a path to file which may be read in common way.
     */
    public static @Nullable
    String cacheAssetFile(@NonNull Context context, @NonNull String assetFile, boolean overwrite) {
        AssetManager assetManager = context.getAssets();
        try {
            File cacheFile = new File(context.getFilesDir(), assetFile);
            if (!overwrite && cacheFile.exists() && cacheFile.length() > 0) {
                return cacheFile.getAbsolutePath();
            }

            // Read data from assets.
            InputStream inputStream = assetManager.open(assetFile);
            BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream);

            byte[] data = new byte[bufferedInputStream.available()];
            int read = bufferedInputStream.read(data);
            bufferedInputStream.close();
            inputStream.close();
            if (read < 1) {
                return null;
            }

            // Create copy file in storage.
            FileOutputStream fileOutputStream = new FileOutputStream(cacheFile);
            fileOutputStream.write(data);
            fileOutputStream.close();

            // Return a path to file which may be read in common way.
            return cacheFile.getAbsolutePath();
        } catch (Exception ignored) {
            Log.e(Tag + "-cacheAssetFile", "Cache asset file failed");
        }

        return null;
    }

    /**
     * @param textFile UTF-8 string lines
     * @return the string lines split by LF
     */
    public static @Nullable
    String[] readAssetText(@NonNull Context context, @NonNull String textFile) {
        AssetManager assetManager = context.getAssets();
        try {
            // Read data from assets.
            InputStream inputStream = assetManager.open(textFile);
            BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream);

            byte[] data = new byte[bufferedInputStream.available()];
            int read = bufferedInputStream.read(data);
            bufferedInputStream.close();
            inputStream.close();
            if (read < 1) {
                return null;
            }

            String lines = new String(data, StandardCharsets.UTF_8);
            return lines.split("\n");
        } catch (Exception ignored) {
            Log.e(Tag + "-readAssetText", "Load asset text error");
        }

        return null;
    }
}
