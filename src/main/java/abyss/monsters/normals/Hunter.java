package abyss.monsters.normals;

import abyss.Abyss;
import basemod.abstracts.CustomMonster;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.AnimateFastAttackAction;
import com.megacrit.cardcrawl.actions.animations.AnimateSlowAttackAction;
import com.megacrit.cardcrawl.actions.animations.FastShakeAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.RollMoveAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.MonsterStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.GainStrengthPower;
import com.megacrit.cardcrawl.powers.PlatedArmorPower;
import com.megacrit.cardcrawl.powers.StrengthPower;

public class Hunter extends CustomMonster {
    public static final String ID = "Abyss:Hunter";
    private static final MonsterStrings monsterStrings;
    public static final String NAME;
    public static final String[] MOVES;
    private static final String IMG = Abyss.monsterImage(ID);
    private boolean firstMove = true;
    private static final byte RAZOR_SPINES_ATTACK = 1;
    private static final byte SKULK_ATTACK = 2;
    private static final byte EMPOWER_ATTACK = 3;
    private static final int RAZOR_SPINES_DAMAGE = 11;
    private static final int A2_RAZOR_SPINES_DAMAGE = 13;
    private static final int SKULK_DAMAGE = 8;
    private static final int A2_SKULK_DAMAGE = 9;
    private static final int SKULK_PLATED_ARMOR = 2;
    private static final int A7_SKULK_PLATED_ARMOR = 4;
    private static final int EMPOWER_DAMAGE = 6;
    private static final int A2_EMPOWER_DAMAGE = 7;
    private static final int EMPOWER_STRENGTH = 1;
    private static final int A17_EMPOWER_STRENGTH = 2;
    private static final int HP_MIN = 53;
    private static final int HP_MAX = 57;
    private static final int A7_HP_MIN = 56;
    private static final int A7_HP_MAX = 60;
    private boolean startWithSkulk;
    private int razorSpinesDamage;
    private int skulkDamage;
    private int skulkPlatedArmor;
    private int empowerDamage;
    private int empowerStrength;

    public Hunter() {
        this(0.0f, 0.0f, false);
    }

    public Hunter(final float x, final float y, boolean startWithSkulk) {
        super(Hunter.NAME, ID, HP_MAX, -5.0F, 0, 245.0f, 265.0f, IMG, x, y);
        this.type = EnemyType.NORMAL;
        this.startWithSkulk = startWithSkulk;
        if (AbstractDungeon.ascensionLevel >= 7) {
            this.setHp(A7_HP_MIN, A7_HP_MAX);
            this.skulkPlatedArmor = A7_SKULK_PLATED_ARMOR;
        } else {
            this.setHp(HP_MIN, HP_MAX);
            this.skulkPlatedArmor = SKULK_PLATED_ARMOR;
        }

        if (AbstractDungeon.ascensionLevel >= 2) {
            this.razorSpinesDamage = A2_RAZOR_SPINES_DAMAGE;
            this.skulkDamage = A2_SKULK_DAMAGE;
            this.empowerDamage = A2_EMPOWER_DAMAGE;
        } else {
            this.razorSpinesDamage = RAZOR_SPINES_DAMAGE;
            this.skulkDamage = SKULK_DAMAGE;
            this.empowerDamage = EMPOWER_DAMAGE;
        }
        this.damage.add(new DamageInfo(this, this.razorSpinesDamage));
        this.damage.add(new DamageInfo(this, this.skulkDamage));
        this.damage.add(new DamageInfo(this, this.empowerDamage));

        if (AbstractDungeon.ascensionLevel >= 17) {
            this.empowerStrength = A17_EMPOWER_STRENGTH;
        } else {
            this.empowerStrength = EMPOWER_STRENGTH;
        }
    }

    @Override
    public void takeTurn() {
        if (this.firstMove) {
            this.firstMove = false;
        }
        switch (this.nextMove) {
            case RAZOR_SPINES_ATTACK:
                AbstractDungeon.actionManager.addToBottom(new AnimateFastAttackAction(this));
                AbstractDungeon.actionManager.addToBottom(new DamageAction(AbstractDungeon.player, this.damage.get(0), AbstractGameAction.AttackEffect.SLASH_HEAVY));
                break;
            case SKULK_ATTACK:
                AbstractDungeon.actionManager.addToBottom(new AnimateFastAttackAction(this));
                AbstractDungeon.actionManager.addToBottom(new DamageAction(AbstractDungeon.player, this.damage.get(1), AbstractGameAction.AttackEffect.SLASH_DIAGONAL));
                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(this, this, new PlatedArmorPower(this, this.skulkPlatedArmor), this.skulkPlatedArmor));
                break;
            case EMPOWER_ATTACK:
                AbstractDungeon.actionManager.addToBottom(new AnimateSlowAttackAction(this));
                AbstractDungeon.actionManager.addToBottom(new DamageAction(AbstractDungeon.player, this.damage.get(2), AbstractGameAction.AttackEffect.SLASH_HORIZONTAL));
                AbstractDungeon.actionManager.addToBottom(new FastShakeAction(this, 0.5F, 0.2F));
                for (AbstractMonster m : AbstractDungeon.getCurrRoom().monsters.monsters) {
                    if (m == this || !m.isDying) {
                        // For this enemy, those to the left of it (which have already acted), and those that aren't attacking, give strength
                        // For enemies to the right of this enemy, give gain strength at end of turn, so their damage doesn't increase
                        if (m == this) {
                            AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(m, this, new StrengthPower(m, this.empowerStrength), this.empowerStrength));
                        }
                        else if (m.drawX < this.drawX || m.getIntentDmg() == -1) {
                            AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(m, this, new StrengthPower(m, this.empowerStrength), this.empowerStrength));
                        }
                        else {
                            AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(m, this, new GainStrengthPower(m, this.empowerStrength), this.empowerStrength));
                        }
                    }
                }
                break;
        }
        AbstractDungeon.actionManager.addToBottom(new RollMoveAction(this));
    }

    @Override
    protected void getMove(final int num) {
        if ((this.firstMove && !this.startWithSkulk) || this.lastMove(EMPOWER_ATTACK)) {
            this.setMove(MOVES[0], RAZOR_SPINES_ATTACK, Intent.ATTACK, this.razorSpinesDamage);
        }
        else if ((this.firstMove && this.startWithSkulk) || this.lastMove(RAZOR_SPINES_ATTACK)) {
            this.setMove(MOVES[1], SKULK_ATTACK, Intent.ATTACK_DEFEND, this.skulkDamage);
        }
        else {
            this.setMove(MOVES[2], EMPOWER_ATTACK, Intent.ATTACK_BUFF, this.empowerDamage);
        }
    }

    static {
        monsterStrings = CardCrawlGame.languagePack.getMonsterStrings(ID);
        NAME = Hunter.monsterStrings.NAME;
        MOVES = Hunter.monsterStrings.MOVES;
    }
}