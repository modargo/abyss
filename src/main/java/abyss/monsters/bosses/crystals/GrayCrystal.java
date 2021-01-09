package abyss.monsters.bosses.crystals;

import abyss.Abyss;
import abyss.powers.crystals.GrayCrystalPower;
import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.MonsterStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;

public class GrayCrystal extends AbstractCrystal {
    public static final String ID = "Abyss:GrayCrystal";
    private static final MonsterStrings monsterStrings;
    public static final String NAME;
    private static final String IMG = Abyss.monsterImage(ID);

    public GrayCrystal() {
        this(0.0f, 0.0f);
    }

    public GrayCrystal(final float x, final float y) {
        super(GrayCrystal.NAME, ID, IMG, x, y, false);
    }

    @Override
    protected AbstractPower getBuffPower() {
        return new GrayCrystalPower(this);
    }

    @Override
    protected Color getColor() {
        return Color.DARK_GRAY;
    }

    static {
        monsterStrings = CardCrawlGame.languagePack.getMonsterStrings(ID);
        NAME = GrayCrystal.monsterStrings.NAME;
    }
}