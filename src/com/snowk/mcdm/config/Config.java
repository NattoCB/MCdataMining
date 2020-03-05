package com.snowk.mcdm.config;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.List;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import com.snowk.mcdm.mcdm;

public class Config {
	
	public static void loadConfig(String ymlName) {
		String path = mcdm.snowkPlugin.getDataFolder().getAbsolutePath()+File.separator+ymlName;
		File file = new File(path);
	    if (!file.exists()) {
	    	mcdm.snowkPlugin.saveDefaultConfig();
	    }
		try {
			FileInputStream fileinputstream = new FileInputStream(file);
			YamlConfiguration config = new YamlConfiguration();
			config.load(new InputStreamReader(fileinputstream, Charset.forName("UTF-8")));			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InvalidConfigurationException e) {
			e.printStackTrace();
		}
	}
	
	public static double getDouble(String label) {
		return mcdm.snowkPlugin.getConfig().getDouble(label);
	}
	public static String getString(String label) {
		return mcdm.snowkPlugin.getConfig().getString(label).replaceAll("&", "¡ì");
	}
	public static boolean getBoolean(String label) {
		return mcdm.snowkPlugin.getConfig().getBoolean(label);
	}
	public static List<String> getStringList(String label) {
		return mcdm.snowkPlugin.getConfig().getStringList(label);
	}
	public static List<Integer> getIntegerList(String label) {
		return mcdm.snowkPlugin.getConfig().getIntegerList(label);
	}
}
