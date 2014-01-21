package com.jnetsend.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.Map;

public final class Utils {

	public static Map importFile(String fileName) throws IOException {
		return importFile(new File(fileName));
	}

	public static Map importFile(File file) throws IOException {

		if (file.length() == 0L)
			return null;
		HashMap hm = new HashMap();
		BufferedReader fileBuffer = new BufferedReader(new FileReader(file));
		String line;
		while ((line = fileBuffer.readLine()) != null) {
			int separatorIndex = line.indexOf('#');
			if (separatorIndex == -1) {
				return null;
			}
			String key = line.substring(0, separatorIndex);
			String value = line.substring(separatorIndex + 1);
			hm.put(key, value);
		}
		fileBuffer.close();
		return hm;
	}

	public static void exportFile(Map data, String fileName) throws IOException {
		exportFile(data, new File(fileName));
	}

	public static void exportFile(Map data, File file) throws IOException {
		int length = data.keySet().toArray().length;
		Object[] logins = new Object[length];
		logins = data.keySet().toArray();
		BufferedWriter fileBuffer = new BufferedWriter(new FileWriter(file));
		for (int i = 0; i < length; i++) {
			fileBuffer.write(logins[i].toString() + "#"
					+ data.get(logins[i]).toString());
			fileBuffer.newLine();
		}
		fileBuffer.close();
	}
	
	public static ApplicationConfig loadConfiguration() {
		ApplicationConfig config = null;
		try {
			ObjectInputStream in = new ObjectInputStream(new FileInputStream(
					Constants.CONFIG_FILE_NAME));
			config = (ApplicationConfig) in.readObject();
			in.close();
		} catch (FileNotFoundException e) { //if file doesn't exist, create a new one
			try {
				config = new ApplicationConfig();
				ObjectOutputStream out = new ObjectOutputStream(
						new FileOutputStream(Constants.CONFIG_FILE_NAME));
				out.writeObject(config);
				out.close();
			} catch (IOException ex) {
				throw new RuntimeException("Config file cannot be created. " + ex.getMessage());
			}
		} catch (Exception e) {
			throw new RuntimeException("Unexpected error. " + e);
		}

		return config;
	}

	public static void saveConfiguration(ApplicationConfig config) throws IOException {
		ObjectOutputStream out = new ObjectOutputStream(
				new FileOutputStream(Constants.CONFIG_FILE_NAME));
		out.writeObject(config);
		out.close();
	}
}
