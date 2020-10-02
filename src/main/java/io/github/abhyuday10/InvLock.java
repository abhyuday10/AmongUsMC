package io.github.abhyuday10;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;
import java.util.Set;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.wrappers.Pair;
import com.comphenix.protocol.wrappers.EnumWrappers.ItemSlot;

/**
 * InvLock
 */
public class InvLock extends JavaPlugin {
    private ProtocolManager protocolManager;

    @Override
    public void onEnable() {
        getLogger().info("Enabled InvLock v1.0");
        getServer().getPluginManager().registerEvents(new Listeners(), this);
        protocolManager = ProtocolLibrary.getProtocolManager();
        protocolManager.addPacketListener(
                new PacketAdapter(this, ListenerPriority.NORMAL, PacketType.Play.Server.ENTITY_EQUIPMENT) {
                    @Override
                    public void onPacketSending(PacketEvent event) {
                        Player player = event.getPlayer();
                        Set<String> playerTags = player.getScoreboardTags();
                        if (playerTags.contains("au_inGame")) {
                            List<Pair<ItemSlot, ItemStack>> slotStacks = event.getPacket().getSlotStackPairLists()
                                    .read(0);
                            for (Pair<ItemSlot, ItemStack> pair : slotStacks) {
                                ItemSlot slot = pair.getFirst();
                                ItemStack stack = pair.getSecond();
                                if (slot == ItemSlot.MAINHAND || slot == ItemSlot.OFFHAND) {
                                    // event.setCancelled(true);
                                    stack.setAmount(0);
                                }
                            }

                        }
                    };
                });
    }

    @Override
    public void onDisable() {
        getLogger().info("InvLock has been disabled :(");
    }
}