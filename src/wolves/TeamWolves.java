package wolves;

import java.util.HashMap;
import java.util.Set;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Wolf;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;

public class TeamWolves implements Listener, CommandExecutor {
	
	private BetterWolves main;
	HashMap<String, String> teams; /*  <name, team>  */
	
	public TeamWolves(BetterWolves main) {
		this.main = main;
		
		teams = new HashMap<String, String>();
		if (main.config.contains("team")) {
			Set<String> names = main.config.getConfigurationSection("team").getKeys(false);
			for (String name : names) {
				teams.put(name, main.config.getString("team." + name));
			}
		}
		else {
			main.config.createSection("team");
			main.saveConfig();
		}
	}
	
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String string, String[] args) {
		
		Player player = null;
		if (sender instanceof Player) {
			player = (Player)sender;
		}
		else {
			return false;
		}
		
		
		if(cmd.getName().equalsIgnoreCase("jointeam")) {
			if (args.length != 1) {
				player.sendMessage(ChatColor.RED + "Please enter a team to join.");
				return false;
			}
			
			String path = "team." + player.getName();
			main.config.set(path, args[0].toLowerCase());
			main.saveConfig();
			teams.put(player.getName(), args[0].toLowerCase());
			player.sendMessage(ChatColor.GREEN + "You have successfully been added to team " + ChatColor.DARK_PURPLE + args[0] + ChatColor.GREEN + ".");
			return true;
		}
		else if (cmd.getName().equalsIgnoreCase("getteam")) {
			for (String name : teams.keySet()) {
				if (name.equalsIgnoreCase(player.getName())) {
					String team = teams.get(name);
					player.sendMessage(ChatColor.GREEN + "You are currently in team " + ChatColor.DARK_PURPLE + team + ChatColor.GREEN + ".");
					return true;
				}
			}
			player.sendMessage(ChatColor.RED + "You are not in a team yet.");
			return true;
		}
		else if (cmd.getName().equalsIgnoreCase("allteams")) {
			for (String name : teams.keySet()) {
				String team = teams.get(name);
				player.sendMessage(ChatColor.GREEN + name + ": " + ChatColor.DARK_PURPLE + team);
			}
			
			return true;
		}
		else {
			return false;
		}
	}
	
	
	@EventHandler
	public void playerInteract(PlayerInteractEntityEvent event) {
		Entity en = event.getRightClicked();
		if (en.getType() == EntityType.WOLF) {
			Wolf wolf = (Wolf) en;
			if (!wolf.getOwner().getName().equals(event.getPlayer().getName()) && isTeamWolf(wolf, event.getPlayer())) {
				wolf.setOwner(event.getPlayer()); // may need to manually set collar color...
			}
		}
		
	}
	
	boolean isTeamWolf(Wolf wolf, Player player) {
		String wolfTeam = teams.get(wolf.getOwner().getName());
		String playerTeam = teams.get(player.getName());
		if (playerTeam != null)
			return playerTeam.equals(wolfTeam);
		else
			return false;
	}
	
	
}
