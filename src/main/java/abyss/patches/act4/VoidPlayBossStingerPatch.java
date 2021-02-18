package abyss.patches.act4;

import abyss.act.VoidAct;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireReturn;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

@SpirePatch(
        clz = AbstractMonster.class,
        method = "playBossStinger"
)
public class VoidPlayBossStingerPatch {
    @SpirePrefixPatch
    public static SpireReturn<Void> PlayFinalBossStinger() {
        CardCrawlGame.sound.play("BOSS_VICTORY_STINGER");
        if (AbstractDungeon.id.equals(VoidAct.ID)) {
            CardCrawlGame.music.playTempBgmInstantly("STS_EndingStinger_v1.ogg", false);
            return SpireReturn.Return(null);
        }
        return SpireReturn.Continue();
    }
}
