package com.snowk.mcdm.command;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.CommandSender;

import com.snowk.mcdm.command.algorithm.DBSCAN;

public class doDBSCAN {

	public static void run (CommandSender sender, String worldInput, List<Location> locationList, String task) {
		
		ArrayList<double[]> arrayXYZ = new ArrayList<>();
		
		for (Location sample : locationList) {
			if (sample.getWorld().getName().toString().equals(worldInput)) {
				int ATRIBUTE_NUMBER = 3; //x y z
				
				String [] xyz = (sample.getX() + "," + sample.getY() + "," + sample.getZ()).split(",");
				double[] array = new double[ATRIBUTE_NUMBER];
	            for (int i = 0; i < ATRIBUTE_NUMBER; i++) {
	                array[i] = Double.parseDouble(xyz[i]);
	            }
	            arrayXYZ.add(array);
			}
		}
		if (locationList.size() == 0) {
			sender.sendMessage("§b[Snowk]§e未搜索到结果，请确认世界是否已开启");
		} else if (arrayXYZ.size() == 0) {
			sender.sendMessage("§b[Snowk]§e未找到世界，请确认世界名是否与下列匹配");
			List<World> worlds = Bukkit.getWorlds();
			for (World w : worlds) {
				sender.sendMessage("==== " + w.getName());
			}
		} else {
			// start DBSCAN
			// arrayXYZ is input of DBSCAN
			DBSCAN.main(sender, arrayXYZ, locationList, task);
		}
	}
	
}
