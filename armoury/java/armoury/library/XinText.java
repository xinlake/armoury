package armoury.library;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

/**
 * @author XinLake
 * @version 2020.09
 */
public class XinText {
    public static String connectWords(@NonNull String delimiter, String... words) {
        StringBuilder stringBuilder = new StringBuilder();
        for (String word : words) {
            if (word != null && !word.isEmpty()) {
                stringBuilder.append(word).append(delimiter);
            }
        }

        return stringBuilder.substring(0, stringBuilder.length() - delimiter.length());
    }

    /**
     * format the integer to an ip address string
     * @return return the string or null if error
     */
    @Nullable
    public static String formatIp(int ip) {
        int b1, b2, b3, b4;
        b1 = (ip) & 0x000000ff;
        b2 = (ip >> 8) & 0x000000ff;
        b3 = (ip >> 16) & 0x000000ff;
        b4 = (ip >> 24) & 0x000000ff;

        String ipAddress;
        try {
            ipAddress = String.format(Locale.ENGLISH, "%d.%d.%d.%d", b1, b2, b3, b4);
        } catch (Exception ignored) {
            ipAddress = null;
        }
        return ipAddress;
    }

    // times ---------------------------------------------------------------------------------------

    /**
     * @param pattern such as "yyyy-MM-dd HH:mm:ss"
     * @return the string of current time
     */
    public static String formatCurrentTime(String pattern) {
        // https://docs.oracle.com/javase/8/docs/api/java/time/format/DateTimeFormatter.html
        return LocalDateTime.now().format(DateTimeFormatter.ofPattern(pattern));
    }

    public static String formatTime(long milliseconds) {
        if (milliseconds < 1000 * 2) {
            return String.format(Locale.US, "%.2f", (float) milliseconds / 1000);
        }

        // > 10 seconds
        long seconds = milliseconds / 1000;
        if (seconds < 3600) {
            int m = (int) (seconds / 60);
            int s = (int) (seconds % 60);
            return String.format(Locale.US, "%02d:%02d", m, s);
        } else {
            int h = (int) (seconds / 3600);
            int m = (int) (seconds % 3600 / 60);
            int s = (int) (seconds % 60);
            return String.format(Locale.US, "%d:%02d:%02d", h, m, s);
        }
    }

    /**
     * readable size measure by 1024
     * MI6: 4ms
     */
    public static String formatSize1024(long bytes) {
        if (bytes <= 0) {
            return "0";
        }

        final String[] units = new String[]{"B", "KB", "MB", "GB", "TB"};
        int digitGroups = (int) (Math.log10(bytes) / Math.log10(1024));
        return new DecimalFormat("#,##0.##").format(bytes / Math.pow(1024, digitGroups)) + " " + units[digitGroups];
    }

    /**
     * readable size measure by 1000
     * MI6: 17ms,25ms
     */
    public static String formatSize1000(long bytes) {
        if (bytes <= 0) {
            return "0";
        }

        final String[] units = new String[]{"B", "KB", "MB", "GB", "TB"};
        int digitGroups = (int) (Math.log10(bytes) / Math.log10(1000));
        return new DecimalFormat("#,##0.##").format(bytes / Math.pow(1000, digitGroups)) + " " + units[digitGroups];
    }
}
