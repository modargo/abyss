package abyss.patches;

import actlikeit.savefields.BreadCrumbs;
import basemod.ReflectionHacks;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.Exordium;
import com.megacrit.cardcrawl.screens.DeathScreen;
import com.megacrit.cardcrawl.screens.GameOverScreen;
import com.megacrit.cardcrawl.screens.GameOverStat;
import com.megacrit.cardcrawl.screens.VictoryScreen;

import java.util.ArrayList;
import java.util.Map;

public class CreateGameOverStatsPatch {
    private static String EliteScoreStringKey = "Beyond Elites Killed";
    private static String EliteName = CardCrawlGame.languagePack.getScoreString(EliteScoreStringKey).NAME;
    private static int ActNum = 3;
    private static String ActID = Exordium.ID;

    public static void RemoveScoreEntries(ArrayList<GameOverStat> stats) {
        int elitesSlain = CardCrawlGame.elites1Slain;
        String statLabel = EliteName + " (" + elitesSlain + ")";
        if (!Settings.isEndless && elitesSlain == 0) {
            Map<Integer, String> breadCrumbs = BreadCrumbs.getBreadCrumbs();
            if (breadCrumbs != null && breadCrumbs.containsKey(ActNum) && !breadCrumbs.get(ActNum).equals(ActID)) {
                stats.removeIf(stat -> stat != null && stat.label != null && stat.label.equals(statLabel));
            }
        }
    }

    @SpirePatch(
            clz = VictoryScreen.class,
            method = "createGameOverStats"
    )
    public static class VictoryScreenPatch {
        @SpirePostfixPatch
        public static void VictoryScreenPatch(VictoryScreen __instance) {
            ArrayList<GameOverStat> stats = (ArrayList<GameOverStat>) ReflectionHacks.getPrivate(__instance, GameOverScreen.class, "stats");
            RemoveScoreEntries(stats);
        }
    }

    @SpirePatch(
            clz = DeathScreen.class,
            method = "createGameOverStats"
    )
    public static class DeathScreenPatch {
        @SpirePostfixPatch
        public static void DeathScreenPatch(DeathScreen __instance) {
            ArrayList<GameOverStat> stats = (ArrayList<GameOverStat>) ReflectionHacks.getPrivate(__instance, GameOverScreen.class, "stats");
            RemoveScoreEntries(stats);
        }
    }
}
