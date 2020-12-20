package abyss.powers;

import abyss.Abyss;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.HealAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.StrengthPower;

import java.text.MessageFormat;

public class ThoughtStealerPower extends AbstractPower {
    public static final String POWER_ID = "Abyss:ThoughtStealer";
    private static final PowerStrings powerStrings;
    public static final String NAME;
    public static final String[] DESCRIPTIONS;
    private static final int STRENGTH_GAIN = 1;
    private static final int HEAL = 20;
    private static final int INITIAL_SAFE_DRAW = 5;
    private static final int TRIGGER_THRESHOLD = 6;
    private int cardsDrawn = 0;

    public ThoughtStealerPower(AbstractCreature owner) {
        this.name = NAME;
        this.ID = POWER_ID;
        this.owner = owner;
        this.amount = 0;
        this.updateDescription();
        Abyss.LoadPowerImage(this);
    }

    @Override
    public void atEndOfRound() {
        this.cardsDrawn = 0;
    }

    @Override
    public void onCardDraw(AbstractCard card) {
        this.cardsDrawn++;
        if (this.cardsDrawn > INITIAL_SAFE_DRAW) {
            this.amount++;
        }
        if (this.amount >= TRIGGER_THRESHOLD) {
            for (AbstractMonster m : AbstractDungeon.getMonsters().monsters) {
                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(m, this.owner, new StrengthPower(m, STRENGTH_GAIN), STRENGTH_GAIN));
                AbstractDungeon.actionManager.addToBottom(new HealAction(m, this.owner, HEAL));
            }
            this.amount -= TRIGGER_THRESHOLD;
            this.flash();
        }
        this.updateDescription();
    }

    @Override
    public void updateDescription() {
        this.description = MessageFormat.format(DESCRIPTIONS[0], INITIAL_SAFE_DRAW);
    }

    static {
        powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
        NAME = powerStrings.NAME;
        DESCRIPTIONS = powerStrings.DESCRIPTIONS;
    }
}
