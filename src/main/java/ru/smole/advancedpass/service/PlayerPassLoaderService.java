package ru.smole.advancedpass.service;

import lombok.SneakyThrows;
import lombok.val;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;
import ru.smole.advancedpass.AdvancedPass;
import ru.smole.advancedpass.pass.holder.PassHolder;
import ru.smole.advancedpass.pass.holder.entity.SimplePassHolder;

import java.util.Map;
import java.util.UUID;

public class PlayerPassLoaderService implements Listener {

    private final Map<UUID, PassHolder> passHolders;

    public PlayerPassLoaderService(Map<UUID, PassHolder> passHolders) {
        this.passHolders = passHolders;

        Bukkit.getPluginManager().registerEvents(this, JavaPlugin.getProvidingPlugin(PlayerPassLoaderService.class));
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        val player = event.getPlayer();
        val uniqueId = player.getUniqueId();

        val foundHolder = AdvancedPass.getPassHolderDao().findByUniqueId(uniqueId);

        val holder = foundHolder.orElseGet(() -> (SimplePassHolder) SimplePassHolder
                .builder()
                .uniqueId(uniqueId)
                .premium(false)
                .build());

        passHolders.put(uniqueId, holder);
    }

    @EventHandler
    @SneakyThrows
    public void onQuit(PlayerQuitEvent event) {
        val player = event.getPlayer();
        val uniqueId = player.getUniqueId();

        val holder = (SimplePassHolder) passHolders.remove(uniqueId);

        AdvancedPass.getPassHolderDao().createOrUpdate(holder);
    }
}
