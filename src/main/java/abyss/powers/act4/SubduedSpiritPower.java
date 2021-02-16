package abyss.powers.act4;

import abyss.Abyss;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.stances.DivinityStance;
import com.megacrit.cardcrawl.stances.WrathStance;

public class SubduedSpiritPower extends AbstractPower {
    public static final String POWER_ID = "Abyss:SubduedSpirit";
    private static final PowerStrings powerStrings;
    public static final String NAME;
    public static final String[] DESCRIPTIONS;

    public SubduedSpiritPower(AbstractCreature owner) {
        this.name = NAME;
        this.ID = POWER_ID;
        this.owner = owner;
        // Deliberately not a debuff -- part of the Universal Void effects
        this.description = DESCRIPTIONS[0].replace("{0}", this.amount + "");
        Abyss.LoadPowerImage(this);
    }

    @Override
    public float atDamageGive(float damage, DamageInfo.DamageType type) {
        if (type != DamageInfo.DamageType.NORMAL) {
            return damage;
        }
        if (AbstractDungeon.player.stance.ID.equals(WrathStance.STANCE_ID) || AbstractDungeon.player.stance.ID.equals(DivinityStance.STANCE_ID)) {
            return (damage * 2.0F) / 3.0F;
        }
        return damage;
    }

    static {
        powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
        NAME = powerStrings.NAME;
        DESCRIPTIONS = powerStrings.DESCRIPTIONS;
    }
}