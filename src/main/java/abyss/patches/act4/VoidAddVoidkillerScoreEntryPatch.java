package abyss.patches.act4;

import abyss.act.VoidAct;
import basemod.ReflectionHacks;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.dungeons.TheEnding;
import com.megacrit.cardcrawl.localization.ScoreBonusStrings;
import com.megacrit.cardcrawl.screens.GameOverScreen;
import com.megacrit.cardcrawl.screens.GameOverStat;
import com.megacrit.cardcrawl.screens.VictoryScreen;
import javassist.CtBehavior;

import java.util.ArrayList;
import java.util.Collections;

@SpirePatch(
        clz = VictoryScreen.class,
        method = "createGameOverStats"
)
public class VoidAddVoidkillerScoreEntryPatch {
    private static final ScoreBonusStrings VOIDKILLER;

    @SpireInsertPatch(locator = VoidAddVoidkillerScoreEntryPatch.Locator.class)
    public static void AddVoidkillerScoreEntry(VictoryScreen __instance) {
        if (CardCrawlGame.dungeon instanceof VoidAct) {
            ArrayList<GameOverStat> stats = (ArrayList<GameOverStat>) ReflectionHacks.getPrivate(__instance, GameOverScreen.class, "stats");
            stats.add(new GameOverStat(VOIDKILLER.NAME, VOIDKILLER.DESCRIPTIONS[0], Integer.toString(250)));
        }
    }
    private static class Locator extends SpireInsertLocator {
        @Override
        public int[] Locate(CtBehavior ctMethodToPatch) throws Exception {
            Matcher firstMatcher = new Matcher.FieldAccessMatcher(AbstractDungeon.class, "isAscensionMode");
            Matcher finalMatcher = new Matcher.InstanceOfMatcher(TheEnding.class);
            return LineFinder.findInOrder(ctMethodToPatch, Collections.singletonList(firstMatcher), finalMatcher);
        }
    }

    static {
        VOIDKILLER = CardCrawlGame.languagePack.getScoreString("Voidkiller");
    }
}
