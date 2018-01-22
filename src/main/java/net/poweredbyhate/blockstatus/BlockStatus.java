package net.poweredbyhate.blockstatus;

import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.npc.NPC;
import net.poweredbyhate.blockstatus.listeners.PlayerListener;
import net.poweredbyhate.blockstatus.listeners.SignChangeListener;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;

public final class BlockStatus extends JavaPlugin {

    private static BlockStatus instance;
    public static ArrayList<StatusBlock> statusblocks = new ArrayList<>();
    public boolean debug = false;

    @Override
    public void onEnable() {
        instance = this;
        checkDependencies();
        saveDefaultConfig();
        Bukkit.getPluginManager().registerEvents(new SignChangeListener(), this);
        Bukkit.getPluginManager().registerEvents(new PlayerListener(), this);
        debug = getConfig().getBoolean("debug");
        new BukkitRunnable() {
            @Override
            public void run() {
                readyBlocks();
            }
        }.runTaskLater(this, 100); //make pretty l8r
    }

    @Override
    public void onDisable() {
        saveBlocks();
    }

    public void checkDependencies() {
        debug("Checking for dependencies");
        if (getServer().getPluginManager().getPlugin("Citizens") == null) {
            getLogger().log(Level.SEVERE, "Citizens plugin not found!");
            getServer().getPluginManager().disablePlugin(this);
        }
        debug("Found citizens plugin.");
    }

    public static BlockStatus getInstance() {
        return instance;
    }

    public void readyBlocks() {
        debug("Readying blocks");
        if (getDataConfig().getConfigurationSection("data") == null) {
            return;
        }
        for (String block : getDataConfig().getConfigurationSection("data").getKeys(false)) {
            int npcid = getDataConfig().getInt("data."+block+".npcid");
            String name = getDataConfig().getString("data."+block+".name");

            debug("Readying Block: " + block);
            debug("Block name: " + name);
            debug("Block npcid: " + npcid);

            NPC npc = CitizensAPI.getNPCRegistry().getById(npcid);
            StatusBlock leStatus = new StatusBlock(stringToLoc(block).getBlock(), name, npc);
            if (!npc.isSpawned()) {
                npc.spawn(npc.getStoredLocation());
            }
            statusblocks.add(leStatus);
        }
    }

    public void saveBlocks() {
        debug("Saving blocks");
        FileConfiguration config = getDataConfig();
        for (StatusBlock block : statusblocks) {
            String loc = locToString(block.getBlock().getLocation());
            if (config.getConfigurationSection("data."+loc) == null) {
                debug("Saving block: " + loc);
                config.set("data."+loc+".name", block.getPlayer());
                config.set("data."+loc+".npcid", block.getNpc().getId());
            }
        }
        try {
            config.save(new File(getDataFolder(), "data.yml"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public FileConfiguration getDataConfig() {
        File leFile = new File(getDataFolder(), "data.yml");
        if (!leFile.exists()) {
            saveResource("data.yml", false);
        }
        try {
            FileConfiguration ledata = new YamlConfiguration();
            ledata.load(leFile);
            return ledata;
        } catch (InvalidConfigurationException | IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public String locToString(Location loc) {
        String world = loc.getWorld().getName();
        int x = loc.getBlockX();
        int y = loc.getBlockY();
        int z = loc.getBlockZ();
        return world+"~"+x+"~"+y+"~"+z;
    }

    public Location stringToLoc(String s) {
        String[] loc = s.split("~");
        return new Location(Bukkit.getWorld(loc[0]),Double.valueOf(loc[1]),Double.valueOf(loc[2]),Double.valueOf(loc[3]));
    }

    public void debug(Object o) {
        if (debug) {
            getLogger().log(Level.INFO, o.toString());
        }
    }
}