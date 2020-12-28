package abyss.monsters.normals;

import abyss.Abyss;
import abyss.powers.DelayedAbysstouchedPower;
import basemod.abstracts.CustomMonster;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.AnimateFastAttackAction;
import com.megacrit.cardcrawl.actions.animations.FastShakeAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInDiscardAction;
import com.megacrit.cardcrawl.actions.common.RollMoveAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.cards.status.Slimed;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.MonsterStrings;
import com.megacrit.cardcrawl.powers.MalleablePower;

public class SquirmingHorror extends CustomMonster {
    public static final String ID = "Abyss:SquirmingHorror";
    private static final MonsterStrings monsterStrings;
    public static final String NAME;
    public static final String[] MOVES;
    private static final String IMG = Abyss.monsterImage(ID);
    private boolean firstMove = true;
    private static final byte TENTACLE_WRAP_ATTACK = 1;
    private static final byte EMBRACE_DEBUFF = 2;
    private static final int TENTACLE_WRAP_DAMAGE = 10;
    private static final int A2_TENTACLE_WRAP_DAMAGE = 12;
    private static final int TENTACLE_WRAP_ABYSSTOUCHED = 2;
    private static final int A17_TENTACLE_WRAP_ABYSSTOUCHED = 3;
    private static final int EMBRACE_ABYSSTOUCHED = 5;
    private static final int A17_EMBRACE_ABYSSTOUCHED = 7;
    private static final int EMBRACE_SLIMES = 1;
    private static final int A17_EMBRACE_SLIMES = 1;
    private static final int MALLEABLE = 2;
    private static final int HP_MIN = 90;
    private static final int HP_MAX = 94;
    private static final int A7_HP_MIN = 93;
    private static final int A7_HP_MAX = 97;
    private int tentacleWrapDamage;
    private int tentacleWrapAbysstouched;
    private int embraceAbysstouched;
    private int embraceSlimes;

    public SquirmingHorror() {
        this(0.0f, 0.0f);
    }

    public SquirmingHorror(final float x, final float y) {
        super(SquirmingHorror.NAME, ID, HP_MAX, -5.0F, 0, 280.0f, 205.0f, IMG, x, y);
        this.type = EnemyType.NORMAL;
        if (AbstractDungeon.ascensionLevel >= 7) {
            this.setHp(A7_HP_MIN, A7_HP_MAX);
        } else {
            this.setHp(HP_MIN, HP_MAX);
        }

        if (AbstractDungeon.ascensionLevel >= 2) {
            this.tentacleWrapDamage = A2_TENTACLE_WRAP_DAMAGE;
        } else {
            this.tentacleWrapDamage = TENTACLE_WRAP_DAMAGE;
        }
        this.damage.add(new DamageInfo(this, this.tentacleWrapDamage));

        if (AbstractDungeon.ascensionLevel >= 17) {
            this.tentacleWrapAbysstouched = A17_TENTACLE_WRAP_ABYSSTOUCHED;
            this.embraceAbysstouched = A17_EMBRACE_ABYSSTOUCHED;
            this.embraceSlimes = A17_EMBRACE_SLIMES;
        } else {
            this.tentacleWrapAbysstouched = TENTACLE_WRAP_ABYSSTOUCHED;
            this.embraceAbysstouched = EMBRACE_ABYSSTOUCHED;
            this.embraceSlimes = EMBRACE_SLIMES;
        }
    }

    @Override
    public void usePreBattleAction() {
        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(this, this, new MalleablePower(this, MALLEABLE), MALLEABLE));
    }

    @Override
    public void takeTurn() {
        if (this.firstMove) {
            this.firstMove = false;
        }
        switch (this.nextMove) {
            case TENTACLE_WRAP_ATTACK:
                AbstractDungeon.actionManager.addToBottom(new AnimateFastAttackAction(this));
                AbstractDungeon.actionManager.addToBottom(new DamageAction(AbstractDungeon.player, this.damage.get(0), AbstractGameAction.AttackEffect.BLUNT_LIGHT));
                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(AbstractDungeon.player, this, new DelayedAbysstouchedPower(AbstractDungeon.player, this.tentacleWrapAbysstouched), this.tentacleWrapAbysstouched));
                break;
            case EMBRACE_DEBUFF:
                AbstractDungeon.actionManager.addToBottom(new FastShakeAction(this, 0.5F, 0.2F));
                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(AbstractDungeon.player, this, new DelayedAbysstouchedPower(AbstractDungeon.player, this.embraceAbysstouched), this.embraceAbysstouched));
                AbstractDungeon.actionManager.addToBottom(new MakeTempCardInDiscardAction(new Slimed(), this.embraceSlimes));
                break;
        }
        AbstractDungeon.actionManager.addToBottom(new RollMoveAction(this));
    }

    @Override
    protected void getMove(final int num) {
        byte move;
        if (this.lastMove(TENTACLE_WRAP_ATTACK) && this.lastMoveBefore(TENTACLE_WRAP_ATTACK)) {
            move = EMBRACE_DEBUFF;
        }
        else if (this.lastMove(EMBRACE_DEBUFF) && this.lastMoveBefore(EMBRACE_DEBUFF)) {
            move = TENTACLE_WRAP_ATTACK;
        }
        else {
            move = num < 60 ? TENTACLE_WRAP_ATTACK : EMBRACE_DEBUFF;
        }
        if (move == TENTACLE_WRAP_ATTACK) {
            this.setMove(MOVES[0], TENTACLE_WRAP_ATTACK, Intent.ATTACK_DEBUFF, this.tentacleWrapDamage);
        } else {
            this.setMove(MOVES[1], EMBRACE_DEBUFF, Intent.DEBUFF);
        }
    }

    static {
        monsterStrings = CardCrawlGame.languagePack.getMonsterStrings(ID);
        NAME = SquirmingHorror.monsterStrings.NAME;
        MOVES = SquirmingHorror.monsterStrings.MOVES;
    }
}