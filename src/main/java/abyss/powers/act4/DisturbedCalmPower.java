package abyss.powers.act4;

import abyss.Abyss;
import com.megacrit.cardcrawl.actions.unique.LoseEnergyAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.stances.AbstractStance;
import com.megacrit.cardcrawl.stances.CalmStance;
import com.megacrit.cardcrawl.stances.DivinityStance;

public class DisturbedCalmPower extends AbstractPower {
    public static final String POWER_ID = "Abyss:DisturbedCalm";
    private static final PowerStrings powerStrings;
    public static final String NAME;
    public static final String[] DESCRIPTIONS;

    public DisturbedCalmPower(AbstractCreature owner) {
        this.name = NAME;
        this.ID = POWER_ID;
        this.owner = owner;
        // Deliberately not a debuff -- part of the Universal Void effects
        this.description = DESCRIPTIONS[0].replace("{0}", this.amount + "");
        Abyss.LoadPowerImage(this);
    }

    public void afterChangeStance(AbstractStance oldStance, AbstractStance newStance) {
        if (!oldStance.ID.equals(newStance.ID)) {
            if (oldStance.ID.equals(CalmStance.STANCE_ID)) {
                this.flash();
                this.addToBot(new LoseEnergyAction(1));
            }
            if (newStance.ID.equals(DivinityStance.STANCE_ID)) {
                this.flash();
                this.addToBot(new LoseEnergyAction(1));
            }
        }
    }

    static {
        powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
        NAME = powerStrings.NAME;
        DESCRIPTIONS = powerStrings.DESCRIPTIONS;
    }
}