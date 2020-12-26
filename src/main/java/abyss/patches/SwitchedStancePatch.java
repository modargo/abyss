package abyss.patches;

import abyss.monsters.MonsterUtil;
import abyss.powers.crystals.GrayCrystalPower;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.powers.AbstractPower;

import java.util.List;

@SpirePatch(
        clz = AbstractPlayer.class,
        method = "switchedStance"
)
public class SwitchedStancePatch {
    @SpirePostfixPatch
    public static void CallGrayCrystalPower(AbstractPlayer __instance) {
        List<AbstractPower> powers = MonsterUtil.getMonsterPowers(GrayCrystalPower.POWER_ID);
        for (AbstractPower p : powers) {
            ((GrayCrystalPower)p).onPlayerSwitchedStance();
        }
    }
}