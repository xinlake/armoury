package armoury;

import android.content.Context;

import androidx.annotation.NonNull;

/**
 * Armoury library global config
 * @author XinLake
 * @version 2021.02
 */
public class Core {
    private static Core core;

    private String logPath;

    // TODO log file path
    public static void init(@NonNull Context appContext, String logPath) {
        core = new Core();

        //File docDir = appContext.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS);
        core.logPath = logPath;
    }

    public static String getLogPath() {
        return core.logPath;
    }
}
