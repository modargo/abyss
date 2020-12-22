package abyss.patches;

import abyss.cards.Drained;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

@SpirePatch(
        clz = CardGroup.class,
        method = "moveToExhaustPile",
        paramtypez = AbstractCard.class
)
public class MoveToExhaustPilePatch {
    @SpirePostfixPatch
    public static void CheckDrained(CardGroup __instance, AbstractCard c) {
        for (AbstractCard card : AbstractDungeon.player.hand.group) {
            if (card.cardID.equals(Drained.ID)) {
                //TODO Consider a visual effect
                AbstractDungeon.actionManager.addToBottom(new DamageAction(AbstractDungeon.player, new DamageInfo(AbstractDungeon.player, Drained.DAMAGE, DamageInfo.DamageType.THORNS), AbstractGameAction.AttackEffect.NONE));
            }
        }
    }
}