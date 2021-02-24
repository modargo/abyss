package abyss.patches.act4;

import abyss.act.VoidAct;
import actlikeit.savefields.ElitesSlain;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.screens.GameOverScreen;

@SpirePatch(
        clz = GameOverScreen.class,
        method = "calcScore"
)
public class VoidCalcScorePatch {
    @SpirePostfixPatch
    public static int AdjustScore(int tmp, boolean isVictory) {
        //Add 250 points for winning against Universal Void, just like the heart
        if (isVictory && CardCrawlGame.dungeon instanceof VoidAct) {
            tmp += 250;
        }
        //Subtract the points ActLikeIt grants for beating the elite fight in the Void, since vanilla act 4 doesn't give points for killing the elites
        if (ElitesSlain.getKilledElites().containsKey(VoidAct.ID)) {
            tmp -= ElitesSlain.getKilledElites().get(VoidAct.ID).kills * VoidAct.ACT_NUM * 10;
        }

        return tmp;
    }
}
