package abyss.relics;

import abyss.Abyss;
import abyss.util.TextureLoader;
import basemod.abstracts.CustomRelic;
import com.badlogic.gdx.graphics.Texture;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.megacrit.cardcrawl.actions.common.HealAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.AbstractRelic;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CrystalEnergy extends CustomRelic {
    public static final String ID = "Abyss:CrystalEnergy";
    private static final Texture IMG = TextureLoader.getTexture(Abyss.relicImage(ID));
    private static final Texture OUTLINE = TextureLoader.getTexture(Abyss.relicOutlineImage(ID));
    private static final int HEAL = 5;
    private static final int DAMAGE_THRESHOLD = 25;

    private static final Map<String, Integer> stats = new HashMap<>();
    private static final String HEAL_STAT = "heal";

    public CrystalEnergy() {
        super(ID, IMG, OUTLINE, RelicTier.SPECIAL, LandingSound.HEAVY);
    }

    @Override
    public String getUpdatedDescription() {
        return MessageFormat.format(DESCRIPTIONS[0], DAMAGE_THRESHOLD, HEAL);
    }

    @Override
    public void atBattleStart() {
        this.counter = 0;
    }

    @Override
    public void atTurnStart() {
        this.counter = 0;
    }

    @Override
    public void onLoseHp(int damageAmount) {
        int oldCounter = this.counter;
        this.counter += damageAmount;
        if (oldCounter < DAMAGE_THRESHOLD && this.counter >= DAMAGE_THRESHOLD) {
            incrementHealStat();
            this.flash();
            this.addToTop(new HealAction(AbstractDungeon.player, AbstractDungeon.player, HEAL));
        }
    }

    @Override
    public void onVictory() {
        this.counter = -1;
    }

    @Override
    public AbstractRelic makeCopy() {
        return new CrystalEnergy();
    }

    public String getStatsDescription() {
        return MessageFormat.format(DESCRIPTIONS[1], stats.get(HEAL_STAT));
    }

    public String getExtendedStatsDescription(int totalCombats, int totalTurns) {
        return getStatsDescription();
    }

    public void resetStats() {
        stats.put(HEAL_STAT, 0);
    }

    public JsonElement onSaveStats() {
        Gson gson = new Gson();
        List<Integer> statsToSave = new ArrayList<>();
        statsToSave.add(stats.get(HEAL_STAT));
        return gson.toJsonTree(statsToSave);
    }

    public void onLoadStats(JsonElement jsonElement) {
        if (jsonElement != null) {
            JsonArray jsonArray = jsonElement.getAsJsonArray();
            stats.put(HEAL_STAT, jsonArray.get(0).getAsInt());
        } else {
            resetStats();
        }
    }

    public static void incrementHealStat() {
        stats.put(HEAL_STAT, stats.getOrDefault(HEAL_STAT, 0) + HEAL);
    }
}
