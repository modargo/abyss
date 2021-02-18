package abyss.powers.act4;

import abyss.Abyss;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.StrengthPower;

public class StrengthDegenerationPower extends AbstractPower {
    public static final String POWER_ID = "Abyss:StrengthDegeneration";
    private static final PowerStrings powerStrings;
    public static final String NAME;
    public static final String[] DESCRIPTIONS;
    private static final int DEGEN_PERCENT = 20;

    public StrengthDegenerationPower(AbstractCreature owner) {
        this.name = NAME;
        this.ID = POWER_ID;
        this.owner = owner;
        // Deliberately not a debuff -- part of the Universal Void effects
        this.description = DESCRIPTIONS[0].replace("{0}", DEGEN_PERCENT + "");
        Abyss.LoadPowerImage(this);
    }

    @Override
    public void atStartOfTurn() {
        this.flash();
        AbstractPower strengthPower = this.owner.getPower(StrengthPower.POWER_ID);
        int currentStrength = strengthPower != null ? strengthPower.amount : 0;

        int strengthLoss = currentStrength > 0 ? (currentStrength * DEGEN_PERCENT) / 100 : 0;
        if (strengthLoss > 0) {
            AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(this.owner, this.owner, new StrengthPower(this.owner, -strengthLoss), -strengthLoss));
        }
    }

    static {
        powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
        NAME = powerStrings.NAME;
        DESCRIPTIONS = powerStrings.DESCRIPTIONS;
    }
}