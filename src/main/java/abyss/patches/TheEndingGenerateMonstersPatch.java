package abyss.patches;

import abyss.Abyss;
import abyss.act.AbyssAct;
import abyss.act.Encounters;
import abyss.settings.ModSettings;
import actlikeit.savefields.BreadCrumbs;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.dungeons.TheEnding;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Map;

@SpirePatch(
        clz = TheEnding.class,
        method = "generateMonsters"
)
// A patch to make certain events only appear if the player fulfills some condition
public class TheEndingGenerateMonstersPatch {
    @SpirePostfixPatch
    public static void CheckForAbyssElite(TheEnding __instance) {
        if (ModSettings.isAct4EliteEnabled()) {
            Map<Integer, String> breadCrumbs = BreadCrumbs.getBreadCrumbs();
            if (breadCrumbs != null && breadCrumbs.containsKey(AbyssAct.ACT_NUM) && breadCrumbs.get(AbyssAct.ACT_NUM).equals(AbyssAct.ID)) {
                AbstractDungeon.monsterList.clear();
                AbstractDungeon.monsterList.add(Encounters.ANNIHILATION_DUO);
                AbstractDungeon.monsterList.add(Encounters.ANNIHILATION_DUO);
                AbstractDungeon.monsterList.add(Encounters.ANNIHILATION_DUO);
                AbstractDungeon.eliteMonsterList.clear();
                AbstractDungeon.eliteMonsterList.add(Encounters.ANNIHILATION_DUO);
                AbstractDungeon.eliteMonsterList.add(Encounters.ANNIHILATION_DUO);
                AbstractDungeon.eliteMonsterList.add(Encounters.ANNIHILATION_DUO);
            }
        }
    }
}