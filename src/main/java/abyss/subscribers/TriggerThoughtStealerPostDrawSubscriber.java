package abyss.subscribers;

import abyss.monsters.MonsterUtil;
import abyss.powers.ThoughtStealerPower;
import basemod.interfaces.PostDrawSubscriber;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.powers.AbstractPower;

import java.util.List;

public class TriggerThoughtStealerPostDrawSubscriber implements PostDrawSubscriber {
    @Override
    public void receivePostDraw(AbstractCard card) {
        List<AbstractPower> powers = MonsterUtil.getMonsterPowers(ThoughtStealerPower.POWER_ID);
        for (AbstractPower p : powers) {
            ((ThoughtStealerPower)p).onPlayerCardDraw();
        }
    }
}
