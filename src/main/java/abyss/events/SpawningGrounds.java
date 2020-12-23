package abyss.events;

import abyss.Abyss;
import abyss.cards.CardUtil;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.events.AbstractImageEvent;
import com.megacrit.cardcrawl.localization.EventStrings;
import com.megacrit.cardcrawl.rewards.RewardItem;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.vfx.UpgradeShineEffect;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardBrieflyEffect;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class SpawningGrounds extends AbstractImageEvent {
    public static final String ID = "Abyss:SpawningGrounds";
    private static final EventStrings eventStrings = CardCrawlGame.languagePack.getEventString(ID);
    private static final String NAME = eventStrings.NAME;
    private static final String[] DESCRIPTIONS = eventStrings.DESCRIPTIONS;
    private static final String[] OPTIONS = eventStrings.OPTIONS;
    public static final String IMG = Abyss.eventImage(ID);
    private static final int DAMAGE = 8;
    private static final int A15_DAMAGE = 10;
    private int damage;

    private int screenNum = 0;

    public SpawningGrounds() {
        super(NAME, DESCRIPTIONS[0], IMG);
        this.noCardsInRewards = true;

        this.damage = AbstractDungeon.ascensionLevel >= 15 ? A15_DAMAGE : DAMAGE;

        this.imageEventText.setDialogOption(OPTIONS[0]);
    }

    @Override
    public void update() {
        super.update();
        if (!AbstractDungeon.isScreenUp && !AbstractDungeon.gridSelectScreen.selectedCards.isEmpty()) {
            AbstractCard c = AbstractDungeon.gridSelectScreen.selectedCards.get(0);
            c.upgrade();
            AbstractDungeon.player.bottledCardUpgradeCheck(c);
            AbstractDungeon.effectsQueue.add(new ShowCardBrieflyEffect(c.makeStatEquivalentCopy()));
            AbstractDungeon.topLevelEffects.add(new UpgradeShineEffect((float)Settings.WIDTH / 2.0F, (float)Settings.HEIGHT / 2.0F));
            AbstractDungeon.gridSelectScreen.selectedCards.clear();
            logMetric(ID, "Enter", null, null, null, Collections.singletonList(c.cardID),null, null,null, this.damage, 0, 0, 0, 0, 0);
        }
    }

    @Override
    protected void buttonEffect(int buttonPressed) {
        switch (screenNum) {
            case 0:
                this.imageEventText.updateBodyText(DESCRIPTIONS[1]);
                this.screenNum = 1;
                this.imageEventText.clearAllDialogs();
                this.imageEventText.setDialogOption(MessageFormat.format(OPTIONS[1], this.damage));
                this.imageEventText.setDialogOption(MessageFormat.format(OPTIONS[2], this.damage));
                this.imageEventText.setDialogOption(OPTIONS[3]);
                break;
            case 1:
                switch (buttonPressed) {
                    case 0: // Familiar
                        this.imageEventText.updateBodyText(DESCRIPTIONS[2]);
                        AbstractDungeon.player.damage(new DamageInfo(AbstractDungeon.player, this.damage));
                        if (CardGroup.getGroupWithoutBottledCards(AbstractDungeon.player.masterDeck.getUpgradableCards()).size() > 0) {
                            AbstractDungeon.gridSelectScreen.open(AbstractDungeon.player.masterDeck.getUpgradableCards(), 1, OPTIONS[3], true, false, false, false);
                        }
                        this.screenNum = 2;
                        this.imageEventText.updateDialogOption(0, OPTIONS[3]);
                        this.imageEventText.clearRemainingOptions();
                        break;
                    case 1: // Unfamiliar
                        this.imageEventText.updateBodyText(DESCRIPTIONS[2]);
                        AbstractDungeon.player.damage(new DamageInfo(AbstractDungeon.player, this.damage));
                        this.showCardReward(1);
                        this.screenNum = 2;
                        this.imageEventText.updateDialogOption(0, OPTIONS[3]);
                        this.imageEventText.clearRemainingOptions();
                    case 2: // Turn Back
                        this.imageEventText.updateBodyText(DESCRIPTIONS[3]);
                        logMetricIgnored(ID);
                        this.screenNum = 2;
                        this.imageEventText.updateDialogOption(0, OPTIONS[4]);
                        this.imageEventText.clearRemainingOptions();
                        break;
                }
                break;
            default:
                this.openMap();
                break;
        }
    }

    private void showCardReward(int numRewards) {
        AbstractDungeon.getCurrRoom().rewards.clear();
        for(int i = 0; i < numRewards; ++i) {
            RewardItem reward = new RewardItem();
            ArrayList<AbstractCard> cards = new ArrayList<>();
            //We see what was already generated and use that, to avoid advancing the rare counter further
            for (AbstractCard c : reward.cards) {
                AbstractCard.CardRarity rarity = c.rarity == AbstractCard.CardRarity.COMMON || c.rarity == AbstractCard.CardRarity.UNCOMMON || c.rarity == AbstractCard.CardRarity.RARE ? c.rarity : AbstractCard.CardRarity.COMMON;
                cards.add(CardUtil.getOtherColorCard(rarity, Arrays.asList(AbstractDungeon.player.getCardColor(), AbstractCard.CardColor.COLORLESS)));
            }
            reward.cards = cards;
            AbstractDungeon.getCurrRoom().addCardReward(reward);
        }

        AbstractDungeon.getCurrRoom().phase = AbstractRoom.RoomPhase.COMPLETE;
        AbstractDungeon.combatRewardScreen.open();
    }
}