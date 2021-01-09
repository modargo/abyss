package abyss.monsters.bosses.crystals;

import abyss.Abyss;
import abyss.powers.crystals.GoldenCrystalPower;
import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.MonsterStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;

public class GoldenCrystal extends AbstractCrystal {
    public static final String ID = "Abyss:GoldenCrystal";
    private static final MonsterStrings monsterStrings;
    public static final String NAME;
    private static final String IMG = Abyss.monsterImage(ID);

    public GoldenCrystal() {
        this(0.0f, 0.0f);
    }

    public GoldenCrystal(final float x, final float y) {
        super(GoldenCrystal.NAME, ID, IMG, x, y, false);
    }

    @Override
    protected AbstractPower getBuffPower() {
        return new GoldenCrystalPower(this);
    }

    @Override
    protected Color getColor() {
        return Color.GOLD;
    }
    static {
        monsterStrings = CardCrawlGame.languagePack.getMonsterStrings(ID);
        NAME = GoldenCrystal.monsterStrings.NAME;
    }
}