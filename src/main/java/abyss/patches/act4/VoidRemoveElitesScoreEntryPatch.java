package abyss.patches.act4;

import abyss.act.VoidAct;
import actlikeit.ActLikeIt;
import actlikeit.dungeons.CustomDungeon;
import actlikeit.savefields.ElitesSlain;
import basemod.ReflectionHacks;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.screens.DeathScreen;
import com.megacrit.cardcrawl.screens.GameOverScreen;
import com.megacrit.cardcrawl.screens.GameOverStat;
import com.megacrit.cardcrawl.screens.VictoryScreen;

import java.util.ArrayList;

public class VoidRemoveElitesScoreEntryPatch {
    private static final String ActID = VoidAct.ID;

    public static void RemoveScoreEntryForVoid(ArrayList<GameOverStat> stats) {
        int elitesSlain = ElitesSlain.getKilledElites().containsKey(ActID) ? ElitesSlain.getKilledElites().get(ActID).kills : 0;
        String actName = CustomDungeon.dungeons.containsKey(ActID) ? CustomDungeon.dungeons.get(ActID).name : "The Void";
        String[] parts = CardCrawlGame.languagePack.getScoreString(ActLikeIt.makeID("ElitesKilled")).DESCRIPTIONS;
        String statLabel = parts[0] + actName + parts[2] + " (" + elitesSlain + ")";
        stats.removeIf(stat -> stat != null && stat.label != null && stat.label.equals(statLabel));
    }

    @SpirePatch(
            clz = VictoryScreen.class,
            method = "createGameOverStats"
    )
    public static class VictoryScreenPatch {
        @SpirePostfixPatch
        public static void victoryScreenPatch(VictoryScreen __instance) {
            ArrayList<GameOverStat> stats = ReflectionHacks.getPrivate(__instance, GameOverScreen.class, "stats");
            RemoveScoreEntryForVoid(stats);
        }
    }

    @SpirePatch(
            clz = DeathScreen.class,
            method = "createGameOverStats"
    )
    public static class DeathScreenPatch {
        @SpirePostfixPatch
        public static void deathScreenPatch(DeathScreen __instance) {
            ArrayList<GameOverStat> stats = ReflectionHacks.getPrivate(__instance, GameOverScreen.class, "stats");
            RemoveScoreEntryForVoid(stats);
        }
    }
}
