package abyss.monsters.act4;

import abyss.Abyss;
import abyss.cards.act4.*;
import abyss.effects.*;
import abyss.powers.act4.*;
import basemod.abstracts.CustomMonster;
import basemod.helpers.VfxBuilder;
import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInDrawPileAction;
import com.megacrit.cardcrawl.actions.common.RollMoveAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.localization.MonsterStrings;
import com.megacrit.cardcrawl.powers.*;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;
import com.megacrit.cardcrawl.vfx.BorderFlashEffect;
import com.megacrit.cardcrawl.vfx.combat.HeartBuffEffect;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Arrays;

public class UniversalVoid extends CustomMonster
{
    private static final Logger logger = LogManager.getLogger(UniversalVoid.class.getName());
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
    private static final byte RUIN_DEBUFF = 7;
    private static final int OBLITERATE_DAMAGE = 40;
    private static final int A4_OBLITERATE_DAMAGE = 45;
    private static final int CALAMITY_DEBUFF_AMOUNT = 2;
    private static final int RAVAGE_DAMAGE = 8;
    private static final int A4_RAVAGE_DAMAGE = 8;
    private static final int RAVAGE_HITS = 4;
    private static final int A4_RAVAGE_HITS = 5;
    private static final int DAMNATION_DAMAGE = 28;
    private static final int A4_DAMNATION_DAMAGE = 32;
    private static final int EMBRACE_THE_END_DEMON_FORM = 2;
    private static final int EMBRACE_THE_END_FIRST_STRENGTH = 2;
    private static final int EMBRACE_THE_END_STRENGTH = 30;
    private static final int EMBRACE_THE_END_FINAL_STRENGTH = 60;
    private static final int ALL_IS_DUST_DAMAGE = 23;
    private static final int A4_ALL_IS_DUST_DAMAGE = 26;
    private static final int PAINFUL_STABS_AMOUNT = 2;
    private static final int INVINCIBLE = 300;
    private static final int A19_INVINCIBLE = 200;
    private static final int BEAT_OF_DEATH = 1;
    private static final int A19_BEAT_OF_DEATH = 2;
    private static final int STARTING_STRENGTH = 0;
    private static final int HP = 650;
    private static final int A9_HP = 700;
    private int obliterateDamage;
    private int ravageDamage;
    private int ravageHits;
    private int damnationDamage;
    private int allIsDustDamage;
    private int beatOfDeath;
    private int invincible;

    public UniversalVoid() {
        this(0.0f, 0.0f);
    }

    public UniversalVoid(final float x, final float y) {
        super(UniversalVoid.NAME, ID, HP, -5.0F, 0, 530.0f, 575.0f, IMG, x, y);
        this.type = EnemyType.BOSS;
        if (AbstractDungeon.ascensionLevel >= 9) {
            this.setHp(A9_HP);
        } else {
            this.setHp(HP);
        }

        if (AbstractDungeon.ascensionLevel >= 4) {
            this.obliterateDamage = A4_OBLITERATE_DAMAGE - STARTING_STRENGTH;
            this.ravageDamage = A4_RAVAGE_DAMAGE - STARTING_STRENGTH;
            this.ravageHits = A4_RAVAGE_HITS;
            this.damnationDamage = A4_DAMNATION_DAMAGE - STARTING_STRENGTH;
            this.allIsDustDamage = A4_ALL_IS_DUST_DAMAGE - STARTING_STRENGTH;
        } else {
            this.obliterateDamage = OBLITERATE_DAMAGE - STARTING_STRENGTH;
            this.ravageDamage = RAVAGE_DAMAGE - STARTING_STRENGTH;
            this.ravageHits = RAVAGE_HITS;
            this.damnationDamage = DAMNATION_DAMAGE - STARTING_STRENGTH;
            this.allIsDustDamage = ALL_IS_DUST_DAMAGE - STARTING_STRENGTH;
        }
        this.damage.add(new DamageInfo(this, this.obliterateDamage));
        this.damage.add(new DamageInfo(this, this.ravageDamage));
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
        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(this, this, new EmptinessPower(this)));
        if (STARTING_STRENGTH > 0) {
            AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(this, this, new StrengthPower(this, STARTING_STRENGTH), STARTING_STRENGTH));
        }
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
                AbstractDungeon.actionManager.addToBottom(new VFXAction(new ObliterateEffect(AbstractDungeon.player.hb.cX, AbstractDungeon.player.hb.cY), 0.5F));
                AbstractDungeon.actionManager.addToBottom(new DamageAction(AbstractDungeon.player, this.damage.get(0), AbstractGameAction.AttackEffect.BLUNT_HEAVY));
                break;
            case CALAMITY_DEBUFF:
                AbstractDungeon.actionManager.addToBottom(new VFXAction(new CalamityDebuffEffect(), 1.0F));
                this.applyCalamityDebuff();
                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(AbstractDungeon.player, this, new FrailPower(AbstractDungeon.player, CALAMITY_DEBUFF_AMOUNT, true), CALAMITY_DEBUFF_AMOUNT));
                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(AbstractDungeon.player, this, new WeakPower(AbstractDungeon.player, CALAMITY_DEBUFF_AMOUNT, true), CALAMITY_DEBUFF_AMOUNT));
                break;
            case RAVAGE_ATTACK:
                for (int i = 0; i < this.ravageHits; i++) {
                    float delayIncrement = (Settings.FAST_MODE ? 0.15f : 0.4f);
                    float delay = delayIncrement * i;
                    float secondPartDuration = Settings.FAST_MODE ? 0.1f : 0.3f;
                    float thirdPartDuration = Settings.FAST_MODE ? 0.1f : 0.2f;
                    float x1 = AbstractDungeon.player.hb.cX + 100.0f;
                    float y1 = AbstractDungeon.player.hb.cY + 100.0f - (50.0f * i);
                    AbstractGameEffect effect = new VfxBuilder(ImageMaster.ORB_DARK, x1, y1, delay)
                            .setScale(1.5f)
                            .fadeIn(delay)
                            .playSoundAt(delay, 0.4F, "GHOST_ORB_IGNITE_1")
                            .andThen(secondPartDuration)
                            .moveX(x1, AbstractDungeon.player.hb.cX)
                            .moveY(y1, AbstractDungeon.player.hb.cY)
                            .andThen(thirdPartDuration)
                            .fadeOut(thirdPartDuration)
                            .build();
                    AbstractDungeon.effectsQueue.add(effect);
                    AbstractDungeon.actionManager.addToBottom(new VFXAction(new DoNothingEffect(), i == 0 ? secondPartDuration : delayIncrement));
                    AbstractDungeon.actionManager.addToBottom(new DamageAction(AbstractDungeon.player, this.damage.get(1), AbstractGameAction.AttackEffect.BLUNT_HEAVY, true));
                }
                break;
            case DAMNATION_ATTACK:
                int fireballs = 3;
                AbstractDungeon.actionManager.addToBottom(new VFXAction(new DamnationEffect(AbstractDungeon.player.hb.cX, AbstractDungeon.player.hb.cY, fireballs), fireballs * DamnationEffect.TIME_PER_FIREBALL));
                AbstractDungeon.actionManager.addToBottom(new DamageAction(AbstractDungeon.player, this.damage.get(2), AbstractGameAction.AttackEffect.NONE));
                if (cycle == 0) {
                    AbstractDungeon.actionManager.addToBottom(new MakeTempCardInDrawPileAction(new Pressured(), 1, true, false, false, (float) Settings.WIDTH * 0.30F, (float) Settings.HEIGHT / 2.0F));
                    AbstractDungeon.actionManager.addToBottom(new MakeTempCardInDrawPileAction(new Shadowed(), 1, true, false, false, (float) Settings.WIDTH * 0.5F, (float) Settings.HEIGHT / 2.0F));
                    AbstractDungeon.actionManager.addToBottom(new MakeTempCardInDrawPileAction(new Silenced(), 1, true, false, false, (float) Settings.WIDTH * 0.70F, (float) Settings.HEIGHT / 2.0F));
                }
                else {
                    AbstractDungeon.actionManager.addToBottom(new MakeTempCardInDrawPileAction(new Overwhelmed(), 1, true, true));
                }
                break;
            case EMBRACE_THE_END_BUFF:
                AbstractDungeon.actionManager.addToBottom(new VFXAction(new BorderFlashEffect(Color.NAVY.cpy())));
                AbstractDungeon.actionManager.addToBottom(new VFXAction(new HeartBuffEffect(this.hb.cX, this.hb.cY)));
                if (cycle < 4) {
                    int strengthGain = this.hasPower(StrengthPower.POWER_ID) ? -this.getPower(StrengthPower.POWER_ID).amount : 0;
                    if (strengthGain > 0) {
                        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(this, this, new StrengthPower(this, strengthGain), strengthGain));
                    }
                    AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(this, this, new RitualPower(this, EMBRACE_THE_END_DEMON_FORM, false), EMBRACE_THE_END_DEMON_FORM));
                    AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(this, this, new StrengthPower(this, EMBRACE_THE_END_DEMON_FORM), EMBRACE_THE_END_DEMON_FORM + EMBRACE_THE_END_FIRST_STRENGTH));
                }
                else if (cycle == 4) {
                    AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(this, this, new StrengthPower(this, EMBRACE_THE_END_STRENGTH), EMBRACE_THE_END_STRENGTH));
                }
                else {
                    AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(this, this, new StrengthPower(this, EMBRACE_THE_END_FINAL_STRENGTH), EMBRACE_THE_END_FINAL_STRENGTH));
                }
                break;
            case ALL_IS_DUST_ATTACK:
                this.addToBot(new VFXAction(new AllIsDustEffect(this.hb.cX, this.hb.cY, AbstractDungeon.player.hb.cX, AbstractDungeon.player.hb.cY), 0.5F));
                AbstractDungeon.actionManager.addToBottom(new DamageAction(AbstractDungeon.player, this.damage.get(3), AbstractGameAction.AttackEffect.SLASH_HEAVY));
                if (cycle < 2) {
                    AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(this, this, new BeatOfDeathPower(this, 1), 1));
                }
                else {
                    AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(this, this, new PainfulStabsWithAmountPower(this, PAINFUL_STABS_AMOUNT)));
                }
                break;
            case RUIN_DEBUFF:
                AbstractDungeon.actionManager.addToBottom(new VFXAction(new BorderFlashEffect(Color.FIREBRICK.cpy())));
                if (cycle < 3) {
                    AbstractDungeon.actionManager.addToBottom(new MakeTempCardInDrawPileAction(new Maimed(), 1, true, true));
                    AbstractDungeon.actionManager.addToBottom(new MakeTempCardInDrawPileAction(new Doomed(), 1, true, true));
                }
                else {
                    AbstractDungeon.actionManager.addToBottom(new MakeTempCardInDrawPileAction(new Overwhelmed(), 1, true, true));
                }
                break;
        }
        AbstractDungeon.actionManager.addToBottom(new RollMoveAction(this));
    }

    private void applyCalamityDebuff() {
        logger.info("Player class: " + AbstractDungeon.player.chosenClass.name());

        boolean isUnknownClass = !Arrays.asList(AbstractPlayer.PlayerClass.IRONCLAD, AbstractPlayer.PlayerClass.THE_SILENT, AbstractPlayer.PlayerClass.DEFECT, AbstractPlayer.PlayerClass.WATCHER).contains(AbstractDungeon.player.chosenClass);
        if (AbstractDungeon.player.chosenClass == AbstractPlayer.PlayerClass.IRONCLAD) {
            AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new ReducedHealingPower(AbstractDungeon.player)));
            AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new StrengthDegenerationPower(AbstractDungeon.player)));
        }
        if (AbstractDungeon.player.chosenClass == AbstractPlayer.PlayerClass.THE_SILENT) {
            AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new FeeblePoisonPower(AbstractDungeon.player)));
            AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new FlimsyDaggersPower(AbstractDungeon.player)));
        }
        if (AbstractDungeon.player.chosenClass == AbstractPlayer.PlayerClass.DEFECT) {
            AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new WeakOrbsPower(AbstractDungeon.player)));
            AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new OrbDecayPower(AbstractDungeon.player)));
        }
        if (AbstractDungeon.player.chosenClass == AbstractPlayer.PlayerClass.WATCHER) {
            AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new DisturbedCalmPower(AbstractDungeon.player)));
            AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new SubduedSpiritPower(AbstractDungeon.player)));
        }
        if (isUnknownClass) {
            AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new VoidEmbracePower(AbstractDungeon.player)));
            AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new StrengthDegenerationPower(AbstractDungeon.player)));
        }
    }

    @Override
    protected void getMove(final int num) {
        int m = this.moveHistory.size();
        int cycle = (m - 2) / 3;
        int index = (m - 2) % 3;
        byte cycleAttack = cycle == 0 || cycle >= 3 ? DAMNATION_ATTACK : ALL_IS_DUST_ATTACK;
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
            move = cycle == 0 || cycle >= 3 ? EMBRACE_THE_END_BUFF : RUIN_DEBUFF;
        }

        switch (move) {
            case OBLITERATE_ATTACK:
                this.setMove(MOVES[0], OBLITERATE_ATTACK, Intent.ATTACK, this.obliterateDamage);
                break;
            case CALAMITY_DEBUFF:
                this.setMove(MOVES[1], CALAMITY_DEBUFF, Intent.STRONG_DEBUFF);
                break;
            case RAVAGE_ATTACK:
                this.setMove(MOVES[2], RAVAGE_ATTACK, Intent.ATTACK, this.ravageDamage, this.ravageHits, true);
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
            case RUIN_DEBUFF:
                this.setMove(MOVES[6], RUIN_DEBUFF, Intent.DEBUFF);
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