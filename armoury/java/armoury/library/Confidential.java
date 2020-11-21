package armoury.library;

import android.util.Base64;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.nio.charset.StandardCharsets;


/**
 * Security
 * @author XinLake
 * @version 2020.08
 */
public class Confidential {
    private static final String Tag = Confidential.class.getSimpleName();

    public static String base64Encode(@NonNull String data, int flag) {
        byte[] bytes = Base64.encode(data.getBytes(), flag);
        return new String(bytes);
    }

    @Nullable
    public static String base64Decode(@NonNull String data, int flag) {
        try {
            byte[] bytes = Base64.decode(data.getBytes(), flag);
            return new String(bytes);
        } catch (Exception ignored) {
        }

        return null;
    }

    @Nullable
    public static String base64DecodeX(String data) {
        if (data == null) {
            return null;
        }

        int[] flags = new int[]{
            Base64.DEFAULT,
            Base64.NO_PADDING,
            Base64.NO_WRAP,
            Base64.CRLF,
            Base64.URL_SAFE,
            Base64.NO_CLOSE};

        for (int flag : flags) {
            try {
                byte[] bytes = Base64.decode(data, flag);
                return new String(bytes, StandardCharsets.UTF_8);
            } catch (Exception ignored) {
            }
        }

        return null;
    }
}
