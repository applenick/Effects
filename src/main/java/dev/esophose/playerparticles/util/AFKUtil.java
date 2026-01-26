package dev.esophose.playerparticles.util;


import com.google.common.collect.Maps;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.Plugin;

/**
 * A self-contained AFK utility that tracks player activity.
 */
public class AFKUtil implements Listener {

    private final Plugin plugin;
    private final Map<UUID, Instant> lastActive;
    private final Duration timeout;
    private final String exemptPermission;

    public AFKUtil(Plugin plugin) {
        this(plugin, Duration.ofMinutes(10), null);
    }

    public AFKUtil(Plugin plugin, Duration timeout, String exemptPermission) {
        this.plugin = plugin;
        this.timeout = timeout;
        this.exemptPermission = exemptPermission;
        this.lastActive = Maps.newHashMap();

        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    public Duration getTimeout() {
        return timeout;
    }

    public String getExemptPermission() {
        return exemptPermission;
    }

    public Duration getTimeSinceLastActivity(Player player) {
        if (player == null) return Duration.ZERO;

        Instant lastActivity = lastActive.get(player.getUniqueId());
        if (lastActivity == null) return Duration.ZERO;

        return Duration.between(lastActivity, Instant.now());
    }

    public boolean isAFK(Player player) {
        if (player == null) return false;

        // Check exemption permission
        if (exemptPermission != null && player.hasPermission(exemptPermission)) {
            return false;
        }

        Instant lastActivity = lastActive.get(player.getUniqueId());
        if (lastActivity == null) return false;

        Duration timeSince = Duration.between(lastActivity, Instant.now());
        return timeSince.compareTo(timeout) >= 0;
    }

    public List<Player> getAFKPlayers() {
        List<Player> afkPlayers = new ArrayList<>();

        for (Player player : plugin.getServer().getOnlinePlayers()) {
            if (isAFK(player)) {
                afkPlayers.add(player);
            }
        }

        return afkPlayers;
    }

    public void logActivity(Player player) {
        if (player != null) {
            this.lastActive.put(player.getUniqueId(), Instant.now());
        }
    }

    public void clearAll() {
        this.lastActive.clear();
    }

    public void removePlayer(Player player) {
        if (player != null) {
            this.lastActive.remove(player.getUniqueId());
        }
    }

    public void shutdown() {
        HandlerList.unregisterAll(this);
        this.lastActive.clear();
    }

    // Event Handlers

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onMove(PlayerMoveEvent event) {
        // Only log activity if the player actually moved their view
        boolean viewChanged =
                event.getFrom().getYaw() != event.getTo().getYaw()
                        || event.getFrom().getPitch() != event.getTo().getPitch();

        if (viewChanged) {
            logActivity(event.getPlayer());
        }
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onChat(AsyncPlayerChatEvent event) {
        logActivity(event.getPlayer());
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onCommand(PlayerCommandPreprocessEvent event) {
        logActivity(event.getPlayer());
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onJoin(PlayerJoinEvent event) {
        logActivity(event.getPlayer());
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onQuit(PlayerQuitEvent event) {
        removePlayer(event.getPlayer());
    }
}
