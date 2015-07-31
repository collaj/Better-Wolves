package wolves;

import org.bukkit.plugin.java.JavaPlugin;

public final class BetterWolves extends JavaPlugin {

	@Override
	public void onEnable(){
		getCommand("sit").setExecutor(new WolfCommands());
		getCommand("stand").setExecutor(new WolfCommands());
		getCommand("wolves").setExecutor(new WolfCommands());
		getCommand("safestand").setExecutor(new WolfCommands());
	}
	
	@Override
	public void onDisable(){
		
	}

}
