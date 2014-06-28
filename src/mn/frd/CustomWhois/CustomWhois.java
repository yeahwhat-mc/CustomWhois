package mn.frd.CustomWhois;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.v1_7_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import net.milkbowl.vault.economy.Economy;
import net.minecraft.server.v1_7_R3.EntityPlayer;

public class CustomWhois extends JavaPlugin implements Listener {
    public static Economy econ = null;

	@Override
	public void onEnable(){
		  if (!setupEconomy() ) {
            getLogger().severe(String.format("[%s] - Disabled due to no Vault dependency found!", getDescription().getName()));
            getServer().getPluginManager().disablePlugin(this);
            return;
        }
		
		// Register events
		getServer().getPluginManager().registerEvents(this, this);
		// Log loading
		getLogger().info("Loaded " + getDescription().getName() + " v" + getDescription().getVersion());		
	}

	@Override
	public void onDisable(){
		// Log disabling
		getLogger().info("Disabled " + getDescription().getName() + " v" + getDescription().getVersion());	
	}
	
	@SuppressWarnings("deprecation")
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
		if (cmd.getName().equalsIgnoreCase("whois")){
			// Define prefix
			String prefix = ChatColor.RED + "[Whois]" + ChatColor.RESET;
			
			// Check arguments
			if(args.length == 0) {
				// Abort, no arguments
				sender.sendMessage(prefix + " " +  "No argument given!");
				return true;
			} else if(args.length == 1) {
				// Proceed, one argument found
				Player p = getServer().getPlayer(args[0]);
				
				/* 
				 * Gather informations
				 */
				
				// Real IGN
				String pign = p.getName();
				
				// Nickname
				String pnick = p.getDisplayName();

				// Health
				Double phealth = p.getHealth();
				Double pmaxhealth = p.getMaxHealth();

				// Hunger
				Integer pmaxhunger = 20;
				Float phunger = pmaxhunger - p.getExhaustion();
				Float psaturation = p.getSaturation();
				
				// Exp
				Integer pexp = p.getTotalExperience();
				Integer pexplevel = p.getLevel();
				
				// Location
				String plocworld = p.getLocation().getWorld().getName();
				Double plocx = p.getLocation().getX();
				Double plocy = p.getLocation().getY();
				Double plocz = p.getLocation().getZ();
				String plocation = plocworld + " (" + plocx + " " + plocy + " " + plocz + ")";
				
				// Money
				Double pmoney = econ.getBalance(p);
				
				// Gamemode
				// God mode
				// OP
				// Fly mode
				// AFK
				// Jailed
				// Muted
				
				/*
				 * Return actual whois message
				 */
				
				sender.sendMessage(ChatColor.GOLD + "====== Whois: " + ChatColor.RED + pign + ChatColor.GOLD + " ======");
				sender.sendMessage(ChatColor.GOLD + "- Nick: " + ChatColor.RESET + pnick);
				sender.sendMessage(ChatColor.GOLD + "- Health: " + ChatColor.RESET + phealth.intValue() + "/" + pmaxhealth.intValue());
				sender.sendMessage(ChatColor.GOLD + "- Hunger: " + ChatColor.RESET + phunger.intValue() + "/" + pmaxhunger.intValue() + " (+" + psaturation.intValue() + ")");
				sender.sendMessage(ChatColor.GOLD + "- Exp: " + ChatColor.RESET + pexp + " (Level " + pexplevel + ")");
				sender.sendMessage(ChatColor.GOLD + "- Location: " + ChatColor.RESET + plocation);
				sender.sendMessage(ChatColor.GOLD + "- Money: " + ChatColor.RESET + pmoney.doubleValue() + "$");
				return true;
			} else {
				// Abort, too many arguments
				sender.sendMessage(prefix + " " +  "Too many arguments given!");
				return true;
			}
		}
		return false;
	}
	
	private boolean setupEconomy() {
        if (getServer().getPluginManager().getPlugin("Vault") == null) {
            return false;
        }
        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            return false;
        }
        econ = rsp.getProvider();
        return econ != null;
    }
}
