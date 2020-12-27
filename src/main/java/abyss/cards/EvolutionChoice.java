package abyss.cards;

import abyss.Abyss;
import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

//This only exists to easily have an image for the evolution choices
public class EvolutionChoice extends CustomCard {
    public static final String ID = "Abyss:EvolutionChoice";
    public static final String IMG = Abyss.cardImage(ID);
    private static final int COST = -2;
    private Runnable action;

    public EvolutionChoice(String name, String description, Runnable action) {
        super(ID, name, IMG, COST, description, CardType.STATUS, CardColor.COLORLESS, CardRarity.SPECIAL, CardTarget.SELF);
        this.action = action;
    }

    public void use(AbstractPlayer p, AbstractMonster m) {
    }

    public void upgrade() {
    }

    @Override
    public void onChoseThisOption() {
        this.action.run();
    }

    public AbstractCard makeCopy() {
        return new EvolutionChoice(this.name, this.rawDescription, action);
    }
}
