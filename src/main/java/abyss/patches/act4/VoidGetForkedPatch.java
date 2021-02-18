package abyss.patches.act4;

import abyss.act.VoidAct;
import actlikeit.events.GetForked;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.megacrit.cardcrawl.core.Settings;

@SpirePatch(
        clz = GetForked.class,
        method = "nextDungeon"
)
public class VoidGetForkedPatch {
    @SpirePostfixPatch
    public static void AddKeysBack(String dungeonID) {
        //ActLikeIt removes the keys as part of entering key-required acts, so we restore them
        if (dungeonID.equals(VoidAct.ID)) {
            Settings.hasEmeraldKey = true;
            Settings.hasSapphireKey = true;
            Settings.hasRubyKey = true;
        }
    }
}
