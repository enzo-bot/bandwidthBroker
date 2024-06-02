package bandwidthBroker;
import org.apache.commons.net.util.SubnetUtils;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class NetworkPrefixExtractor {

    public static String getNetworkPrefix(String ipAddress, int prefixLength) throws UnknownHostException {
        InetAddress address = InetAddress.getByName(ipAddress);
        
        if (address.getAddress().length == 4) { // IPv4
            return getIPv4NetworkPrefix(ipAddress, prefixLength);
        } else if (address.getAddress().length == 16) { // IPv6
            return getIPv6NetworkPrefix(ipAddress, prefixLength);
        } else {
            throw new IllegalArgumentException("Invalid IP address");
        }
    }

    private static String getIPv4NetworkPrefix(String ipAddress, int prefixLength) {
        SubnetUtils subnetUtils = new SubnetUtils(ipAddress + "/" + prefixLength);
        return subnetUtils.getInfo().getNetworkAddress() + "/" + prefixLength;
    }

    private static String getIPv6NetworkPrefix(String ipAddress, int prefixLength) throws UnknownHostException {
        InetAddress address = InetAddress.getByName(ipAddress);
        byte[] addressBytes = address.getAddress();

        int fullBytes = prefixLength / 8;
        int remainingBits = prefixLength % 8;

        for (int i = fullBytes; i < addressBytes.length; i++) {
            if (i == fullBytes) {
                addressBytes[i] &= (byte) (0xFF << (8 - remainingBits));
            } else {
                addressBytes[i] = 0;
            }
        }

        InetAddress networkAddress = InetAddress.getByAddress(addressBytes);
        return networkAddress.getHostAddress() + "/" + prefixLength;
    }

    public static void main(String[] args) {
        try {
            String ipv4Address = "192.168.1.10";
            int ipv4PrefixLength = 24;
            System.out.println("IPv4 Network Prefix: " + getNetworkPrefix(ipv4Address, ipv4PrefixLength));

            String ipv6Address = "2001:db8::1";
            int ipv6PrefixLength = 64;
            System.out.println("IPv6 Network Prefix: " + getNetworkPrefix(ipv6Address, ipv6PrefixLength));
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
    }
}

