package games.negative.spiritchat.data;

import org.jetbrains.annotations.NotNull;
import java.util.*;

public class IgnoreManager {
    private final Map<UUID, Set<UUID>> ignoredPlayers = new HashMap<>();

    public void ignorePlayer(@NotNull UUID player, @NotNull UUID ignored) {
        ignoredPlayers.computeIfAbsent(player, k -> new HashSet<>()).add(ignored);
    }

    public void unignorePlayer(@NotNull UUID player, @NotNull UUID ignored) {
        Set<UUID> ignored_players = ignoredPlayers.get(player);
        if (ignored_players != null) {
            ignored_players.remove(ignored);
        }
    }

    public boolean isIgnored(@NotNull UUID player, @NotNull UUID target) {
        Set<UUID> ignored_players = ignoredPlayers.get(player);
        return ignored_players != null && ignored_players.contains(target);
    }

    public Set<UUID> getIgnoredPlayers(@NotNull UUID player) {
        return ignoredPlayers.getOrDefault(player, new HashSet<>());
    }
}
