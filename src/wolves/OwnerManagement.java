package wolves;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.bukkit.ChatColor;
import org.bukkit.DyeColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.AnimalTamer;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Wolf;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;
import org.bukkit.event.entity.EntityTameEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.Dye;

public class OwnerManagement implements Listener, CommandExecutor {
	
	private BetterWolves main;
	HashMap<String, DyeColor> claimedColors;
	
	public OwnerManagement(BetterWolves better) {
		main = better;
		
		claimedColors = new HashMap<String, DyeColor>();
		if (main.config.contains("color")) {
			Set<String> names = main.config.getConfigurationSection("color").getKeys(false);
			for (String name : names) {
				claimedColors.put(name, DyeColor.valueOf(main.config.getString("color." + name)));
			}
		}
		else {
			main.config.createSection("color");
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
		
		String path = "color." + player.getName();
		if(cmd.getName().equalsIgnoreCase("wolfcolor")) {
			if (args.length != 1) {
				player.sendMessage(ChatColor.RED + "Please enter a color to claim.");
				return false;
			}
			
			DyeColor color;
			try {
				color = DyeColor.valueOf(args[0].toUpperCase());
			} 
			catch (IllegalArgumentException e) {
				player.sendMessage(ChatColor.RED + "Please enter a valid color to claim.");
				return false;
			}
			
			if (color == DyeColor.RED) {
				player.sendMessage(ChatColor.RED + "RED cannot be claimed as a color.");
				return false;
			}
			
			Iterator<String> it = claimedColors.keySet().iterator();
			while (it.hasNext()) {
				if (claimedColors.get(it.next()) == color) {
					player.sendMessage(ChatColor.RED + "Please enter another color, this one is already in use.");
					return false;
				}
			}
			
			
			main.config.set(path, color.name());
			main.saveConfig();
			claimedColors.put(player.getName(), color);
			player.sendMessage(ChatColor.GREEN + "Your color has been succesfully saved.");
			return true;
			
			
		}
		else if(cmd.getName().equalsIgnoreCase("wolfrefresh")) {
			List<LivingEntity> list = player.getWorld().getLivingEntities();
			for (LivingEntity entity : list) {
				if(entity instanceof Wolf) {
					Wolf wolf = (Wolf) entity;
					if (wolf.isTamed()) {
						DyeColor color = DyeColor.RED;
						AnimalTamer p = wolf.getOwner();
						if (claimedColors.containsKey(p.getName()))
							color = claimedColors.get(p.getName());
						wolf.setCollarColor(color);
					}
				}
			}
			player.sendMessage(ChatColor.GREEN + "Colors updated.");
			return true;
			
		}
		else if(cmd.getName().equalsIgnoreCase("allcolors")) {
			player.sendMessage(ChatColor.GREEN + "Colors in use:");
			for (String name : claimedColors.keySet()) {
				player.sendMessage(ChatColor.DARK_PURPLE + claimedColors.get(name).toString());
			}
			
			return true;
		}
		else {
			return false;
		}
	}
	
	@EventHandler
	public void playerInteract(PlayerInteractEntityEvent event) {
		ItemStack item = event.getPlayer().getItemInHand();
		if (item.getData() instanceof Dye) {
			Entity en = event.getRightClicked();
			if (en.getType() == EntityType.WOLF) {
				Wolf wolf = (Wolf) en;
				if (wolf.getOwner().getName().equalsIgnoreCase(event.getPlayer().getName())) {
					Dye dye = (Dye) item.getData();
					DyeColor color = dye.getColor();
					String name = findKey(claimedColors, color);
					if (name != null) {
						Player player = main.getServer().getPlayer(name);    // need to eventually find a new way to do this
						wolf.setOwner(player);
					} 
					else {
						event.setCancelled(true);
					}
				}
				else {
					event.setCancelled(true);
				}
			}
		}
	}
	
	@EventHandler
	public void playerTameWolf(EntityTameEvent event) {
		if (event.getEntityType() == EntityType.WOLF) {
			if (claimedColors.containsKey(event.getOwner().getName())) {
				Wolf wolf = (Wolf) event.getEntity();
				wolf.setCollarColor(claimedColors.get(event.getOwner().getName()));
			}
		}
	}
	
	@EventHandler
	public void WolfBreed(CreatureSpawnEvent event) {
		if (event.getEntityType() == EntityType.WOLF && event.getSpawnReason() == SpawnReason.BREEDING) {
			Wolf wolf = (Wolf) event.getEntity();
			String name = wolf.getOwner().getName();
			if (claimedColors.containsKey(name))
				wolf.setCollarColor(claimedColors.get(name));
		}
	}
	
	// only works if HashMap is a 1:1 relation
	<T, R> T findKey(HashMap<T, R> hash, R value) {
		Set<T> set = hash.keySet();
		for (T t : set) {
			if (hash.get(t).equals(value)) {
				return t;
			}
		}
		return null;
	}
	
}
