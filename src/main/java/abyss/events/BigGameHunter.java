package abyss.events;

import abyss.Abyss;
import abyss.relics.HuntersRespect;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.events.AbstractImageEvent;
import com.megacrit.cardcrawl.localization.EventStrings;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.relics.Circlet;

import java.text.MessageFormat;
import java.util.Collections;

public class BigGameHunter extends AbstractImageEvent {
    public static final String ID = "Abyss:BigGameHunter";
    private static final EventStrings eventStrings = CardCrawlGame.languagePack.getEventString(ID);
    private static final String NAME = eventStrings.NAME;
    private static final String[] DESCRIPTIONS = eventStrings.DESCRIPTIONS;
    private static final String[] OPTIONS = eventStrings.OPTIONS;
    public static final String IMG = Abyss.eventImage(ID);
    private static final int MAX_HEALTH_LOSS = 4;
    private static final int A15_MAX_HEALTH_LOSS = 5;
    private static final int MAX_HEALTH_GAIN = 7;
    private static final int A15_MAX_HEALTH_GAIN = 5;
    private static final String HuntersKnifeId = "Menagerie:HuntersKnife";
    private static final String HuntersBracerId = "Elementarium:HuntersBracer";
    private static final String HuntersElixirId = "Elementarium:HuntersElixir";
    private static final String HuntersSlingId = "Elementarium:HuntersSling";

    private int maxHealthLoss;
    private int maxHealthGain;
    private AbstractRelic relicReward;
    private AbstractRelic knifeRelic;
    private AbstractRelic otherRelic;
    private int screenNum = 0;

    public BigGameHunter() {
        super(NAME, DESCRIPTIONS[0], IMG);
        this.noCardsInRewards = true;

        this.maxHealthLoss = AbstractDungeon.ascensionLevel >= 15 ? A15_MAX_HEALTH_LOSS : MAX_HEALTH_LOSS;
        this.maxHealthGain = AbstractDungeon.ascensionLevel >= 15 ? A15_MAX_HEALTH_GAIN : MAX_HEALTH_GAIN;
        this.relicReward = new HuntersRespect();
        for (AbstractRelic relic : AbstractDungeon.player.relics) {
            if (relic.relicId.equals(HuntersKnifeId) ) {
                this.knifeRelic = relic;
            }
            if (relic.relicId.equals(HuntersBracerId) || relic.relicId.equals(HuntersElixirId) || relic.relicId.equals(HuntersSlingId)) {
                this.otherRelic = relic;
            }
        }

        imageEventText.setDialogOption(OPTIONS[0]);
    }

    @Override
    protected void buttonEffect(int buttonPressed) {
        switch (screenNum) {
            case 0:
                this.imageEventText.updateBodyText(DESCRIPTIONS[1]);
                this.screenNum = 1;
                this.imageEventText.clearAllDialogs();
                imageEventText.setDialogOption(MessageFormat.format(OPTIONS[1], this.relicReward.name, this.maxHealthLoss), this.relicReward);
                if (this.knifeRelic != null) {
                    imageEventText.setDialogOption(MessageFormat.format(OPTIONS[2], this.relicReward.name, this.knifeRelic.name), this.relicReward);
                }
                else {
                    imageEventText.setDialogOption(OPTIONS[3], true);
                }
                if (this.otherRelic != null) {
                    imageEventText.setDialogOption(MessageFormat.format(OPTIONS[4], this.relicReward.name, this.maxHealthGain, this.otherRelic.name), this.relicReward);
                }
                else {
                    imageEventText.setDialogOption(OPTIONS[5], true);
                }
                imageEventText.setDialogOption(OPTIONS[6]);
                break;
            case 1:
                switch (buttonPressed) {
                    case 0: // Trade (Blood)
                        this.imageEventText.updateBodyText(DESCRIPTIONS[2]);
                        logMetricObtainRelicAndLoseMaxHP(ID, "Blood", this.relicReward, this.maxHealthLoss);
                        AbstractDungeon.player.decreaseMaxHealth(this.maxHealthLoss);
                        this.giveRelicReward();
                        this.screenNum = 2;
                        this.imageEventText.updateDialogOption(0, OPTIONS[6]);
                        this.imageEventText.clearRemainingOptions();
                        break;
                    case 1: // Trade (Knife)
                        this.imageEventText.updateBodyText(DESCRIPTIONS[2]);
                        logMetricRelicSwap(ID, "Knife", this.relicReward, this.knifeRelic);
                        AbstractDungeon.player.loseRelic(this.knifeRelic.relicId);
                        this.giveRelicReward();
                        this.screenNum = 2;
                        this.imageEventText.updateDialogOption(0, OPTIONS[6]);
                        this.imageEventText.clearRemainingOptions();
                        break;
                    case 2: // Trade (Other)
                        this.imageEventText.updateBodyText(DESCRIPTIONS[2]);
                        logMetric(ID, "Other", null, null, null, null, Collections.singletonList(this.relicReward.relicId), null, Collections.singletonList(this.otherRelic.relicId), 0, 0, 0, this.maxHealthGain, 0, 0);
                        AbstractDungeon.player.loseRelic(this.otherRelic.relicId);
                        this.giveRelicReward();
                        AbstractDungeon.player.increaseMaxHp(this.maxHealthGain, true);
                        this.screenNum = 2;
                        this.imageEventText.updateDialogOption(0, OPTIONS[6]);
                        this.imageEventText.clearRemainingOptions();
                        break;
                    default: // Leave
                        this.imageEventText.updateBodyText(DESCRIPTIONS[3]);
                        logMetricIgnored(ID);
                        this.screenNum = 2;
                        this.imageEventText.updateDialogOption(0, OPTIONS[6]);
                        this.imageEventText.clearRemainingOptions();
                        break;
                }
                break;
            default:
                this.openMap();
                break;
        }
    }

    private void giveRelicReward() {
        if (!AbstractDungeon.player.hasRelic(this.relicReward.relicId)) {
            AbstractDungeon.getCurrRoom().spawnRelicAndObtain((float) (Settings.WIDTH / 2), (float) (Settings.HEIGHT / 2), this.relicReward);
        }
        else {
            AbstractDungeon.getCurrRoom().spawnRelicAndObtain((float) (Settings.WIDTH / 2), (float) (Settings.HEIGHT / 2), new Circlet());
        }
    }
}
