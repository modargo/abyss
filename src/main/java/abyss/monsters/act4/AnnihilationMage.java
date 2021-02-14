package abyss.monsters.act4;

import abyss.Abyss;
import abyss.cards.Tormented;
import abyss.powers.AbysstouchedPower;
import abyss.powers.AbysstouchedPulsePower;
import abyss.powers.DelayedAbysstouchedPower;
import basemod.abstracts.CustomMonster;
import basemod.helpers.VfxBuilder;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.FastShakeAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInDrawPileAction;
import com.megacrit.cardcrawl.actions.common.RollMoveAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.localization.MonsterStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.ArtifactPower;
import com.megacrit.cardcrawl.powers.StrengthPower;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;

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
    private static final int CHAOS_BOLT_DAMAGE = 13;
    private static final int A3_CHAOS_BOLT_DAMAGE = 15;
    private static final int LESSER_ANNIHILATION_ABYSSTOUCHED = 3;
    private static final int LESSER_ANNIHILATION_HITS = 4;
    private static final int A18_LESSER_ANNIHILATION_HITS = 5;
    private static final int LESSER_ANNIHILATION_THRESHOLD = 5;
    private static final int A18_LESSER_ANNIHILATION_THRESHOLD = 10;
    private static final int STRENGTH_FROM_THE_VOID_STRENGTH = 2;
    private static final int A18_STRENGTH_FROM_THE_VOID_STRENGTH = 2;
    private static final int STRENGTH_FROM_THE_VOID_ARTIFACT = 1;
    private static final int A18_STRENGTH_FROM_THE_VOID_ARTIFACT = 1;
    private static final int BREATH_OF_DARKNESS_DAMAGE = 0;
    private static final int A3_BREATH_OF_DARKNESS_DAMAGE = 1;
    private static final int BREATH_OF_DARKNESS_HITS = 3;
    private static final int BREATH_OF_DARKNESS_ABYSSTOUCHED_PULSE = 1;
    private static final int A18_BREATH_OF_DARKNESS_ABYSSTOUCHED_PULSE = 1;
    private static final int ABYSSTOUCHED_PULSE_AMOUNT = 1;
    private static final int A18_ABYSSTOUCHED_PULSE_AMOUNT = 2;
    private static final int ARTIFACT_AMOUNT = 1;
    private static final int A18_ARTIFACT_AMOUNT = 1;
    private static final int HP_MIN = 180;
    private static final int HP_MAX = 180;
    private static final int A8_HP_MIN = 200;
    private static final int A8_HP_MAX = 200;
    private int annihilationCurseHits;
    private int chaosBoltDamage;
    private int lesserAnnihilationHits;
    private int lesserAnnihilationThreshold;
    private int strengthFromTheVoidStrength;
    private int strengthFromTheVoidArtifact;
    private int breathOfDarknessDamage;
    private int breathOfDarknessAbysstouchedPulse;
    private int abysstouchedPulse;
    private int artifact;

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
            this.artifact = A18_ARTIFACT_AMOUNT;
        }
        else {
            this.annihilationCurseHits = ANNIHILATION_CURSE_HITS;
            this.lesserAnnihilationHits = LESSER_ANNIHILATION_HITS;
            this.lesserAnnihilationThreshold = LESSER_ANNIHILATION_THRESHOLD;
            this.strengthFromTheVoidStrength = STRENGTH_FROM_THE_VOID_STRENGTH;
            this.strengthFromTheVoidArtifact = STRENGTH_FROM_THE_VOID_ARTIFACT;
            this.breathOfDarknessAbysstouchedPulse = BREATH_OF_DARKNESS_ABYSSTOUCHED_PULSE;
            this.abysstouchedPulse = ABYSSTOUCHED_PULSE_AMOUNT;
            this.artifact = ARTIFACT_AMOUNT;
        }
    }

    @Override
    public void usePreBattleAction() {
        this.addToBot(new ApplyPowerAction(this, this, new AbysstouchedPulsePower(this, this.abysstouchedPulse), this.abysstouchedPulse));
        this.addToBot(new ApplyPowerAction(this, this, new ArtifactPower(this, this.artifact), this.artifact));
    }

    @Override
    public void takeTurn() {
        if (this.firstMove) {
            this.firstMove = false;
        }
        switch (this.nextMove) {
            case ANNIHILATION_CURSE_DEBUFF:
                AbstractDungeon.actionManager.addToBottom(new FastShakeAction(this, 0.5F, 0.2F));
                for (int i=0; i < this.annihilationCurseHits; i++) {
                    AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(AbstractDungeon.player, this, new AbysstouchedPower(AbstractDungeon.player, ANNIHILATION_CURSE_ABYSSTOUCHED), ANNIHILATION_CURSE_ABYSSTOUCHED));
                }
                break;
            case CHAOS_BOLT_ATTACK:
                float x1 = this.hb.cX;
                float y1 = this.hb.cY;
                float x2 = AbstractDungeon.player.drawX + AbstractDungeon.player.hb_w / 2.0f;
                float y2 = AbstractDungeon.player.drawY + AbstractDungeon.player.hb_h / 4.0f;
                int numEffects = 6;
                Texture[] textures = new Texture[] { ImageMaster.ORB_LIGHTNING, ImageMaster.ORB_PLASMA, ImageMaster.ORB_DARK};
                String[] soundKeys = new String[] { "ORB_LIGHTNING_EVOKE", "ORB_PLASMA_EVOKE", "ORB_DARK_EVOKE"};
                for (int i=0; i < numEffects; i++) {
                    float delay = 0.2f * i;
                    AbstractGameEffect effect = new VfxBuilder(textures[i % textures.length], x1, y1, 0.3f + delay)
                            .setScale(1.5f)
                            .rotate(-400.0f)
                            .moveX(x1, x2)
                            .moveY(y1, y2)
                            .playSoundAt(delay, -0.4F, soundKeys[i % soundKeys.length])
                            .build();
                    AbstractDungeon.effectsQueue.add(effect);
                }
                AbstractDungeon.actionManager.addToBottom(new DamageAction(AbstractDungeon.player, this.damage.get(0), AbstractGameAction.AttackEffect.NONE));
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
                float x1a = this.hb.cX;
                float y1a = this.hb.cY;
                float x2a = AbstractDungeon.player.drawX + AbstractDungeon.player.hb_w / 2.0f;
                float y2a = AbstractDungeon.player.drawY + AbstractDungeon.player.hb_h / 2.0f;
                AbstractGameEffect effect = new VfxBuilder(ImageMaster.ORB_DARK, x1a, y1a, 1.0f)
                        .setScale(2.5f)
                        .rotate(-200.0f)
                        .moveX(x1a, x2a)
                        .moveY(y1a, y2a)
                        .playSoundAt(0.0f, -0.4F, "ORB_DARK_EVOKE")
                        .playSoundAt(0.5f, -0.4F, "ORB_DARK_EVOKE")
                        .build();
                AbstractDungeon.effectsQueue.add(effect);
                for (int i=0; i < BREATH_OF_DARKNESS_HITS; i++) {
                    AbstractDungeon.actionManager.addToBottom(new DamageAction(AbstractDungeon.player, this.damage.get(1), AbstractGameAction.AttackEffect.NONE));
                    AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(this, this, new AbysstouchedPulsePower(this, this.breathOfDarknessAbysstouchedPulse), this.breathOfDarknessAbysstouchedPulse));
                }
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
            this.setMove(MOVES[1], CHAOS_BOLT_ATTACK, Intent.ATTACK_DEBUFF, this.chaosBoltDamage);
        }
        else if (this.lastMove(CHAOS_BOLT_ATTACK)) {
            AbstractPower p1 = AbstractDungeon.player.getPower(AbysstouchedPower.POWER_ID);
            AbstractPower p2 = AbstractDungeon.player.getPower(DelayedAbysstouchedPower.POWER_ID);
            int abysstouchedAmount = (p1 != null ? p1.amount : 0) + (p2 != null ? p2.amount : 0);
            if (abysstouchedAmount <= this.lesserAnnihilationThreshold) {
                this.setMove(MOVES[2], LESSER_ANNIHILATION_DEBUFF, Intent.DEBUFF);
            }
            else {
                this.setMove(MOVES[3], STRENGTH_FROM_THE_VOID_BUFF, Intent.BUFF);
            }
        }
        else {
            this.setMove(MOVES[4], BREATH_OF_DARKNESS_ATTACK, Intent.ATTACK_BUFF, this.breathOfDarknessDamage, BREATH_OF_DARKNESS_HITS, true);
        }
    }

    static {
        monsterStrings = CardCrawlGame.languagePack.getMonsterStrings(ID);
        NAME = AnnihilationMage.monsterStrings.NAME;
        MOVES = AnnihilationMage.monsterStrings.MOVES;
    }
}