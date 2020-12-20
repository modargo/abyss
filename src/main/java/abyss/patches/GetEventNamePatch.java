package abyss.patches;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireReturn;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.helpers.EventHelper;

@SpirePatch(
        clz = EventHelper.class,
        method = "getEventName",
        paramtypez = String.class
)
public class GetEventNamePatch {
    private static String ModPrefix = "Abyss:";

    @SpirePrefixPatch
    public static SpireReturn<String> GetEventName(String eventID) {
        if (eventID != null && eventID.startsWith(ModPrefix)) {
            return SpireReturn.Return(CardCrawlGame.languagePack.getEventString(eventID).NAME);
        }
        return SpireReturn.Continue();
    }
}