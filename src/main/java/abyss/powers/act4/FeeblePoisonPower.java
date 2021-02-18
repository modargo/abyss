package abyss.powers.act4;

import abyss.Abyss;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;

public class FeeblePoisonPower extends AbstractPower {
    public static final String POWER_ID = "Abyss:FeeblePoison";
    private static final PowerStrings powerStrings;
    public static final String NAME;
    public static final String[] DESCRIPTIONS;
    private static final int POISON_REDUCTION_PERCENT = 30;

    public FeeblePoisonPower(AbstractCreature owner) {
        this.name = NAME;
        this.ID = POWER_ID;
        this.owner = owner;
        // Deliberately not a debuff -- part of the Universal Void effects
        this.description = DESCRIPTIONS[0].replace("{0}", POISON_REDUCTION_PERCENT + "");
        Abyss.LoadPowerImage(this);
    }

    public int modifyPoisonAmount(int poisonAmount) {
        int newAmount = (poisonAmount * (100 - FeeblePoisonPower.POISON_REDUCTION_PERCENT)) / 100;
        return poisonAmount > 0 && newAmount == 0 ? 1 : newAmount;
    }

    static {
        powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
        NAME = powerStrings.NAME;
        DESCRIPTIONS = powerStrings.DESCRIPTIONS;
    }
}