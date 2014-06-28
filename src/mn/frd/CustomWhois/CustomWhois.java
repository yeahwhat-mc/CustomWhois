package mn.frd.CustomWhois;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import com.earth2me.essentials.Essentials;
import com.earth2me.essentials.User;

import net.milkbowl.vault.economy.Economy;

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
				if (p == null) {
					sender.sendMessage(prefix + " " +  "Couldn't find online player " + ChatColor.GRAY + args[0] + ChatColor.RESET + "!");
					return true;
				}
				
				// Check if command sender matching the argument 
				if (sender.getName() == p.getName()){
					// If sender has not permission "customwhois.whois.self"
					if (!sender.hasPermission("customwhois.whois.self")){
						sender.sendMessage(prefix + " " +  "You dont have the permission: " + ChatColor.GRAY + "customwhois.whois.self");
						return true;
					}	
				} else {
					// If sender has not permission "customwhois.whois.others"
					if (!sender.hasPermission("customwhois.whois.others")){
						sender.sendMessage(prefix + " " +  "You dont have the permission: " + ChatColor.GRAY + "customwhois.whois.others");
						return true;
					}
				}
			
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
				
				// IP
				String pip = p.getAddress().getHostName();
				
				// Gamemode
				String pgamemode = p.getGameMode().toString();
				
				// God mode
				Essentials essentials = (Essentials)Bukkit.getServer().getPluginManager().getPlugin("Essentials");
				User user = essentials.getUser(p);
				String pgodmode = String.valueOf(user.isGodModeEnabled());
				
				// OP
				String pop = String.valueOf(p.isOp());
				
				// Fly mode
				String pfly = String.valueOf(p.isFlying());
				
				// AFK
				String pafk = String.valueOf(user.isAfk());
				
				// Jailed
				String pjailed = String.valueOf(user.isJailed());

				// Muted
				String pmuted = String.valueOf(user.isMuted());
				
				/*
				 * Return actual whois message
				 */
				
				sender.sendMessage(ChatColor.GOLD + "====== Whois: " + ChatColor.RED + pign + ChatColor.GOLD + " ======");
				
				if ((sender.hasPermission("customwhois.custom.nick")) && !(sender.hasPermission("customwhois.deny."+ plocworld +".nick"))){
					sender.sendMessage(ChatColor.GOLD + "- Nick: " + ChatColor.RESET + pnick);					
				}
				if ((sender.hasPermission("customwhois.custom.health")) && !(sender.hasPermission("customwhois.deny."+ plocworld +".health"))){
					sender.sendMessage(ChatColor.GOLD + "- Health: " + ChatColor.RESET + phealth.intValue() + "/" + pmaxhealth.intValue());
				}
				if ((sender.hasPermission("customwhois.custom.hunger")) && !(sender.hasPermission("customwhois.deny."+ plocworld +".hunger"))){
					sender.sendMessage(ChatColor.GOLD + "- Hunger: " + ChatColor.RESET + phunger.intValue() + "/" + pmaxhunger.intValue() + " (+" + psaturation.intValue() + ")");
				}
				if ((sender.hasPermission("customwhois.custom.exp")) && !(sender.hasPermission("customwhois.deny."+ plocworld +".exp"))){
					sender.sendMessage(ChatColor.GOLD + "- Exp: " + ChatColor.RESET + pexp + " (Level " + pexplevel + ")");
				}
				if ((sender.hasPermission("customwhois.custom.location")) && !(sender.hasPermission("customwhois.deny."+ plocworld +".location"))){
					sender.sendMessage(ChatColor.GOLD + "- Location: " + ChatColor.RESET + plocation);
				}
				if ((sender.hasPermission("customwhois.custom.money")) && !(sender.hasPermission("customwhois.deny."+ plocworld +".money"))){
					sender.sendMessage(ChatColor.GOLD + "- Money: " + ChatColor.RESET + pmoney.doubleValue() + "$");
				}
				if ((sender.hasPermission("customwhois.custom.ip")) && !(sender.hasPermission("customwhois.deny."+ plocworld +".ip"))){
					sender.sendMessage(ChatColor.GOLD + "- IP: " + ChatColor.RESET + pip);
				}
				if ((sender.hasPermission("customwhois.custom.gamemode")) && !(sender.hasPermission("customwhois.deny."+ plocworld +".gamemode"))){
					sender.sendMessage(ChatColor.GOLD + "- Gamemode: " + ChatColor.RESET + pgamemode.toLowerCase());
				}
				if ((sender.hasPermission("customwhois.custom.godmode")) && !(sender.hasPermission("customwhois.deny."+ plocworld +".godmode"))){
					sender.sendMessage(ChatColor.GOLD + "- God mode: " + ChatColor.RESET + colorizeBool(pgodmode));
				}
				if ((sender.hasPermission("customwhois.custom.op")) && !(sender.hasPermission("customwhois.deny."+ plocworld +".op"))){
					sender.sendMessage(ChatColor.GOLD + "- OP: " + ChatColor.RESET + colorizeBool(pop));
				}
				if ((sender.hasPermission("customwhois.custom.flying")) && !(sender.hasPermission("customwhois.deny."+ plocworld +".flying"))){
					sender.sendMessage(ChatColor.GOLD + "- Flying: " + ChatColor.RESET + colorizeBool(pfly));
				}
				if ((sender.hasPermission("customwhois.custom.afk")) && !(sender.hasPermission("customwhois.deny."+ plocworld +".afk"))){
					sender.sendMessage(ChatColor.GOLD + "- AFK: " + ChatColor.RESET + colorizeBool(pafk));
				}
				if ((sender.hasPermission("customwhois.custom.jailed")) && !(sender.hasPermission("customwhois.deny."+ plocworld +".jailed"))){
					sender.sendMessage(ChatColor.GOLD + "- Jailed: " + ChatColor.RESET + colorizeBool(pjailed));
				}				
				if ((sender.hasPermission("customwhois.custom.muted")) && !(sender.hasPermission("customwhois.deny."+ plocworld +".muted"))){
					sender.sendMessage(ChatColor.GOLD + "- Muted: " + ChatColor.RESET + colorizeBool(pmuted));
				}
				
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
	
	public String colorizeBool(String bool) { 
		if (bool == "true"){
			String stringbool = ChatColor.GREEN + "true" + ChatColor.RESET;
			return stringbool;
		} else {
			String stringbool = ChatColor.RED + "false" + ChatColor.RESET;
			return stringbool;
		}
	}
}
