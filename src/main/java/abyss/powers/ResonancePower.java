package abyss.powers;

import abyss.Abyss;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.cards.DamageInfo.DamageType;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;

import java.text.MessageFormat;

public class ResonancePower extends AbstractPower {
    public static final String POWER_ID = "Abyss:Resonance";
    private static final PowerStrings powerStrings;
    public static final String NAME;
    public static final String[] DESCRIPTIONS;
    private boolean justApplied;

    public ResonancePower(AbstractCreature owner, int amount, boolean justApplied) {
        this.name = NAME;
        this.ID = POWER_ID;
        this.owner = owner;
        this.amount = amount;
        this.justApplied = justApplied;
        this.updateDescription();
        this.priority = 50;
        Abyss.LoadPowerImage(this);
    }

    @Override
    public void updateDescription() {
        this.description = MessageFormat.format(DESCRIPTIONS[0], this.amount);
    }

    @Override
    public float atDamageFinalReceive(float damage, DamageType type) {
        return this.calculateDamageTakenAmount(damage, type);
    }

    private float calculateDamageTakenAmount(float damage, DamageType type) {
        return type != DamageType.HP_LOSS && type != DamageType.THORNS ? damage * (1 - (this.amount / 100.0F)) : damage;
    }

    @Override
    public void atEndOfTurn(boolean isPlayer) {
        if (this.justApplied) {
            this.justApplied = false;
        } else {
            this.flash();
            this.addToBot(new RemoveSpecificPowerAction(this.owner, this.owner, this));
        }
    }

    static {
        powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
        NAME = powerStrings.NAME;
        DESCRIPTIONS = powerStrings.DESCRIPTIONS;
    }
}

