package abyss.monsters.bosses.crystals;

import abyss.Abyss;
import abyss.powers.crystals.GrayCrystalPower;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.MonsterStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;

public class GrayCrystal extends AbstractCrystal {
    public static final String ID = "Abyss:GrayCrystal";
    private static final MonsterStrings monsterStrings;
    public static final String NAME;
    private static final String IMG = Abyss.monsterImage(ID);
    private static final int BUFF = 1;
    private static final int A19_BUFF = 2;
    private int buff;

    public GrayCrystal() {
        this(0.0f, 0.0f);
    }

    public GrayCrystal(final float x, final float y) {
        super(GrayCrystal.NAME, ID, IMG, x, y, false);

        if (AbstractDungeon.ascensionLevel >= 19) {
            this.buff = A19_BUFF;
        } else {
            this.buff = BUFF;
        }
    }

    @Override
    protected AbstractPower getBuffPower() {
        return new GrayCrystalPower(this, this.buff);
    }

    static {
        monsterStrings = CardCrawlGame.languagePack.getMonsterStrings(ID);
        NAME = GrayCrystal.monsterStrings.NAME;
    }
}