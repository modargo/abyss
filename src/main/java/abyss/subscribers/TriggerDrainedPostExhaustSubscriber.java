package abyss.subscribers;

import abyss.cards.Drained;
import basemod.interfaces.PostExhaustSubscriber;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

public class TriggerDrainedPostExhaustSubscriber implements PostExhaustSubscriber {
    @Override
    public void receivePostExhaust(AbstractCard card) {
        for (AbstractCard c : AbstractDungeon.player.hand.group) {
            if (c.cardID.equals(Drained.ID)) {
                ((Drained)c).onPlayerCardExhausted(card);
            }
        }
    }
}
