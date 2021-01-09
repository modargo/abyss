package abyss.monsters.bosses;

import abyss.Abyss;
import abyss.powers.AbysstouchedPower;
import basemod.abstracts.CustomMonster;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.AnimateFastAttackAction;
import com.megacrit.cardcrawl.actions.animations.FastShakeAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.*;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.cards.status.Wound;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.MonsterStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.*;
import com.megacrit.cardcrawl.vfx.combat.ExplosionSmallEffect;

public class DeepTyrant extends CustomMonster
{
    public static final String ID = "Abyss:DeepTyrant";
    private static final MonsterStrings monsterStrings;
    public static final String NAME;
    public static final String[] MOVES;
    private static final String IMG = Abyss.monsterImage(ID);
    private boolean firstMove = true;
    private static final byte BLAST_ATTACK = 1;
    private static final byte TERRIFYING_GAZE_DEBUFF = 2;
    private static final byte THRASH_ATTACK = 3;
    private static final byte BODY_SLAM_ATTACK = 4;
    private static final byte CALL_THE_ABYSS_MOVE = 5;
    private static final int BLAST_DAMAGE = 19;
    private static final int A4_BLAST_DAMAGE = 21;
    private static final int BLAST_WOUNDS = 2;
    private static final int ABYSSAL_GAZE_WEAK_FRAIL = 2;
    private static final int A19_ABYSSAL_GAZE_WEAK_FRAIL = 2;
    private static final int ABYSSAL_GAZE_AMOUNT = 5;
    private static final int A19_ABYSSAL_GAZE_AMOUNT = 8;
    private static final int THRASH_DAMAGE = 12;
    private static final int A4_THRASH_DAMAGE = 14;
    private static final int THRASH_HITS = 2;
    private static final int THRASH_STRENGTH = 1;
    private static final int A19_THRASH_STRENGTH = 1;
    private static final int THRASH_ARTIFACT = 1;
    private static final int A19_THRASH_ARTIFACT = 1;
    private static final int BODY_SLAM_DAMAGE = 27;
    private static final int A4_BODY_SLAM_DAMAGE = 30;
    private static final int CALL_THE_ABYSS_SUMMONS = 2;
    private static final int A19_CALL_THE_ABYSS_SUMMONS = 3;
    private static final int HP = 475;
    private static final int A9_HP = 500;
    private int blastDamage;
    private int terrifyingGazeWeakFrail;
    private int terrifyingGazeAmount;
    private int thrashDamage;
    private int thrashStrength;
    private int thrashArtifact;
    private int bodySlamDamage;
    private int callTheAbyssSummons;

    private boolean summoned;

    public DeepTyrant() {
        this(0.0f, 0.0f);
    }

    public DeepTyrant(final float x, final float y) {
        super(DeepTyrant.NAME, ID, HP, -5.0F, 0, 565.0f, 600.0f, IMG, x, y);
        this.type = EnemyType.BOSS;
        this.summoned = false;
        if (AbstractDungeon.ascensionLevel >= 9) {
            this.setHp(A9_HP);
        } else {
            this.setHp(HP);
        }

        if (AbstractDungeon.ascensionLevel >= 4) {
            this.blastDamage = A4_BLAST_DAMAGE;
            this.thrashDamage = A4_THRASH_DAMAGE;
            this.bodySlamDamage = A4_BODY_SLAM_DAMAGE;
        } else {
            this.blastDamage = BLAST_DAMAGE;
            this.thrashDamage = THRASH_DAMAGE;
            this.bodySlamDamage = BODY_SLAM_DAMAGE;
        }
        this.damage.add(new DamageInfo(this, this.blastDamage));
        this.damage.add(new DamageInfo(this, this.thrashDamage));
        this.damage.add(new DamageInfo(this, this.bodySlamDamage));

        if (AbstractDungeon.ascensionLevel >= 19) {
            this.terrifyingGazeWeakFrail = A19_ABYSSAL_GAZE_WEAK_FRAIL;
            this.terrifyingGazeAmount = A19_ABYSSAL_GAZE_AMOUNT;
            this.thrashStrength = A19_THRASH_STRENGTH;
            this.thrashArtifact = A19_THRASH_ARTIFACT;
            this.callTheAbyssSummons = A19_CALL_THE_ABYSS_SUMMONS;
        }
        else {
            this.terrifyingGazeWeakFrail = ABYSSAL_GAZE_WEAK_FRAIL;
            this.terrifyingGazeAmount = ABYSSAL_GAZE_AMOUNT;
            this.thrashStrength = THRASH_STRENGTH;
            this.thrashArtifact = THRASH_ARTIFACT;
            this.callTheAbyssSummons = CALL_THE_ABYSS_SUMMONS;
        }
    }

    @Override
    public void usePreBattleAction() {
        CardCrawlGame.music.unsilenceBGM();
        AbstractDungeon.scene.fadeOutAmbiance();
        AbstractDungeon.getCurrRoom().playBgmInstantly("BOSS_BEYOND");
    }

    @Override
    public void takeTurn() {
        if (this.firstMove) {
            this.firstMove = false;
        }
        switch (this.nextMove) {
            case BLAST_ATTACK:
                AbstractDungeon.actionManager.addToBottom(new AnimateFastAttackAction(this));
                AbstractDungeon.actionManager.addToBottom(new DamageAction(AbstractDungeon.player, this.damage.get(0), AbstractGameAction.AttackEffect.FIRE));
                if (AbstractDungeon.ascensionLevel >= 19) {
                    AbstractDungeon.actionManager.addToBottom(new MakeTempCardInDrawPileAction(new Wound(), BLAST_WOUNDS, true, true));
                }
                else {
                    AbstractDungeon.actionManager.addToBottom(new MakeTempCardInDiscardAction(new Wound(), BLAST_WOUNDS));
                }
                break;
            case TERRIFYING_GAZE_DEBUFF:
                AbstractDungeon.actionManager.addToBottom(new FastShakeAction(this, 0.5F, 0.2F));
                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(AbstractDungeon.player, this, new WeakPower(AbstractDungeon.player, this.terrifyingGazeWeakFrail, true), this.terrifyingGazeWeakFrail));
                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(AbstractDungeon.player, this, new FrailPower(AbstractDungeon.player, this.terrifyingGazeWeakFrail, true), this.terrifyingGazeWeakFrail));
                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(AbstractDungeon.player, this, new AbysstouchedPower(AbstractDungeon.player, this.terrifyingGazeAmount), this.terrifyingGazeAmount));
                break;
            case THRASH_ATTACK:
                AbstractDungeon.actionManager.addToBottom(new AnimateFastAttackAction(this));
                for (int i = 0; i < THRASH_HITS; i++) {
                    AbstractDungeon.actionManager.addToBottom(new DamageAction(AbstractDungeon.player, this.damage.get(1), AbstractGameAction.AttackEffect.SLASH_VERTICAL));
                }
                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(this, this, new StrengthPower(this, this.thrashStrength), this.thrashStrength));
                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(this, this, new ArtifactPower(this, this.thrashArtifact), this.thrashArtifact));
                break;
            case BODY_SLAM_ATTACK:
                AbstractDungeon.actionManager.addToBottom(new AnimateFastAttackAction(this));
                AbstractDungeon.actionManager.addToBottom(new DamageAction(AbstractDungeon.player, this.damage.get(2), AbstractGameAction.AttackEffect.SLASH_HEAVY));
                break;
            case CALL_THE_ABYSS_MOVE:
                for (int i = 0; i < this.callTheAbyssSummons; i++) {
                    Manifestation manifestation = new Manifestation(-325.0F, i * 200.0F);
                    AbstractDungeon.actionManager.addToBottom(new SpawnMonsterAction(manifestation, true));
                }
                this.summoned = true;
                break;
        }
        AbstractDungeon.actionManager.addToBottom(new RollMoveAction(this));
    }

    @Override
    protected void getMove(final int num) {
        if (this.firstMove || this.lastMove(BODY_SLAM_ATTACK)) {
            this.setMove(MOVES[0], BLAST_ATTACK, Intent.ATTACK_DEBUFF, this.blastDamage);
        }
        else if (this.lastMove(BLAST_ATTACK) && !this.summoned) {
            this.setMove(MOVES[4], CALL_THE_ABYSS_MOVE, Intent.UNKNOWN);
        }
        else if (this.lastMove(BLAST_ATTACK) || this.lastMove(CALL_THE_ABYSS_MOVE)) {
            this.setMove(MOVES[1], TERRIFYING_GAZE_DEBUFF, Intent.STRONG_DEBUFF);
        }
        else if (this.lastMove(TERRIFYING_GAZE_DEBUFF)) {
            this.setMove(MOVES[2], THRASH_ATTACK, Intent.ATTACK_BUFF, this.thrashDamage, THRASH_HITS, true);
        }
        else {
            this.setMove(MOVES[3], BODY_SLAM_ATTACK, Intent.ATTACK, this.bodySlamDamage);
        }
    }

    @Override
    public void die() {
        super.die();
        boolean allMonstersBasicallyDead = true;
        for (AbstractMonster m : AbstractDungeon.getCurrRoom().monsters.monsters) {
            allMonstersBasicallyDead = allMonstersBasicallyDead && (m == this || m.id.equals(Manifestation.ID));
            if (m.id.equals(Manifestation.ID) && !m.isDying) {
                //TODO See if I can change the color of the explosion
                AbstractDungeon.actionManager.addToBottom(new VFXAction(new ExplosionSmallEffect(m.hb.cX, m.hb.cY), 0.1F));
                AbstractDungeon.actionManager.addToBottom(new SuicideAction(m));
            }
        }
        if (allMonstersBasicallyDead) {
            this.useFastShakeAnimation(5.0F);
            CardCrawlGame.screenShake.rumble(4.0F);
            this.onBossVictoryLogic();
        }
    }

    static {
        monsterStrings = CardCrawlGame.languagePack.getMonsterStrings(ID);
        NAME = DeepTyrant.monsterStrings.NAME;
        MOVES = DeepTyrant.monsterStrings.MOVES;
    }
}