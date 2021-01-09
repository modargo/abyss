package abyss.monsters.act4;

import abyss.Abyss;
import abyss.cards.Withering;
import abyss.powers.ChainsOfDoomPower;
import abyss.powers.DelayedAbysstouchedPower;
import basemod.abstracts.CustomMonster;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.AnimateFastAttackAction;
import com.megacrit.cardcrawl.actions.animations.AnimateSlowAttackAction;
import com.megacrit.cardcrawl.actions.animations.FastShakeAction;
import com.megacrit.cardcrawl.actions.common.*;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.MonsterStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.BufferPower;
import com.megacrit.cardcrawl.powers.StrengthPower;

import java.util.ArrayList;
import java.util.List;

public class AnnihilationWarrior extends CustomMonster
{
    public static final String ID = "Abyss:AnnihilationWarrior";
    private static final MonsterStrings monsterStrings;
    public static final String NAME;
    public static final String[] MOVES;
    private static final String IMG = Abyss.monsterImage(ID);
    private boolean firstMove = true;
    private static final byte WITHERING_STRIKE_ATTACK = 1;
    private static final byte DARK_BARRIER_MOVE = 2;
    private static final byte TOUCH_OF_SUFFERING_DEBUFF = 3;
    private static final byte CIRCLE_OF_PROTECTION_MOVE = 4;
    private static final byte POLEAXE_ATTACK = 5;
    private static final int WITHERING_STRIKE_DAMAGE = 22;
    private static final int A3_WITHERING_STRIKE_DAMAGE = 25;
    private static final int DARK_BARRIER_BLOCK = 30;
    private static final int A8_DARK_BARRIER_BLOCK = 50;
    private static final int DARK_BARRIER_STRENGTH = 2;
    private static final int A18_DARK_BARRIER_STRENGTH = 4;
    private static final int DARK_BARRIER_BUFFER = 0;
    private static final int A18_DARK_BARRIER_BUFFER = 1;
    private static final int TOUCH_OF_SUFFERING_ABYSSTOUCHED = 3;
    private static final int A18_TOUCH_OF_SUFFERING_ABYSSTOUCHED = 5;
    private static final int CIRCLE_OF_PROTECTION_BLOCK = 50;
    private static final int A8_CIRCLE_OF_PROTECTION_BLOCK = 80;
    private static final int POLEAXE_DAMAGE = 9;
    private static final int A3_POLEAXE_DAMAGE = 10;
    private static final int POLEAXE_BLOCK = 15;
    private static final int A8_POLEAXE_BLOCK = 25;
    private static final int HP_MIN = 130;
    private static final int HP_MAX = 130;
    private static final int A8_HP_MIN = 145;
    private static final int A8_HP_MAX = 145;
    private int witheringStrikeDamage;
    private int darkBarrierBlock;
    private int darkBarrierStrength;
    private int darkBarrierBuffer;
    private int touchOfSufferingAbysstouched;
    private int circleOfProtectionBlock;
    private int poleaxeDamage;
    private int poleaxeBlock;

    public AnnihilationWarrior() {
        this(0.0f, 0.0f);
    }

    public AnnihilationWarrior(final float x, final float y) {
        super(AnnihilationWarrior.NAME, ID, HP_MAX, -5.0F, 0, 320.0f, 380.0f, IMG, x, y);
        this.type = EnemyType.ELITE;
        if (AbstractDungeon.ascensionLevel >= 8) {
            this.setHp(A8_HP_MIN, A8_HP_MAX);
            this.darkBarrierBlock = A8_DARK_BARRIER_BLOCK;
            this.circleOfProtectionBlock = A8_CIRCLE_OF_PROTECTION_BLOCK;
            this.poleaxeBlock = A8_POLEAXE_BLOCK;
        } else {
            this.setHp(HP_MIN, HP_MAX);
            this.darkBarrierBlock = DARK_BARRIER_BLOCK;
            this.circleOfProtectionBlock = CIRCLE_OF_PROTECTION_BLOCK;
            this.poleaxeBlock = POLEAXE_BLOCK;
        }

        if (AbstractDungeon.ascensionLevel >= 3) {
            this.witheringStrikeDamage = A3_WITHERING_STRIKE_DAMAGE;
            this.poleaxeDamage = A3_POLEAXE_DAMAGE;
        } else {
            this.witheringStrikeDamage = WITHERING_STRIKE_DAMAGE;
            this.poleaxeDamage = POLEAXE_DAMAGE;
        }
        this.damage.add(new DamageInfo(this, this.witheringStrikeDamage));
        this.damage.add(new DamageInfo(this, this.poleaxeDamage));

        if (AbstractDungeon.ascensionLevel >= 18) {
            this.darkBarrierStrength = A18_DARK_BARRIER_STRENGTH;
            this.darkBarrierBuffer = A18_DARK_BARRIER_BUFFER;
            this.touchOfSufferingAbysstouched = A18_TOUCH_OF_SUFFERING_ABYSSTOUCHED;
        }
        else {
            this.darkBarrierStrength = DARK_BARRIER_STRENGTH;
            this.darkBarrierBuffer = DARK_BARRIER_BUFFER;
            this.touchOfSufferingAbysstouched = TOUCH_OF_SUFFERING_ABYSSTOUCHED;
        }
    }

    @Override
    public void usePreBattleAction() {
        this.addToBot(new ApplyPowerAction(this, this, new ChainsOfDoomPower(this)));
    }

    @Override
    public void takeTurn() {
        if (this.firstMove) {
            this.firstMove = false;
        }
        switch (this.nextMove) {
            case WITHERING_STRIKE_ATTACK:
                //TODO sound would really benefit from a curse sound effect
                AbstractDungeon.actionManager.addToBottom(new AnimateSlowAttackAction(this));
                AbstractDungeon.actionManager.addToBottom(new DamageAction(AbstractDungeon.player, this.damage.get(0), AbstractGameAction.AttackEffect.SLASH_HEAVY));
                if (AbstractDungeon.ascensionLevel >= 18) {
                    AbstractDungeon.actionManager.addToBottom(new MakeTempCardInDrawPileAction(new Withering(), 1, false, true));
                }
                else {
                    AbstractDungeon.actionManager.addToBottom(new MakeTempCardInDiscardAction(new Withering(), 1));
                }
                break;
            case DARK_BARRIER_MOVE:
                AbstractDungeon.actionManager.addToBottom(new GainBlockAction(this, this.darkBarrierBlock));
                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(this, this, new StrengthPower(this, this.darkBarrierStrength), this.darkBarrierStrength));
                if (this.darkBarrierBuffer > 0) {
                    AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(this, this, new BufferPower(this, this.darkBarrierBuffer), this.darkBarrierBuffer));
                }
                break;
            case TOUCH_OF_SUFFERING_DEBUFF:
                AbstractDungeon.actionManager.addToBottom(new FastShakeAction(this, 0.5F, 0.2F));
                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(AbstractDungeon.player, this, new DelayedAbysstouchedPower(AbstractDungeon.player, this.touchOfSufferingAbysstouched), this.touchOfSufferingAbysstouched));
                break;
            case CIRCLE_OF_PROTECTION_MOVE:
                for (AbstractMonster m : AbstractDungeon.getCurrRoom().monsters.monsters) {
                    if (!m.isDying) {
                        AbstractDungeon.actionManager.addToBottom(new GainBlockAction(m, this.circleOfProtectionBlock));
                    }
                }
                break;
            case POLEAXE_ATTACK:
                AbstractDungeon.actionManager.addToBottom(new AnimateFastAttackAction(this));
                AbstractDungeon.actionManager.addToBottom(new DamageAction(AbstractDungeon.player, this.damage.get(1), AbstractGameAction.AttackEffect.SLASH_HEAVY));
                AbstractDungeon.actionManager.addToBottom(new GainBlockAction(this, this.poleaxeBlock));
                break;
        }
        AbstractDungeon.actionManager.addToBottom(new RollMoveAction(this));
    }

    @Override
    protected void getMove(final int num) {
        byte move;
        List<Byte> moves = new ArrayList<>();
        moves.add(DARK_BARRIER_MOVE);
        moves.add(TOUCH_OF_SUFFERING_DEBUFF);
        moves.add(CIRCLE_OF_PROTECTION_MOVE);
        moves.add(POLEAXE_ATTACK);
        switch (this.moveHistory.size() % 5) {
            case 0:
                move = WITHERING_STRIKE_ATTACK;
                break;
            case 1:
                move = num < 50 ? DARK_BARRIER_MOVE : num < 75 ? TOUCH_OF_SUFFERING_DEBUFF : CIRCLE_OF_PROTECTION_MOVE;
                break;
            case 2:
                move = this.lastMove(DARK_BARRIER_MOVE) || this.lastMove(CIRCLE_OF_PROTECTION_MOVE)
                        ? (num < 50 ? TOUCH_OF_SUFFERING_DEBUFF : POLEAXE_ATTACK)
                        : (num < 50 ? DARK_BARRIER_MOVE : CIRCLE_OF_PROTECTION_MOVE);
                break;
            case 3:
                moves.remove(this.moveHistory.get(this.moveHistory.size() - 1));
                moves.remove(this.moveHistory.get(this.moveHistory.size() - 2));
                move = moves.contains(TOUCH_OF_SUFFERING_DEBUFF) ? (num < 50 ? moves.get(0) : moves.get(1)) : TOUCH_OF_SUFFERING_DEBUFF;
                break;
            case 4:
                moves.remove(this.moveHistory.get(this.moveHistory.size() - 1));
                moves.remove(this.moveHistory.get(this.moveHistory.size() - 2));
                moves.remove(this.moveHistory.get(this.moveHistory.size() - 3));
                move = moves.get(0);
                break;
            default:
                throw new RuntimeException("Impossible case");
        }

        switch (move) {
            case WITHERING_STRIKE_ATTACK:
                this.setMove(MOVES[0], WITHERING_STRIKE_ATTACK, Intent.ATTACK_DEBUFF, this.witheringStrikeDamage);
                break;
            case DARK_BARRIER_MOVE:
                this.setMove(MOVES[1], DARK_BARRIER_MOVE, Intent.DEFEND_BUFF);
                break;
            case TOUCH_OF_SUFFERING_DEBUFF:
                this.setMove(MOVES[2], TOUCH_OF_SUFFERING_DEBUFF, Intent.DEBUFF);
                break;
            case CIRCLE_OF_PROTECTION_MOVE:
                this.setMove(MOVES[3], CIRCLE_OF_PROTECTION_MOVE, Intent.DEFEND);
                break;
            case POLEAXE_ATTACK:
                this.setMove(MOVES[4], POLEAXE_ATTACK, Intent.ATTACK_DEFEND, this.poleaxeDamage);
                break;
        }
    }

    static {
        monsterStrings = CardCrawlGame.languagePack.getMonsterStrings(ID);
        NAME = AnnihilationWarrior.monsterStrings.NAME;
        MOVES = AnnihilationWarrior.monsterStrings.MOVES;
    }
}