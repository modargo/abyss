package abyss.monsters.bosses.crystals;

import abyss.Abyss;
import abyss.powers.crystals.GoldenCrystalPower;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.MonsterStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;

public class GoldenCrystal extends AbstractCrystal {
    public static final String ID = "Abyss:GoldenCrystal";
    private static final MonsterStrings monsterStrings;
    public static final String NAME;
    private static final String IMG = Abyss.monsterImage(ID);
    private static final int BUFF = 2;
    private static final int A19_BUFF = 3;
    private int buff;

    public GoldenCrystal() {
        this(0.0f, 0.0f);
    }

    public GoldenCrystal(final float x, final float y) {
        super(GoldenCrystal.NAME, ID, IMG, x, y, true);

        if (AbstractDungeon.ascensionLevel >= 19) {
            this.buff = A19_BUFF;
        } else {
            this.buff = BUFF;
        }
    }

    @Override
    protected AbstractPower getBuffPower() {
        return new GoldenCrystalPower(this, this.buff);
    }

    static {
        monsterStrings = CardCrawlGame.languagePack.getMonsterStrings(ID);
        NAME = GoldenCrystal.monsterStrings.NAME;
    }
}