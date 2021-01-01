package abyss.patches;

import abyss.monsters.MonsterUtil;
import abyss.powers.crystals.GrayCrystalPower;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.DexterityPower;
import com.megacrit.cardcrawl.powers.FocusPower;
import com.megacrit.cardcrawl.powers.StrengthPower;

import java.util.List;

// The Animator mod completely replaces the normal ApplyPowerAction with its own system
// To have GrayCrystalPower work properly with that, we patch directly into the methods
// of the powers themselves that The Animator calls
// See https://github.com/EatYourBeetS/STS-AnimatorMod/blob/ef97700be2cebaae03e25a02670fad2051649f8b/EatYourBeetSVG/src/main/java/eatyourbeets/actions/powers/ApplyPower.java
public class AnimatorStrengthDexterityFocusGainPatch {
    public static String ANIMATOR_CLASS_ID = "THE_ANIMATOR";

    public static void CallGrayCrystalPower() {
        List<AbstractPower> powers = MonsterUtil.getMonsterPowers(GrayCrystalPower.POWER_ID);
        for (AbstractPower p : powers) {
            ((GrayCrystalPower)p).onPlayerGainStrengthDexterityOrFocus();
        }
    }

    public static void CallGrayCrystalPower(AbstractPower power, Integer stackAmount) {
        if ((power.ID.equals(StrengthPower.POWER_ID) || power.ID.equals(DexterityPower.POWER_ID) || power.ID.equals(FocusPower.POWER_ID))
            && power.owner.isPlayer
            && (stackAmount == null || stackAmount > 0)
            && AbstractDungeon.player.chosenClass.name().equals("THE_ANIMATOR")) {
            AnimatorStrengthDexterityFocusGainPatch.CallGrayCrystalPower();
        }
    }

    @SpirePatch(
            clz = AbstractPower.class,
            method = "onInitialApplication"
    )
    public static class AbstractPowerOnInitialApplicationPatch {
        @SpirePostfixPatch
        public static void Patch(AbstractPower power) {
            AnimatorStrengthDexterityFocusGainPatch.CallGrayCrystalPower(power, null);
        }
    }

    @SpirePatch(
            clz = AbstractPower.class,
            method = "stackPower"
    )
    public static class AbstractPowerStackPowerPatch {
        @SpirePostfixPatch
        public static void Patch(AbstractPower power, int stackAmount) {
            AnimatorStrengthDexterityFocusGainPatch.CallGrayCrystalPower(power, stackAmount);
        }
    }
}