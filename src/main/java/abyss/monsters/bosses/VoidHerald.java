package abyss.monsters.bosses;

import abyss.Abyss;
import abyss.monsters.MonsterUtil;
import abyss.powers.ResummonPower;
import abyss.powers.TemporaryHexPower;
import abyss.powers.ThoughtStealerPower;
import basemod.abstracts.CustomMonster;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.AnimateFastAttackAction;
import com.megacrit.cardcrawl.actions.animations.FastShakeAction;
import com.megacrit.cardcrawl.actions.animations.TalkAction;
import com.megacrit.cardcrawl.actions.common.*;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.cards.status.Burn;
import com.megacrit.cardcrawl.cards.status.Dazed;
import com.megacrit.cardcrawl.cards.status.Slimed;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.MonsterStrings;
import com.megacrit.cardcrawl.powers.DrawCardNextTurnPower;
import com.megacrit.cardcrawl.powers.FrailPower;
import com.megacrit.cardcrawl.powers.VulnerablePower;
import com.megacrit.cardcrawl.powers.WeakPower;

import java.util.function.Supplier;

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
    private static final byte HYMN_TO_THE_VOID_DEBUFF = 2;
    private static final byte CORRUPTING_TOUCH_ATTACK = 3;
    private static final byte ONSLAUGHT_ATTACK = 4;
    private static final int DURESS_FRAIL = 1;
    private static final int A19_DURESS_FRAIL = 2;
    private static final int DURESS_TEMPORARY_HEX = 1;
    private static final int HYMN_TO_THE_VOID_VULNERABLE = 1;
    private static final int A19_HYMN_TO_THE_VOID_VULNERABLE = 2;
    private static final int HYMN_TO_THE_VOID_SLIMES = 1;
    private static final int A19_HYMN_TO_THE_VOID_SLIMES = 2;
    private static final int CORRUPTING_TOUCH_DAMAGE = 14;
    private static final int A4_CORRUPTING_TOUCH_DAMAGE = 16;
    private static final int CORRUPTING_TOUCH_WEAK = 1;
    private static final int A19_CORRUPTING_TOUCH_WEAK = 1;
    private static final int CORRUPTING_TOUCH_DRAW = 1;
    private static final int A19_CORRUPTING_TOUCH_DRAW = 2;
    private static final int ONSLAUGHT_DAMAGE = 6;
    private static final int A4_ONSLAUGHT_DAMAGE = 7;
    private static final int ONSLAUGHT_HITS = 2;
    private static final int HP = 380;
    private static final int A9_HP = 400;
    private int duressFrail;
    private int hymnToTheVoidVulnerable;
    private int hymnToTheVoidSlimes;
    private int corruptingTouchDamage;
    private int corruptingTouchWeak;
    private int corruptingTouchDraw;
    private int onslaughtDamage;

    public VoidHerald() {
        this(0.0f, 0.0f);
    }

    public VoidHerald(final float x, final float y) {
        super(VoidHerald.NAME, ID, HP, -5.0F, 0, 455.0f, 405.0f, IMG, x, y);
        this.dialogX = -100;
        this.dialogY = 50;
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
            this.duressFrail = A19_DURESS_FRAIL;
            this.hymnToTheVoidVulnerable = A19_HYMN_TO_THE_VOID_VULNERABLE;
            this.hymnToTheVoidSlimes = A19_HYMN_TO_THE_VOID_SLIMES;
            this.corruptingTouchWeak = A19_CORRUPTING_TOUCH_WEAK;
            this.corruptingTouchDraw = A19_CORRUPTING_TOUCH_DRAW;
        }
        else {
            this.duressFrail = DURESS_FRAIL;
            this.hymnToTheVoidVulnerable = HYMN_TO_THE_VOID_VULNERABLE;
            this.hymnToTheVoidSlimes = HYMN_TO_THE_VOID_SLIMES;
            this.corruptingTouchWeak = CORRUPTING_TOUCH_WEAK;
            this.corruptingTouchDraw = CORRUPTING_TOUCH_DRAW;
        }
    }

    @Override
    public void usePreBattleAction() {
        CardCrawlGame.music.unsilenceBGM();
        AbstractDungeon.scene.fadeOutAmbiance();
        AbstractDungeon.getCurrRoom().playBgmInstantly("BOSS_BEYOND");

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
                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(AbstractDungeon.player, this, new TemporaryHexPower(AbstractDungeon.player, DURESS_TEMPORARY_HEX, true), DURESS_TEMPORARY_HEX));
                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(AbstractDungeon.player, this, new FrailPower(AbstractDungeon.player, this.duressFrail, true), this.duressFrail));
                break;
            case HYMN_TO_THE_VOID_DEBUFF:
                AbstractDungeon.actionManager.addToBottom(new FastShakeAction(this, 0.5F, 0.2F));
                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(AbstractDungeon.player, this, new VulnerablePower(AbstractDungeon.player, this.hymnToTheVoidVulnerable, true), this.hymnToTheVoidVulnerable));
                AbstractDungeon.actionManager.addToBottom(new MakeTempCardInDiscardAction(new Slimed(), this.hymnToTheVoidSlimes));
                break;
            case CORRUPTING_TOUCH_ATTACK:
                AbstractDungeon.actionManager.addToBottom(new AnimateFastAttackAction(this));
                AbstractDungeon.actionManager.addToBottom(new DamageAction(AbstractDungeon.player, this.damage.get(0), AbstractGameAction.AttackEffect.POISON));
                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(AbstractDungeon.player, this, new WeakPower(AbstractDungeon.player, this.corruptingTouchWeak, true), this.corruptingTouchWeak));
                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(AbstractDungeon.player, this, new DrawCardNextTurnPower(AbstractDungeon.player, this.corruptingTouchDraw), this.corruptingTouchDraw));
                AbstractDungeon.actionManager.addToBottom(new MakeTempCardInDrawPileAction(new Dazed(), this.corruptingTouchDraw, false, true));
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
        boolean lastTwoNotDebuff = !this.lastMove(DURESS_DEBUFF) && !this.lastMoveBefore(DURESS_DEBUFF) && !this.lastMove(HYMN_TO_THE_VOID_DEBUFF) && !this.lastMoveBefore(HYMN_TO_THE_VOID_DEBUFF);
        boolean lastThreeNotDebuff = lastTwoNotDebuff && !MonsterUtil.lastMoveX(this, DURESS_DEBUFF, 3) && !MonsterUtil.lastMoveX(this, HYMN_TO_THE_VOID_DEBUFF, 3);
        boolean lastFourNotDebuff = lastThreeNotDebuff && !MonsterUtil.lastMoveX(this, DURESS_DEBUFF, 4) && !MonsterUtil.lastMoveX(this, HYMN_TO_THE_VOID_DEBUFF, 4);
        Supplier<Byte> randomDebuff = () -> AbstractDungeon.aiRng.randomBoolean() ? DURESS_DEBUFF : HYMN_TO_THE_VOID_DEBUFF;
        if (!this.firstMove && lastFourNotDebuff) {
            move = randomDebuff.get();
        }
        else if (lastThreeNotDebuff && num < 50) {
            move = randomDebuff.get();
        }
        else if (lastTwoNotDebuff) {
            move = num < 25 ? randomDebuff.get() : num < 60 ? ONSLAUGHT_ATTACK : CORRUPTING_TOUCH_ATTACK;
        }
        else {
            move = num < 40 ? ONSLAUGHT_ATTACK : CORRUPTING_TOUCH_ATTACK;
        }
        switch (move) {
            case DURESS_DEBUFF:
                this.setMove(MOVES[0], move, Intent.STRONG_DEBUFF);
                break;
            case HYMN_TO_THE_VOID_DEBUFF:
                this.setMove(MOVES[1], move, Intent.DEBUFF);
                break;
            case ONSLAUGHT_ATTACK:
                this.setMove(MOVES[2], move, Intent.ATTACK, this.onslaughtDamage, ONSLAUGHT_HITS, true);
                break;
            case CORRUPTING_TOUCH_ATTACK:
                this.setMove(MOVES[3], move, Intent.ATTACK_DEBUFF, this.corruptingTouchDamage);
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
                hasCard = hasCard || (tag != null && tag.name().equals(tagName));
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