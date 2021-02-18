package abyss.powers.act4;

import abyss.Abyss;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.ArtifactPower;

import java.text.MessageFormat;

public class EmptinessPower extends AbstractPower {
    public static final String POWER_ID = "Abyss:Emptiness";
    private static final PowerStrings powerStrings;
    public static final String NAME;
    public static final String[] DESCRIPTIONS;
    private static final int THRESHOLD = 10;
    private static final int ARTIFACT = 1;
    private int damage;
    private boolean attackedThisTurn;

    public EmptinessPower(AbstractCreature owner) {
        this.name = NAME;
        this.ID = POWER_ID;
        this.owner = owner;
        this.updateDescription();
        Abyss.LoadPowerImage(this);
    }

    @Override
    public void atStartOfTurn() {
        this.damage = 0;
        this.attackedThisTurn = false;
        this.updateDescription();
    }

    @Override
    public void onAttack(DamageInfo info, int damageAmount, AbstractCreature target) {
        if (info.type != DamageInfo.DamageType.THORNS) {
            this.attackedThisTurn = true;
        }
    }

    @Override
    public void onInflictDamage(DamageInfo info, int damageAmount, AbstractCreature target) {
        if (damageAmount > 0 && info.type != DamageInfo.DamageType.THORNS) {
            this.damage += damageAmount;
        }
    }

    @Override
    public void atEndOfTurn(boolean isPlayer) {
        if (this.attackedThisTurn && this.damage <= 10) {
            AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(this.owner, this.owner, new ArtifactPower(this.owner, ARTIFACT), ARTIFACT));
        }
    }

    @Override
    public void updateDescription() {
        this.description = MessageFormat.format(DESCRIPTIONS[0], ARTIFACT, THRESHOLD);
    }

    static {
        powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
        NAME = powerStrings.NAME;
        DESCRIPTIONS = powerStrings.DESCRIPTIONS;
    }
}
