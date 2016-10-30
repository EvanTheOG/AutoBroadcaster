package me.evanog.draconbroadcast;

import java.io.File; 
import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
 
public class DraconBroadcast extends JavaPlugin {

	private LinkedList<Announcement> announcements;
	private File cfile;
	private FileConfiguration config;
	private int interval = 0;
	

	public void onEnable() {
		announcements = new LinkedList<Announcement>();
		this.initializeConfig();
		this.loadAnnouncements();
		/*
		for (Announcement a : announcements) {
			System.out.print(a.getID());
			for (String s : a.getTextTranslated()) {
				System.out.print(s);
			}
		}
		*/
		interval = config.getInt("Interval");
		this.startBroadcast();
	}
	public void onDisable() {
		announcements = null;
		config = null;
		cfile = null;
	}
	private void initializeConfig() {
		this.cfile = new File(this.getDataFolder(),"config.yml");
		this.config = YamlConfiguration.loadConfiguration(cfile);
		config.addDefault("Interval", Integer.valueOf(10));
		config.addDefault("Sound.Enabled", Boolean.valueOf(true));
		config.addDefault("Sound.Sound_Name", "ORB_PICKUP");
		config.addDefault("Announcements.1.Message", new String[]{"&9&l&m----------","&eThe author of this plugin is EvanOG :)","&9&l&m----------"});
		config.addDefault("Announcements.2.Message", new String[]{"&9&l&m----------","&eCoded with love <3","&9&l&m----------"});
		config.options().copyDefaults(true);
		 getConfig().options().copyDefaults(false);
		try  {
			config.save(cfile);
		}catch(IOException e) {
			System.out.print("Config.yml could not be saved!");
		}
	}
	private void loadAnnouncements() {
		for (String s : config.getConfigurationSection("Announcements").getKeys(false)) {
			announcements.add(new Announcement(Integer.valueOf(s), config.getStringList("Announcements." + s + ".Message")));
		}
	}
	
	
	private void startBroadcast() {
		new BukkitRunnable() {
			int pos = 0;
			int max = announcements.size() -1;
			Announcement a;
			List<String> text;
			@Override
			public void run() {
				if (pos == max) {
					pos = 0;
					a = announcements.getLast();
					text = a.getText();
					for (Player p : Bukkit.getOnlinePlayers()) {
						if (config.getBoolean("Sound.Enabled")) {
							p.playSound(p.getLocation(), Sound.valueOf(config.getString("Sound.Sound_Name")), 2f, 2f);
						}
						for (int i = 0; i < text.size(); i++) {
							p.sendMessage(ChatColor.translateAlternateColorCodes('&', text.get(i)));
						}
					}
				}else {
					a = announcements.get(pos);
					text = a.getText();
					for (Player p : Bukkit.getOnlinePlayers()) {
						if (config.getBoolean("Sound.Enabled")) {
							p.playSound(p.getLocation(), Sound.valueOf(config.getString("Sound.Sound_Name")), 2f, 2f);
						}
						for (int i = 0; i < text.size(); i++) {
							p.sendMessage(ChatColor.translateAlternateColorCodes('&', text.get(i)));
						}
					}
					pos++;
				}
				
			}
			
		}.runTaskTimer(this, 0, 20*interval);
	}
	
	
	
	class Announcement {
		
		private int id;
		private List<String> text;
		
		public Announcement(int id,List<String>list) {
			this.id = id;
			this.text = list;
		}
		public List<String> getText() {
			return text;
		}
		public int getID() {
			return id;
		}
		public String[] getTextTranslated() {
			String[] temp = new String[]{};
			for (int i = 0; i < text.size(); i++) {
				for (String s : text) {
					temp[i] = ChatColor.translateAlternateColorCodes('&',s);
				}
			}
			return temp;
		}
		
		
	}
}
