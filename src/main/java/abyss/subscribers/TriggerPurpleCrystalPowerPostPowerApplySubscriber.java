package abyss.subscribers;

import abyss.powers.crystals.PurpleCrystalPower;
import basemod.interfaces.PostPowerApplySubscriber;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.powers.AbstractPower;

public class TriggerPurpleCrystalPowerPostPowerApplySubscriber implements PostPowerApplySubscriber {
    @Override
    public void receivePostPowerApplySubscriber(AbstractPower power, AbstractCreature target, AbstractCreature source) {
        if (power.type == AbstractPower.PowerType.DEBUFF && source.isPlayer) {
            AbstractPower purpleCrystalPower = target.getPower(PurpleCrystalPower.POWER_ID);
            if (purpleCrystalPower != null) {
                ((PurpleCrystalPower)purpleCrystalPower).onPlayerApplyDebuffToThisEnemy();
            }
        }
    }
}
