package abyss.powers.act4;

import abyss.Abyss;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;

import java.text.MessageFormat;

public class FlimsyDaggersPower extends AbstractPower {
    public static final String POWER_ID = "Abyss:FlimsyDaggers";
    private static final PowerStrings powerStrings;
    public static final String NAME;
    public static final String[] DESCRIPTIONS;
    private static final int ZERO_COST_ATTACK_REDUCTION_PERCENT = 30;

    public FlimsyDaggersPower(AbstractCreature owner) {
        this.name = NAME;
        this.ID = POWER_ID;
        this.owner = owner;
        // Deliberately not a debuff -- part of the Universal Void effects
        this.description = MessageFormat.format(DESCRIPTIONS[0], ZERO_COST_ATTACK_REDUCTION_PERCENT);
        Abyss.LoadPowerImage(this);
    }

    public float atDamageModify(float damage, AbstractCard c) {
        return c.costForTurn != 0 && (!c.freeToPlayOnce || c.cost == -1) ? damage : damage * (100 - ZERO_COST_ATTACK_REDUCTION_PERCENT) / 100.0f;
    }

    static {
        powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
        NAME = powerStrings.NAME;
        DESCRIPTIONS = powerStrings.DESCRIPTIONS;
    }
}