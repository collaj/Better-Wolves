package wolves;

import java.util.Iterator;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Wolf;

public class WolfCommands implements CommandExecutor {

	public boolean onCommand(CommandSender sender, Command cmd, String string, String[] args) {
		
		Player player = null;
		if (sender instanceof Player) {
			player = (Player)sender;
		}
		else {
			return false;
		}
		
		if(cmd.getName().equalsIgnoreCase("sit")) {
			Iterator<LivingEntity> iter8r = player.getWorld().getLivingEntities().iterator();
			do {
				if (!iter8r.hasNext()) {
					break;
				}
				LivingEntity entity = (LivingEntity)iter8r.next();
				if(entity instanceof Wolf) {
					Wolf thisWolf = (Wolf)entity;
					if (thisWolf.isTamed() && thisWolf.getOwner().getName().equalsIgnoreCase(player.getName())) {
						thisWolf.setSitting(true);
					}
				}
			} while (true);
			player.sendMessage((new StringBuilder()).append(ChatColor.GREEN).append("Your wolves are now on their asses.").toString());
			return true;
		} 
		
		else if (cmd.getName().equalsIgnoreCase("stand")) {
			Iterator<LivingEntity> iter8r = player.getWorld().getLivingEntities().iterator();
			do {
				if (!iter8r.hasNext()) {
					break;
				}
				LivingEntity entity = (LivingEntity)iter8r.next();
				if(entity instanceof Wolf) {
					Wolf thisWolf = (Wolf)entity;
					if (thisWolf.isTamed() && thisWolf.getOwner().getName().equalsIgnoreCase(player.getName())) {
						Location here = player.getLocation();
                        int twoUp = 2 + here.getBlockY();
                        here.setY(twoUp);
                        thisWolf.teleport(here);
						thisWolf.setSitting(false);
					}
				}
			} while (true);
			player.sendMessage((new StringBuilder()).append(ChatColor.GREEN).append("Your wolves are now ready for war.").toString());
			return true;
		}
		else if(cmd.getName().equalsIgnoreCase("wolves")) {
			int numOfWolves = 0;
			Iterator<LivingEntity> iter8r = player.getWorld().getLivingEntities().iterator();
			do {
				if (!iter8r.hasNext()) {
					break;
				}
				LivingEntity entity = (LivingEntity)iter8r.next();
				if(entity instanceof Wolf) {
					Wolf thisWolf = (Wolf)entity;
					if (thisWolf.isTamed() && thisWolf.getOwner().getName().equalsIgnoreCase(player.getName())) {
						numOfWolves++;
					}
				}
			} while (true);
			player.sendMessage((new StringBuilder()).append(ChatColor.GREEN).append("You have ").append(numOfWolves).append(" in your pack.").toString());
			return true;
			
		} 
		
		else if (cmd.getName().equalsIgnoreCase("safestand")) {
			
			Iterator<LivingEntity> iter8r = player.getWorld().getLivingEntities().iterator();
			int acc = 2;
			do {
				if (!iter8r.hasNext()) {
					break;
				}
					LivingEntity entity = (LivingEntity)iter8r.next();
					if(entity instanceof Wolf) {
						Wolf thisWolf = (Wolf)entity;
						if (thisWolf.isTamed() && thisWolf.getOwner().getName().equalsIgnoreCase(player.getName())) {
							if (acc <= 0) {
								Location here = player.getLocation();
								int twoUp = 2 + here.getBlockY();
								here.setY(twoUp);
								thisWolf.teleport(here);
								thisWolf.setSitting(false);
								
							}
							else {
								acc--;
							}
						}
					}
					
			} while (true);
			player.sendMessage((new StringBuilder()).append(ChatColor.GREEN).append("All but two of your wolves are ready for war.").toString());
			return true;
			
		}
		
		else {
			return false;
		}
	}
}
