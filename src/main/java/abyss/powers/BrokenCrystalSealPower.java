package abyss.powers;

import abyss.Abyss;
import abyss.cards.BrokenCrystal;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInDrawPileAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.RegenerateMonsterPower;

import java.text.MessageFormat;

public class BrokenCrystalSealPower extends AbstractPower {
    public static final String POWER_ID = "Abyss:BrokenCrystalSeal";
    private static final PowerStrings powerStrings;
    public static final String NAME;
    public static final String[] DESCRIPTIONS;

    public BrokenCrystalSealPower(AbstractCreature owner, int amount) {
        this.name = NAME;
        this.ID = POWER_ID;
        this.owner = owner;
        this.amount = amount;
        this.updateDescription();
        Abyss.LoadPowerImage(this);
    }

    @Override
    public void atEndOfTurn(boolean isPlayer) {
        AbstractPower regenPower = this.owner.getPower(RegenerateMonsterPower.POWER_ID);
        if (regenPower != null) {
            AbstractDungeon.actionManager.addToBottom(new MakeTempCardInDrawPileAction(new BrokenCrystal(), this.amount, true, true));
        }
        else {
            AbstractDungeon.actionManager.addToBottom(new RemoveSpecificPowerAction(this.owner, this.owner, this));
        }
    }

    @Override
    public void updateDescription() {
        this.description = MessageFormat.format(DESCRIPTIONS[0], this.amount);
    }

    static {
        powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
        NAME = powerStrings.NAME;
        DESCRIPTIONS = powerStrings.DESCRIPTIONS;
    }
}
