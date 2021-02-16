package abyss.effects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;
import com.megacrit.cardcrawl.vfx.combat.RedFireballEffect;

public class DamnationEffect extends AbstractGameEffect {
    public static final float TIME_PER_FIREBALL = 0.2F;
    private float x;
    private float y;
    private int counter;
    private int fireballs;

    public DamnationEffect(float x, float y, int fireballs) {
        this.startingDuration = fireballs * TIME_PER_FIREBALL;
        this.duration = fireballs * TIME_PER_FIREBALL;
        this.counter = fireballs;
        this.fireballs = fireballs;
        this.x = x;
        this.y = y;
    }

    public void update() {
        float timeForNextFireball = this.counter * TIME_PER_FIREBALL;
        if (this.duration <= timeForNextFireball) {
            int n = this.fireballs - this.counter;
            this.fireball(n * 10, n * 0.1F);
            this.counter--;
        }
        this.duration -= Gdx.graphics.getDeltaTime();
        if (this.duration < 0.0F) {
            this.isDone = true;
        }
    }

    private void fireball(int intensity, float pitchAdjust) {
        CardCrawlGame.sound.playA("ATTACK_FIRE", 0.3F + pitchAdjust);
        float dst = 180.0F + intensity * 3.0F;
        AbstractDungeon.effectsQueue.add(new RedFireballEffect(this.x - dst * Settings.scale, this.y, this.x + dst * Settings.scale, this.y - 50.0F * Settings.scale, intensity));
    }

    public void render(SpriteBatch sb) {
    }

    public void dispose() {
    }
}
