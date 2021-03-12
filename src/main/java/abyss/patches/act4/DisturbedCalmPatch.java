package abyss.patches.act4;

import abyss.powers.act4.DisturbedCalmPower;
import basemod.ReflectionHacks;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.actions.watcher.ChangeStanceAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.stances.AbstractStance;
import javassist.CtBehavior;

@SpirePatch(
        clz = ChangeStanceAction.class,
        method = "update"
)
public class DisturbedCalmPatch {
    @SpireInsertPatch(locator = DisturbedCalmPatch.Locator.class, localvars = { "oldStance" })
    public static void ReduceEnergyByOne(ChangeStanceAction __instance, AbstractStance oldStance) {
        if (AbstractDungeon.player.hasPower(DisturbedCalmPower.POWER_ID)) {
            AbstractStance newStance = ReflectionHacks.getPrivate(__instance, ChangeStanceAction.class, "newStance");
            DisturbedCalmPower power = (DisturbedCalmPower)AbstractDungeon.player.getPower(DisturbedCalmPower.POWER_ID);
            power.afterChangeStance(oldStance, newStance);
        }
    }

    private static class Locator extends SpireInsertLocator {
        @Override
        public int[] Locate(CtBehavior ctMethodToPatch) throws Exception {
            Matcher finalMatcher = new Matcher.MethodCallMatcher(AbstractPlayer.class, "switchedStance");
            return LineFinder.findInOrder(ctMethodToPatch, finalMatcher);
        }
    }
}
