package abyss.monsters.act4;

import abyss.Abyss;
import abyss.cards.Tormented;
import abyss.powers.AbysstouchedPower;
import abyss.powers.AbysstouchedPulsePower;
import basemod.abstracts.CustomMonster;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.AnimateFastAttackAction;
import com.megacrit.cardcrawl.actions.animations.FastShakeAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInDrawPileAction;
import com.megacrit.cardcrawl.actions.common.RollMoveAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.MonsterStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.ArtifactPower;
import com.megacrit.cardcrawl.powers.StrengthPower;

public class AnnihilationMage extends CustomMonster
{
    public static final String ID = "Abyss:AnnihilationMage";
    private static final MonsterStrings monsterStrings;
    public static final String NAME;
    public static final String[] MOVES;
    private static final String IMG = Abyss.monsterImage(ID);
    private boolean firstMove = true;
    private static final byte ANNIHILATION_CURSE_DEBUFF = 1;
    private static final byte CHAOS_BOLT_ATTACK = 2;
    private static final byte LESSER_ANNIHILATION_DEBUFF = 3;
    private static final byte STRENGTH_FROM_THE_VOID_BUFF = 4;
    private static final byte BREATH_OF_DARKNESS_ATTACK = 5;
    private static final int ANNIHILATION_CURSE_ABYSSTOUCHED = 3;
    private static final int ANNIHILATION_CURSE_HITS = 5;
    private static final int A18_ANNIHILATION_CURSE_HITS = 6;
    private static final int CHAOS_BOLT_DAMAGE = 4;
    private static final int A3_CHAOS_BOLT_DAMAGE = 5;
    private static final int CHAOS_BOLT_HITS = 3;
    private static final int LESSER_ANNIHILATION_ABYSSTOUCHED = 3;
    private static final int LESSER_ANNIHILATION_HITS = 3;
    private static final int A18_LESSER_ANNIHILATION_HITS = 4;
    private static final int LESSER_ANNIHILATION_THRESHOLD = 5;
    private static final int A18_LESSER_ANNIHILATION_THRESHOLD = 10;
    private static final int STRENGTH_FROM_THE_VOID_STRENGTH = 1;
    private static final int A18_STRENGTH_FROM_THE_VOID_STRENGTH = 1;
    private static final int STRENGTH_FROM_THE_VOID_ARTIFACT = 1;
    private static final int A18_STRENGTH_FROM_THE_VOID_ARTIFACT = 1;
    private static final int BREATH_OF_DARKNESS_DAMAGE = 4;
    private static final int A3_BREATH_OF_DARKNESS_DAMAGE = 5;
    private static final int BREATH_OF_DARKNESS_ABYSSTOUCHED_PULSE = 1;
    private static final int A18_BREATH_OF_DARKNESS_ABYSSTOUCHED_PULSE = 1;
    private static final int ABYSSTOUCHED_PULSE_AMOUNT = 1;
    private static final int A18_ABYSSTOUCHED_PULSE_AMOUNT = 2;
    private static final int HP_MIN = 170;
    private static final int HP_MAX = 170;
    private static final int A8_HP_MIN = 190;
    private static final int A8_HP_MAX = 190;
    private int annihilationCurseHits;
    private int chaosBoltDamage;
    private int lesserAnnihilationHits;
    private int lesserAnnihilationThreshold;
    private int strengthFromTheVoidStrength;
    private int strengthFromTheVoidArtifact;
    private int breathOfDarknessDamage;
    private int breathOfDarknessAbysstouchedPulse;
    private int abysstouchedPulse;

    public AnnihilationMage() {
        this(0.0f, 0.0f);
    }

    public AnnihilationMage(final float x, final float y) {
        super(AnnihilationMage.NAME, ID, HP_MAX, -5.0F, 0, 300.0f, 405.0f, IMG, x, y);
        this.type = EnemyType.ELITE;
        if (AbstractDungeon.ascensionLevel >= 8) {
            this.setHp(A8_HP_MIN, A8_HP_MAX);
        } else {
            this.setHp(HP_MIN, HP_MAX);
        }

        if (AbstractDungeon.ascensionLevel >= 3) {
            this.chaosBoltDamage = A3_CHAOS_BOLT_DAMAGE;
            this.breathOfDarknessDamage = A3_BREATH_OF_DARKNESS_DAMAGE;
        } else {
            this.chaosBoltDamage = CHAOS_BOLT_DAMAGE;
            this.breathOfDarknessDamage = BREATH_OF_DARKNESS_DAMAGE;
        }
        this.damage.add(new DamageInfo(this, this.chaosBoltDamage));
        this.damage.add(new DamageInfo(this, this.breathOfDarknessDamage));

        if (AbstractDungeon.ascensionLevel >= 18) {
            this.annihilationCurseHits = A18_ANNIHILATION_CURSE_HITS;
            this.lesserAnnihilationHits = A18_LESSER_ANNIHILATION_HITS;
            this.lesserAnnihilationThreshold = A18_LESSER_ANNIHILATION_THRESHOLD;
            this.strengthFromTheVoidStrength = A18_STRENGTH_FROM_THE_VOID_STRENGTH;
            this.strengthFromTheVoidArtifact = A18_STRENGTH_FROM_THE_VOID_ARTIFACT;
            this.breathOfDarknessAbysstouchedPulse = A18_BREATH_OF_DARKNESS_ABYSSTOUCHED_PULSE;
            this.abysstouchedPulse = A18_ABYSSTOUCHED_PULSE_AMOUNT;
        }
        else {
            this.annihilationCurseHits = ANNIHILATION_CURSE_HITS;
            this.lesserAnnihilationHits = LESSER_ANNIHILATION_HITS;
            this.lesserAnnihilationThreshold = LESSER_ANNIHILATION_THRESHOLD;
            this.strengthFromTheVoidStrength = STRENGTH_FROM_THE_VOID_STRENGTH;
            this.strengthFromTheVoidArtifact = STRENGTH_FROM_THE_VOID_ARTIFACT;
            this.breathOfDarknessAbysstouchedPulse = BREATH_OF_DARKNESS_ABYSSTOUCHED_PULSE;
            this.abysstouchedPulse = ABYSSTOUCHED_PULSE_AMOUNT;
        }
    }

    @Override
    public void usePreBattleAction() {
        this.addToBot(new ApplyPowerAction(this, this, new AbysstouchedPulsePower(this, this.abysstouchedPulse), this.abysstouchedPulse));
    }

    @Override
    public void takeTurn() {
        if (this.firstMove) {
            this.firstMove = false;
        }
        switch (this.nextMove) {
            case ANNIHILATION_CURSE_DEBUFF:
                //TODO sound would really benefit from a curse sound effect
                AbstractDungeon.actionManager.addToBottom(new FastShakeAction(this, 0.5F, 0.2F));
                for (int i=0; i < this.annihilationCurseHits; i++) {
                    AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(AbstractDungeon.player, this, new AbysstouchedPower(AbstractDungeon.player, ANNIHILATION_CURSE_ABYSSTOUCHED), ANNIHILATION_CURSE_ABYSSTOUCHED));
                }
                break;
            case CHAOS_BOLT_ATTACK:
                AbstractDungeon.actionManager.addToBottom(new AnimateFastAttackAction(this));
                for (int i=0; i < CHAOS_BOLT_HITS; i++) {
                    AbstractDungeon.actionManager.addToBottom(new DamageAction(AbstractDungeon.player, this.damage.get(0), AbstractGameAction.AttackEffect.FIRE));
                }
                AbstractDungeon.actionManager.addToBottom(new MakeTempCardInDrawPileAction(new Tormented(), 1, true, true));
                break;
            case LESSER_ANNIHILATION_DEBUFF:
                AbstractDungeon.actionManager.addToBottom(new FastShakeAction(this, 0.5F, 0.2F));
                for (int i=0; i < this.lesserAnnihilationHits; i++) {
                    AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(AbstractDungeon.player, this, new AbysstouchedPower(AbstractDungeon.player, LESSER_ANNIHILATION_ABYSSTOUCHED), LESSER_ANNIHILATION_ABYSSTOUCHED));
                }
                break;
            case STRENGTH_FROM_THE_VOID_BUFF:
                for (AbstractMonster m : AbstractDungeon.getCurrRoom().monsters.monsters) {
                    if (!m.isDying) {
                        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(m, this, new StrengthPower(m, this.strengthFromTheVoidStrength), this.strengthFromTheVoidStrength));
                        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(m, this, new ArtifactPower(m, this.strengthFromTheVoidArtifact), this.strengthFromTheVoidArtifact));
                    }
                }
                break;
            case BREATH_OF_DARKNESS_ATTACK:
                AbstractDungeon.actionManager.addToBottom(new DamageAction(AbstractDungeon.player, this.damage.get(1), AbstractGameAction.AttackEffect.NONE));
                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(this, this, new AbysstouchedPulsePower(this, this.breathOfDarknessAbysstouchedPulse), this.breathOfDarknessAbysstouchedPulse));
                break;
        }
        AbstractDungeon.actionManager.addToBottom(new RollMoveAction(this));
    }

    @Override
    protected void getMove(final int num) {
        if (this.firstMove) {
            this.setMove(MOVES[0], ANNIHILATION_CURSE_DEBUFF, Intent.STRONG_DEBUFF);
        }
        else if (this.lastMove(ANNIHILATION_CURSE_DEBUFF) || this.lastMove(BREATH_OF_DARKNESS_ATTACK)) {
            this.setMove(MOVES[1], CHAOS_BOLT_ATTACK, Intent.ATTACK_DEBUFF, this.chaosBoltDamage, CHAOS_BOLT_HITS, true);
        }
        else if (this.lastMove(CHAOS_BOLT_ATTACK)) {
            AbstractPower p = AbstractDungeon.player.getPower(AbysstouchedPower.POWER_ID);
            if (p != null && p.amount <= this.lesserAnnihilationThreshold) {
                this.setMove(MOVES[2], LESSER_ANNIHILATION_DEBUFF, Intent.DEBUFF);
            }
            else {
                this.setMove(MOVES[3], STRENGTH_FROM_THE_VOID_BUFF, Intent.BUFF);
            }
        }
        else {
            this.setMove(MOVES[4], BREATH_OF_DARKNESS_ATTACK, Intent.ATTACK_BUFF, this.breathOfDarknessDamage);
        }
    }

    static {
        monsterStrings = CardCrawlGame.languagePack.getMonsterStrings(ID);
        NAME = AnnihilationMage.monsterStrings.NAME;
        MOVES = AnnihilationMage.monsterStrings.MOVES;
    }
}