package armoury;

import android.content.Context;
import android.os.Environment;

import java.io.File;

public class Core {
    private static Core core;

    public String logPath;

    public static void init(Context appContext, String logName) {
        core = new Core();

        File docDir = appContext.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS);
        core.logPath = docDir + "/" + logName;
    }

    public static String getLogPath() {
        return core.logPath;
    }
}
