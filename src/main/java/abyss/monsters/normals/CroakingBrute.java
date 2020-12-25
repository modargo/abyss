package abyss.monsters.normals;

import abyss.Abyss;
import abyss.powers.WaryPower;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.AnimateFastAttackAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.MonsterStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;

public class CroakingBrute extends AbstractCroaking {
    public static final String ID = "Abyss:CroakingBrute";
    private static final MonsterStrings monsterStrings;
    public static final String NAME;
    public static final String[] MOVES;
    public static final String[] DIALOG;
    private static final String IMG = Abyss.monsterImage(ID);
    private static final byte BULL_RUSH_ATTACK = 2;
    private static final int BULL_RUSH_DAMAGE = 8;
    private static final int A2_BULL_RUSH_DAMAGE = 9;
    private int bullRushDamage;

    public CroakingBrute() {
        this(0.0f, 0.0f);
    }

    public CroakingBrute(final float x, final float y) {
        super(CroakingBrute.NAME, ID, -5.0F, 0, 230.0f, 205.0f, IMG, x, y);

        if (AbstractDungeon.ascensionLevel >= 2) {
            this.bullRushDamage = A2_BULL_RUSH_DAMAGE;
        } else {
            this.bullRushDamage = BULL_RUSH_DAMAGE;
        }
        this.damage.add(new DamageInfo(this, this.bullRushDamage));
    }

    @Override
    protected AbstractPower getBuffPower(int amount) {
        return new WaryPower(this, amount);
    }

    @Override
    protected String getDialog() {
        return DIALOG[0];
    }

    @Override
    protected String getFirstMoveName() {
        return MOVES[0];
    }

    @Override
    protected AbstractGameAction.AttackEffect getFirstMoveAttackEffect() {
        return AbstractGameAction.AttackEffect.SLASH_HORIZONTAL;
    }

    @Override
    protected void executeSecondMove() {
        AbstractDungeon.actionManager.addToBottom(new AnimateFastAttackAction(this));
        AbstractDungeon.actionManager.addToBottom(new DamageAction(AbstractDungeon.player, this.damage.get(1), AbstractGameAction.AttackEffect.BLUNT_HEAVY));
    }

    @Override
    protected void setSecondMoveIntent() {
        this.setMove(MOVES[1], BULL_RUSH_ATTACK, Intent.ATTACK, this.bullRushDamage);
    }

    static {
        monsterStrings = CardCrawlGame.languagePack.getMonsterStrings(ID);
        NAME = CroakingBrute.monsterStrings.NAME;
        MOVES = CroakingBrute.monsterStrings.MOVES;
        DIALOG = CroakingBrute.monsterStrings.DIALOG;
    }
}