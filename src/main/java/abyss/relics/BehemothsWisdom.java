package abyss.relics;

import abyss.Abyss;
import abyss.actions.BehemothsWisdomAction;
import abyss.util.TextureLoader;
import basemod.abstracts.CustomRelic;
import com.badlogic.gdx.graphics.Texture;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.AbstractRelic;

import java.text.DecimalFormat;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BehemothsWisdom extends CustomRelic {
    public static final String ID = "Abyss:BehemothsWisdom";
    private static final Texture IMG = TextureLoader.getTexture(Abyss.relicImage(ID));
    private static final Texture OUTLINE = TextureLoader.getTexture(Abyss.relicOutlineImage(ID));
    private static final int CARDS = 2;
    private boolean activated = false;

    private static final Map<String, Integer> stats = new HashMap<>();
    private static final String EXHAUST_STAT = "exhaust";

    public BehemothsWisdom() {
        super(ID, IMG, OUTLINE, AbstractRelic.RelicTier.SPECIAL, LandingSound.HEAVY);
    }

    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0].replace("{0}", CARDS + "");
    }

    @Override
    public void atBattleStartPreDraw() {
        this.activated = false;
    }

    @Override
    public void atTurnStartPostDraw() {
        if (!this.activated) {
            this.activated = true;
            this.flash();
            this.addToBot(new RelicAboveCreatureAction(AbstractDungeon.player, this));
            this.addToBot(new BehemothsWisdomAction(AbstractDungeon.player, CARDS));
        }
    }

    @Override
    public AbstractRelic makeCopy() {
        return new BehemothsWisdom();
    }

    public String getStatsDescription() {
        return MessageFormat.format(DESCRIPTIONS[1], stats.get(EXHAUST_STAT));
    }

    public String getExtendedStatsDescription(int totalCombats, int totalTurns) {
        DecimalFormat format = new DecimalFormat("#.###");
        float exhaust = stats.get(EXHAUST_STAT);
        String exhaustPerCombat = format.format(exhaust / Math.max(totalCombats, 1));
        return getStatsDescription() + MessageFormat.format(DESCRIPTIONS[2], exhaustPerCombat);
    }

    public void resetStats() {
        stats.put(EXHAUST_STAT, 0);
    }

    public JsonElement onSaveStats() {
        Gson gson = new Gson();
        List<Integer> statsToSave = new ArrayList<>();
        statsToSave.add(stats.get(EXHAUST_STAT));
        return gson.toJsonTree(statsToSave);
    }

    public void onLoadStats(JsonElement jsonElement) {
        if (jsonElement != null) {
            JsonArray jsonArray = jsonElement.getAsJsonArray();
            stats.put(EXHAUST_STAT, jsonArray.get(0).getAsInt());
        } else {
            resetStats();
        }
    }

    public static void incrementExhaustStat(int amount) {
        stats.put(EXHAUST_STAT, stats.getOrDefault(EXHAUST_STAT, 0) + amount);
    }
}
