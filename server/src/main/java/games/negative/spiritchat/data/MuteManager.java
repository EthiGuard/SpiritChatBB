package games.negative.spiritchat.data;

import org.jetbrains.annotations.NotNull;
import java.util.*;

public class MuteManager {
    private final Map<UUID, Long> mutedPlayers = new HashMap<>();

    public void mutePlayer(@NotNull UUID uuid, long duration) {
        mutedPlayers.put(uuid, duration == 0 ? 0 : System.currentTimeMillis() + duration);
    }

    public void unmutePlayer(@NotNull UUID uuid) {
        mutedPlayers.remove(uuid);
    }

    public boolean isMuted(@NotNull UUID uuid) {
        if (!mutedPlayers.containsKey(uuid)) return false;
        
        long expiry = mutedPlayers.get(uuid);
        if (expiry == 0) return true; // Permanent mute
        
        if (System.currentTimeMillis() > expiry) {
            mutedPlayers.remove(uuid);
            return false;
        }
        
        return true;
    }

    public long getRemainingTime(@NotNull UUID uuid) {
        if (!mutedPlayers.containsKey(uuid)) return 0;
        long expiry = mutedPlayers.get(uuid);
        if (expiry == 0) return 0; // Permanent mute
        return Math.max(0, expiry - System.currentTimeMillis());
    }
}
