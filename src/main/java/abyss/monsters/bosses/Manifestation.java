package abyss.monsters.bosses;

import abyss.Abyss;
import basemod.abstracts.CustomMonster;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.AnimateFastAttackAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.RollMoveAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.MonsterStrings;
import com.megacrit.cardcrawl.powers.StrengthPower;

public class Manifestation extends CustomMonster {
    public static final String ID = "Abyss:Manifestation";
    private static final MonsterStrings monsterStrings;
    public static final String NAME;
    public static final String[] MOVES;
    private static final String IMG = Abyss.monsterImage(ID);
    private boolean firstMove = true;
    private static final byte GRASP_THE_ABYSS_ATTACK = 1;
    private static final byte MADNESS_BUFF = 2;
    private static final int GRASP_THE_ABYSS_DAMAGE = 6;
    private static final int A4_GRASP_THE_ABYSS_DAMAGE = 7;
    private static final int MADNESS_STRENGTH = 2;
    private static final int A19_MADNESS_STRENGTH = 4;
    private static final int HP_MIN = 44;
    private static final int HP_MAX = 47;
    private static final int A9_HP_MIN = 46;
    private static final int A9_HP_MAX = 49;
    private int graspTheAbyssDamage;
    private int madnessStrength;

    public Manifestation() {
        this(0.0f, 0.0f);
    }

    public Manifestation(final float x, final float y) {
        super(Manifestation.NAME, ID, HP_MAX, -5.0F, 0, 105.0f, 105.0f, IMG, x, y);
        this.type = EnemyType.NORMAL;
        if (AbstractDungeon.ascensionLevel >= 9) {
            this.setHp(A9_HP_MIN, A9_HP_MAX);
        } else {
            this.setHp(HP_MIN, HP_MAX);
        }

        if (AbstractDungeon.ascensionLevel >= 4) {
            this.graspTheAbyssDamage = A4_GRASP_THE_ABYSS_DAMAGE;
        } else {
            this.graspTheAbyssDamage = GRASP_THE_ABYSS_DAMAGE;
        }
        this.damage.add(new DamageInfo(this, this.graspTheAbyssDamage));

        if (AbstractDungeon.ascensionLevel >= 19) {
            this.madnessStrength = A19_MADNESS_STRENGTH;
        } else {
            this.madnessStrength = MADNESS_STRENGTH;
        }
    }

    @Override
    public void takeTurn() {
        if (this.firstMove) {
            this.firstMove = false;
        }
        switch (this.nextMove) {
            case GRASP_THE_ABYSS_ATTACK:
                //TODO Add a cool effect
                AbstractDungeon.actionManager.addToBottom(new AnimateFastAttackAction(this));
                AbstractDungeon.actionManager.addToBottom(new DamageAction(AbstractDungeon.player, this.damage.get(0), AbstractGameAction.AttackEffect.NONE));
                break;
            case MADNESS_BUFF:
                //TODO Add a cool effect
                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(this, this, new StrengthPower(this, this.madnessStrength), this.madnessStrength));
                break;
        }
        AbstractDungeon.actionManager.addToBottom(new RollMoveAction(this));
    }

    @Override
    protected void getMove(final int num) {
        if (!this.lastMove(GRASP_THE_ABYSS_ATTACK) || !this.lastMoveBefore(GRASP_THE_ABYSS_ATTACK)) {
            this.setMove(MOVES[0], GRASP_THE_ABYSS_ATTACK, Intent.ATTACK, this.graspTheAbyssDamage);
        }
        else {
            this.setMove(MOVES[1], MADNESS_BUFF, Intent.BUFF);
        }
    }

    static {
        monsterStrings = CardCrawlGame.languagePack.getMonsterStrings(ID);
        NAME = Manifestation.monsterStrings.NAME;
        MOVES = Manifestation.monsterStrings.MOVES;
    }
}