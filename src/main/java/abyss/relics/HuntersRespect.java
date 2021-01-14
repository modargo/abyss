package abyss.relics;

import abyss.Abyss;
import abyss.util.TextureLoader;
import basemod.abstracts.CustomRelic;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.DrawCardNextTurnPower;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.rooms.AbstractRoom;

public class HuntersRespect extends CustomRelic {
    public static final String ID = "Abyss:HuntersRespect";
    private static final Texture IMG = TextureLoader.getTexture(Abyss.relicImage(ID));
    private static final Texture OUTLINE = TextureLoader.getTexture(Abyss.relicOutlineImage(ID));
    private static final int CARDS = 1;

    public HuntersRespect() {
        super(ID, IMG, OUTLINE, RelicTier.SPECIAL, LandingSound.HEAVY);
    }

    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0];
    }

    @Override
    public void atBattleStart() {
        this.flash();
        this.addToBot(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new DrawCardNextTurnPower(AbstractDungeon.player, CARDS), CARDS));
        this.grayscale = true;
    }

    @Override
    public void onVictory() {
        this.grayscale = false;
    }

    @Override
    public AbstractRelic makeCopy() {
        return new HuntersRespect();
    }
}
