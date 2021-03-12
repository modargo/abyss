package abyss.events;

import abyss.Abyss;
import abyss.cards.CardUtil;
import abyss.relics.BehemothsCourage;
import abyss.relics.BehemothsWisdom;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.colorless.Apparition;
import com.megacrit.cardcrawl.cards.colorless.Bite;
import com.megacrit.cardcrawl.cards.colorless.JAX;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.events.AbstractImageEvent;
import com.megacrit.cardcrawl.localization.EventStrings;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.relics.GoldenIdol;
import com.megacrit.cardcrawl.vfx.cardManip.PurgeCardEffect;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class ElderBehemoth extends AbstractImageEvent {
    public static final String ID = "Abyss:ElderBehemoth";
    private static final EventStrings eventStrings = CardCrawlGame.languagePack.getEventString(ID);
    private static final String NAME = eventStrings.NAME;
    private static final String[] DESCRIPTIONS = eventStrings.DESCRIPTIONS;
    private static final String[] OPTIONS = eventStrings.OPTIONS;
    public static final String IMG = Abyss.eventImage(ID);

    private static final float HEAL_PERCENTAGE = 0.50F;
    private static final float MAX_HEALTH_LOSS_PERCENTAGE = 0.10F;
    private static final float A15_MAX_HEALTH_LOSS_PERCENTAGE = 0.14F;
    private static final int GOLDEN_IDOL_UPGRADES = 4;
    private static final int A15_GOLDEN_IDOL_UPGRADES = 3;
    private static final int BITE_MAX_HEALTH = 3;
    private static final int A15_BITE_MAX_HEALTH = 2;
    private static final int JAX_MAX_HEALTH = 2;
    private static final int A15_JAX_MAX_HEALTH = 1;
    private static final int APPARITION_BLADE_MAX_HEALTH = 20;
    private static final int A15_APPARITION_BLADE_MAX_HEALTH = 15;
    private static final int GILDED_GOLD = 80;
    private static final int A15_GILDED_GOLD = 50;

    private ArrayList<CardCostType> cardOptionsList;
    private Map<CardCostType, AbstractCard> cardOptions;

    private AbstractRelic relicCost;
    private int goldenIdolUpgrades;
    private int healAmount;
    private int maxHealthCost;
    private int biteMaxHealth;
    private int jaxMaxHealth;
    private int apparitionBladeMaxHealth;
    private AbstractRelic apparitionBladeRelic;
    private int gildedGold;
    private AbstractRelic rareRelic;

    private int screenNum = 0;

    public ElderBehemoth() {
        super(NAME, DESCRIPTIONS[0], IMG);

        this.relicCost = AbstractDungeon.player.getRelic(GoldenIdol.ID);
        this.goldenIdolUpgrades = AbstractDungeon.ascensionLevel >= 15 ? A15_GOLDEN_IDOL_UPGRADES : GOLDEN_IDOL_UPGRADES;

        this.cardOptionsList = new ArrayList<>();
        this.cardOptions = new HashMap<>();
        AbstractCard biteCardCost = this.getRandomCardById(Bite.ID);
        if (biteCardCost != null) {
            cardOptionsList.add(CardCostType.Bite);
            cardOptions.put(CardCostType.Bite, biteCardCost);
        }
        AbstractCard jaxCost = this.getRandomCardById(JAX.ID);
        if (jaxCost != null) {
            cardOptionsList.add(CardCostType.Jax);
            cardOptions.put(CardCostType.Jax, jaxCost);
        }
        AbstractCard apparitionCardCost = this.getRandomCardById(Apparition.ID);
        if (apparitionCardCost != null) {
            cardOptionsList.add(CardCostType.Apparition);
            cardOptions.put(CardCostType.Apparition, apparitionCardCost);
        }
        AbstractCard gildedCardCost = this.getRandomCardByTag("GILDED");
        if (gildedCardCost != null) {
            cardOptionsList.add(CardCostType.Gilded);
            cardOptions.put(CardCostType.Gilded, gildedCardCost);
        }
        AbstractCard bladeCardCost = this.getRandomCardByTag("ELEMENTAL_BLADE");
        if (bladeCardCost != null) {
            cardOptionsList.add(CardCostType.Blade);
            cardOptions.put(CardCostType.Blade, bladeCardCost);
        }
        AbstractCard spellCardCost = this.getRandomCardByTag("GRAND_MAGUS_SPELL");
        if (spellCardCost != null) {
            cardOptionsList.add(CardCostType.Spell);
            cardOptions.put(CardCostType.Spell, spellCardCost);
        }
        AbstractCard rareCardCost = this.getRandomCardByRarity(AbstractCard.CardRarity.RARE);
        if (rareCardCost != null) {
            cardOptionsList.add(CardCostType.Rare);
            cardOptions.put(CardCostType.Rare, rareCardCost);
        }
        AbstractCard uncommonCardCost = this.getRandomCardByRarity(AbstractCard.CardRarity.UNCOMMON);
        if (uncommonCardCost != null) {
            cardOptionsList.add(CardCostType.Uncommon);
            cardOptions.put(CardCostType.Uncommon, uncommonCardCost);
        }

        // Trim down the options if there's too many
        if (cardOptions.containsKey(CardCostType.Apparition) && cardOptions.containsKey(CardCostType.Blade)) {
            if (AbstractDungeon.miscRng.randomBoolean()) {
                cardOptionsList.remove(CardCostType.Apparition);
                cardOptions.remove(CardCostType.Apparition);
            }
            else {
                cardOptionsList.remove(CardCostType.Blade);
                cardOptions.remove(CardCostType.Blade);
            }
        }
        ArrayList<CardCostType> biteGildedSpellOptions = new ArrayList<>();
        if (cardOptions.containsKey(CardCostType.Bite)) {
            biteGildedSpellOptions.add(CardCostType.Bite);
        }
        if (cardOptions.containsKey(CardCostType.Jax)) {
            biteGildedSpellOptions.add(CardCostType.Jax);
        }
        if (cardOptions.containsKey(CardCostType.Gilded)) {
            biteGildedSpellOptions.add(CardCostType.Gilded);
        }
        if (cardOptions.containsKey(CardCostType.Spell)) {
            biteGildedSpellOptions.add(CardCostType.Spell);
        }
        if (biteGildedSpellOptions.size() > 1) {
            Collections.shuffle(biteGildedSpellOptions, AbstractDungeon.miscRng.random);
            // Remove all but the first option
            for (int i = 1; i < biteGildedSpellOptions.size(); i++) {
                cardOptionsList.remove(biteGildedSpellOptions.get(i));
                cardOptions.remove(biteGildedSpellOptions.get(i));
            }
        }

        this.healAmount = (int) ((float) AbstractDungeon.player.maxHealth * HEAL_PERCENTAGE);
        this.apparitionBladeRelic = new BehemothsCourage();
        this.rareRelic = new BehemothsWisdom();
        if (AbstractDungeon.ascensionLevel >= 15) {
            this.maxHealthCost = (int) ((float) AbstractDungeon.player.maxHealth * A15_MAX_HEALTH_LOSS_PERCENTAGE);
            this.biteMaxHealth = A15_BITE_MAX_HEALTH;
            this.jaxMaxHealth = A15_JAX_MAX_HEALTH;
            this.apparitionBladeMaxHealth = A15_APPARITION_BLADE_MAX_HEALTH;
            this.gildedGold = A15_GILDED_GOLD;
        }
        else {
            this.maxHealthCost = (int) ((float) AbstractDungeon.player.maxHealth * MAX_HEALTH_LOSS_PERCENTAGE);
            this.biteMaxHealth = BITE_MAX_HEALTH;
            this.jaxMaxHealth = JAX_MAX_HEALTH;
            this.apparitionBladeMaxHealth = APPARITION_BLADE_MAX_HEALTH;
            this.gildedGold = GILDED_GOLD;
        }

        imageEventText.setDialogOption(OPTIONS[0]);
    }

    private AbstractCard getRandomCardByTag(String tagName) {
        ArrayList<AbstractCard> list = new ArrayList<>();
        for (AbstractCard c : AbstractDungeon.player.masterDeck.group) {
            for (AbstractCard.CardTags tag : c.tags) {
                if (tag.name().equals(tagName)) {
                    list.add(c);
                }
            }
        }

        if (list.isEmpty()) {
            return null;
        } else {
            Collections.shuffle(list, AbstractDungeon.miscRng.random);
            return list.get(0);
        }
    }

    private AbstractCard getRandomCardById(String id) {
        ArrayList<AbstractCard> list = new ArrayList<>();
        for (AbstractCard c : AbstractDungeon.player.masterDeck.group) {
            if (c.cardID.equals(id)) {
                list.add(c);
            }
        }

        if (list.isEmpty()) {
            return null;
        } else {
            Collections.shuffle(list, AbstractDungeon.miscRng.random);
            return list.get(0);
        }
    }

    private AbstractCard getRandomCardByRarity(AbstractCard.CardRarity rarity) {
        ArrayList<AbstractCard> list = new ArrayList<>();
        for (AbstractCard c : AbstractDungeon.player.masterDeck.group) {
            if (c.rarity == rarity) {
                list.add(c);
            }
        }

        if (list.isEmpty()) {
            return null;
        } else {
            Collections.shuffle(list, AbstractDungeon.miscRng.random);
            return list.get(0);
        }
    }

    private void setCardOfferDialogOption(CardCostType t) {
        switch (t) {
            case Bite:
                imageEventText.setDialogOption(MessageFormat.format(OPTIONS[4], this.biteMaxHealth, this.cardOptions.get(t).name), this.cardOptions.get(t).makeStatEquivalentCopy());
                break;
            case Jax:
                imageEventText.setDialogOption(MessageFormat.format(OPTIONS[5], this.jaxMaxHealth, this.cardOptions.get(t).name), this.cardOptions.get(t).makeStatEquivalentCopy());
                break;
            case Apparition:
                imageEventText.setDialogOption(MessageFormat.format(OPTIONS[6], this.apparitionBladeMaxHealth, this.apparitionBladeRelic.name, this.cardOptions.get(t).name), this.cardOptions.get(t).makeStatEquivalentCopy(), this.apparitionBladeRelic);
                break;
            case Gilded:
                imageEventText.setDialogOption(MessageFormat.format(OPTIONS[7], this.gildedGold, this.cardOptions.get(t).name), this.cardOptions.get(t).makeStatEquivalentCopy());
                break;
            case Blade:
                imageEventText.setDialogOption(MessageFormat.format(OPTIONS[8], this.apparitionBladeMaxHealth, this.apparitionBladeRelic.name, this.cardOptions.get(t).name), this.cardOptions.get(t).makeStatEquivalentCopy(), this.apparitionBladeRelic);
                break;
            case Spell:
                imageEventText.setDialogOption(MessageFormat.format(OPTIONS[9], this.cardOptions.get(t).name), this.cardOptions.get(t).makeStatEquivalentCopy());
                break;
            case Rare:
                imageEventText.setDialogOption(MessageFormat.format(OPTIONS[10], this.rareRelic.name, this.cardOptions.get(t).name), this.cardOptions.get(t).makeStatEquivalentCopy(), this.rareRelic);
                break;
            case Uncommon:
                imageEventText.setDialogOption(MessageFormat.format(OPTIONS[11], this.cardOptions.get(t).name), this.cardOptions.get(t).makeStatEquivalentCopy());
                break;
            default: throw new RuntimeException("Unrecognized card cost type " + t.name());
        }
    }

    private void offerCard(CardCostType t) {
        AbstractCard cardCost = cardOptions.get(t);
        AbstractDungeon.topLevelEffects.add(new PurgeCardEffect(cardCost, (float)Settings.WIDTH / 2.0F, (float)Settings.HEIGHT / 2.0F));
        AbstractDungeon.player.masterDeck.removeCard(cardCost);
        String choiceName;
        int maxHp = 0;
        int gold = 0;
        AbstractRelic relic = null;
        switch (t) {
            case Bite:
                maxHp = this.biteMaxHealth;
                choiceName = "Vampiric";
                break;
            case Jax:
                maxHp = this.jaxMaxHealth;
                choiceName = "Narcotic";
                break;
            case Apparition:
                maxHp = this.apparitionBladeMaxHealth;
                relic = this.apparitionBladeRelic;
                choiceName = "Ghostly";
                break;
            case Gilded:
                gold = this.gildedGold;
                choiceName = "Gilded";
                break;
            case Blade:
                maxHp = this.apparitionBladeMaxHealth;
                relic = this.apparitionBladeRelic;
                choiceName = "Elemental";
                break;
            case Spell:
                // Nothing
                choiceName = "Magical";
                break;
            case Rare:
                relic = this.rareRelic;
                choiceName = "Exceptional";
                break;
            case Uncommon:
                // Nothing
                choiceName = "Useful";
                break;
            default:
                throw new RuntimeException("Unrecognized card cost type " + t.name());
        }
        if (maxHp != 0) {
            AbstractDungeon.player.increaseMaxHp(maxHp, true);
        }
        if (gold != 0) {
            AbstractDungeon.player.gainGold(gold);
        }
        if (relic != null) {
            AbstractDungeon.getCurrRoom().spawnRelicAndObtain((float)(Settings.WIDTH / 2), (float)(Settings.HEIGHT / 2), relic);
        }
        logMetric(ID, choiceName, null, Collections.singletonList(cardCost.cardID), null, null, relic != null ? Collections.singletonList(relic.relicId) : null, null, null, 0, 0, 0, maxHp, gold, 0);
    }

    @Override
    protected void buttonEffect(int buttonPressed) {
        switch (screenNum) {
            case 0:
                this.imageEventText.updateBodyText(DESCRIPTIONS[1]);
                this.screenNum = 1;
                this.imageEventText.clearAllDialogs();
                this.imageEventText.setDialogOption(MessageFormat.format(OPTIONS[1], this.healAmount, this.maxHealthCost));
                if (this.relicCost != null) {
                    this.imageEventText.setDialogOption(MessageFormat.format(OPTIONS[2], this.goldenIdolUpgrades, this.relicCost.name), this.relicCost);
                } else {
                    this.imageEventText.setDialogOption(MessageFormat.format(OPTIONS[3], new GoldenIdol().name), true);
                }
                for (CardCostType t : this.cardOptionsList) {
                    this.setCardOfferDialogOption(t);
                }
                break;
            case 1:
                if (buttonPressed == 0) {
                    // Offer Appreciation
                    this.imageEventText.updateBodyText(DESCRIPTIONS[2]);
                    CardCrawlGame.sound.play("BLUNT_HEAVY");
                    AbstractDungeon.player.decreaseMaxHealth(this.maxHealthCost);
                    AbstractDungeon.player.heal(this.healAmount);
                    logMetricHealAndLoseMaxHP(ID, "Appreciative", this.healAmount, this.maxHealthCost);
                    this.screenNum = 2;
                    this.imageEventText.updateDialogOption(0, OPTIONS[11]);
                    this.imageEventText.clearRemainingOptions();
                }
                else if (buttonPressed == 1) {
                    // Offer Golden Idol
                    this.imageEventText.updateBodyText(DESCRIPTIONS[3]);
                    AbstractDungeon.player.loseRelic(this.relicCost.relicId);
                    ArrayList<String> upgradedCards = new ArrayList<>();
                    int numUpgrades = AbstractDungeon.ascensionLevel >= 15 ? A15_GOLDEN_IDOL_UPGRADES : GOLDEN_IDOL_UPGRADES;
                    for (int i = 0; i < numUpgrades; i++) {
                        AbstractCard c = CardUtil.upgradeRandomCard();
                        if (c != null) {
                            upgradedCards.add(c.cardID);
                        }
                    }
                    logMetric(ID, "Golden", null, null, null, upgradedCards, null, null, Collections.singletonList(this.relicCost.relicId), 0, 0, 0, 0, 0, 0);
                    this.screenNum = 2;
                    this.imageEventText.updateDialogOption(0, OPTIONS[11]);
                    this.imageEventText.clearRemainingOptions();
                    break;
                }
                else {
                    CardCostType t = this.cardOptionsList.get(buttonPressed - 2);
                    if (t == CardCostType.Uncommon || t == CardCostType.Spell) {
                        this.imageEventText.updateBodyText(DESCRIPTIONS[4]);
                    }
                    else {
                        this.imageEventText.updateBodyText(DESCRIPTIONS[5]);
                    }
                    this.offerCard(t);
                    this.screenNum = 2;
                    this.imageEventText.updateDialogOption(0, OPTIONS[11]);
                    this.imageEventText.clearRemainingOptions();
                }
                break;
            default:
                this.openMap();
                break;
        }
    }

    private enum CardCostType {
        Bite,
        Jax,
        Apparition,
        Gilded,
        Blade,
        Spell,
        Rare,
        Uncommon
    }
}
