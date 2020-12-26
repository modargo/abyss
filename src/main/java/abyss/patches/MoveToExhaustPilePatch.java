package abyss.patches;

import abyss.cards.Drained;
import abyss.monsters.MonsterUtil;
import abyss.powers.crystals.RedCrystalPower;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.AbstractPower;

import java.util.List;

@SpirePatch(
        clz = CardGroup.class,
        method = "moveToExhaustPile",
        paramtypez = AbstractCard.class
)
public class MoveToExhaustPilePatch {
    @SpirePrefixPatch
    public static void CallRedCrystalPower(CardGroup __instance, AbstractCard c) {
        List<AbstractPower> powers = MonsterUtil.getMonsterPowers(RedCrystalPower.POWER_ID);
        for (AbstractPower p : powers) {
            ((RedCrystalPower)p).onPlayerExhaust(c);
        }
    }

    @SpirePostfixPatch
    public static void CheckDrained(CardGroup __instance, AbstractCard c) {
        for (AbstractCard card : AbstractDungeon.player.hand.group) {
            if (card.cardID.equals(Drained.ID)) {
                ((Drained)card).onPlayerCardExhausted(c);
            }
        }
    }
}