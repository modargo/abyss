package abyss.monsters.bosses.crystals;

import abyss.Abyss;
import abyss.powers.crystals.PurpleCrystalPower;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.MonsterStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;

public class PurpleCrystal extends AbstractCrystal {
    public static final String ID = "Abyss:PurpleCrystal";
    private static final MonsterStrings monsterStrings;
    public static final String NAME;
    private static final String IMG = Abyss.monsterImage(ID);

    public PurpleCrystal() {
        this(0.0f, 0.0f);
    }

    public PurpleCrystal(final float x, final float y) {
        super(PurpleCrystal.NAME, ID, IMG, x, y, true);
    }

    @Override
    protected AbstractPower getBuffPower() {
        return new PurpleCrystalPower(this);
    }

    static {
        monsterStrings = CardCrawlGame.languagePack.getMonsterStrings(ID);
        NAME = PurpleCrystal.monsterStrings.NAME;
    }
}