package abyss.powers.act4;

import abyss.Abyss;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;

public class ReducedCombatHealingPower extends AbstractPower {
    public static final String POWER_ID = "Abyss:ReducedCombatHealing";
    private static final PowerStrings powerStrings;
    public static final String NAME;
    public static final String[] DESCRIPTIONS;
    private static final int HEAL_MULTIPLIER = 30;

    public ReducedCombatHealingPower(AbstractCreature owner) {
        this.name = NAME;
        this.ID = POWER_ID;
        this.owner = owner;
        // Deliberately not a debuff -- part of the Universal Void effects
        this.description = DESCRIPTIONS[0].replace("{0}", HEAL_MULTIPLIER + "");
        Abyss.LoadPowerImage(this);
    }

    @Override
    public int onHeal(int healAmount) {
        this.flash();
        return (healAmount * (100 - HEAL_MULTIPLIER)) / 100;
    }

    static {
        powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
        NAME = powerStrings.NAME;
        DESCRIPTIONS = powerStrings.DESCRIPTIONS;
    }
}