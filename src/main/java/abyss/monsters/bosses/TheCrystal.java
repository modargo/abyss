package abyss.monsters.bosses;

import abyss.Abyss;
import abyss.cards.Mineralized;
import abyss.monsters.bosses.crystals.AbstractCrystal;
import abyss.powers.CrystalLinkPower;
import abyss.powers.InactiveCrystalPower;
import basemod.abstracts.CustomMonster;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.AnimateFastAttackAction;
import com.megacrit.cardcrawl.actions.animations.FastShakeAction;
import com.megacrit.cardcrawl.actions.common.*;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.MonsterStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.StrengthPower;

public class TheCrystal extends CustomMonster
{
    public static final String ID = "Abyss:TheCrystal";
    private static final MonsterStrings monsterStrings;
    public static final String NAME;
    public static final String[] MOVES;
    private static final String IMG = Abyss.monsterImage(ID);
    private boolean firstMove = true;
    private static final byte INACTIVE_MOVE = 1;
    private static final byte CRYSTAL_BARRAGE_ATTACK = 2;
    private static final byte CRYSTAL_SPEAR_ATTACK = 3;
    private static final byte RESONANCE_BUFF = 4;
    private static final int CRYSTAL_BARRAGE_DAMAGE = 0;
    private static final int A4_CRYSTAL_BARRAGE_DAMAGE = 1;
    private static final int CRYSTAL_BARRAGE_HITS = 2;
    private static final int CRYSTAL_SPEAR_DAMAGE = 9;
    private static final int A4_CRYSTAL_SPEAR_DAMAGE = 10;
    private static final int CRYSTAL_SPEAR_MINERALIZES = 1;
    private static final int A19_CRYSTAL_SPEAR_MINERALIZES = 1;
    private static final int RESONANCE_STRENGTH = 1;
    private static final int A19_RESONANCE_STRENGTH = 2;
    private static final int RESONANCE_HEAL = 30;
    private static final int A19_RESONANCE_HEAL = 40;
    private static final int CRYSTAL_LINK_PERCENT = 100;
    private static final int A19_CRYSTAL_LINK_PERCENT = 150;
    public static final int ACTIVATION_CRYSTAL_COUNT = 3;
    private static final int HP = 280;
    private static final int A9_HP = 300;
    private int crystalBarrageDamage;
    private int crystalSpearDamage;
    private int crystalSpearMineralizes;
    private int resonanceStrength;
    private int resonanceHeal;
    private int crystalLinkPercent;

    private boolean active;

    public TheCrystal() {
        this(0.0f, 0.0f);
    }

    public TheCrystal(final float x, final float y) {
        super(TheCrystal.NAME, ID, HP, -5.0F, 0, 470.0f, 505.0f, IMG, x, y);
        this.type = EnemyType.BOSS;
        this.active = false;
        if (AbstractDungeon.ascensionLevel >= 9) {
            this.setHp(A9_HP);
        } else {
            this.setHp(HP);
        }

        if (AbstractDungeon.ascensionLevel >= 4) {
            this.crystalBarrageDamage = A4_CRYSTAL_BARRAGE_DAMAGE;
            this.crystalSpearDamage = A4_CRYSTAL_SPEAR_DAMAGE;
        } else {
            this.crystalBarrageDamage = CRYSTAL_BARRAGE_DAMAGE;
            this.crystalSpearDamage = CRYSTAL_SPEAR_DAMAGE;
        }
        this.damage.add(new DamageInfo(this, this.crystalBarrageDamage));
        this.damage.add(new DamageInfo(this, this.crystalSpearDamage));

        if (AbstractDungeon.ascensionLevel >= 19) {
            this.crystalSpearMineralizes = A19_CRYSTAL_SPEAR_MINERALIZES;
            this.resonanceStrength = A19_RESONANCE_STRENGTH;
            this.resonanceHeal = A19_RESONANCE_HEAL;
            this.crystalLinkPercent = A19_CRYSTAL_LINK_PERCENT;
        }
        else {
            this.crystalSpearMineralizes = CRYSTAL_SPEAR_MINERALIZES;
            this.resonanceStrength = RESONANCE_STRENGTH;
            this.resonanceHeal = RESONANCE_HEAL;
            this.crystalLinkPercent = CRYSTAL_LINK_PERCENT;
        }
    }

    @Override
    public void usePreBattleAction() {
        CardCrawlGame.music.unsilenceBGM();
        AbstractDungeon.scene.fadeOutAmbiance();
        AbstractDungeon.getCurrRoom().playBgmInstantly("BOSS_BEYOND");

        this.addToBot(new ApplyPowerAction(this, this, new InactiveCrystalPower(this)));
        this.addToBot(new ApplyPowerAction(this, this, new CrystalLinkPower(this, this.crystalLinkPercent)));
    }

    @Override
    public void takeTurn() {
        if (this.firstMove) {
            this.firstMove = false;
        }
        switch (this.nextMove) {
            case INACTIVE_MOVE:
                //TODO Some kind of effect?
                break;
            case CRYSTAL_BARRAGE_ATTACK:
                AbstractDungeon.actionManager.addToBottom(new AnimateFastAttackAction(this));
                for (int i = 0; i < CRYSTAL_BARRAGE_HITS; i++) {
                    AbstractDungeon.actionManager.addToBottom(new DamageAction(AbstractDungeon.player, this.damage.get(0), AbstractGameAction.AttackEffect.SLASH_VERTICAL));
                }
                break;
            case CRYSTAL_SPEAR_ATTACK:
                AbstractDungeon.actionManager.addToBottom(new AnimateFastAttackAction(this));
                AbstractDungeon.actionManager.addToBottom(new DamageAction(AbstractDungeon.player, this.damage.get(1), AbstractGameAction.AttackEffect.SLASH_HEAVY));
                AbstractDungeon.actionManager.addToBottom(new MakeTempCardInDiscardAction(new Mineralized(), this.crystalSpearMineralizes));
                AbstractDungeon.actionManager.addToBottom(new MakeTempCardInDrawPileAction(new Mineralized(), this.crystalSpearMineralizes, true, true));
                break;
            case RESONANCE_BUFF:
                AbstractDungeon.actionManager.addToBottom(new FastShakeAction(this, 0.5F, 0.2F));
                AbstractDungeon.actionManager.addToBottom(new HealAction(this, this, this.resonanceHeal));
                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(this, this, new StrengthPower(this, this.resonanceStrength), this.resonanceStrength));
                break;
        }

        this.activationCheck();

        AbstractDungeon.actionManager.addToBottom(new RollMoveAction(this));
    }

    private void activationCheck() {
        if (!this.active) {
            int crystalCount = this.countRemainingCrystals();
            if (crystalCount <= ACTIVATION_CRYSTAL_COUNT) {
                this.active = true;
                AbstractPower inactiveCrystalPower = this.getPower(InactiveCrystalPower.POWER_ID);
                if (inactiveCrystalPower != null) {
                    ((InactiveCrystalPower)inactiveCrystalPower).activate();
                }
            }
        }
    }

    private int countRemainingCrystals() {
        int crystalCount = 0;
        for (AbstractMonster m : AbstractDungeon.getCurrRoom().monsters.monsters) {
            if (!m.isDying && m instanceof AbstractCrystal) {
                crystalCount++;
            }
        }
        return crystalCount;
    }

    @Override
    protected void getMove(final int num) {
        if (!this.active) {
            // Deliberately left nameless
            this.setMove(INACTIVE_MOVE, Intent.UNKNOWN);
        }
        else if (!this.lastMove(CRYSTAL_BARRAGE_ATTACK) && !this.lastMoveBefore(CRYSTAL_BARRAGE_ATTACK)) {
            this.setMove(MOVES[1], CRYSTAL_BARRAGE_ATTACK, Intent.ATTACK, this.crystalBarrageDamage, CRYSTAL_BARRAGE_HITS, true);
        }
        else if (this.lastMove(RESONANCE_BUFF) || (num < 50 && this.lastMove(CRYSTAL_SPEAR_ATTACK))) {
            this.setMove(MOVES[2], CRYSTAL_SPEAR_ATTACK, Intent.ATTACK_DEBUFF, this.crystalSpearDamage);
        }
        else {
            this.setMove(MOVES[3], RESONANCE_BUFF, Intent.BUFF);
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

    static {
        monsterStrings = CardCrawlGame.languagePack.getMonsterStrings(ID);
        NAME = TheCrystal.monsterStrings.NAME;
        MOVES = TheCrystal.monsterStrings.MOVES;
    }
}