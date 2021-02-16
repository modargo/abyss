package abyss.patches.act4;

import abyss.monsters.MonsterUtil;
import abyss.powers.act4.ChainsOfDoomPower;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.megacrit.cardcrawl.potions.SmokeBomb;

@SpirePatch(
        clz = SmokeBomb.class,
        method = "canUse"
)
public class SmokeBombCanUsePatch {
    @SpirePostfixPatch
    public static boolean CheckForAbyssElite(boolean __result, SmokeBomb __instance) {
        return __result && MonsterUtil.getMonsterPowers(ChainsOfDoomPower.POWER_ID).isEmpty();
    }
}