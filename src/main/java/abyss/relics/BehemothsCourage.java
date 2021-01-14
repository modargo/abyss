package abyss.relics;

import abyss.Abyss;
import abyss.util.TextureLoader;
import basemod.abstracts.CustomRelic;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.BlurPower;
import com.megacrit.cardcrawl.powers.MetallicizePower;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.rooms.AbstractRoom;

import java.text.MessageFormat;

public class BehemothsCourage extends CustomRelic {
    public static final String ID = "Abyss:BehemothsCourage";
    private static final Texture IMG = TextureLoader.getTexture(Abyss.relicImage(ID));
    private static final Texture OUTLINE = TextureLoader.getTexture(Abyss.relicOutlineImage(ID));
    private static final int BLUR = 1;
    private static final int METALLICIZE = 3;

    public BehemothsCourage() {
        super(ID, IMG, OUTLINE, RelicTier.SPECIAL, LandingSound.HEAVY);
    }

    @Override
    public String getUpdatedDescription() {
        return MessageFormat.format(DESCRIPTIONS[0], METALLICIZE, BLUR);
    }

    @Override
    public void atBattleStart() {
        this.flash();
        this.addToBot(new RelicAboveCreatureAction(AbstractDungeon.player, this));
        this.addToBot(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new BlurPower(AbstractDungeon.player, BLUR), BLUR));
        this.addToBot(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new MetallicizePower(AbstractDungeon.player, METALLICIZE), METALLICIZE));
        this.grayscale = true;
    }

    @Override
    public void onVictory() {
        this.grayscale = false;
    }

    @Override
    public AbstractRelic makeCopy() {
        return new BehemothsCourage();
    }
}
