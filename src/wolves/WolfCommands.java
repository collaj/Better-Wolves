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
			while (iter8r.hasNext()) {
				LivingEntity entity = iter8r.next();
				if(entity instanceof Wolf) {
					Wolf thisWolf = (Wolf)entity;
					if (thisWolf.isTamed() && thisWolf.getOwner().getName().equalsIgnoreCase(player.getName())) {
						thisWolf.setSitting(true);
					}
				}
			}
			player.sendMessage(ChatColor.GREEN + "Your wolves are now sitting.");
			return true;
		} 
		
		else if (cmd.getName().equalsIgnoreCase("stand")) {
			Iterator<LivingEntity> iter8r = player.getWorld().getLivingEntities().iterator();
			while (iter8r.hasNext()) {
				LivingEntity entity = iter8r.next();
				if(entity instanceof Wolf) {
					Wolf thisWolf = (Wolf)entity;
					if (thisWolf.isTamed() && thisWolf.getOwner().getName().equalsIgnoreCase(player.getName())) {
						Location here = player.getLocation();
                        here.add(0, 2, 0);
                        thisWolf.teleport(here);
						thisWolf.setSitting(false);
					}
				}
			}
			player.sendMessage(ChatColor.GREEN + "Your wolves are now ready for war.");
			return true;
		}
		else if(cmd.getName().equalsIgnoreCase("wolves")) {
			int numOfWolves = 0;
			Iterator<LivingEntity> iter8r = player.getWorld().getLivingEntities().iterator();
			 while (iter8r.hasNext()) {
				LivingEntity entity = iter8r.next();
				if(entity instanceof Wolf) {
					Wolf thisWolf = (Wolf)entity;
					if (thisWolf.isTamed() && thisWolf.getOwner().getName().equalsIgnoreCase(player.getName())) {
						numOfWolves++;
					}
				}
			}
			player.sendMessage(ChatColor.GREEN + "You have " + numOfWolves + " in your pack.");
			return true;
			
		} 
		
		else if (cmd.getName().equalsIgnoreCase("safestand")) {
			
			Iterator<LivingEntity> iter8r = player.getWorld().getLivingEntities().iterator();
			int acc = 2;
			while (iter8r.hasNext()) {
				LivingEntity entity = iter8r.next();
				if (entity instanceof Wolf) {
					Wolf thisWolf = (Wolf) entity;
					if (thisWolf.isTamed() && thisWolf.getOwner().getName().equalsIgnoreCase(player.getName())) {
						if (acc <= 0) {
							Location here = player.getLocation();
	                        here.add(0, 2, 0);
	                        thisWolf.teleport(here);
							thisWolf.setSitting(false);
						} 
						else {
							acc--;
						}
					}
				}
			}
			player.sendMessage(ChatColor.GREEN + "Two puppies were left at home.");
			return true;
			
		}
		
		else {
			return false;
		}
	}
}
