package abyss.powers.act4;

import com.megacrit.cardcrawl.actions.common.MakeTempCardInDiscardAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.cards.DamageInfo.DamageType;
import com.megacrit.cardcrawl.cards.status.Wound;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;

public class PainfulStabsWithAmountPower extends AbstractPower {
    public static final String POWER_ID = "Abyss:PainfulStabsWithAmount";
    private static final PowerStrings powerStrings;
    public static final String NAME;
    public static final String[] DESCRIPTIONS;

    public PainfulStabsWithAmountPower(AbstractCreature owner, int amount) {
        this.name = NAME;
        this.ID = POWER_ID;
        this.owner = owner;
        this.amount = amount;
        this.updateDescription();
        this.loadRegion("painfulStabs");
    }

    public void updateDescription() {
        this.description = DESCRIPTIONS[0].replace("{0}", this.amount + "");
    }

    public void onInflictDamage(DamageInfo info, int damageAmount, AbstractCreature target) {
        if (damageAmount > 0 && info.type != DamageType.THORNS) {
            this.addToBot(new MakeTempCardInDiscardAction(new Wound(), this.amount));
        }
    }

    static {
        powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
        NAME = powerStrings.NAME;
        DESCRIPTIONS = powerStrings.DESCRIPTIONS;
    }
}
