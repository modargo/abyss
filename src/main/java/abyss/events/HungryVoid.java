package abyss.events;

import abyss.Abyss;
import abyss.cards.BrokenCrystal;
import com.evacipated.cardcrawl.modthespire.Loader;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.colorless.Apotheosis;
import com.megacrit.cardcrawl.cards.colorless.Apparition;
import com.megacrit.cardcrawl.cards.colorless.Madness;
import com.megacrit.cardcrawl.cards.curses.Decay;
import com.megacrit.cardcrawl.cards.curses.Doubt;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.events.AbstractImageEvent;
import com.megacrit.cardcrawl.helpers.CardLibrary;
import com.megacrit.cardcrawl.localization.EventStrings;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.rewards.RewardItem;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardAndObtainEffect;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class HungryVoid extends AbstractImageEvent {
    public static final String ID = "Abyss:HungryVoid";
    private static final EventStrings eventStrings = CardCrawlGame.languagePack.getEventString(ID);
    private static final String NAME = eventStrings.NAME;
    private static final String[] DESCRIPTIONS = eventStrings.DESCRIPTIONS;
    private static final String[] OPTIONS = eventStrings.OPTIONS;
    public static final String IMG = Abyss.eventImage(ID);
    private static final float MAX_HEALTH_LOSS_PERCENT = 0.125F;
    private static final float A15_MAX_HEALTH_LOSS_PERCENT = 0.18F;
    private static final float HEAL_PERCENT = 0.25F;
    private static final float A15_HEAL_PERCENT = 0.20F;
    private static final int GOLD_LOSS = 40;
    private static final int A15_GOLD_LOSS = 60;
    private static final int CARD_REWARD_AMOUNT = 2;

    private int maxHealthLoss;
    private int healAmount;
    private int goldLoss;
    private AbstractCard cardReward;
    private AbstractCard curse;

    private int screenNum = 0;

    public HungryVoid() {
        super(NAME, DESCRIPTIONS[0], IMG);
        this.noCardsInRewards = true;

        if (AbstractDungeon.ascensionLevel >= 15) {
            this.maxHealthLoss = (int)((float)AbstractDungeon.player.maxHealth * A15_MAX_HEALTH_LOSS_PERCENT);
            this.healAmount = (int)((float)AbstractDungeon.player.maxHealth * A15_HEAL_PERCENT);
            this.goldLoss = A15_GOLD_LOSS;
        }
        else {
            this.maxHealthLoss = (int)((float)AbstractDungeon.player.maxHealth * A15_MAX_HEALTH_LOSS_PERCENT);
            this.healAmount = (int)((float)AbstractDungeon.player.maxHealth * A15_HEAL_PERCENT);
            this.goldLoss = A15_GOLD_LOSS;
        }
        this.cardReward = new BrokenCrystal();
        this.curse = new Decay(); // TODO Replace with a special curse

        imageEventText.setDialogOption(MessageFormat.format(OPTIONS[0], CARD_REWARD_AMOUNT, this.cardReward.name, this.maxHealthLoss), this.cardReward);
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
                        AbstractDungeon.player.decreaseMaxHealth(this.maxHealthLoss);
                        logMetricObtainCardsLoseMapHP(ID, "Shield", cards, this.maxHealthLoss);
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

    private void showColorlessCardReward(int numCards) {
        AbstractDungeon.getCurrRoom().rewards.clear();
        for(int i = 0; i < numCards; ++i) {
            RewardItem reward = new RewardItem(AbstractCard.CardColor.COLORLESS);
            AbstractDungeon.getCurrRoom().addCardReward(reward);
        }

        AbstractDungeon.getCurrRoom().phase = AbstractRoom.RoomPhase.COMPLETE;
        AbstractDungeon.combatRewardScreen.open();
    }

    private void showSpecialCardReward(int numCards) {
        AbstractDungeon.getCurrRoom().rewards.clear();
        for(int i = 0; i < numCards; ++i) {
            RewardItem reward = new RewardItem();
            reward.cards = this.getSpecialCardReward();
            AbstractDungeon.getCurrRoom().addCardReward(reward);
        }

        AbstractDungeon.getCurrRoom().phase = AbstractRoom.RoomPhase.COMPLETE;
        AbstractDungeon.combatRewardScreen.open();
    }

    private ArrayList<AbstractCard> getSpecialCardReward() {
        ArrayList<AbstractCard> rewardCards = new ArrayList<>();

        rewardCards.add(AbstractDungeon.getCard(AbstractCard.CardRarity.RARE));
        rewardCards.add(CardLibrary.getAnyColorCard(AbstractCard.CardRarity.RARE));

        List<AbstractCard> powerfulCards = Arrays.asList(new Apparition(), new Apotheosis());
        rewardCards.add(powerfulCards.get(AbstractDungeon.eventRng.random(powerfulCards.size() - 1)));

        if (Loader.isModLoaded(Abyss.ElementariumModId)) {
            List<String> bladeCardIds = Arrays.asList(
                    "Elementarium:FireblessedBlade",
                    "Elementarium:WindblessedBlade",
                    "Elementarium:EarthblessedBlade",
                    "Elementarium:IceblessedBlade",
                    "Elementarium:VoidblessedBlade"
            );
            String bladeCardId = bladeCardIds.get(AbstractDungeon.eventRng.random(bladeCardIds.size() - 1));
            rewardCards.add(CardLibrary.getCard(bladeCardId).makeCopy());
        }

        if (Loader.isModLoaded(Abyss.MenagerieModId)) {
            List<String> spellCardIds = Arrays.asList(
                    "Menagerie:CrumblingSanctuary",
                    "Menagerie:MirarisWake",
                    "Menagerie:WallOfBlossoms"
                    //TODO Add more
            );
            String spellCardId = spellCardIds.get(AbstractDungeon.eventRng.random(spellCardIds.size() - 1));
            rewardCards.add(CardLibrary.getCard(spellCardId).makeCopy());
        }

        for (AbstractRelic r : AbstractDungeon.player.relics) {
            for (AbstractCard c : rewardCards) {
                r.onPreviewObtainCard(c);
            }
        }

        return new ArrayList<>(rewardCards);
    }
}
