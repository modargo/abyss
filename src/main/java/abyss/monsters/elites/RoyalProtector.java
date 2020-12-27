package abyss.monsters.elites;

import abyss.Abyss;
import basemod.abstracts.CustomMonster;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.AnimateFastAttackAction;
import com.megacrit.cardcrawl.actions.animations.FastShakeAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.*;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.cards.status.Burn;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.MonsterStrings;
import com.megacrit.cardcrawl.powers.*;
import com.megacrit.cardcrawl.vfx.combat.ExplosionSmallEffect;

public class RoyalProtector extends CustomMonster {
    public static final String ID = "Abyss:RoyalProtector";
    private static final MonsterStrings monsterStrings;
    public static final String NAME;
    public static final String[] MOVES;
    private static final String IMG = Abyss.monsterImage(ID);
    private boolean firstMove = true;
    private static final byte CARAPACE_MOVE = 1;
    private static final byte BURNING_SPIT_DEBUFF = 2;
    private static final byte ACID_EXPLOSION_ATTACK = 3;
    private static final byte FOR_THE_QUEEN_ATTACK = 4;
    private static final int CARAPACE_BLOCK_OR_PLATED_ARMOR = 5;
    private static final int ACID_EXPLOSION_DAMAGE = 23;
    private static final int A3_ACID_EXPLOSION_DAMAGE = 26;
    private static final int FOR_THE_QUEEN_DAMAGE = 4;
    private static final int A3_FOR_THE_QUEEN_DAMAGE = 5;
    private static final int FOR_THE_QUEEN_AMOUNT = 1;
    private static final int A18_FOR_THE_QUEEN_AMOUNT = 2;
    private static final int HP_MIN = 24;
    private static final int HP_MAX = 28;
    private static final int A8_HP_MIN = 26;
    private static final int A8_HP_MAX = 30;
    private int carapaceBlockOrPlatedArmor;
    private int acidExplosionDamage;
    private int forTheQueenDamage;
    private int forTheQueenAmount;

    public RoyalProtector() {
        this(0.0f, 0.0f);
    }

    public RoyalProtector(final float x, final float y) {
        super(RoyalProtector.NAME, ID, HP_MAX, -5.0F, 0, 135.0f, 135.0f, IMG, x, y);
        this.type = EnemyType.NORMAL;
        this.carapaceBlockOrPlatedArmor = CARAPACE_BLOCK_OR_PLATED_ARMOR;
        if (AbstractDungeon.ascensionLevel >= 8) {
            this.setHp(A8_HP_MIN, A8_HP_MAX);
        } else {
            this.setHp(HP_MIN, HP_MAX);
        }

        if (AbstractDungeon.ascensionLevel >= 3) {
            this.acidExplosionDamage = A3_ACID_EXPLOSION_DAMAGE;
            this.forTheQueenDamage = A3_FOR_THE_QUEEN_DAMAGE;
        } else {
            this.acidExplosionDamage = ACID_EXPLOSION_DAMAGE;
            this.forTheQueenDamage = FOR_THE_QUEEN_DAMAGE;
        }
        this.damage.add(new DamageInfo(this, this.acidExplosionDamage));
        this.damage.add(new DamageInfo(this, this.forTheQueenDamage));

        if (AbstractDungeon.ascensionLevel >= 18) {
            this.forTheQueenAmount = A18_FOR_THE_QUEEN_AMOUNT;
        } else {
            this.forTheQueenAmount = FOR_THE_QUEEN_AMOUNT;
        }
    }

    @Override
    public void takeTurn() {
        if (this.firstMove) {
            this.firstMove = false;
        }
        switch (this.nextMove) {
            case CARAPACE_MOVE:
                if (AbstractDungeon.ascensionLevel >= 18) {
                    AbstractDungeon.actionManager.addToBottom(new GainBlockAction(this, this.carapaceBlockOrPlatedArmor));
                }
                else {
                    this.addToBot(new ApplyPowerAction(this, this, new PlatedArmorPower(this, this.carapaceBlockOrPlatedArmor), this.carapaceBlockOrPlatedArmor));
                }
                break;
            case BURNING_SPIT_DEBUFF:
                //TODO animation -- adapt BloodShotEffect
                AbstractDungeon.actionManager.addToBottom(new MakeTempCardInDiscardAction(new Burn(), 1));
                break;
            case ACID_EXPLOSION_ATTACK:
                //TODO animation -- maybe Fireball effect, just making it green?
                AbstractDungeon.actionManager.addToBottom(new AnimateFastAttackAction(this));
                AbstractDungeon.actionManager.addToBottom(new DamageAction(AbstractDungeon.player, this.damage.get(0), AbstractGameAction.AttackEffect.POISON));
                break;
            case FOR_THE_QUEEN_ATTACK:
                //TODO animation -- a green explosion (make ExplosionSmallColorEffect)
                AbstractDungeon.actionManager.addToBottom(new FastShakeAction(this, 0.5F, 0.2F));
                AbstractDungeon.actionManager.addToBottom(new DamageAction(AbstractDungeon.player, this.damage.get(1), AbstractGameAction.AttackEffect.BLUNT_LIGHT));
                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(AbstractDungeon.player, this, new WeakPower(AbstractDungeon.player, this.forTheQueenAmount, true), this.forTheQueenAmount));
                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(AbstractDungeon.player, this, new FrailPower(AbstractDungeon.player, this.forTheQueenAmount, true), this.forTheQueenAmount));
                AbstractDungeon.actionManager.addToBottom(new RemoveAllBlockAction(this, this));
                AbstractDungeon.actionManager.addToBottom(new VFXAction(new ExplosionSmallEffect(this.hb.cX, this.hb.cY), 0.1F));
                AbstractDungeon.actionManager.addToBottom(new SuicideAction(this));
                break;
        }
        AbstractDungeon.actionManager.addToBottom(new RollMoveAction(this));
    }

    @Override
    protected void getMove(final int num) {
        if (this.firstMove) {
            this.setMove(MOVES[0], CARAPACE_MOVE, Intent.DEFEND);
        }
        else if (this.lastMove(CARAPACE_MOVE)) {
            this.setMove(MOVES[1], BURNING_SPIT_DEBUFF, Intent.DEBUFF);
        }
        else if (this.lastMove(BURNING_SPIT_DEBUFF)) {
            this.setMove(MOVES[2], ACID_EXPLOSION_ATTACK, Intent.ATTACK, this.acidExplosionDamage);
        }
        else {
            this.setMove(MOVES[3], FOR_THE_QUEEN_ATTACK, Intent.ATTACK_DEBUFF, this.forTheQueenDamage);
        }
    }

    static {
        monsterStrings = CardCrawlGame.languagePack.getMonsterStrings(ID);
        NAME = RoyalProtector.monsterStrings.NAME;
        MOVES = RoyalProtector.monsterStrings.MOVES;
    }
}