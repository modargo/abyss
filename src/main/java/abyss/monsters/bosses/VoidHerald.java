package abyss.monsters.bosses;

import abyss.Abyss;
import abyss.monsters.MonsterUtil;
import abyss.monsters.normals.UnboundAbyssal;
import abyss.powers.ResummonPower;
import abyss.powers.TemporaryHexPower;
import abyss.powers.ThoughtStealerPower;
import basemod.abstracts.CustomMonster;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.AnimateFastAttackAction;
import com.megacrit.cardcrawl.actions.animations.FastShakeAction;
import com.megacrit.cardcrawl.actions.animations.TalkAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInDiscardAction;
import com.megacrit.cardcrawl.actions.common.RollMoveAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.cards.status.Slimed;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.MonsterStrings;
import com.megacrit.cardcrawl.powers.ThornsPower;
import com.megacrit.cardcrawl.powers.VulnerablePower;

public class VoidHerald extends CustomMonster
{
    public static final String ID = "Abyss:VoidHerald";
    private static final MonsterStrings monsterStrings;
    public static final String NAME;
    public static final String[] MOVES;
    public static final String[] DIALOG;
    private static final String IMG = Abyss.monsterImage(ID);
    private boolean firstMove = true;
    private static final byte DURESS_DEBUFF = 1;
    private static final byte CORRUPTING_TOUCH_ATTACK = 2;
    private static final byte ONSLAUGHT_ATTACK = 3;
    private static final int DURESS_VULNERABLE = 1;
    private static final int A19_DURESS_VULNERABLE = 2;
    private static final int DURESS_TEMPORARY_HEX = 1;
    private static final int CORRUPTING_TOUCH_DAMAGE = 6;
    private static final int A4_CORRUPTING_TOUCH_DAMAGE = 7;
    private static final int CORRUPTING_TOUCH_HITS = 2;
    private static final int CORRUPTING_TOUCH_SLIMES = 1;
    private static final int A19_CORRUPTING_TOUCH_SLIMES = 2;
    private static final int ONSLAUGHT_DAMAGE = 3;
    private static final int A4_ONSLAUGHT_DAMAGE = 4;
    private static final int ONSLAUGHT_HITS = 3;
    private static final int THORNS = 3;
    private static final int A19_THORNS = 4;
    private static final int HP = 380;
    private static final int A9_HP = 400;
    private int duressVulnerable;
    private int corruptingTouchDamage;
    private int corruptingTouchSlimes;
    private int onslaughtDamage;
    private int thorns;

    public VoidHerald() {
        this(0.0f, 0.0f);
    }

    public VoidHerald(final float x, final float y) {
        super(VoidHerald.NAME, ID, HP, -5.0F, 0, 455.0f, 405.0f, IMG, x, y);
        this.type = EnemyType.BOSS;
        if (AbstractDungeon.ascensionLevel >= 9) {
            this.setHp(A9_HP);
        } else {
            this.setHp(HP);
        }

        if (AbstractDungeon.ascensionLevel >= 4) {
            this.corruptingTouchDamage = A4_CORRUPTING_TOUCH_DAMAGE;
            this.onslaughtDamage = A4_ONSLAUGHT_DAMAGE;
        } else {
            this.corruptingTouchDamage = CORRUPTING_TOUCH_DAMAGE;
            this.onslaughtDamage = ONSLAUGHT_DAMAGE;
        }
        this.damage.add(new DamageInfo(this, this.corruptingTouchDamage));
        this.damage.add(new DamageInfo(this, this.onslaughtDamage));

        if (AbstractDungeon.ascensionLevel >= 19) {
            this.duressVulnerable = A19_DURESS_VULNERABLE;
            this.corruptingTouchSlimes = A19_CORRUPTING_TOUCH_SLIMES;
            this.thorns = A19_THORNS;
        }
        else {
            this.duressVulnerable = DURESS_VULNERABLE;
            this.corruptingTouchSlimes = CORRUPTING_TOUCH_SLIMES;
            this.thorns = THORNS;
        }
    }

    @Override
    public void usePreBattleAction() {
        CardCrawlGame.music.unsilenceBGM();
        AbstractDungeon.scene.fadeOutAmbiance();
        AbstractDungeon.getCurrRoom().playBgmInstantly("BOSS_BEYOND");

        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(this, this, new ThornsPower(this, this.thorns), this.thorns));
        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(this, this, new ThoughtStealerPower(this)));
        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(this, this, new ResummonPower(this)));
    }

    @Override
    public void takeTurn() {
        if (this.firstMove) {
            if (this.hasCardWithTag("ELEMENTAL_BLADE")) {
                AbstractDungeon.actionManager.addToBottom(new TalkAction(this, DIALOG[1], 0.5F, 2.0F));
            }
            else {
                AbstractDungeon.actionManager.addToBottom(new TalkAction(this, DIALOG[0], 0.5F, 2.0F));
            }
            this.firstMove = false;
        }
        switch (this.nextMove) {
            case DURESS_DEBUFF:
                AbstractDungeon.actionManager.addToBottom(new FastShakeAction(this, 0.5F, 0.2F));
                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(AbstractDungeon.player, this, new VulnerablePower(AbstractDungeon.player, this.duressVulnerable, true), this.duressVulnerable));
                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(AbstractDungeon.player, this, new TemporaryHexPower(AbstractDungeon.player, DURESS_TEMPORARY_HEX, true), DURESS_TEMPORARY_HEX));
                break;
            case CORRUPTING_TOUCH_ATTACK:
                AbstractDungeon.actionManager.addToBottom(new AnimateFastAttackAction(this));
                for (int i = 0; i < CORRUPTING_TOUCH_HITS; i++) {
                    AbstractDungeon.actionManager.addToBottom(new DamageAction(AbstractDungeon.player, this.damage.get(0), AbstractGameAction.AttackEffect.POISON));
                }
                AbstractDungeon.actionManager.addToBottom(new MakeTempCardInDiscardAction(new Slimed(), this.corruptingTouchSlimes));
                break;
            case ONSLAUGHT_ATTACK:
                AbstractDungeon.actionManager.addToBottom(new AnimateFastAttackAction(this));
                for (int i = 0; i < ONSLAUGHT_HITS; i++) {
                    AbstractDungeon.actionManager.addToBottom(new DamageAction(AbstractDungeon.player, this.damage.get(1), AbstractGameAction.AttackEffect.SLASH_HORIZONTAL));
                }
                break;
        }
        AbstractDungeon.actionManager.addToBottom(new RollMoveAction(this));
    }

    @Override
    protected void getMove(final int num) {
        byte move;
        boolean lastTwoNotDuress = !this.lastMove(DURESS_DEBUFF) && !this.lastMoveBefore(DURESS_DEBUFF);
        boolean lastThreeNotDuress = lastTwoNotDuress && !MonsterUtil.lastMoveX(this, DURESS_DEBUFF, 3);
        boolean lastFourNotDuress = lastThreeNotDuress && !MonsterUtil.lastMoveX(this, DURESS_DEBUFF, 4);
        if (!this.firstMove && lastFourNotDuress) {
                move = DURESS_DEBUFF;
        }
        else if (lastThreeNotDuress && num < 50) {
            move = DURESS_DEBUFF;
        }
        else if (lastTwoNotDuress) {
            move = num < 25 ? DURESS_DEBUFF : num < 60 ? ONSLAUGHT_ATTACK : CORRUPTING_TOUCH_ATTACK;
        }
        else {
            move = num < 40 ? ONSLAUGHT_ATTACK : CORRUPTING_TOUCH_ATTACK;
        }
        switch (move) {
            case DURESS_DEBUFF:
                this.setMove(MOVES[0], move, Intent.STRONG_DEBUFF);
                break;
            case ONSLAUGHT_ATTACK:
                this.setMove(MOVES[1], move, Intent.ATTACK, this.onslaughtDamage, ONSLAUGHT_HITS, true);
                break;
            case CORRUPTING_TOUCH_ATTACK:
                this.setMove(MOVES[2], move, Intent.ATTACK_DEBUFF, this.corruptingTouchDamage, CORRUPTING_TOUCH_HITS, true);
                break;
        }
    }

    @Override
    public void die() {
        super.die();
        if (AbstractDungeon.getMonsters().areMonstersBasicallyDead()) {
            this.useFastShakeAnimation(5.0F);
            CardCrawlGame.screenShake.rumble(4.0F);
            this.onBossVictoryLogic();
        }
    }

    private boolean hasCardWithTag(String tagName) {
        boolean hasCard = false;
        for (AbstractCard c : AbstractDungeon.player.masterDeck.group) {
            for (AbstractCard.CardTags tag : c.tags) {
                hasCard = hasCard || tag.name().equals(tagName);
            }
        }

        return hasCard;
    }

    static {
        monsterStrings = CardCrawlGame.languagePack.getMonsterStrings(ID);
        NAME = VoidHerald.monsterStrings.NAME;
        MOVES = VoidHerald.monsterStrings.MOVES;
        DIALOG = VoidHerald.monsterStrings.DIALOG;
    }
}