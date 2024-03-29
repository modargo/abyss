package abyss.subscribers;

import abyss.monsters.MonsterUtil;
import abyss.patches.AnimatorStrengthDexterityFocusGainPatch;
import abyss.powers.crystals.GrayCrystalPower;
import basemod.interfaces.PostPowerApplySubscriber;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.DexterityPower;
import com.megacrit.cardcrawl.powers.FocusPower;
import com.megacrit.cardcrawl.powers.StrengthPower;

import java.util.List;

public class TriggerGrayCrystalPowerPostPowerApplySubscriber implements PostPowerApplySubscriber {
    @Override
    public void receivePostPowerApplySubscriber(AbstractPower power, AbstractCreature target, AbstractCreature source) {
        if (AbstractDungeon.player.chosenClass.name().equals(AnimatorStrengthDexterityFocusGainPatch.ANIMATOR_CLASS_ID)) {
            return;
        }
        if (target.isPlayer && (power.ID.equals(StrengthPower.POWER_ID) || power.ID.equals(DexterityPower.POWER_ID) || power.ID.equals(FocusPower.POWER_ID)) && power.amount > 0) {
            List<AbstractPower> powers = MonsterUtil.getMonsterPowers(GrayCrystalPower.POWER_ID);
            for (AbstractPower p : powers) {
                ((GrayCrystalPower)p).onPlayerGainStrengthDexterityOrFocus();
            }
        }
    }
}
