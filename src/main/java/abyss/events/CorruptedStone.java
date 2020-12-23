package abyss.events;

import abyss.Abyss;
import com.evacipated.cardcrawl.modthespire.Loader;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.colorless.Apotheosis;
import com.megacrit.cardcrawl.cards.colorless.Apparition;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.events.AbstractImageEvent;
import com.megacrit.cardcrawl.helpers.CardLibrary;
import com.megacrit.cardcrawl.localization.EventStrings;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.rewards.RewardItem;
import com.megacrit.cardcrawl.rooms.AbstractRoom;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CorruptedStone extends AbstractImageEvent {
    public static final String ID = "Abyss:CorruptedStone";
    private static final EventStrings eventStrings = CardCrawlGame.languagePack.getEventString(ID);
    private static final String NAME = eventStrings.NAME;
    private static final String[] DESCRIPTIONS = eventStrings.DESCRIPTIONS;
    private static final String[] OPTIONS = eventStrings.OPTIONS;
    public static final String IMG = Abyss.eventImage(ID);
    private static final float MAX_HEALTH_LOSS_PERCENT = 0.12F;
    private static final float A15_MAX_HEALTH_LOSS_PERCENT = 0.16F;
    private static final int MAX_HEALTH_LOSS_FLOOR = 4;
    private static final int A15_MAX_HEALTH_LOSS_FLOOR = 5;

    private int maxHealthLoss;
    private int screenNum = 0;

    public CorruptedStone() {
        super(NAME, DESCRIPTIONS[0], IMG);
        this.noCardsInRewards = true;

        float maxHealthLossPercent = AbstractDungeon.ascensionLevel >= 15 ? A15_MAX_HEALTH_LOSS_PERCENT : MAX_HEALTH_LOSS_PERCENT;
        int maxHealthLossFloor = AbstractDungeon.ascensionLevel >= 15 ? A15_MAX_HEALTH_LOSS_FLOOR : MAX_HEALTH_LOSS_FLOOR;
        this.maxHealthLoss = Math.max((int)((float)AbstractDungeon.player.maxHealth * maxHealthLossPercent), maxHealthLossFloor);

        imageEventText.setDialogOption(OPTIONS[0]);
        imageEventText.setDialogOption(MessageFormat.format(OPTIONS[1], this.maxHealthLoss));
        imageEventText.setDialogOption(OPTIONS[2]);
    }

    @Override
    protected void buttonEffect(int buttonPressed) {
        switch (screenNum) {
            case 0:
                switch (buttonPressed) {
                    case 0: // Observe
                        this.imageEventText.updateBodyText(DESCRIPTIONS[1]);
                        logMetric(ID, "Observe");
                        this.showColorlessCardReward(1);
                        this.screenNum = 2;
                        this.imageEventText.updateDialogOption(0, OPTIONS[4]);
                        this.imageEventText.clearRemainingOptions();
                        break;
                    case 1: // Touch
                        this.imageEventText.updateBodyText(DESCRIPTIONS[2]);
                        this.screenNum = 1;
                        this.imageEventText.updateDialogOption(0, OPTIONS[3]);
                        this.imageEventText.clearRemainingOptions();
                        break;
                    default: // Bypass
                        this.imageEventText.updateBodyText(DESCRIPTIONS[3]);
                        logMetricIgnored(ID);
                        this.screenNum = 2;
                        this.imageEventText.updateDialogOption(0, OPTIONS[4]);
                        this.imageEventText.clearRemainingOptions();
                        break;
                }
                break;
            case 1:
                this.imageEventText.updateBodyText(DESCRIPTIONS[3]);
                AbstractDungeon.player.decreaseMaxHealth(this.maxHealthLoss);
                logMetricMaxHPLoss(ID, "Touch", this.maxHealthLoss);
                this.showSpecialCardReward(1);
                this.imageEventText.updateDialogOption(0, OPTIONS[4]);
                this.imageEventText.clearRemainingOptions();
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
        rewardCards.add(powerfulCards.get(AbstractDungeon.miscRng.random(powerfulCards.size() - 1)));

        if (Loader.isModLoaded(Abyss.ElementariumModId)) {
            List<String> bladeCardIds = Arrays.asList(
                    "Elementarium:FireblessedBlade",
                    "Elementarium:WindblessedBlade",
                    "Elementarium:EarthblessedBlade",
                    "Elementarium:IceblessedBlade",
                    "Elementarium:VoidblessedBlade"
            );
            String bladeCardId = bladeCardIds.get(AbstractDungeon.miscRng.random(bladeCardIds.size() - 1));
            rewardCards.add(CardLibrary.getCard(bladeCardId).makeCopy());
        }

        if (Loader.isModLoaded(Abyss.MenagerieModId)) {
            List<String> spellCardIds = Arrays.asList(
                    "Menagerie:CrumblingSanctuary",
                    "Menagerie:MirarisWake",
                    "Menagerie:QuirionDryad"
            );

            String spellCardId = spellCardIds.get(AbstractDungeon.miscRng.random(spellCardIds.size() - 1));
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
