package net.poweredbyhate.blockstatus.listeners;

import net.citizensnpcs.api.CitizensAPI;
import net.poweredbyhate.blockstatus.BlockStatus;
import net.poweredbyhate.blockstatus.StatusBlock;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.material.Sign;

import java.util.Arrays;

/**
 * Created by Lax on 12/20/2017.
 */
public class SignChangeListener implements Listener {

    @EventHandler
    public void onChange(SignChangeEvent ev) {
        if (ev.getBlock().getType() == Material.SIGN_POST) {
            return;
        }
        if ((ev.getLine(0).equalsIgnoreCase("[blockstatus]") || ev.getLine(0).equalsIgnoreCase("[statusblock]")) && ev.getLine(1) != null) {
            if (!ev.getPlayer().hasPermission("statusblock.create") || !ev.getPlayer().hasPermission("blockstatus.create")) {
                return;
            }
            Sign s = (Sign) ev.getBlock().getState().getData();
            BlockStatus.getInstance().debug("Detected statusblock by " + ev.getPlayer().getName());
            BlockStatus.getInstance().debug("Lines " + Arrays.toString(ev.getLines()));
            StatusBlock statusBlock = new StatusBlock(ev.getBlock().getRelative(s.getAttachedFace()), ev.getLine(1), CitizensAPI.getNPCRegistry().createNPC(EntityType.PLAYER, ev.getLine(1)));
            BlockStatus.statusblocks.add(statusBlock);
            statusBlock.getNpc().spawn(statusBlock.getBlock().getLocation().add(0.5,1,0.5));
            statusBlock.getNpc().faceLocation(ev.getPlayer().getLocation());
            statusBlock.getNpc().setProtected(true);
            BlockStatus.getInstance().saveBlocks();
        }
    }

}
