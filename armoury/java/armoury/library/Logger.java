package armoury.library;

import androidx.annotation.NonNull;

import java.io.FileOutputStream;

import armoury.Core;

public class Logger {
    private static final String timeFormat = "yyyy-MM-dd HH:mm:ss";

    public static void write(@NonNull String head, String message) {
        try (FileOutputStream fileOutputStream = new FileOutputStream(Core.getLogPath(), true)) {
            byte[] headLine = (XinText.formatCurrentTime(timeFormat) + ". " + head + "\r\n").getBytes();
            fileOutputStream.write(headLine);

            if (message != null) {
                byte[] messageLine = (message + "\r\n").getBytes();
                fileOutputStream.write(messageLine);
            }

            fileOutputStream.flush();
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }
}
