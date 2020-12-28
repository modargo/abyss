package abyss.powers;

import abyss.Abyss;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.GainStrengthPower;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class CrystalLinkPower extends AbstractPower {
    public static final Logger logger = LogManager.getLogger(CrystalLinkPower.class.getName());
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

        logger.info("Crystal Link strength gain: " + gain + " from " + crystal.id);
    }

    static {
        powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
        NAME = powerStrings.NAME;
        DESCRIPTIONS = powerStrings.DESCRIPTIONS;
    }
}
