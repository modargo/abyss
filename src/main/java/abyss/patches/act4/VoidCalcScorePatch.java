package abyss.patches.act4;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.TheEnding;
import com.megacrit.cardcrawl.screens.GameOverScreen;

public class VoidCalcScorePatch {
    //Forget this for now. Asked Casey to make it much easier for us to do score stuff, and then this would change massively.

    @SpirePatch(
            clz = GameOverScreen.class,
            method = "calcScore"
    )
    public static class DeathScreenPatch {
        @SpirePostfixPatch
        public static int AddUniversalVoidKillBonus(int tmp, boolean isVictory) {
            if (isVictory && CardCrawlGame.dungeon instanceof TheEnding) {
                tmp += 250;
            }

            return tmp;
        }
    }
}
