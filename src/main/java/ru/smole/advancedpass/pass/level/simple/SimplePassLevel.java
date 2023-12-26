package ru.smole.advancedpass.pass.level.simple;

import lombok.Builder;
import lombok.Getter;
import ru.smole.advancedpass.pass.holder.PassHolder;
import ru.smole.advancedpass.pass.level.PassLevel;

import java.util.function.Consumer;

@Getter
@Builder
public class SimplePassLevel implements PassLevel {

    private int ordinal;
    private Consumer<PassHolder> freeRewardConsumer;
    private Consumer<PassHolder> premiumRewardConsumer;

    @Override
    public boolean hasFreeReward() {
        return freeRewardConsumer != null;
    }

    @Override
    public boolean hasPremiumReward() {
        return premiumRewardConsumer != null;
    }

    @Override
    public void takeFreeReward(PassHolder passHolder) {
        if (!hasFreeReward()) return;

        passHolder.getTakenRewards().put(ordinal, false, true);
        freeRewardConsumer.accept(passHolder);
    }

    @Override
    public void takePremiumReward(PassHolder passHolder) {
        if (!hasPremiumReward()) return;

        passHolder.getTakenRewards().put(ordinal, true, true);
        premiumRewardConsumer.accept(passHolder);
    }

    public static class SimplePassLevelBuilder {

        public PassLevel build() {
            return new SimplePassLevel(ordinal, freeRewardConsumer, premiumRewardConsumer);
        }
    }
}
