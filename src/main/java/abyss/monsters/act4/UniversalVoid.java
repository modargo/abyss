package abyss.monsters.act4;

import abyss.Abyss;
import abyss.cards.act4.Doomed;
import abyss.cards.act4.Overwhelmed;
import abyss.cards.act4.Shadowed;
import abyss.cards.act4.Silenced;
import abyss.effects.CalamityDebuffEffect;
import abyss.effects.DamnationEffect;
import abyss.effects.DoNothingEffect;
import abyss.powers.act4.*;
import basemod.abstracts.CustomMonster;
import basemod.helpers.VfxBuilder;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.AnimateFastAttackAction;
import com.megacrit.cardcrawl.actions.animations.FastShakeAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.*;
import com.megacrit.cardcrawl.actions.defect.DecreaseMaxOrbAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.localization.MonsterStrings;
import com.megacrit.cardcrawl.powers.*;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;
import com.megacrit.cardcrawl.vfx.combat.ViceCrushEffect;

import java.util.Arrays;

public class UniversalVoid extends CustomMonster
{
    public static final String ID = "Abyss:UniversalVoid";
    private static final MonsterStrings monsterStrings;
    public static final String NAME;
    public static final String[] MOVES;
    private static final String IMG = Abyss.monsterImage(ID);
    private boolean firstMove = true;
    private static final byte OBLITERATE_ATTACK = 1;
    private static final byte CALAMITY_DEBUFF = 2;
    private static final byte RAVAGE_ATTACK = 3;
    private static final byte DAMNATION_ATTACK = 4;
    private static final byte EMBRACE_THE_END_BUFF = 5;
    private static final byte ALL_IS_DUST_ATTACK = 6;
    private static final byte ENDURE_DEFEND = 7;
    private static final int OBLITERATE_DAMAGE = 45;
    private static final int A4_OBLITERATE_DAMAGE = 50;
    private static final int CALAMITY_AMOUNT = 2;
    private static final int RAVAGE_DAMAGE = 8;
    private static final int RAVAGE_HITS = 4;
    private static final int A4_RAVAGE_HITS = 5;
    private static final int DAMNATION_DAMAGE = 32;
    private static final int A4_DAMNATION_DAMAGE = 36;
    private static final int EMBRACE_THE_END_DEMON_FORM = 2;
    private static final int EMBRACE_THE_END_STRENGTH = 30;
    private static final int ALL_IS_DUST_DAMAGE = 16;
    private static final int A4_ALL_IS_DUST_DAMAGE = 18;
    private static final int ENDURE_BLOCK = 10;
    private static final int A9_ENDURE_BLOCK = 20;
    private static final int ENDURE_METALLICIZE = 5;
    private static final int A9_ENDURE_METALLICIZE = 10;
    private static final int INVINCIBLE = 300;
    private static final int A19_INVINCIBLE = 200;
    private static final int BEAT_OF_DEATH = 1;
    private static final int A19_BEAT_OF_DEATH = 2;
    private static final int EMPTINESS = 1;
    private static final int HP = 750;
    private static final int A9_HP = 800;
    private int obliterateDamage;
    private int ravageHits;
    private int damnationDamage;
    private int allIsDustDamage;
    private int endureBlock;
    private int endureMetallicize;
    private int beatOfDeath;
    private int invincible;

    public UniversalVoid() {
        this(0.0f, 0.0f);
    }

    public UniversalVoid(final float x, final float y) {
        super(UniversalVoid.NAME, ID, HP, -5.0F, 0, 1005.0f, 560.0f, IMG, x, y);
        this.type = EnemyType.BOSS;
        if (AbstractDungeon.ascensionLevel >= 9) {
            this.setHp(A9_HP);
            this.endureBlock = A9_ENDURE_BLOCK;
            this.endureMetallicize = A9_ENDURE_METALLICIZE;
        } else {
            this.setHp(HP);
            this.endureBlock = ENDURE_BLOCK;
            this.endureMetallicize = ENDURE_METALLICIZE;
        }

        if (AbstractDungeon.ascensionLevel >= 4) {
            this.obliterateDamage = A4_OBLITERATE_DAMAGE;
            this.ravageHits = A4_RAVAGE_HITS;
            this.damnationDamage = A4_DAMNATION_DAMAGE;
            this.allIsDustDamage = A4_ALL_IS_DUST_DAMAGE;
        } else {
            this.obliterateDamage = OBLITERATE_DAMAGE;
            this.ravageHits = RAVAGE_HITS;
            this.damnationDamage = DAMNATION_DAMAGE;
            this.allIsDustDamage = ALL_IS_DUST_DAMAGE;
        }
        this.damage.add(new DamageInfo(this, this.obliterateDamage));
        this.damage.add(new DamageInfo(this, RAVAGE_DAMAGE));
        this.damage.add(new DamageInfo(this, this.damnationDamage));
        this.damage.add(new DamageInfo(this, this.allIsDustDamage));

        if (AbstractDungeon.ascensionLevel >= 19) {
            this.invincible = A19_INVINCIBLE;
            this.beatOfDeath = A19_BEAT_OF_DEATH;
        }
        else {
            this.invincible = INVINCIBLE;
            this.beatOfDeath = BEAT_OF_DEATH;
        }
    }

    @Override
    public void usePreBattleAction() {
        CardCrawlGame.music.unsilenceBGM();
        AbstractDungeon.scene.fadeOutAmbiance();
        AbstractDungeon.getCurrRoom().playBgmInstantly("BOSS_ENDING");

        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(this, this, new InvinciblePower(this, this.invincible), this.invincible));
        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(this, this, new BeatOfDeathPower(this, this.beatOfDeath), this.beatOfDeath));
        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(this, this, new EmptinessPower(this, EMPTINESS), EMPTINESS));
    }

    @Override
    public void takeTurn() {
        if (this.firstMove) {
            this.firstMove = false;
        }
        int m = this.moveHistory.size();
        int cycle = (m - 2) / 3;
        switch (this.nextMove) {
            case OBLITERATE_ATTACK:
                AbstractDungeon.actionManager.addToBottom(new VFXAction(new ViceCrushEffect(AbstractDungeon.player.hb.cX, AbstractDungeon.player.hb.cY), 0.5F));
                AbstractDungeon.actionManager.addToBottom(new DamageAction(AbstractDungeon.player, this.damage.get(0), AbstractGameAction.AttackEffect.BLUNT_HEAVY));
                break;
            case CALAMITY_DEBUFF:
                AbstractDungeon.actionManager.addToBottom(new VFXAction(new CalamityDebuffEffect(), 0.5F));
                boolean isUnknownClass = !Arrays.asList(AbstractPlayer.PlayerClass.IRONCLAD, AbstractPlayer.PlayerClass.THE_SILENT, AbstractPlayer.PlayerClass.DEFECT, AbstractPlayer.PlayerClass.WATCHER).contains(AbstractDungeon.player.chosenClass);
                if (AbstractDungeon.player.chosenClass == AbstractPlayer.PlayerClass.IRONCLAD) {
                    AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new ReducedCombatHealingPower(AbstractDungeon.player)));
                    AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new StrengthDegenerationPower(AbstractDungeon.player)));
                }
                if (AbstractDungeon.player.chosenClass == AbstractPlayer.PlayerClass.THE_SILENT) {
                    AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new FeeblePoisonPower(AbstractDungeon.player)));
                    AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new FlimsyDaggersPower(AbstractDungeon.player)));
                }
                if (AbstractDungeon.player.chosenClass == AbstractPlayer.PlayerClass.DEFECT) {
                    AbstractDungeon.actionManager.addToBottom(new DecreaseMaxOrbAction(1));
                    AbstractPower focus =  new FocusPower(AbstractDungeon.player, -1);
                    focus.type = AbstractPower.PowerType.BUFF;
                    AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new FocusPower(AbstractDungeon.player, -1), -1));
                }
                if (AbstractDungeon.player.chosenClass == AbstractPlayer.PlayerClass.WATCHER) {
                    AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new DisturbedCalmPower(AbstractDungeon.player)));
                    AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new SubduedSpiritPower(AbstractDungeon.player)));
                }
                if (isUnknownClass) {
                    AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new ReducedCombatHealingPower(AbstractDungeon.player)));
                    AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new StrengthDegenerationPower(AbstractDungeon.player)));
                }

                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(AbstractDungeon.player, this, new FrailPower(AbstractDungeon.player, CALAMITY_AMOUNT, true), CALAMITY_AMOUNT));
                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(AbstractDungeon.player, this, new WeakPower(AbstractDungeon.player, CALAMITY_AMOUNT, true), CALAMITY_AMOUNT));
                break;
            case RAVAGE_ATTACK:
                for (int i = 0; i < this.ravageHits; i++) {
                    float delayIncrement = (Settings.FAST_MODE ? 0.15f : 0.4f);
                    float delay = delayIncrement * i;
                    float secondPartDuration = Settings.FAST_MODE ? 0.1f : 0.3f;
                    float thirdPartDuration = Settings.FAST_MODE ? 0.1f : 0.2f;
                    float x1 = AbstractDungeon.player.hb.cX + 100.0f;
                    float y1 = AbstractDungeon.player.hb.cY + 100.0f - (50.0f * i);
                    VfxBuilder builder = new VfxBuilder(ImageMaster.ORB_DARK, x1, y1, delay)
                            .setScale(1.5f)
                            .fadeIn(delay)
                            .playSoundAt(delay, 0.4F, "GHOST_ORB_IGNITE_1")
                            .andThen(secondPartDuration)
                            .moveX(x1, AbstractDungeon.player.hb.cX)
                            .moveY(y1, AbstractDungeon.player.hb.cY)
                            .andThen(thirdPartDuration)
                            .fadeOut(thirdPartDuration);
                    //if (i == this.ravageHits - 1) {
                    //    builder.whenComplete(x -> {
                    //        for (int j = 0; j < this.ravageHits; j++) {
                    //            AbstractDungeon.actionManager.addToBottom(new DamageAction(AbstractDungeon.player, this.damage.get(1), AbstractGameAction.AttackEffect.BLUNT_HEAVY, true));
                    //        }
                    //    });
                    //}
                    AbstractGameEffect effect = builder.build();
                    AbstractDungeon.effectsQueue.add(effect);
                    AbstractDungeon.actionManager.addToBottom(new VFXAction(new DoNothingEffect(), i == 0 ? secondPartDuration : delayIncrement));
                    AbstractDungeon.actionManager.addToBottom(new DamageAction(AbstractDungeon.player, this.damage.get(1), AbstractGameAction.AttackEffect.BLUNT_HEAVY, true));
                }
                break;
            case DAMNATION_ATTACK:
                int fireballs = 3;
                AbstractDungeon.actionManager.addToBottom(new VFXAction(new DamnationEffect(AbstractDungeon.player.hb.cX, AbstractDungeon.player.hb.cY, fireballs), fireballs * DamnationEffect.TIME_PER_FIREBALL));
                AbstractDungeon.actionManager.addToBottom(new DamageAction(AbstractDungeon.player, this.damage.get(2), AbstractGameAction.AttackEffect.NONE));
                AbstractDungeon.actionManager.addToBottom(new MakeTempCardInDrawPileAction(new Doomed(), 1, true, true));
                AbstractDungeon.actionManager.addToBottom(new MakeTempCardInDrawPileAction(new Overwhelmed(), 1, true, true));
                AbstractDungeon.actionManager.addToBottom(new MakeTempCardInDrawPileAction(new Shadowed(), 1, true, true));
                AbstractDungeon.actionManager.addToBottom(new MakeTempCardInDrawPileAction(new Silenced(), 1, true, true));
                break;
            case EMBRACE_THE_END_BUFF:
                AbstractDungeon.actionManager.addToBottom(new FastShakeAction(this, 0.5F, 0.2F));
                if (cycle < 3) {
                    int strengthGain = this.hasPower(StrengthPower.POWER_ID) ? -this.getPower(StrengthPower.POWER_ID).amount : 0;
                    if (strengthGain > 0) {
                        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(this, this, new StrengthPower(this, strengthGain), strengthGain));
                    }
                    AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(this, this, new RitualPower(this, EMBRACE_THE_END_DEMON_FORM, false), EMBRACE_THE_END_DEMON_FORM));
                    AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(this, this, new StrengthPower(this, EMBRACE_THE_END_DEMON_FORM), EMBRACE_THE_END_DEMON_FORM));
                }
                else {
                    AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(this, this, new StrengthPower(this, EMBRACE_THE_END_STRENGTH), EMBRACE_THE_END_STRENGTH));
                }
                break;
            case ALL_IS_DUST_ATTACK:
                AbstractDungeon.actionManager.addToBottom(new AnimateFastAttackAction(this));
                //TODO VFX
                AbstractDungeon.actionManager.addToBottom(new DamageAction(AbstractDungeon.player, this.damage.get(3), AbstractGameAction.AttackEffect.SLASH_HEAVY));
                if (cycle < 3) {
                    AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(this, this, new BeatOfDeathPower(this, 1), 1));
                }
                else {
                    AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(this, this, new EmptinessPower(this, 1), 1));
                }
                break;
            case ENDURE_DEFEND:
                AbstractDungeon.actionManager.addToBottom(new GainBlockAction(this, this.endureBlock));
                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(this, this, new MetallicizePower(this, this.endureMetallicize), this.endureMetallicize));
                break;
        }
        AbstractDungeon.actionManager.addToBottom(new RollMoveAction(this));
    }

    @Override
    protected void getMove(final int num) {
        int m = this.moveHistory.size();
        int cycle = (m - 2) / 3;
        int index = (m - 2) % 3;
        byte cycleAttack = cycle >= 3 || cycle % 2 == 0 ? DAMNATION_ATTACK : ALL_IS_DUST_ATTACK;
        byte move;
        if (m == 0) {
            move = OBLITERATE_ATTACK;
        }
        else if (m == 1) {
            move = CALAMITY_DEBUFF;
        }
        else if (index == 0) {
            move = num < 50 ? RAVAGE_ATTACK : cycleAttack;
        }
        else if (index == 1) {
            move = this.lastMove(RAVAGE_ATTACK) ? cycleAttack : RAVAGE_ATTACK;
        }
        else {
            move = cycle == 0 || cycle >= 3 ? EMBRACE_THE_END_BUFF : ENDURE_DEFEND;
        }

        switch (move) {
            case OBLITERATE_ATTACK:
                this.setMove(MOVES[0], OBLITERATE_ATTACK, Intent.ATTACK, this.obliterateDamage);
                break;
            case CALAMITY_DEBUFF:
                this.setMove(MOVES[1], CALAMITY_DEBUFF, Intent.STRONG_DEBUFF);
                break;
            case RAVAGE_ATTACK:
                this.setMove(MOVES[2], RAVAGE_ATTACK, Intent.ATTACK, RAVAGE_DAMAGE, this.ravageHits, true);
                break;
            case DAMNATION_ATTACK:
                this.setMove(MOVES[3], DAMNATION_ATTACK, Intent.ATTACK_DEBUFF, this.damnationDamage);
                break;
            case EMBRACE_THE_END_BUFF:
                this.setMove(MOVES[4], EMBRACE_THE_END_BUFF, Intent.BUFF);
                break;
            case ALL_IS_DUST_ATTACK:
                this.setMove(MOVES[5], ALL_IS_DUST_ATTACK, Intent.ATTACK_BUFF, this.allIsDustDamage);
                break;
            case ENDURE_DEFEND:
                this.setMove(MOVES[6], ENDURE_DEFEND, Intent.DEFEND_BUFF);
                break;
            default:
                throw new RuntimeException("Impossible case");
        }
    }

    @Override
    public void die() {
        if (!AbstractDungeon.getCurrRoom().cannotLose) {
            super.die();
            this.onBossVictoryLogic();
            this.onFinalBossVictoryLogic();
            CardCrawlGame.stopClock = true;
        }
    }

    static {
        monsterStrings = CardCrawlGame.languagePack.getMonsterStrings(ID);
        NAME = UniversalVoid.monsterStrings.NAME;
        MOVES = UniversalVoid.monsterStrings.MOVES;
    }
}