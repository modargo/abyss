package abyss.powers.act4;

import abyss.Abyss;
import com.megacrit.cardcrawl.actions.unique.LoseEnergyAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;

public class VoidEmbracePower extends AbstractPower {
    public static final String POWER_ID = "Abyss:VoidEmbrace";
    private static final PowerStrings powerStrings;
    public static final String NAME;
    public static final String[] DESCRIPTIONS;

    public VoidEmbracePower(AbstractCreature owner) {
        this.name = NAME;
        this.ID = POWER_ID;
        this.owner = owner;
        this.amount = 0;
        this.isTurnBased = true;
        // Deliberately not a debuff -- part of the Universal Void effects
        this.description = DESCRIPTIONS[0];
        Abyss.LoadPowerImage(this);
    }

    @Override
    public void atStartOfTurn() {
        if (this.amount == 0) {
            this.flash();
            AbstractDungeon.player.gameHandSize--;
        }
        else {
            this.addToBot(new LoseEnergyAction(1));
        }
    }

    @Override
    public void atStartOfTurnPostDraw() {
        if (this.amount == 0) {
            AbstractDungeon.player.gameHandSize++;
        }
        this.amount = (this.amount + 1) % 2;
    }

    static {
        powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
        NAME = powerStrings.NAME;
        DESCRIPTIONS = powerStrings.DESCRIPTIONS;
    }
}