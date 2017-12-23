package net.poweredbyhate.blockstatus.listeners;

import net.poweredbyhate.blockstatus.BlockStatus;
import net.poweredbyhate.blockstatus.StatusBlock;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

/**
 * Created by Lax on 12/21/2017.
 */
public class PlayerListener implements Listener {

    @EventHandler
    public void onJoin(PlayerJoinEvent ev) {
        BlockStatus.getInstance().debug("PJE called");
        for (StatusBlock s : BlockStatus.statusblocks) {
            if (s.getPlayer().equalsIgnoreCase(ev.getPlayer().getName())) {
                BlockStatus.getInstance().debug("JE|Changing Block Status of: " + ev.getPlayer().getName());
                s.setStatus(true);
            }
        }
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent ev) {
        BlockStatus.getInstance().debug("PQE called");
        for (StatusBlock s : BlockStatus.statusblocks) {
            if (s.getPlayer().equalsIgnoreCase(ev.getPlayer().getName())) {
                BlockStatus.getInstance().debug("QE|Changing Block Status of: " + ev.getPlayer().getName());
                s.setStatus(false);
            }
        }
    }

    @EventHandler
    public void onCommand(PlayerCommandPreprocessEvent ev) { ///shhh nobody has to know
        if (ev.getMessage().startsWith("/sblock debug") && (ev.getPlayer().hasPermission("statusblock.create") || ev.getPlayer().hasPermission("blockstatus.create"))) {
            BlockStatus.getInstance().debug = true;
        }
    }
}
