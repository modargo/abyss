package abyss.powers;

import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.IntangiblePower;

import java.text.MessageFormat;

public class FixedTextIntangiblePower extends IntangiblePower {
    public static final String POWER_ID = "Abyss:FixedTextIntangiblePower";
    private static final PowerStrings powerStrings;
    public static final String[] DESCRIPTIONS;

    public FixedTextIntangiblePower(AbstractCreature owner, int turns) {
        super(owner, turns);
    }

    @Override
    public void updateDescription() {
        this.description = MessageFormat.format(DESCRIPTIONS[0], this.amount);
    }

    static {
        powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
        DESCRIPTIONS = powerStrings.DESCRIPTIONS;
    }
}
