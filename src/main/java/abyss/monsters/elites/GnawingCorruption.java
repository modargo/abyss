package abyss.monsters.elites;

import abyss.Abyss;
import abyss.cards.*;
import abyss.effects.SmallColorLaserEffect;
import basemod.abstracts.CustomMonster;
import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.AnimateSlowAttackAction;
import com.megacrit.cardcrawl.actions.animations.FastShakeAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.*;
import com.megacrit.cardcrawl.actions.utility.SFXAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.MonsterStrings;

import java.util.AbstractList;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class GnawingCorruption extends CustomMonster
{
    public static final String ID = "Abyss:GnawingCorruption";
    private static final MonsterStrings monsterStrings;
    public static final String NAME;
    public static final String[] MOVES;
    private static final String IMG = Abyss.monsterImage(ID);
    private boolean firstMove = true;
    private static final byte GNAWING_CORRUPTION_DEBUFF = 1;
    private static final byte TENTACLE_SWEEP_ATTACK = 2;
    private static final byte LEFT_EYE_ATTACK = 3;
    private static final byte RIGHT_EYE_ATTACK = 4;
    private static final byte ALL_EYES_ATTACK = 5;
    private static final int GNAWING_CORRUPTION_CURSES = 3;
    private static final int A18_GNAWING_CORRUPTION_CURSES = 5;
    private static final int TENTACLE_SWEEP_DAMAGE = 27;
    private static final int A3_TENTACLE_SWEEP_DAMAGE = 30;
    private static final int LEFT_EYE_DAMAGE = 18;
    private static final int A3_LEFT_EYE_DAMAGE = 20;
    private static final int LEFT_EYE_CURSES = 1;
    private static final int A18_LEFT_EYE_CURSES = 2;
    private static final int RIGHT_EYE_DAMAGE = 18;
    private static final int A3_RIGHT_EYE_DAMAGE = 20;
    private static final int RIGHT_EYE_HEAL = 25;
    private static final int A18_RIGHT_EYE_HEAL = 50;
    private static final int ALL_EYES_DAMAGE = 10;
    private static final int A3_ALL_EYES_DAMAGE = 11;
    private static final int ALL_EYES_HITS = 5;
    private static final int HP_MIN = 290;
    private static final int HP_MAX = 290;
    private static final int A8_HP_MIN = 325;
    private static final int A8_HP_MAX = 325;
    private int gnawingCorruptionCurses;
    private int tentacleSweepDamage;
    private int leftEyeDamage;
    private int leftEyeCurses;
    private int rightEyeDamage;
    private int rightEyeHeal;
    private int allEyesDamage;

    public GnawingCorruption() {
        this(0.0f, 0.0f);
    }

    public GnawingCorruption(final float x, final float y) {
        super(GnawingCorruption.NAME, ID, HP_MAX, -5.0F, 0, 500.0f, 335.0f, IMG, x, y);
        this.type = EnemyType.ELITE;
        if (AbstractDungeon.ascensionLevel >= 8) {
            this.setHp(A8_HP_MIN, A8_HP_MAX);
        } else {
            this.setHp(HP_MIN, HP_MAX);
        }

        if (AbstractDungeon.ascensionLevel >= 3) {
            this.tentacleSweepDamage = A3_TENTACLE_SWEEP_DAMAGE;
            this.leftEyeDamage = A3_LEFT_EYE_DAMAGE;
            this.rightEyeDamage = A3_RIGHT_EYE_DAMAGE;
            this.allEyesDamage = A3_ALL_EYES_DAMAGE;
        } else {
            this.tentacleSweepDamage = TENTACLE_SWEEP_DAMAGE;
            this.leftEyeDamage = LEFT_EYE_DAMAGE;
            this.rightEyeDamage = RIGHT_EYE_DAMAGE;
            this.allEyesDamage = ALL_EYES_DAMAGE;
        }
        this.damage.add(new DamageInfo(this, this.tentacleSweepDamage));
        this.damage.add(new DamageInfo(this, this.leftEyeDamage));
        this.damage.add(new DamageInfo(this, this.rightEyeDamage));
        this.damage.add(new DamageInfo(this, this.allEyesDamage));

        if (AbstractDungeon.ascensionLevel >= 18) {
            this.gnawingCorruptionCurses = A18_GNAWING_CORRUPTION_CURSES;
            this.leftEyeCurses = A18_LEFT_EYE_CURSES;
            this.rightEyeHeal = A18_RIGHT_EYE_HEAL;
        }
        else {
            this.gnawingCorruptionCurses = GNAWING_CORRUPTION_CURSES;
            this.leftEyeCurses = LEFT_EYE_CURSES;
            this.rightEyeHeal = RIGHT_EYE_HEAL;
        }
    }

    @Override
    public void takeTurn() {
        if (this.firstMove) {
            this.firstMove = false;
        }
        switch (this.nextMove) {
            case GNAWING_CORRUPTION_DEBUFF:
                //TODO sound would really benefit from a curse sound effect
                AbstractDungeon.actionManager.addToBottom(new FastShakeAction(this, 0.5F, 0.2F));
                for (AbstractCard c : this.getCurses(this.gnawingCorruptionCurses)) {
                    AbstractDungeon.actionManager.addToBottom(new MakeTempCardInDiscardAction(c, 1));
                }
                break;
            case TENTACLE_SWEEP_ATTACK:
                AbstractDungeon.actionManager.addToBottom(new AnimateSlowAttackAction(this));
                AbstractDungeon.actionManager.addToBottom(new DamageAction(AbstractDungeon.player, this.damage.get(0), AbstractGameAction.AttackEffect.BLUNT_HEAVY));
                break;
            case LEFT_EYE_ATTACK:
                AbstractDungeon.actionManager.addToBottom(new SFXAction("ATTACK_MAGIC_BEAM_SHORT", 0.5F));
                AbstractDungeon.actionManager.addToBottom(new VFXAction(new SmallColorLaserEffect(AbstractDungeon.player.hb.cX, AbstractDungeon.player.hb.cY, this.hb.cX, this.hb.cY, Color.MAROON), Settings.FAST_MODE ? 0.1F : 0.3F));
                AbstractDungeon.actionManager.addToBottom(new DamageAction(AbstractDungeon.player, this.damage.get(1), AbstractGameAction.AttackEffect.NONE));
                for (AbstractCard c : this.getCurses(this.leftEyeCurses)) {
                    AbstractDungeon.actionManager.addToBottom(new MakeTempCardInDrawPileAction(c, 1, true, true));
                }
                break;
            case RIGHT_EYE_ATTACK:
                AbstractDungeon.actionManager.addToBottom(new SFXAction("ATTACK_MAGIC_BEAM_SHORT", 0.5F));
                AbstractDungeon.actionManager.addToBottom(new VFXAction(new SmallColorLaserEffect(AbstractDungeon.player.hb.cX, AbstractDungeon.player.hb.cY, this.hb.cX, this.hb.cY, Color.NAVY), Settings.FAST_MODE ? 0.1F : 0.3F));
                AbstractDungeon.actionManager.addToBottom(new DamageAction(AbstractDungeon.player, this.damage.get(2), AbstractGameAction.AttackEffect.NONE));
                AbstractDungeon.actionManager.addToBottom(new HealAction(this, this, this.rightEyeHeal));
                break;
            case ALL_EYES_ATTACK:
                Color[] laserColors = {Color.RED, Color.GREEN, Color.BLUE, Color.PURPLE, Color.GOLD};
                for (int i=0; i < ALL_EYES_ATTACK; i++) {
                    AbstractDungeon.actionManager.addToBottom(new SFXAction("ATTACK_MAGIC_BEAM_SHORT", 0.5F));
                    AbstractDungeon.actionManager.addToBottom(new VFXAction(new SmallColorLaserEffect(AbstractDungeon.player.hb.cX, AbstractDungeon.player.hb.cY, this.hb.cX, this.hb.cY, laserColors[i]), Settings.FAST_MODE ? 0.1F : 0.3F));
                    AbstractDungeon.actionManager.addToBottom(new DamageAction(AbstractDungeon.player, this.damage.get(3), AbstractGameAction.AttackEffect.NONE));
                }
                break;
        }
        AbstractDungeon.actionManager.addToBottom(new RollMoveAction(this));
    }

    private List<AbstractCard> getCurses(int amount) {
        AbstractList<AbstractCard> curses = new ArrayList<>();
        curses.add(new Drained());
        curses.add(new Panic());
        curses.add(new Staggered());
        curses.add(new Tormented());
        curses.add(new Withering());
        Collections.shuffle(curses, AbstractDungeon.aiRng.random);
        return curses.subList(0, amount);
    }

    @Override
    protected void getMove(final int num) {
        byte move;
        switch (this.moveHistory.size() % 4) {
            case 0:
                move = num < 50 ? GNAWING_CORRUPTION_DEBUFF : this.getVariableAttack();
                break;
            case 1:
                move = this.lastMove(GNAWING_CORRUPTION_DEBUFF) ? this.getVariableAttack() : GNAWING_CORRUPTION_DEBUFF;
                break;
            case 2:
                move = ALL_EYES_ATTACK;
                break;
            case 3:
                move = this.getVariableAttack();
                break;
            default:
                throw new RuntimeException("Impossible case");
        }

        switch (move) {
            case GNAWING_CORRUPTION_DEBUFF:
                this.setMove(MOVES[0], GNAWING_CORRUPTION_DEBUFF, Intent.DEBUFF);
                break;
            case TENTACLE_SWEEP_ATTACK:
                this.setMove(MOVES[1], TENTACLE_SWEEP_ATTACK, Intent.ATTACK, this.tentacleSweepDamage);
                break;
            case LEFT_EYE_ATTACK:
                this.setMove(MOVES[2], LEFT_EYE_ATTACK, Intent.ATTACK_DEBUFF, this.leftEyeDamage);
                break;
            case RIGHT_EYE_ATTACK:
                this.setMove(MOVES[3], RIGHT_EYE_ATTACK, Intent.ATTACK_BUFF, this.rightEyeDamage);
                break;
            case ALL_EYES_ATTACK:
                this.setMove(MOVES[4], ALL_EYES_ATTACK, Intent.ATTACK, this.allEyesDamage, ALL_EYES_HITS, true);
                break;
        }
    }

    private byte getVariableAttack() {
        List<Byte> variableAttackOptions = new ArrayList<>();
        if (!this.firstMove) {
            variableAttackOptions.add(TENTACLE_SWEEP_ATTACK);
        }
        variableAttackOptions.add(LEFT_EYE_ATTACK);
        variableAttackOptions.add(RIGHT_EYE_ATTACK);
        for (byte move : this.moveHistory) {
            if (variableAttackOptions.contains(move)) {
                variableAttackOptions.removeIf(m -> m == move);
                break;
            }
        }
        Collections.shuffle(variableAttackOptions, AbstractDungeon.aiRng.random);
        return variableAttackOptions.get(0);
    }

    static {
        monsterStrings = CardCrawlGame.languagePack.getMonsterStrings(ID);
        NAME = GnawingCorruption.monsterStrings.NAME;
        MOVES = GnawingCorruption.monsterStrings.MOVES;
    }
}