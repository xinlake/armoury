package armoury.network;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Enumeration;

public class IpAddress {
    /**
     *
     */
    public static String getIpAddress() {
        try {
            Enumeration<NetworkInterface> interfaceEnumeration = NetworkInterface.getNetworkInterfaces();
            while (interfaceEnumeration.hasMoreElements()) {
                NetworkInterface networkInterface = interfaceEnumeration.nextElement();

                Enumeration<InetAddress> addressEnumeration = networkInterface.getInetAddresses();
                while (addressEnumeration.hasMoreElements()) {
                    InetAddress inetAddress = addressEnumeration.nextElement();
                    if (!inetAddress.isLoopbackAddress()) {
                        if (inetAddress instanceof Inet4Address) {

                        }
                    }
                }
            }
        } catch (Exception ignored) {
        } // for now eat exceptions

        return null;
    }
}
