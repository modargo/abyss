package abyss.patches.act4;

import abyss.powers.act4.FeeblePoisonPower;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.PoisonPower;
import javassist.CtBehavior;

@SpirePatch(
        clz = ApplyPowerAction.class,
        method = SpirePatch.CONSTRUCTOR,
        paramtypez = {AbstractCreature.class, AbstractCreature.class, AbstractPower.class, int.class, boolean.class, AbstractGameAction.AttackEffect.class}
)
public class FeeblePoisonPowerPatch {
    @SpireInsertPatch(locator = FeeblePoisonPowerPatch.Locator.class)
    public static void FeeblePoison(ApplyPowerAction __instance, AbstractCreature target, AbstractCreature source, AbstractPower powerToApply, int stackAmount, boolean isFast, AbstractGameAction.AttackEffect effect) {
        if (AbstractDungeon.player.hasPower(FeeblePoisonPower.POWER_ID) && source != null && source.isPlayer && target != source && powerToApply.ID.equals(PoisonPower.POWER_ID)) {
            AbstractDungeon.player.getPower(FeeblePoisonPower.POWER_ID).flash();
            int newAmount = (int)Math.ceil(powerToApply.amount * (FeeblePoisonPower.POISON_REDUCTION_PERCENT / 100.0F));
            powerToApply.amount = newAmount;
            __instance.amount = newAmount;
        }
    }

    private static class Locator extends SpireInsertLocator {
        @Override
        public int[] Locate(CtBehavior ctMethodToPatch) throws Exception {
            Matcher finalMatcher = new Matcher.MethodCallMatcher(AbstractPlayer.class, "hasRelic");
            return LineFinder.findInOrder(ctMethodToPatch, finalMatcher);
        }
    }
}
