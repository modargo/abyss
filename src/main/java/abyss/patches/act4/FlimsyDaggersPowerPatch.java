package abyss.patches.act4;

import abyss.powers.act4.FlimsyDaggersPower;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.stances.AbstractStance;
import javassist.CtBehavior;

import java.util.Collections;

@SpirePatch(
        clz = AbstractCard.class,
        method = "applyPowers",
        paramtypez = {}
)
public class FlimsyDaggersPowerPatch {
    @SpireInsertPatch(locator = FlimsyDaggersPowerPatch.FirstLocator.class, localvars = { "tmp" })
    public static void FlimsyDaggersSingleDamage(AbstractCard __instance, @ByRef float[] tmp) {
        if (AbstractDungeon.player.hasPower(FlimsyDaggersPower.POWER_ID)) {
            FlimsyDaggersPower power = (FlimsyDaggersPower)AbstractDungeon.player.getPower(FlimsyDaggersPower.POWER_ID);
            tmp[0] = power.atDamageModify(tmp[0], __instance);
        }
    }

    @SpireInsertPatch(locator = FlimsyDaggersPowerPatch.SecondLocator.class, localvars = { "tmp", "i" })
    public static void FlimsyDaggersMultiDamage(AbstractCard __instance, @ByRef float[][] tmp, int i) {
        if (AbstractDungeon.player.hasPower(FlimsyDaggersPower.POWER_ID)) {
            FlimsyDaggersPower power = (FlimsyDaggersPower)AbstractDungeon.player.getPower(FlimsyDaggersPower.POWER_ID);
            tmp[0][i] = power.atDamageModify(tmp[0][i], __instance);
        }
    }

    private static class FirstLocator extends SpireInsertLocator {
        @Override
        public int[] Locate(CtBehavior ctMethodToPatch) throws Exception {
            Matcher finalMatcher = new Matcher.MethodCallMatcher(AbstractStance.class, "atDamageGive");
            return LineFinder.findInOrder(ctMethodToPatch, finalMatcher);
        }
    }

    private static class SecondLocator extends SpireInsertLocator {
        @Override
        public int[] Locate(CtBehavior ctMethodToPatch) throws Exception {
            Matcher finalMatcher = new Matcher.MethodCallMatcher(AbstractStance.class, "atDamageGive");
            return LineFinder.findInOrder(ctMethodToPatch, Collections.singletonList(finalMatcher), finalMatcher);
        }
    }
}
