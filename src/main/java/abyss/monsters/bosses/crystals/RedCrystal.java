package abyss.monsters.bosses.crystals;

import abyss.Abyss;
import abyss.powers.crystals.RedCrystalPower;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.MonsterStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;

public class RedCrystal extends AbstractCrystal {
    public static final String ID = "Abyss:RedCrystal";
    private static final MonsterStrings monsterStrings;
    public static final String NAME;
    private static final String IMG = Abyss.monsterImage(ID);
    private static final int BUFF = 1;
    private static final int A19_BUFF = 2;
    private int buff;

    public RedCrystal() {
        this(0.0f, 0.0f);
    }

    public RedCrystal(final float x, final float y) {
        super(RedCrystal.NAME, ID, IMG, x, y, false);

        if (AbstractDungeon.ascensionLevel >= 19) {
            this.buff = A19_BUFF;
        } else {
            this.buff = BUFF;
        }
    }

    @Override
    protected AbstractPower getBuffPower() {
        return new RedCrystalPower(this, this.buff);
    }

    static {
        monsterStrings = CardCrawlGame.languagePack.getMonsterStrings(ID);
        NAME = RedCrystal.monsterStrings.NAME;
    }
}