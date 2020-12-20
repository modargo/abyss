package abyss.monsters.normals;

import abyss.Abyss;
import abyss.powers.AbysstouchedPower;
import basemod.abstracts.CustomMonster;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.AnimateFastAttackAction;
import com.megacrit.cardcrawl.actions.animations.FastShakeAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.actions.common.RollMoveAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.MonsterStrings;
import com.megacrit.cardcrawl.powers.ConstrictedPower;

public class ViciousHatchling extends CustomMonster {
    public static final String ID = "Abyss:ViciousHatchling";
    private static final MonsterStrings monsterStrings;
    public static final String NAME;
    public static final String[] MOVES;
    private static final String IMG = Abyss.monsterImage(ID);
    private boolean firstMove = true;
    private static final byte JUST_HATCHED_MOVE = 1;
    private static final byte NIBBLE_ATTACK = 2;
    private static final byte CORROSIVE_SPIT_DEBUFF = 3;
    private static final byte HEADBUTT_ATTACK = 4;
    private static final int NIBBLE_DAMAGE = 6;
    private static final int A2_NIBBLE_DAMAGE = 7;
    private static final int NIBBLE_HITS = 2;
    private static final int CORROSIVE_SPIT_ACID = 4;
    private static final int A17_CORROSIVE_SPIT_ACID = 5;
    private static final int HEADBUTT_DAMAGE = 7;
    private static final int A2_HEADBUTT_DAMAGE = 8;
    private static final int HEADBUTT_BLOCK = 10;
    private static final int A7_HEADBUTT_BLOCK = 12;
    private static final int HP_MIN = 48;
    private static final int HP_MAX = 53;
    private static final int A7_HP_MIN = 52;
    private static final int A7_HP_MAX = 56;
    private boolean justHatched;
    private int nibbleDamage;
    private int corrosiveSpitAcid;
    private int headbuttDamage;
    private int headbuttBlock;

    public ViciousHatchling() {
        this(0.0f, 0.0f, false);
    }

    public ViciousHatchling(final float x, final float y, boolean justHatched) {
        super(ViciousHatchling.NAME, ID, HP_MAX, -5.0F, 0, 230.0f, 170.0f, IMG, x, y);
        this.type = EnemyType.NORMAL;
        this.justHatched = justHatched;
        //TODO How does this get harder at A17+?
        if (AbstractDungeon.ascensionLevel >= 7) {
            this.setHp(A7_HP_MIN, A7_HP_MAX);
            this.headbuttBlock = A7_HEADBUTT_BLOCK;
        } else {
            this.setHp(HP_MIN, HP_MAX);
            this.headbuttBlock = HEADBUTT_BLOCK;
        }

        if (AbstractDungeon.ascensionLevel >= 2) {
            this.nibbleDamage = A2_NIBBLE_DAMAGE;
            this.headbuttDamage = A2_HEADBUTT_DAMAGE;
        } else {
            this.nibbleDamage = NIBBLE_DAMAGE;
            this.headbuttDamage = HEADBUTT_DAMAGE;
        }
        this.damage.add(new DamageInfo(this, this.nibbleDamage));
        this.damage.add(new DamageInfo(this, this.headbuttDamage));

        if (AbstractDungeon.ascensionLevel >= 17) {
            this.corrosiveSpitAcid = A17_CORROSIVE_SPIT_ACID;
        } else {
            this.corrosiveSpitAcid = CORROSIVE_SPIT_ACID;
        }
    }

    @Override
    public void takeTurn() {
        if (this.firstMove) {
            this.firstMove = false;
        }
        switch (this.nextMove) {
            case JUST_HATCHED_MOVE:
                AbstractDungeon.actionManager.addToBottom(new FastShakeAction(this, 0.5F, 0.2F));
                break;
            case NIBBLE_ATTACK:
                AbstractDungeon.actionManager.addToBottom(new AnimateFastAttackAction(this));
                for (int i = 0; i < NIBBLE_HITS; i++) {
                    AbstractDungeon.actionManager.addToBottom(new DamageAction(AbstractDungeon.player, this.damage.get(0), AbstractGameAction.AttackEffect.SLASH_HORIZONTAL));
                }
                break;
            case CORROSIVE_SPIT_DEBUFF:
                AbstractDungeon.actionManager.addToBottom(new AnimateFastAttackAction(this));
                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(AbstractDungeon.player, this, new AbysstouchedPower(AbstractDungeon.player, this.corrosiveSpitAcid), this.corrosiveSpitAcid));
                break;
            case HEADBUTT_ATTACK:
                AbstractDungeon.actionManager.addToBottom(new AnimateFastAttackAction(this));
                AbstractDungeon.actionManager.addToBottom(new DamageAction(AbstractDungeon.player, this.damage.get(1), AbstractGameAction.AttackEffect.SHIELD));
                AbstractDungeon.actionManager.addToBottom(new GainBlockAction(this, this.headbuttBlock));
                break;
        }
        AbstractDungeon.actionManager.addToBottom(new RollMoveAction(this));
    }

    @Override
    protected void getMove(final int num) {
        byte move;
        if (this.firstMove && this.justHatched) {
            move = JUST_HATCHED_MOVE;
        }
        else if (this.lastMove(JUST_HATCHED_MOVE) || this.lastMove(NIBBLE_ATTACK)) {
            move = num < 50 ? CORROSIVE_SPIT_DEBUFF : HEADBUTT_ATTACK;
        }
        else if (this.lastMove(CORROSIVE_SPIT_DEBUFF) && this.lastMoveBefore(CORROSIVE_SPIT_DEBUFF)) {
            move = num < 50 ? NIBBLE_ATTACK : HEADBUTT_ATTACK;
        }
        else if ((this.firstMove && !this.justHatched) || (this.lastMove(HEADBUTT_ATTACK) && this.lastMoveBefore(HEADBUTT_ATTACK))) {
            move = num < 50 ? NIBBLE_ATTACK : CORROSIVE_SPIT_DEBUFF;
        }
        else {
            move = num < 40 ? NIBBLE_ATTACK : num < 70 ? CORROSIVE_SPIT_DEBUFF : HEADBUTT_ATTACK;
        }

        if (move == JUST_HATCHED_MOVE) {
            this.setMove(MOVES[0], JUST_HATCHED_MOVE, Intent.UNKNOWN);
        } else if (move == NIBBLE_ATTACK) {
            this.setMove(MOVES[1], NIBBLE_ATTACK, Intent.ATTACK, this.nibbleDamage, NIBBLE_HITS, true);
        } else if (move == CORROSIVE_SPIT_DEBUFF) {
            this.setMove(MOVES[2], CORROSIVE_SPIT_DEBUFF, Intent.DEBUFF);
        } else {
            this.setMove(MOVES[3], HEADBUTT_ATTACK, Intent.ATTACK_DEFEND, this.headbuttDamage);
        }
    }

    static {
        monsterStrings = CardCrawlGame.languagePack.getMonsterStrings(ID);
        NAME = ViciousHatchling.monsterStrings.NAME;
        MOVES = ViciousHatchling.monsterStrings.MOVES;
    }
}