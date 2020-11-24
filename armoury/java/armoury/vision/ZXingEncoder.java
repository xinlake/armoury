package armoury.vision;

import android.graphics.Bitmap;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.common.BitMatrix;

import java.util.EnumMap;
import java.util.Map;


/**
 * This class does the work of decoding the user's request and extracting all the data
 * to be encoded in a barcode.
 */
public final class ZXingEncoder {
    private static final String Tag = ZXingEncoder.class.getSimpleName();
    private static final int WHITE = 0xFFFFFFFF;
    private static final int BLACK = 0xFF000000;

    public static Bitmap EncodeAsBitmap(@NonNull String contents, int zxingFormat, int dimension) {
        Map<EncodeHintType, Object> hints = null;
        String encoding = guessAppropriateEncoding(contents);
        if (encoding != null) {
            hints = new EnumMap<>(EncodeHintType.class);
            hints.put(EncodeHintType.CHARACTER_SET, encoding);
        }

        BitMatrix result;
        try {
            result = new MultiFormatWriter().encode(contents, ZXingFormat.getFormat(zxingFormat), dimension, dimension, hints);
        } catch (Exception exception) {
            // Unsupported format
            // com.google.zxing.WriterException – if contents cannot be encoded legally in a format
            Log.e(Tag, "EncodeAsBitmap", exception);
            return null;
        }

        int width = result.getWidth();
        int height = result.getHeight();
        int[] pixels = new int[width * height];
        for (int y = 0; y < height; y++) {
            int offset = y * width;
            for (int x = 0; x < width; x++) {
                pixels[offset + x] = result.get(x, y) ? BLACK : WHITE;
            }
        }

        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        bitmap.setPixels(pixels, 0, width, 0, 0, width, height);
        return bitmap;
    }

    private static String guessAppropriateEncoding(CharSequence contents) {
        // Very crude at the moment
        for (int i = 0; i < contents.length(); i++) {
            if (contents.charAt(i) > 0xFF) {
                return "UTF-8";
            }
        }

        return null;
    }
}
