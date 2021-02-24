package abyss.patches.act4;

import abyss.powers.act4.WeakOrbsPower;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.orbs.AbstractOrb;

@SpirePatch(
        clz = AbstractOrb.class,
        method = "applyFocus"
)
public class WeakOrbsPatch {
    @SpirePostfixPatch
    public static void ModifyOrbAmount(AbstractOrb __instance) {
        if (AbstractDungeon.player.hasPower(WeakOrbsPower.POWER_ID)) {
            WeakOrbsPower p = (WeakOrbsPower)AbstractDungeon.player.getPower(WeakOrbsPower.POWER_ID);
            __instance.passiveAmount = p.modifyOrbPassiveAmount(__instance, __instance.passiveAmount);
            __instance.evokeAmount = p.modifyOrbEvokeAmount(__instance, __instance.evokeAmount);
        }
    }
}
