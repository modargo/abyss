package abyss.powers;

import abyss.Abyss;
import abyss.monsters.bosses.VoidHerald;
import abyss.monsters.bosses.VoidSpawn;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.SpawnMonsterAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.StrengthPower;

import java.text.MessageFormat;

public class ResummonPower extends AbstractPower {
    public static final String POWER_ID = "Abyss:Resummon";
    private static final PowerStrings powerStrings;
    public static final String NAME;
    public static final String[] DESCRIPTIONS;
    private static final int VOID_SPAWN_RESUMMON_TURNS = 1;
    private int voidSpawnResummonCounter = 0;

    public ResummonPower(AbstractCreature owner) {
        this.name = NAME;
        this.ID = POWER_ID;
        this.owner = owner;
        this.updateDescription();
        Abyss.LoadPowerImage(this);
    }

    @Override
    public void atEndOfRound() {
        if (this.owner.id.equals(VoidHerald.ID)) {
            boolean voidSpawnAlive = false;
            for (AbstractMonster m : AbstractDungeon.getCurrRoom().monsters.monsters) {
                voidSpawnAlive = voidSpawnAlive || (m != null && m != this.owner && !m.isDying && m.id.equals(VoidSpawn.ID));
            }
            if (!voidSpawnAlive) {
                if (this.voidSpawnResummonCounter >= VOID_SPAWN_RESUMMON_TURNS) {
                    VoidSpawn voidSpawn = new VoidSpawn(-250.0F, 0.0F);
                    voidSpawn.rollMove();
                    voidSpawn.createIntent();
                    AbstractDungeon.actionManager.addToBottom(new SpawnMonsterAction(voidSpawn, true));
                    AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(voidSpawn, this.owner, new DurableMinionPower(voidSpawn)));
                    AbstractPower strengthPower = this.owner.getPower(StrengthPower.POWER_ID);
                    if (strengthPower != null && strengthPower.amount > 0) {
                        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(voidSpawn, this.owner, new StrengthPower(this.owner, strengthPower.amount), strengthPower.amount));
                    }
                    voidSpawn.applyStartingPowers();
                    this.voidSpawnResummonCounter = 0;
                    this.flash();
                }
                else {
                    this.voidSpawnResummonCounter++;
                }
            }
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
