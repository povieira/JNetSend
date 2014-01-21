package com.jnetsend.service;


public class NetSendService {

	public static void start() {
		process(true);
	}

	public static void stop() {
		process(false);
	}

	private static void process(boolean start) {
		Process process;
		try {
			if (start) {
				System.out.print("Starting...");
				process = Runtime.getRuntime().exec("net start messenger");
			} else {
				System.out.print("Stopping...");
				process = Runtime.getRuntime().exec("net stop messenger");
			}
			process.waitFor();
			if (process.exitValue() == 0) {
				System.out.println(" OK.");
			} else {
				System.out.println(" ERROR.");
			}

		} catch (Exception e) {
			throw new RuntimeException("Error trying to stop/start NET SEND service.");
		}
	}
}