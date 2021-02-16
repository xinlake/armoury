package armoury.ready.network;

import android.app.Activity;
import android.app.AlertDialog;
import android.view.View;

import java.util.List;

import armoury.network.WifiInterface;

public class WifiRunner implements View.OnClickListener {
    private final Activity activity;

    public WifiRunner(Activity activity) {
        this.activity = activity;
    }

    @Override
    public void onClick(View view) {
        List<WifiInterface> wifiInterfaces = WifiInterface.getIpAddress();
        StringBuilder stringBuilder = new StringBuilder();
        if (wifiInterfaces.size() > 0) {
            for (WifiInterface wifiInterface : wifiInterfaces) {
                stringBuilder.append(wifiInterface.name).append("\r\n");

                for (String ip4 : wifiInterface.ip4List) {
                    stringBuilder.append(ip4).append("\r\n");
                }

                for (String ip6 : wifiInterface.ip6List) {
                    stringBuilder.append(ip6).append("\r\n");
                }
            }
        } else {
            stringBuilder.append("Wifi is not connected");
        }

        new AlertDialog.Builder(activity)
            .setTitle("Wifi address")
            .setMessage(stringBuilder)
            .setPositiveButton("OK", (dialog, which) -> dialog.dismiss())
            .show();
    }
}
