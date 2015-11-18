package com.mime.minefront;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Properties;

public class Configuration {
	
	Properties properties = new Properties();
	
	public void saveConfiguration(String key, int value){
		String path = "res/settings/config.xml";
		try {
			File file = new File(path);
			boolean exist = file.exists();
			if (!exist) {
				file.createNewFile();
			}
			OutputStream write = new FileOutputStream(path);
			properties.setProperty(key, Integer.toString(value));
			properties.storeToXML(write, "Resolution");
		} catch (Exception e) {
		}
	}
	
	public void loadConfiguration(String path, String Key) {
		try {
			InputStream read = new FileInputStream(path);
			properties.loadFromXML(read);
		} catch (FileNotFoundException e) {
			
		} catch (IOException e1) {
			
		}
	}
	
}
