package abyss.relics;

import abyss.Abyss;
import abyss.actions.BehemothsWisdomAction;
import abyss.util.TextureLoader;
import basemod.abstracts.CustomRelic;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.AbstractRelic;

public class BehemothsWisdom extends CustomRelic {
    public static final String ID = "Abyss:BehemothsWisdom";
    private static final Texture IMG = TextureLoader.getTexture(Abyss.relicImage(ID));
    private static final Texture OUTLINE = TextureLoader.getTexture(Abyss.relicOutlineImage(ID));
    private static final int CARDS = 2;
    private boolean activated = false;

    public BehemothsWisdom() {
        super(ID, IMG, OUTLINE, AbstractRelic.RelicTier.SPECIAL, LandingSound.HEAVY);
    }

    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0].replace("{0}", CARDS + "");
    }

    @Override
    public void atBattleStartPreDraw() {
        this.activated = false;
    }

    @Override
    public void atTurnStartPostDraw() {
        if (!this.activated) {
            this.activated = true;
            this.flash();
            this.addToBot(new RelicAboveCreatureAction(AbstractDungeon.player, this));
            this.addToBot(new BehemothsWisdomAction(AbstractDungeon.player, CARDS));
        }

    }

    @Override
    public AbstractRelic makeCopy() {
        return new BehemothsWisdom();
    }
}
