package abyss.powers.act4;

import abyss.Abyss;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.orbs.AbstractOrb;
import com.megacrit.cardcrawl.orbs.Dark;
import com.megacrit.cardcrawl.orbs.Lightning;
import com.megacrit.cardcrawl.powers.AbstractPower;

import java.text.MessageFormat;

public class WeakOrbsPower extends AbstractPower {
    public static final String POWER_ID = "Abyss:WeakOrbs";
    private static final PowerStrings powerStrings;
    public static final String NAME;
    public static final String[] DESCRIPTIONS;
    private static final int DARK_LIGHTING_ORB_REDUCTION_PERCENT = 30;

    public WeakOrbsPower(AbstractCreature owner) {
        this.name = NAME;
        this.ID = POWER_ID;
        this.owner = owner;
        // Deliberately not a debuff -- part of the Universal Void effects
        this.description = MessageFormat.format(DESCRIPTIONS[0], DARK_LIGHTING_ORB_REDUCTION_PERCENT);
        Abyss.LoadPowerImage(this);
    }

    public int modifyOrbPassiveAmount(AbstractOrb orb, int amount) {
        return orb.ID.equals(Lightning.ORB_ID) ? (int)(amount * (100 - DARK_LIGHTING_ORB_REDUCTION_PERCENT) / 100.0f) : amount;
    }

    public int modifyOrbEvokeAmount(AbstractOrb orb, int amount) {
        return orb.ID.equals(Lightning.ORB_ID) || orb.ID.equals(Dark.ORB_ID) ? (int)(amount * (100 - DARK_LIGHTING_ORB_REDUCTION_PERCENT) / 100.0f) : amount;
    }

    static {
        powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
        NAME = powerStrings.NAME;
        DESCRIPTIONS = powerStrings.DESCRIPTIONS;
    }
}