package wolves;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public final class BetterWolves extends JavaPlugin {
	
	FileConfiguration config;
	
	@Override
	public void onEnable(){
		config = getConfig();
		
		getCommand("sit").setExecutor(new WolfCommands());
		getCommand("stand").setExecutor(new WolfCommands());
		getCommand("wolves").setExecutor(new WolfCommands());
		getCommand("safestand").setExecutor(new WolfCommands());
		
		OwnerManagement om = new OwnerManagement(this);
		getCommand("wolfcolor").setExecutor(om);
		getCommand("wolfrefresh").setExecutor(om);
		getCommand("allcolors").setExecutor(om);
		getServer().getPluginManager().registerEvents(om, this);
	}
	
	@Override
	public void onDisable(){
		
	}

}
