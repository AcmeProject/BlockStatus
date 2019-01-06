package net.poweredbyhate.blockstatus;

import net.citizensnpcs.api.npc.NPC;
import org.bukkit.Material;
import org.bukkit.block.Block;

/**
 * Created by Lax on 12/20/2017.
 */
public class StatusBlock {

    private Block block;
    private NPC npc;
    private String player;
    private Boolean status = false;

    public StatusBlock(Block block, String player, NPC npc) {
        this.block = block;
        this.player = player;
        this.npc = npc;
    }

    public Block getBlock() {
        return this.block;
    }

    public String getPlayer() {
        return this.player;
    }

    public NPC getNpc() {
        return this.npc;
    }

    public Boolean getStatus() {
        return this.status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
        changeStatus();
    }

    public void destroy() {
        if (npc != null) {
            npc.destroy();
        }
    }

    private void changeStatus() {
        if (status) {
            this.block.setType(Material.valueOf(BlockStatus.getInstance().getConfig().getString("onblock")));
        } else {
            this.block.setType(Material.valueOf(BlockStatus.getInstance().getConfig().getString("offblock")));
        }
    }

}
