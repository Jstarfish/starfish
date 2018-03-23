package priv.starfish.common.util;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;

public class NetUtil {
	public static void printNetInterfaces() {
		try {
			Enumeration<NetworkInterface> allNetInterfaces = NetworkInterface.getNetworkInterfaces();
			while (allNetInterfaces.hasMoreElements()) {
				NetworkInterface netInterface = (NetworkInterface) allNetInterfaces.nextElement();
				String realName = netInterface.getName();
				String dispayName = netInterface.getDisplayName();
				Enumeration<InetAddress> addresses = netInterface.getInetAddresses();
				while (addresses.hasMoreElements()) {
					InetAddress ip = (InetAddress) addresses.nextElement();
					if (ip != null && ip instanceof Inet4Address) {
						System.out.println(realName + " : " + ip.getHostAddress() + " [" + dispayName + " ]");
					}
				}
			}
		} catch (java.net.SocketException e) {
			e.printStackTrace();
		}
	}

	public static List<String> getHostIps() {
		try {
			List<String> hostIps = new ArrayList<String>();
			Enumeration<NetworkInterface> allNetInterfaces = NetworkInterface.getNetworkInterfaces();
			while (allNetInterfaces.hasMoreElements()) {
				NetworkInterface netInterface = (NetworkInterface) allNetInterfaces.nextElement();
				Enumeration<InetAddress> addresses = netInterface.getInetAddresses();
				while (addresses.hasMoreElements()) {
					InetAddress ip = (InetAddress) addresses.nextElement();
					if (ip != null && ip instanceof Inet4Address) {
						hostIps.add(ip.getHostAddress());
					}
				}
			}
			return hostIps;
		} catch (java.net.SocketException e) {
			e.printStackTrace();
			return null;
		}
	}

	private static List<String> HostIps = null;
	private static ReentrantLock HostIpsLock = new ReentrantLock();

	public static void cacheHostIps() {
		if (HostIps == null) {
			HostIpsLock.lock();
			if (HostIps == null) {
				HostIps = getHostIps();
			}
			HostIpsLock.unlock();
		}
	}

	public static boolean hasHostIp(String... ips) {
		cacheHostIps();
		for (String ip : ips) {
			if (HostIps.contains(ip)) {
				return true;
			}
		}
		return false;
	}

	public static void main(String[] args) {
		if (args.length == 0) {
			System.out.println("Function : Check whether host has given ips");
			System.out.println("Usage : java NetUtil ip1 ip2 ...");
			System.exit(0);
		}
		for (String ip : args) {
			if (hasHostIp(ip)) {
				System.out.println(ip + " - Yes");
			} else {
				System.out.println(ip + " - No");
			}
		}
	}
}
