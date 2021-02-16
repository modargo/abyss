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
    public static final int POISON_REDUCTION_PERCENT = 50;

    public FeeblePoisonPower(AbstractCreature owner) {
        this.name = NAME;
        this.ID = POWER_ID;
        this.owner = owner;
        // Deliberately not a debuff -- part of the Universal Void effects
        this.description = DESCRIPTIONS[0].replace("{0}", POISON_REDUCTION_PERCENT + "");
        Abyss.LoadPowerImage(this);
    }

    // This power is just a placeholder -- the actual implementation is in a patch

    static {
        powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
        NAME = powerStrings.NAME;
        DESCRIPTIONS = powerStrings.DESCRIPTIONS;
    }
}