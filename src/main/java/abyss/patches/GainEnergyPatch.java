package abyss.patches;

import abyss.monsters.MonsterUtil;
import abyss.powers.crystals.PurpleCrystalPower;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.powers.AbstractPower;

import java.util.List;

@SpirePatch(
        clz = AbstractPlayer.class,
        method = "gainEnergy"
)
public class GainEnergyPatch {
    @SpirePostfixPatch
    public static void CallPurpleCrystalPower(AbstractPlayer __instance, int e) {
        List<AbstractPower> powers = MonsterUtil.getMonsterPowers(PurpleCrystalPower.POWER_ID);
        for (AbstractPower p : powers) {
            ((PurpleCrystalPower)p).onGainEnergy();
        }
    }
}