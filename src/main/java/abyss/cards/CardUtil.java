package abyss.cards;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.CardLibrary;
import com.megacrit.cardcrawl.unlock.UnlockTracker;
import com.megacrit.cardcrawl.vfx.UpgradeShineEffect;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardBrieflyEffect;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class CardUtil {
    public static AbstractCard upgradeRandomCard() {
        ArrayList<AbstractCard> upgradableCards = new ArrayList<>();
        for (AbstractCard c : AbstractDungeon.player.masterDeck.group) {
            if (c.canUpgrade()) {
                upgradableCards.add(c);
            }
        }

        if (!upgradableCards.isEmpty()) {
            AbstractDungeon.topLevelEffects.add(new UpgradeShineEffect((float)Settings.WIDTH / 2.0F, (float)Settings.HEIGHT / 2.0F));
            AbstractCard card = upgradableCards.get(AbstractDungeon.miscRng.random(upgradableCards.size() - 1));
            card.upgrade();
            AbstractDungeon.player.bottledCardUpgradeCheck(card);
            AbstractDungeon.effectList.add(new ShowCardBrieflyEffect(card.makeStatEquivalentCopy()));
            return card;
        }
        return null;
    }

    public static AbstractCard getOtherColorCard(AbstractCard.CardRarity rarity, List<AbstractCard.CardColor> excludedColors) {
        CardGroup anyCard = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);
        Iterator<Map.Entry<String, AbstractCard>> var2 = CardLibrary.cards.entrySet().iterator();

        while(true) {
            Map.Entry<String, AbstractCard> c;
            do {
                do {
                    do {
                        do {
                            if (!var2.hasNext()) {
                                anyCard.shuffle(AbstractDungeon.cardRng);
                                return anyCard.getRandomCard(true, rarity).makeCopy();
                            }

                            c = var2.next();
                        } while(c.getValue().rarity != rarity || excludedColors.contains(c.getValue().color));
                    } while(c.getValue().type == AbstractCard.CardType.CURSE);
                } while(c.getValue().type == AbstractCard.CardType.STATUS);
            } while(UnlockTracker.isCardLocked(c.getKey()) && !Settings.treatEverythingAsUnlocked());

            anyCard.addToBottom(c.getValue());
        }
    }
}
