package mn.frd.CustomWhois;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

public class CustomWhois extends JavaPlugin implements Listener {
	@Override
	public void onEnable(){
		// Register events
		this.getServer().getPluginManager().registerEvents(this, this);
		// Log loading
		this.getLogger().info("Loaded " + this.getDescription().getName() + " v" + this.getDescription().getVersion());	
	}

	@Override
	public void onDisable(){
		// Log disabling
		this.getLogger().info("Disabled " + this.getDescription().getName() + " v" + this.getDescription().getVersion());	
	}
	
	
	//@SuppressWarnings("deprecation")
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
		if (cmd.getName().equalsIgnoreCase("whois")){
			sender.sendMessage("Result");
			return true;
		}
		return false;
	}
}
