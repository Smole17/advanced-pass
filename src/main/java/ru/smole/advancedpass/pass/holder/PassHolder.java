package ru.smole.advancedpass.pass.holder;

import com.google.common.collect.Table;
import lombok.val;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Range;
import ru.smole.advancedpass.AdvancedPass;

import java.util.Map;
import java.util.UUID;

public interface PassHolder {

    @NotNull UUID getUniqueId();

    @NotNull Table<@Range(from = 0, to = Integer.MAX_VALUE) Integer, Boolean, Boolean> getTakenRewards();

    @NotNull Map<String, @Range(from = 0, to = Integer.MAX_VALUE) Integer> getQuestsProgress();

    default void addQuest(String id) {
        AdvancedPass.getPassQuest(id).ifPresent(passQuest -> {
            getQuestsProgress().put(passQuest.getId(), 0);
        });
    }

    default boolean isQuestCompleted(String id) {
        val optionalPassQuest = AdvancedPass.getPassQuest(id);

        if (optionalPassQuest.isEmpty()) return false;

        val passQuest = optionalPassQuest.get();

        return getQuestsProgress().getOrDefault(id, -1) >= passQuest.getNeededAmount();
    }

    default void addQuestProgress(String id, @Range(from = 0, to = Integer.MAX_VALUE) int additionalProgress) {
        val questsProgress = getQuestsProgress();

        if (!questsProgress.containsKey(id) || isQuestCompleted(id)) return;

        questsProgress.merge(id, additionalProgress, Integer::sum);

        if (isQuestCompleted(id))
            AdvancedPass.getPassQuest(id)
                    .ifPresent(passQuest -> addExp(passQuest.getExpReward()));
    }

    boolean isPremium();

    void setPremium(boolean state);

    int getExp();

    void setExp(@Range(from = 0, to = Integer.MAX_VALUE) int exp);

    default void addExp(@Range(from = 0, to = Integer.MAX_VALUE) int additionalExp) {
        setExp(getExp() + additionalExp);
    }

    default @Range(from = 0, to = Integer.MAX_VALUE) int getLevel() {
        return Math.max(AdvancedPass.getLevels(), getExp() / 100);
    }

    default @Range(from = 0, to = 99) int getProgress() {
        return getExp() % 100;
    }
}
