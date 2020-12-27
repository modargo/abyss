package abyss.effects;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.vfx.combat.SmallLaserEffect;

public class SmallColorLaserEffect extends SmallLaserEffect {
    public SmallColorLaserEffect(float sX, float sY, float dX, float dY, Color color) {
        super(sX, sY, dX, dY);
        this.color = color.cpy();
    }
}
