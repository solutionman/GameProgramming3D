package com.mime.minefront;

import java.io.File;

public class Configuration {
	
	public void saveConfiguration(String key, int value){
		try {
			File file = new File("res/settings/config.xml");
			boolean exist = file.exists();
			if (!exist) {
				file.createNewFile();
			}
		} catch (Exception e) {
		}
	}
	
}
