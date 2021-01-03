package abyss.patches;

import abyss.monsters.MonsterUtil;
import abyss.powers.ChainsOfDoomPower;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.megacrit.cardcrawl.potions.SmokeBomb;

@SpirePatch(
        clz = SmokeBomb.class,
        method = "canUse"
)
// A patch to make certain events only appear if the player fulfills some condition
public class SmokeBombCanUsePatch {
    @SpirePostfixPatch
    public static boolean CheckForAbyssElite(boolean __result, SmokeBomb __instance) {
        return __result && MonsterUtil.getMonsterPowers(ChainsOfDoomPower.POWER_ID).isEmpty();
    }
}