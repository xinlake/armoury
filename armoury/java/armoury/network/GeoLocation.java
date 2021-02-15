package armoury.network;

import androidx.annotation.NonNull;

import com.google.gson.Gson;

import java.util.Objects;

import armoury.common.XinText;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * @author XinLake
 * @version 2020.12
 */
public class GeoLocation {
    private final OkHttpClient client = new OkHttpClient();

    /**
     * Fetch from multiple website
     */
    public String fromWebsite(@NonNull String ip) {
        String location = fromWebsiteA(ip);
        if (location == null) {
            location = fromWebsiteB(ip);
        }

        return location;
    }

    public String fromWebsiteA(@NonNull String ip) {
        try {
            final HttpUrl httpUrl = new HttpUrl.Builder()
                .scheme("http")
                .host("ipwhois.app")
                .addPathSegment("json")
                .addPathSegment(ip)
                .addQueryParameter("objects", "country,region,city")
                .build();

            final Request request = new Request.Builder()
                .get()
                .url(httpUrl)
                .build();

            Response response = client.newCall(request).execute();
            String responseBody = Objects.requireNonNull(response.body()).string();
            response.close();

            Gson gson = new Gson();
            final ModelIpWhoIs ipWhoIs = gson.fromJson(responseBody, ModelIpWhoIs.class);
            return XinText.connectWords(", ", ipWhoIs.country, ipWhoIs.region, ipWhoIs.city);
        } catch (Exception exception) {
            exception.printStackTrace();
        }

        return null;
    }

    public String fromWebsiteB(@NonNull String ip) {
        try {
            final HttpUrl httpUrl = new HttpUrl.Builder()
                .scheme("http")
                .host("ipinfo.io")
                .addPathSegment(ip)
                .build();

            final Request request = new Request.Builder()
                .get()
                .url(httpUrl)
                .build();

            Response response = client.newCall(request).execute();
            String responseBody = Objects.requireNonNull(response.body()).string();
            response.close();

            Gson gson = new Gson();
            final ModelIpInfo ipInfo = gson.fromJson(responseBody, ModelIpInfo.class);
            return XinText.connectWords(", ", ipInfo.country, ipInfo.region, ipInfo.city);
        } catch (Exception exception) {
            exception.printStackTrace();
        }

        return null;
    }
}
