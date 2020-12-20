package abyss.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.Settings;

public class IncreaseFlatMaxHpAction extends AbstractGameAction {
    private boolean showEffect;
    private int increaseAmount;

    public IncreaseFlatMaxHpAction(AbstractCreature m, int increaseAmount, boolean showEffect) {
        if (Settings.FAST_MODE) {
            this.startDuration = Settings.ACTION_DUR_XFAST;
        } else {
            this.startDuration = Settings.ACTION_DUR_FAST;
        }

        this.duration = this.startDuration;
        this.showEffect = showEffect;
        this.increaseAmount = increaseAmount;
        this.target = m;
    }

    public void update() {
        if (this.duration == this.startDuration) {
            this.target.increaseMaxHp(this.increaseAmount, this.showEffect);
        }

        this.tickDuration();
    }
}
