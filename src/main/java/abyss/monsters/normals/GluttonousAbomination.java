package abyss.monsters.normals;

import abyss.Abyss;
import abyss.powers.GluttonyPower;
import basemod.abstracts.CustomMonster;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.AnimateFastAttackAction;
import com.megacrit.cardcrawl.actions.animations.AnimateSlowAttackAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.RollMoveAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.MonsterStrings;
import com.megacrit.cardcrawl.powers.StrengthPower;

public class GluttonousAbomination extends CustomMonster {
    public static final String ID = "Abyss:GluttonousAbomination";
    private static final MonsterStrings monsterStrings;
    public static final String NAME;
    public static final String[] MOVES;
    private static final String IMG = Abyss.monsterImage(ID);
    private boolean firstMove = true;
    private static final byte GRASPING_CLAWS_ATTACK = 1;
    private static final byte UNHINGED_JAW_ATTACK = 2;
    private static final int GRASPING_CLAWS_DAMAGE = 5;
    private static final int A2_GRASPING_CLAWS_DAMAGE = 6;
    private static final int GRASPING_CLAWS_HITS = 2;
    private static final int UNHINGED_JAW_DAMAGE = 12;
    private static final int A2_UNHINGED_JAW_DAMAGE = 14;
    private static final int GLUTTONY = 8;
    private static final int A17_GLUTTONY = 7;
    private static final int HP_MIN = 225;
    private static final int HP_MAX = 225;
    private static final int A7_HP_MIN = 250;
    private static final int A7_HP_MAX = 250;
    private int graspingClawsDamage;
    private int unhingedJawDamage;
    private int gluttony;

    public GluttonousAbomination() {
        this(0.0f, 0.0f);
    }

    public GluttonousAbomination(final float x, final float y) {
        super(GluttonousAbomination.NAME, ID, HP_MAX, -5.0F, 0, 455, 365, IMG, x, y);
        this.type = EnemyType.NORMAL;
        if (AbstractDungeon.ascensionLevel >= 7) {
            this.setHp(A7_HP_MIN, A7_HP_MAX);
        } else {
            this.setHp(HP_MIN, HP_MAX);
        }

        if (AbstractDungeon.ascensionLevel >= 2) {
            this.graspingClawsDamage = A2_GRASPING_CLAWS_DAMAGE;
            this.unhingedJawDamage = A2_UNHINGED_JAW_DAMAGE;
        } else {
            this.graspingClawsDamage = GRASPING_CLAWS_DAMAGE;
            this.unhingedJawDamage = UNHINGED_JAW_DAMAGE;
        }
        this.damage.add(new DamageInfo(this, this.graspingClawsDamage));
        this.damage.add(new DamageInfo(this, this.unhingedJawDamage));

        if (AbstractDungeon.ascensionLevel >= 17) {
            this.gluttony = A17_GLUTTONY;
        } else {
            this.gluttony = GLUTTONY;
        }
    }

    @Override
    public void usePreBattleAction() {
        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(this, this, new GluttonyPower(this, this.gluttony)));
        int strength = AbstractDungeon.player.currentHealth / this.gluttony;
        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(this, this, new StrengthPower(this, strength), strength));
    }

    @Override
    public void takeTurn() {
        if (this.firstMove) {
            this.firstMove = false;
        }
        switch (this.nextMove) {
            case GRASPING_CLAWS_ATTACK:
                AbstractDungeon.actionManager.addToBottom(new AnimateFastAttackAction(this));
                for (int i = 0; i < GRASPING_CLAWS_HITS; i++) {
                    AbstractDungeon.actionManager.addToBottom(new DamageAction(AbstractDungeon.player, this.damage.get(0), AbstractGameAction.AttackEffect.SLASH_DIAGONAL));
                }
                break;
            case UNHINGED_JAW_ATTACK:
                AbstractDungeon.actionManager.addToBottom(new AnimateSlowAttackAction(this));
                AbstractDungeon.actionManager.addToBottom(new DamageAction(AbstractDungeon.player, this.damage.get(1), AbstractGameAction.AttackEffect.SLASH_HORIZONTAL));
                break;
        }
        AbstractDungeon.actionManager.addToBottom(new RollMoveAction(this));
    }

    @Override
    protected void getMove(final int num) {
        if ((this.firstMove && AbstractDungeon.ascensionLevel >= 17) || this.lastMove(UNHINGED_JAW_ATTACK)) {
            this.setMove(GRASPING_CLAWS_ATTACK, Intent.ATTACK, this.graspingClawsDamage, GRASPING_CLAWS_HITS, true);
        }
        else {
            this.setMove(UNHINGED_JAW_ATTACK, Intent.ATTACK, this.unhingedJawDamage);
        }
    }

    static {
        monsterStrings = CardCrawlGame.languagePack.getMonsterStrings(ID);
        NAME = GluttonousAbomination.monsterStrings.NAME;
        MOVES = GluttonousAbomination.monsterStrings.MOVES;
    }
}