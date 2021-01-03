package abyss.monsters.bosses.crystals;

import abyss.Abyss;
import abyss.powers.crystals.BlueCrystalPower;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.MonsterStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;

public class BlueCrystal extends AbstractCrystal {
    public static final String ID = "Abyss:BlueCrystal";
    private static final MonsterStrings monsterStrings;
    public static final String NAME;
    private static final String IMG = Abyss.monsterImage(ID);

    public BlueCrystal() {
        this(0.0f, 0.0f);
    }

    public BlueCrystal(final float x, final float y) {
        super(BlueCrystal.NAME, ID, IMG, x, y, true);
    }

    @Override
    protected AbstractPower getBuffPower() {
        return new BlueCrystalPower(this);
    }

    static {
        monsterStrings = CardCrawlGame.languagePack.getMonsterStrings(ID);
        NAME = BlueCrystal.monsterStrings.NAME;
    }
}