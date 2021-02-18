package armoury.common;

import androidx.annotation.NonNull;

import java.io.FileOutputStream;
import java.io.IOException;

import armoury.Armoury;


/**
 * @author XinLake
 * @version 2021.02
 */
public class Logger {
    private static final String timeFormat = "yyyy-MM-dd HH:mm:ss";

    public static void write(@NonNull String head, String message) {
        try (FileOutputStream fileOutputStream = new FileOutputStream(Armoury.getLogFile(), true)) {
            writeHead(fileOutputStream, head);

            if (message != null) {
                byte[] messageLine = (message + "\r\n").getBytes();
                fileOutputStream.write(messageLine);
            }

            fileOutputStream.flush();
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    public static void write(@NonNull String head, Throwable throwable) {
        try (FileOutputStream fileOutputStream = new FileOutputStream(Armoury.getLogFile(), true)) {
            writeHead(fileOutputStream, head);

            if (throwable != null) {
                String message = throwable.getLocalizedMessage();
                if (message != null) {
                    fileOutputStream.write((message + "\r\n").getBytes());
                }
            }

            fileOutputStream.flush();
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    private static void writeHead(FileOutputStream fileOutputStream, String head) throws IOException {
        byte[] headLine = (XinText.formatCurrentTime(timeFormat) + ". " + head + "\r\n").getBytes();
        fileOutputStream.write(headLine);
    }
}
