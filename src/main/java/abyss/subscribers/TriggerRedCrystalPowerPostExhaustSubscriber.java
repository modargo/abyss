package abyss.subscribers;

import abyss.monsters.MonsterUtil;
import abyss.powers.crystals.RedCrystalPower;
import basemod.interfaces.PostExhaustSubscriber;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.powers.AbstractPower;

import java.util.List;

public class TriggerRedCrystalPowerPostExhaustSubscriber implements PostExhaustSubscriber {
    @Override
    public void receivePostExhaust(AbstractCard card) {
        List<AbstractPower> powers = MonsterUtil.getMonsterPowers(RedCrystalPower.POWER_ID);
        for (AbstractPower p : powers) {
            ((RedCrystalPower)p).onPlayerExhaust(card);
        }
    }
}
