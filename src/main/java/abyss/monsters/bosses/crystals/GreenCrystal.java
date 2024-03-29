package abyss.monsters.bosses.crystals;

import abyss.Abyss;
import abyss.powers.crystals.GreenCrystalPower;
import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.MonsterStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;

public class GreenCrystal extends AbstractCrystal {
    public static final String ID = "Abyss:GreenCrystal";
    private static final MonsterStrings monsterStrings;
    public static final String NAME;
    private static final String IMG = Abyss.monsterImage(ID);

    public GreenCrystal() {
        this(0.0f, 0.0f);
    }

    public GreenCrystal(final float x, final float y) {
        super(GreenCrystal.NAME, ID, IMG, x, y, true);
    }

    @Override
    protected AbstractPower getBuffPower() {
        return new GreenCrystalPower(this);
    }

    @Override
    protected Color getColor() {
        return Color.FOREST;
    }

    static {
        monsterStrings = CardCrawlGame.languagePack.getMonsterStrings(ID);
        NAME = GreenCrystal.monsterStrings.NAME;
    }
}