package abyss.monsters.bosses.crystals;

import abyss.Abyss;
import abyss.powers.crystals.RedCrystalPower;
import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.MonsterStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;

public class RedCrystal extends AbstractCrystal {
    public static final String ID = "Abyss:RedCrystal";
    private static final MonsterStrings monsterStrings;
    public static final String NAME;
    private static final String IMG = Abyss.monsterImage(ID);

    public RedCrystal() {
        this(0.0f, 0.0f);
    }

    public RedCrystal(final float x, final float y) {
        super(RedCrystal.NAME, ID, IMG, x, y, false);
    }

    @Override
    protected AbstractPower getBuffPower() {
        return new RedCrystalPower(this);
    }

    @Override
    protected Color getColor() {
        return Color.RED;
    }

    static {
        monsterStrings = CardCrawlGame.languagePack.getMonsterStrings(ID);
        NAME = RedCrystal.monsterStrings.NAME;
    }
}