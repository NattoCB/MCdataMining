package com.snowk.mcdm.command;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import com.snowk.mcdm.command.framework.CommandFramework;
import com.snowk.mcdm.command.doAnalysis;
import com.snowk.mcdm.command.doDBSCAN;

public class CommandHandler extends CommandFramework {
    public CommandHandler(String label) {
        super(label);
    }

	@Override
	public void execute(CommandSender sender, String label, String[] args) {
		
		if (args.length == 0) {
        	sender.sendMessage("§b====== MCdataMining v1.0 By:Bear ======");
            sender.sendMessage("§b/dm reload §f-§e 重载插件 §b快捷指令§6/dm r");
            sender.sendMessage("§b/dm analysis §f-§e 查看服务器统计分析系统使用帮助 §b快捷指令§6/dm a");
            sender.sendMessage("§b/dm dbscan §f-§e 查看服务器聚类系统使用帮助 §b快捷指令§6/dm d");
            sender.sendMessage("§b/dm t <Player> §f-§e 把指定玩家传送到身边");
            
        } else if (args[0].equalsIgnoreCase("reload") || args[0].equalsIgnoreCase("r")) {
	        if (sender.isOp() || sender.hasPermission("snowkfix.reload")) {
	        	sender.sendMessage("§b[Snowk]§e当前版本无需加载config，若一定需要重载请于yum实现热加载");
	        } 

        } else if (args[0].equalsIgnoreCase("t") && args[1] != null) {
        	if (sender.isOp() && sender instanceof Player) {
        		Location loc = ((Player) sender).getLocation();
        		Player target = Bukkit.getPlayerExact(args[1]);
        		sender.sendMessage("§6正在传送...");
        		target.teleport(loc);
        	} else {sender.sendMessage("权限不足，请确定您是否为op，且不可于控制台执行。");}
        	
        } else if (args[0].equalsIgnoreCase("analysis") || args[0].equalsIgnoreCase("a")) {
        	sender.sendMessage("§b====== MCdataMining v1.0 By:Bear ======");
            sender.sendMessage("§b/dm analysis mob §f-§e 计数统计所有世界的所有生物 §b快捷指令§6/dm a m");
            sender.sendMessage("§b/dm analysis drop §f-§e 计数统计所有世界的所有掉落物 §b快捷指令§6/dm a d");
            sender.sendMessage("§b/dm analysis tile §f-§e 计数统计所有世界当前加载的所有tiles方块 §b快捷指令§6/dm a t");
            if (args[1].equalsIgnoreCase("mob") || args[1].equalsIgnoreCase("m")) {
            	doAnalysis.run(sender,"mob",true);
            } else if (args[1].equalsIgnoreCase("drop") || args[1].equalsIgnoreCase("d")) {
            	doAnalysis.run(sender,"drop",true);
            } else if (args[1].equalsIgnoreCase("tiles") || args[1].equalsIgnoreCase("t")) {
            	doAnalysis.run(sender,"tiles",true);
            }
            
        } else if (args[0].equalsIgnoreCase("dbscan") || args[0].equalsIgnoreCase("d")) {
        	sender.sendMessage("§b====== MCdataMining v1.0 By:Bear ======");
            sender.sendMessage("§b/dm dbscan mob <world>  §f-§e 聚类生物并寻找密集中心 §b快捷指令§6/dm d m <w>");
            sender.sendMessage("§b/dm dbscan drop <world>  §f-§e 聚类掉落物并寻找密集中心 §b快捷指令§6/dm d d <w>");
            sender.sendMessage("§b/dm dbscan tile <world>  §f-§e 聚类tiles并寻找密集中心 §b快捷指令§6/dm d t <w>");
            if (args[1].equalsIgnoreCase("mob") || args[1].equalsIgnoreCase("m")) {
            	doDBSCAN.run(sender,args[2],doAnalysis.run(sender,"mob",false),"生物类");
            } else if (args[1].equalsIgnoreCase("drop") || args[1].equalsIgnoreCase("d")) {
            	doDBSCAN.run(sender,args[2],doAnalysis.run(sender,"drop",false),"掉落物");
            } else if (args[1].equalsIgnoreCase("tiles") || args[1].equalsIgnoreCase("t")) {
            	doDBSCAN.run(sender,args[2],doAnalysis.run(sender,"tiles",false),"tiles");
            }
        }
	}
	
}
