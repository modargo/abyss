package abyss.cards;

import abyss.Abyss;
import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInDiscardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.status.Wound;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class Fumbling extends CustomCard {
    public static final String ID = "Abyss:Fumbling";
    public static final String IMG = Abyss.cardImage(ID);
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    private static final int COST = -2;

    public Fumbling() {
        super(ID, NAME, IMG, COST, DESCRIPTION, CardType.CURSE, CardColor.CURSE, CardRarity.SPECIAL, CardTarget.NONE);
    }

    @Override
    public void onPlayCard(AbstractCard c, AbstractMonster m) {
        this.addToBot(new MakeTempCardInDiscardAction(new Wound(), 1));
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {}

    @Override
    public void upgrade() {}

    @Override
    public AbstractCard makeCopy() {
        return new Fumbling();
    }
}
