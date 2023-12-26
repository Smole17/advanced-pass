package ru.smole.advancedpass.pass.quest.simple;

import lombok.Builder;
import lombok.Getter;
import ru.smole.advancedpass.pass.quest.PassQuest;

@Getter
@Builder
public class SimplePassQuest implements PassQuest {

    private String id;
    private int expReward;
    private int neededAmount;

    public static class SimplePassQuestBuilder {

        public PassQuest build() {
            return new SimplePassQuest(id, expReward, neededAmount);
        }
    }
}
