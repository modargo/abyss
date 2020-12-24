package abyss.events;

import abyss.Abyss;
import abyss.cards.BrokenCrystal;
import abyss.cards.Tormented;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.events.AbstractImageEvent;
import com.megacrit.cardcrawl.localization.EventStrings;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardAndObtainEffect;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

public class HungryVoid extends AbstractImageEvent {
    public static final String ID = "Abyss:HungryVoid";
    private static final EventStrings eventStrings = CardCrawlGame.languagePack.getEventString(ID);
    private static final String NAME = eventStrings.NAME;
    private static final String[] DESCRIPTIONS = eventStrings.DESCRIPTIONS;
    private static final String[] OPTIONS = eventStrings.OPTIONS;
    public static final String IMG = Abyss.eventImage(ID);
    private static final float HEALTH_LOSS_PERCENT = 0.125F;
    private static final float A15_HEALTH_LOSS_PERCENT = 0.18F;
    private static final float HEAL_PERCENT = 0.25F;
    private static final float A15_HEAL_PERCENT = 0.20F;
    private static final int GOLD_LOSS = 50;
    private static final int A15_GOLD_LOSS = 70;
    private static final int CARD_REWARD_AMOUNT = 2;

    private int healthLoss;
    private int healAmount;
    private int goldLoss;
    private AbstractCard cardReward;
    private AbstractCard curse;

    private int screenNum = 0;

    public HungryVoid() {
        super(NAME, DESCRIPTIONS[0], IMG);
        this.noCardsInRewards = true;

        if (AbstractDungeon.ascensionLevel >= 15) {
            this.healthLoss = (int)((float)AbstractDungeon.player.maxHealth * A15_HEALTH_LOSS_PERCENT);
            this.healAmount = (int)((float)AbstractDungeon.player.maxHealth * A15_HEAL_PERCENT);
            this.goldLoss = A15_GOLD_LOSS;
        }
        else {
            this.healthLoss = (int)((float)AbstractDungeon.player.maxHealth * HEALTH_LOSS_PERCENT);
            this.healAmount = (int)((float)AbstractDungeon.player.maxHealth * HEAL_PERCENT);
            this.goldLoss = GOLD_LOSS;
        }
        this.goldLoss = Math.min(this.goldLoss, AbstractDungeon.player.gold);
        this.cardReward = new BrokenCrystal();
        this.curse = new Tormented();

        imageEventText.setDialogOption(MessageFormat.format(OPTIONS[0], CARD_REWARD_AMOUNT, this.cardReward.name, this.healthLoss), this.cardReward);
        imageEventText.setDialogOption(MessageFormat.format(OPTIONS[1], this.healAmount), this.curse);
        imageEventText.setDialogOption(MessageFormat.format(OPTIONS[2], this.goldLoss));
    }

    @Override
    protected void buttonEffect(int buttonPressed) {
        switch (screenNum) {
            case 0:
                switch (buttonPressed) {
                    case 0: // Shield
                        this.imageEventText.updateBodyText(DESCRIPTIONS[1]);
                        List<String> cards = new ArrayList<>();
                        for (int i = 0; i < CARD_REWARD_AMOUNT; i++) {
                            AbstractCard c = this.cardReward.makeCopy();
                            cards.add(c.cardID);
                            AbstractDungeon.effectList.add(new ShowCardAndObtainEffect(c, (float)Settings.WIDTH / 2.0F - (350.0F + 350.0F * i) * Settings.xScale, (float)Settings.HEIGHT / 2.0F));
                        }
                        AbstractDungeon.player.damage(new DamageInfo(AbstractDungeon.player, this.healthLoss));
                        logMetric(ID, "Shield", cards, null, null, null, null, null, null, this.healthLoss, 0, 0, 0, 0, 0);
                        this.screenNum = 1;
                        this.imageEventText.updateDialogOption(0, OPTIONS[3]);
                        this.imageEventText.clearRemainingOptions();
                        break;
                    case 1: // Accept
                        this.imageEventText.updateBodyText(DESCRIPTIONS[2]);
                        AbstractDungeon.player.heal(this.healAmount);
                        AbstractDungeon.effectList.add(new ShowCardAndObtainEffect(this.curse, (float)Settings.WIDTH / 2.0F, (float)Settings.HEIGHT / 2.0F));
                        logMetricObtainCardAndHeal(ID, "Accept", this.curse, this.healAmount);
                        this.screenNum = 1;
                        this.imageEventText.updateDialogOption(0, OPTIONS[3]);
                        this.imageEventText.clearRemainingOptions();
                        break;
                    default: // Drag
                        this.imageEventText.updateBodyText(DESCRIPTIONS[3]);
                        AbstractDungeon.player.loseGold(this.goldLoss);
                        logMetricLoseGold(ID, "Drag", this.goldLoss);
                        this.screenNum = 1;
                        this.imageEventText.updateDialogOption(0, OPTIONS[3]);
                        this.imageEventText.clearRemainingOptions();
                        break;
                }
                break;
            default:
                this.openMap();
                break;
        }
    }
}
