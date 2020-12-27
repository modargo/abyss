package abyss.powers;

import abyss.Abyss;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.GainStrengthPower;

public class CrystalLinkPower extends AbstractPower {
    public static final String POWER_ID = "Abyss:CrystalLink";
    private static final PowerStrings powerStrings;
    public static final String NAME;
    public static final String[] DESCRIPTIONS;

    public CrystalLinkPower(AbstractCreature owner, int amount) {
        this.name = NAME;
        this.ID = POWER_ID;
        this.owner = owner;
        this.amount = amount;
        this.updateDescription();
        Abyss.LoadPowerImage(this);
    }

    @Override
    public void updateDescription() {
        this.description = DESCRIPTIONS[0].replace("{0}", this.amount + "");
    }

    public void onCrystalDeath(AbstractMonster crystal, int strength) {
        this.flash();
        int gain = (int)((this.amount / 100.0F) * strength);
        AbstractPower gainStrengthPower = new GainStrengthPower(this.owner, gain);
        gainStrengthPower.type = PowerType.BUFF;
        this.addToBot(new ApplyPowerAction(this.owner, crystal, gainStrengthPower, gain));
    }

    static {
        powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
        NAME = powerStrings.NAME;
        DESCRIPTIONS = powerStrings.DESCRIPTIONS;
    }
}
