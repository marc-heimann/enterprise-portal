package com.swisslog.ep.misc;

import java.net.InetSocketAddress;

public class Helper {
	public static InetSocketAddress getSocketAddress() {
		return new InetSocketAddress("0.0.0.0", 81);
	}
	
	public static boolean inCluster = true;
}
