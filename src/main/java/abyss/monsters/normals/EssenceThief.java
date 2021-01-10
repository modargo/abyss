package abyss.monsters.normals;

import abyss.Abyss;
import abyss.actions.ChangeMaxHpAction;
import abyss.powers.EssenceThiefPower;
import basemod.abstracts.CustomMonster;
import basemod.helpers.VfxBuilder;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.AnimateFastAttackAction;
import com.megacrit.cardcrawl.actions.animations.FastShakeAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.RollMoveAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.localization.MonsterStrings;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;

public class EssenceThief extends CustomMonster {
    public static final String ID = "Abyss:EssenceThief";
    private static final MonsterStrings monsterStrings;
    public static final String NAME;
    public static final String[] MOVES;
    private static final String IMG = Abyss.monsterImage(ID);
    private boolean firstMove = true;
    private static final byte GOO_MISSILE_ATTACK = 1;
    private static final byte STEAL_ESSENCE_DEBUFF = 2;
    private static final int GOO_MISSILE_DAMAGE = 8;
    private static final int A2_GOO_MISSILE_DAMAGE = 9;
    private static final int STEAL_ESSENCE_AMOUNT = 1;
    private static final int A17_STEAL_ESSENCE_AMOUNT = 1;
    private static final int STEAL_ESSENCE_LIMIT = 5;
    private static final int ATTACKS_BEFORE_STEAL_ESSENCE = 2;
    private static final int A17_ATTACKS_BEFORE_STEAL_ESSENCE = 1;
    private static final int HP_MIN = 70;
    private static final int HP_MAX = 75;
    private static final int A7_HP_MIN = 73;
    private static final int A7_HP_MAX = 78;
    private int gooMissileDamage;
    private int stealEssenceAmount;
    private int attacksBeforeStealEssence;

    private int attacksMade;
    private int maxHpStolen;

    public EssenceThief() {
        this(0.0f, 0.0f);
    }

    public EssenceThief(final float x, final float y) {
        super(EssenceThief.NAME, ID, HP_MAX, -5.0F, 0, 305.0f, 245.0f, IMG, x, y);
        this.type = EnemyType.NORMAL;
        this.maxHpStolen = 0;
        if (AbstractDungeon.ascensionLevel >= 7) {
            this.setHp(A7_HP_MIN, A7_HP_MAX);
        } else {
            this.setHp(HP_MIN, HP_MAX);
        }

        if (AbstractDungeon.ascensionLevel >= 2) {
            this.gooMissileDamage = A2_GOO_MISSILE_DAMAGE;
        } else {
            this.gooMissileDamage = GOO_MISSILE_DAMAGE;
        }
        this.damage.add(new DamageInfo(this, this.gooMissileDamage));

        if (AbstractDungeon.ascensionLevel >= 17) {
            this.stealEssenceAmount = A17_STEAL_ESSENCE_AMOUNT;
            this.attacksBeforeStealEssence = A17_ATTACKS_BEFORE_STEAL_ESSENCE;
        } else {
            this.stealEssenceAmount = STEAL_ESSENCE_AMOUNT;
            this.attacksBeforeStealEssence = ATTACKS_BEFORE_STEAL_ESSENCE;
        }
    }

    @Override
    public void usePreBattleAction() {
        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(this, this, new EssenceThiefPower(this)));
    }

    @Override
    public void takeTurn() {
        if (this.firstMove) {
            this.firstMove = false;
        }
        switch (this.nextMove) {
            case GOO_MISSILE_ATTACK:
                //TODO Poison/acid effect would be nice
                AbstractDungeon.actionManager.addToBottom(new AnimateFastAttackAction(this));
                AbstractDungeon.actionManager.addToBottom(new DamageAction(AbstractDungeon.player, this.damage.get(0), AbstractGameAction.AttackEffect.POISON));
                this.attacksMade++;
                break;
            case STEAL_ESSENCE_DEBUFF:
                float x = AbstractDungeon.player.drawX + AbstractDungeon.player.hb_w / 2.0f;
                float y = AbstractDungeon.player.drawY + AbstractDungeon.player.hb_h / 2.0f;
                AbstractGameEffect effect = new VfxBuilder(ImageMaster.INTENT_DEBUFF2, x, y, 0.8f)
                        .scale(1.0f, 3.0f)
                        .fadeIn(0.1f)
                        .fadeOut(0.1f)
                        .playSoundAt(0.1f, -0.75f, "BLOOD_SPLIT")
                        .build();
                AbstractDungeon.effectsQueue.add(effect);
                AbstractDungeon.actionManager.addToBottom(new FastShakeAction(this, 0.5F, 0.2F));
                AbstractDungeon.actionManager.addToBottom(new ChangeMaxHpAction(AbstractDungeon.player, -this.stealEssenceAmount, false));
                this.maxHpStolen++;
                break;
        }
        AbstractDungeon.actionManager.addToBottom(new RollMoveAction(this));
    }

    @Override
    protected void getMove(final int num) {
        if (this.maxHpStolen >= STEAL_ESSENCE_LIMIT || this.attacksMade < this.attacksBeforeStealEssence) {
            this.setMove(MOVES[0], GOO_MISSILE_ATTACK, Intent.ATTACK, this.gooMissileDamage);
        }
        else {
            this.setMove(MOVES[1], STEAL_ESSENCE_DEBUFF, Intent.STRONG_DEBUFF);
        }
    }

    static {
        monsterStrings = CardCrawlGame.languagePack.getMonsterStrings(ID);
        NAME = EssenceThief.monsterStrings.NAME;
        MOVES = EssenceThief.monsterStrings.MOVES;
    }
}