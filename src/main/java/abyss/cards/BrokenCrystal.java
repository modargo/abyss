package abyss.cards;

import abyss.Abyss;
import abyss.actions.ReduceAllEnemyRegenAction;
import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class BrokenCrystal extends CustomCard {
    public static final String ID = "Abyss:BrokenCrystal";
    public static final String IMG = Abyss.cardImage(ID);
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    private static final int COST = 1;
    private static final int MAX_COST = 2;
    private static final int DRAW = 1;
    private static final int REGEN_REDUCTION = 5;
    private static final int UPGRADE_REGEN_REDUCTION = 8;

    public BrokenCrystal() {
        super(ID, NAME, IMG, COST, DESCRIPTION, CardType.STATUS, CardColor.COLORLESS, CardRarity.SPECIAL, CardTarget.SELF);
        this.baseMagicNumber = REGEN_REDUCTION;
        this.magicNumber = this.baseMagicNumber;
        this.exhaust = true;
    }

    public void use(AbstractPlayer p, AbstractMonster m) {
        this.addToBot(new ReduceAllEnemyRegenAction(this.magicNumber));
    }

    @Override
    public void triggerWhenDrawn() {
        if (this.cost >= 0) {
            int newCost = AbstractDungeon.cardRandomRng.random(MAX_COST);
            if (this.cost != newCost) {
                this.cost = newCost;
                this.costForTurn = this.cost;
                this.isCostModified = true;
            }

            this.freeToPlayOnce = false;
        }
        this.addToBot(new DrawCardAction(DRAW));
    }

    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.upgradeMagicNumber(UPGRADE_REGEN_REDUCTION);
        }
    }

    public AbstractCard makeCopy() {
        return new BrokenCrystal();
    }
}
