package ru.smole.advancedpass.pass.holder.entity;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import lombok.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Range;
import ru.smole.advancedpass.pass.holder.PassHolder;
import ru.smole.advancedpass.pass.holder.dao.SimplePassHolderDAOImpl;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@DatabaseTable(tableName = "pass_holders", daoClass = SimplePassHolderDAOImpl.class)
public final class SimplePassHolder implements PassHolder {

    @DatabaseField(generatedId = true)
    private long id;

    @DatabaseField(columnName = "uuid", unique = true, canBeNull = false)
    private UUID uniqueId;

    @DatabaseField(columnName = "is_premium")
    private boolean premium;

    @DatabaseField
    private int exp;

    @DatabaseField(columnName = "quests_progress", dataType = DataType.SERIALIZABLE, canBeNull = false)
    private final HashMap<String, Integer> questsProgress = new HashMap<>();

    @DatabaseField(columnName = "taken_rewards", dataType = DataType.SERIALIZABLE, canBeNull = false)
    private final HashBasedTable<Integer, Boolean, Boolean> takenRewards = HashBasedTable.create();

    @Override
    public @NotNull Table<Integer, Boolean, Boolean> getTakenRewards() {
        return takenRewards;
    }

    @Override
    public @NotNull Map<String, @Range(from = 0, to = Integer.MAX_VALUE) Integer> getQuestsProgress() {
        return questsProgress;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SimplePassHolder simplePassHolder)) return false;
        return id == simplePassHolder.id && Objects.equals(uniqueId, simplePassHolder.uniqueId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, uniqueId, questsProgress, takenRewards, premium, exp);
    }

    public static class SimplePassHolderBuilder {

        public PassHolder build() {
            return new SimplePassHolder(id, uniqueId, premium, exp);
        }
    }
}
