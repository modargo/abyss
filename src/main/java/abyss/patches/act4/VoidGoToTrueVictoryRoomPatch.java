package abyss.patches.act4;

import abyss.act.VoidAct;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.rooms.MonsterRoomBoss;
import com.megacrit.cardcrawl.ui.buttons.ProceedButton;
import javassist.CtBehavior;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

@SpirePatch(
        clz = ProceedButton.class,
        method = "update"
)
public class VoidGoToTrueVictoryRoomPatch {
    @SpireInsertPatch(
            locator = Locator.class
    )
    public static void Insert(ProceedButton __instance) {
        //ActLikeIt prevents the normal call to goToTrueVictoryRoom from happening, so we restore it
        if (AbstractDungeon.getCurrRoom() instanceof MonsterRoomBoss) {
            if (CardCrawlGame.dungeon instanceof VoidAct) {
                try {
                    Method yuckyPrivateMethod = ProceedButton.class.getDeclaredMethod("goToTrueVictoryRoom");
                    yuckyPrivateMethod.setAccessible(true);
                    yuckyPrivateMethod.invoke(__instance);
                } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private static class Locator extends SpireInsertLocator {
        @Override
        public int[] Locate(CtBehavior ctMethodToPatch) throws Exception {
            Matcher finalMatcher = new Matcher.InstanceOfMatcher(MonsterRoomBoss.class);
            return LineFinder.findInOrder(ctMethodToPatch, finalMatcher);
        }
    }
}