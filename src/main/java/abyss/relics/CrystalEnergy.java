package abyss.relics;

import abyss.Abyss;
import abyss.util.TextureLoader;
import basemod.abstracts.CustomRelic;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.actions.common.HealAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.AbstractRelic;

import java.text.MessageFormat;

public class CrystalEnergy extends CustomRelic {
    public static final String ID = "Abyss:CrystalEnergy";
    private static final Texture IMG = TextureLoader.getTexture(Abyss.relicImage(ID));
    private static final Texture OUTLINE = TextureLoader.getTexture(Abyss.relicOutlineImage(ID));
    private static final int HEAL = 5;
    private static final int DAMAGE_THRESHOLD = 30;

    public CrystalEnergy() {
        super(ID, IMG, OUTLINE, RelicTier.SPECIAL, LandingSound.HEAVY);
    }

    @Override
    public String getUpdatedDescription() {
        return MessageFormat.format(DESCRIPTIONS[0], DAMAGE_THRESHOLD, HEAL);
    }

    @Override
    public void atBattleStart() {
        this.counter = 0;
    }

    @Override
    public void atTurnStart() {
        this.counter = 0;
    }

    @Override
    public void onLoseHp(int damageAmount) {
        int oldCounter = this.counter;
        this.counter += damageAmount;
        if (oldCounter < DAMAGE_THRESHOLD && this.counter >= DAMAGE_THRESHOLD) {
            this.flash();
            this.addToTop(new HealAction(AbstractDungeon.player, AbstractDungeon.player, HEAL));
        }
    }

    @Override
    public void onVictory() {
        this.counter = -1;
    }

    @Override
    public AbstractRelic makeCopy() {
        return new CrystalEnergy();
    }
}
