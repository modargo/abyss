package abyss.patches.act4;

import actlikeit.dungeons.CustomDungeon;
import actlikeit.events.GetForked;
import actlikeit.savefields.BehindTheScenesActNum;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.UIStrings;

@SpirePatch(
        clz = GetForked.class,
        method = SpirePatch.CONSTRUCTOR,
        paramtypez = {boolean.class}
)
public class VoidGetForkedConstructorPatch {
    private static final UIStrings uiStrings;
    public static final String[] TEXT;

    @SpirePostfixPatch
    public static void AdjustFirstDialogOption(GetForked __instance, boolean afterdoor) {
        // The way ActLikeIt is set up, if there are multiple alternate act 4s, and some require the keys and some do
        // not require the keys, there will be two separate choice screens shown (before and after the door animation).
        // This is pretty confusing, since there's no indication that the option for the Void exists on the first one.
        // To remedy this, in the first choice (before the door) when going to act 4, we give the first dialog option
        // new text that calls out both The Ending and The Void
        if (!afterdoor && (BehindTheScenesActNum.getActNum() + 1) == CustomDungeon.THEENDING) {
            __instance.imageEventText.updateDialogOption(0, TEXT[0]);
        }
    }

    static {
        uiStrings = CardCrawlGame.languagePack.getUIString("Abyss:VoidGetForkedConstructorPatch");
        TEXT = uiStrings.TEXT;
    }
}
