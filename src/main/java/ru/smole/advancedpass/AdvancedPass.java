package ru.smole.advancedpass;

import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.jdbc.JdbcPooledConnectionSource;
import com.j256.ormlite.table.TableUtils;
import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;
import lombok.val;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Range;
import ru.smole.advancedpass.pass.holder.PassHolder;
import ru.smole.advancedpass.pass.holder.entity.SimplePassHolder;
import ru.smole.advancedpass.pass.level.PassLevel;
import ru.smole.advancedpass.pass.quest.PassQuest;
import ru.smole.advancedpass.service.PassLoaderService;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@UtilityClass
public class AdvancedPass {

    private final Map<Integer, PassLevel> PASS_LEVELS = new HashMap<>();
    private final Map<String, PassQuest> PASS_QUESTS = new HashMap<>();
    private final Map<UUID, PassHolder> PASS_HOLDERS = new HashMap<>();

    @SneakyThrows
    public void initialize(@NotNull String jdbcUrl, @NotNull String username, @NotNull String password) {
        val connectionSource = new JdbcPooledConnectionSource(jdbcUrl);
        connectionSource.setUsername(username);
        connectionSource.setPassword(password);

        TableUtils.createTableIfNotExists(connectionSource, SimplePassHolder.class);

        new PassLoaderService(
                DaoManager.createDao(connectionSource, SimplePassHolder.class),
                PASS_HOLDERS
        );
    }

    public @Range(from = 0, to = Integer.MAX_VALUE) int getLevels() {
        return PASS_LEVELS.keySet().stream().max(Integer::compareTo).orElse(0);
    }

    public @NotNull Optional<@NotNull PassLevel> getPassLevel(int level) {
        return level < 0 || level >= PASS_LEVELS.size() ? Optional.empty() : Optional.of(PASS_LEVELS.get(level + 1));
    }

    public void addPassLevel(@NotNull PassLevel passLevel) {
        PASS_LEVELS.put(passLevel.getOrdinal(), passLevel);
    }

    public @NotNull Optional<@NotNull PassQuest> getPassQuest(@NotNull String id) {
        return !PASS_QUESTS.containsKey(id) ? Optional.empty() : Optional.of(PASS_QUESTS.get(id));
    }

    public void addPassQuest(@NotNull PassQuest passQuest) {
        PASS_QUESTS.put(passQuest.getId(), passQuest);
    }

    public Optional<PassHolder> getPassHolder(UUID uniqueId) {
        return !PASS_HOLDERS.containsKey(uniqueId) ? Optional.empty() : Optional.of(PASS_HOLDERS.get(uniqueId));
    }
}
