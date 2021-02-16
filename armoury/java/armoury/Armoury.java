package armoury;

import android.content.Context;

import androidx.annotation.NonNull;

import java.io.File;

/**
 * Armoury library global config
 * @author XinLake
 * @version 2021.02
 */
public class Armoury {
    private static Armoury armoury;

    private File logFile;

    public static void init(@NonNull Context appContext) {
        armoury = new Armoury();

        //File docDir = appContext.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS);
        armoury.logFile = new File(appContext.getFilesDir(),
            appContext.getApplicationInfo().packageName + ".log");
    }

    public static void init(@NonNull Context appContext, @NonNull File logFile) {
        armoury = new Armoury();
        armoury.logFile = logFile;
    }

    public static File getLogFile() {
        return armoury.logFile;
    }
}
