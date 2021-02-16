package abyss.patches.act4;

import abyss.act.AbyssAct;
import abyss.act.Encounters;
import abyss.monsters.act4.UniversalVoid;
import abyss.settings.ModSettings;
import actlikeit.savefields.BreadCrumbs;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.dungeons.TheEnding;

import java.util.Map;

@SpirePatch(
        clz = TheEnding.class,
        method = "initializeBoss"
)
public class TheEndingInitializeBossPatch {
    @SpirePostfixPatch
    public static void CheckForUniversalVoid(TheEnding __instance) {
        //TODO
        if (ModSettings.isAct4BossEnabled()) {
            Map<Integer, String> breadCrumbs = BreadCrumbs.getBreadCrumbs();
            if (breadCrumbs != null && breadCrumbs.containsKey(AbyssAct.ACT_NUM) && breadCrumbs.get(AbyssAct.ACT_NUM).equals(AbyssAct.ID)) {
                AbstractDungeon.bossList.clear();
                AbstractDungeon.monsterList.add(UniversalVoid.ID);
                AbstractDungeon.monsterList.add(UniversalVoid.ID);
                AbstractDungeon.monsterList.add(UniversalVoid.ID);
            }
        }
    }
}