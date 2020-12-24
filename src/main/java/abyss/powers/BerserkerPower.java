package abyss.powers;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.StrengthPower;

import java.text.MessageFormat;

public class BerserkerPower extends AbstractPower {
    public static final String POWER_ID = "Abyss:Berserker";
    private static final PowerStrings powerStrings;
    public static final String NAME;
    public static final String[] DESCRIPTIONS;

    public BerserkerPower(AbstractCreature owner, int amount) {
        this.name = NAME;
        this.ID = POWER_ID;
        this.owner = owner;
        this.amount = amount;
        this.updateDescription();
        this.loadRegion("berserk");
    }

    @Override
    public void atEndOfRound() {
        boolean hasFriends = false;
        for (AbstractMonster m : AbstractDungeon.getCurrRoom().monsters.monsters) {
            hasFriends = hasFriends || (m != null && m != this.owner && !m.isDying);
        }
        if (!hasFriends) {
            this.flash();
            this.addToTop(new ApplyPowerAction(this.owner, this.owner, new StrengthPower(this.owner, this.amount), this.amount));
            this.addToTop(new RemoveSpecificPowerAction(this.owner, this.owner, this));
        }
    }

    @Override
    public void updateDescription() {
        this.description = MessageFormat.format(DESCRIPTIONS[0], this.amount);
    }

    static {
        powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
        NAME = powerStrings.NAME;
        DESCRIPTIONS = powerStrings.DESCRIPTIONS;
    }
}
