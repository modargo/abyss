package abyss.effects;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;

public class DoNothingEffect extends AbstractGameEffect {
    public DoNothingEffect() {
        this.duration = 0.0F;
        this.startingDuration = 0.0F;
        this.isDone = true;
    }

    @Override
    public void update() {
    }

    @Override
    public void render(SpriteBatch spriteBatch) {}

    @Override
    public void dispose() {}
}
