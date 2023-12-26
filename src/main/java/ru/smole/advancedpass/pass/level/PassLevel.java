package ru.smole.advancedpass.pass.level;

import ru.smole.advancedpass.pass.holder.PassHolder;

public interface PassLevel {

    int getOrdinal();

    boolean hasFreeReward();

    boolean hasPremiumReward();

    void takeFreeReward(PassHolder passHolder);

    void takePremiumReward(PassHolder passHolder);
}
