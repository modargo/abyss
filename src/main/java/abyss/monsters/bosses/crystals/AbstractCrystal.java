package abyss.monsters.bosses.crystals;

import abyss.cards.Mineralized;
import abyss.effects.SmallColorLaserEffect;
import abyss.monsters.MonsterUtil;
import abyss.powers.CrystalLinkPower;
import abyss.powers.ResonancePower;
import basemod.abstracts.CustomMonster;
import basemod.helpers.VfxBuilder;
import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.AnimateFastAttackAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInDiscardAction;
import com.megacrit.cardcrawl.actions.common.RollMoveAction;
import com.megacrit.cardcrawl.actions.utility.SFXAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.localization.MonsterStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.ArtifactPower;
import com.megacrit.cardcrawl.powers.IntangiblePower;
import com.megacrit.cardcrawl.powers.StrengthPower;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;

import java.util.List;

public abstract class AbstractCrystal extends CustomMonster {
    public static final String ID = "Abyss:AbstractCrystal";
    private boolean firstMove = true;
    private static final MonsterStrings monsterStrings;
    public static final String[] MOVES;
    private static final byte CRYSTAL_SHARD_ATTACK = 1;
    private static final byte BEAM_ATTACK = 2;
    private static final byte MINERALIZE_DEBUFF = 3;
    private static final int CRYSTAL_SHARD_DAMAGE = 4;
    private static final int A4_CRYSTAL_SHARD_DAMAGE = 5;
    private static final int BEAM_DAMAGE = 0;
    private static final int A4_BEAM_DAMAGE = 1;
    private static final int BEAM_ARTIFACT = 1;
    private static final int A19_BEAM_ARTIFACT = 1;
    private static final int MINERALIZE_AMOUNT = 1;
    private static final int A19_MINERALIZE_AMOUNT = 1;
    private static final int RESONANCE_AMOUNT = 30;
    private static final int A19_RESONANCE_AMOUNT = 50;
    private static final int HP_MIN = 63;
    private static final int HP_MAX = 63;
    private static final int A9_HP_MIN = 70;
    private static final int A9_HP_MAX = 70;
    private boolean front;
    private int crystalShardDamage;
    private int beamDamage;
    private int beamArtifact;
    private int mineralizeAmount;
    private int resonanceAmount;

    public AbstractCrystal(String name, String id, String imgUrl, float offsetX, float offsetY, boolean front) {
        super(name, id, HP_MAX, -5.0F, 0, 85.0f, 155.0f, imgUrl, offsetX, offsetY);
        this.type = EnemyType.BOSS;
        this.front = front;
        if (AbstractDungeon.ascensionLevel >= 9) {
            this.setHp(A9_HP_MIN, A9_HP_MAX);
        } else {
            this.setHp(HP_MIN, HP_MAX);
        }

        if (AbstractDungeon.ascensionLevel >= 4) {
            this.crystalShardDamage = A4_CRYSTAL_SHARD_DAMAGE;
            this.beamDamage = A4_BEAM_DAMAGE;
        } else {
            this.crystalShardDamage = CRYSTAL_SHARD_DAMAGE;
            this.beamDamage = BEAM_DAMAGE;
        }
        this.damage.add(new DamageInfo(this, this.crystalShardDamage));
        this.damage.add(new DamageInfo(this, this.beamDamage));

        if (AbstractDungeon.ascensionLevel >= 19) {
            this.beamArtifact = A19_BEAM_ARTIFACT;
            this.mineralizeAmount = A19_MINERALIZE_AMOUNT;
            this.resonanceAmount = A19_RESONANCE_AMOUNT;
        } else {
            this.beamArtifact = BEAM_ARTIFACT;
            this.mineralizeAmount = MINERALIZE_AMOUNT;
            this.resonanceAmount = RESONANCE_AMOUNT;
        }
    }

    protected abstract AbstractPower getBuffPower();

    protected abstract Color getColor();

    @Override
    public void usePreBattleAction() {
        AbstractPower buffPower = this.getBuffPower();
        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(this, this, buffPower, buffPower.amount));
        this.intangibleCheck(true);
    }

    @Override
    public void takeTurn() {
        if (this.firstMove) {
            this.firstMove = false;
        }
        switch (this.nextMove) {
            case CRYSTAL_SHARD_ATTACK:
                float x1 = this.hb.cX;
                float y1 = this.hb.cY;
                float x2 = AbstractDungeon.player.drawX + AbstractDungeon.player.hb_w / 2.0f;
                float y2 = AbstractDungeon.player.drawY + AbstractDungeon.player.hb_h / 4.0f;
                float angle = VfxBuilder.calculateAngle(x2, y2, x1, y1) - 90;
                AbstractGameEffect effect = new VfxBuilder(ImageMaster.FROST_ORB_LEFT, x1, y1, 0.15f)
                        .setScale(2.5f)
                        .setAngle(angle)
                        .moveX(x1, x2)
                        .moveY(y1, y2)
                        .playSoundAt(0.0f, -0.4F, "ORB_FROST_EVOKE")
                        .build();
                AbstractDungeon.effectsQueue.add(effect);
                AbstractDungeon.actionManager.addToBottom(new DamageAction(AbstractDungeon.player, this.damage.get(0), AbstractGameAction.AttackEffect.NONE));
                break;
            case BEAM_ATTACK:
                AbstractDungeon.actionManager.addToBottom(new SFXAction("ATTACK_MAGIC_BEAM_SHORT", 0.5F));
                AbstractDungeon.actionManager.addToBottom(new VFXAction(new SmallColorLaserEffect(AbstractDungeon.player.hb.cX, AbstractDungeon.player.hb.cY, this.hb.cX, this.hb.cY, this.getColor()), Settings.FAST_MODE ? 0.1F : 0.3F));
                AbstractDungeon.actionManager.addToBottom(new AnimateFastAttackAction(this));
                AbstractDungeon.actionManager.addToBottom(new DamageAction(AbstractDungeon.player, this.damage.get(1), AbstractGameAction.AttackEffect.NONE));
                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(this, this, new ArtifactPower(this, this.beamArtifact), this.beamArtifact));
                break;
            case MINERALIZE_DEBUFF:
                //TODO Some kind of effect effect, using the color of each crystal
                AbstractDungeon.actionManager.addToBottom(new AnimateFastAttackAction(this));
                AbstractDungeon.actionManager.addToBottom(new MakeTempCardInDiscardAction(new Mineralized(), this.mineralizeAmount));
                break;
        }

        this.intangibleCheck(false);

        AbstractDungeon.actionManager.addToBottom(new RollMoveAction(this));
    }

    private void intangibleCheck(boolean startOfCombat) {
        if (!this.hasPower(ResonancePower.POWER_ID) && (!this.firstMove || !this.front)) {
            AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(this, this, new ResonancePower(this, this.resonanceAmount, !startOfCombat), this.resonanceAmount));
        }
    }

    @Override
    protected void getMove(final int num) {
        if ((this.front || !this.firstMove) && ((this.front && this.firstMove) || this.lastMove(BEAM_ATTACK) || this.lastMove(MINERALIZE_DEBUFF) || (!this.lastMoveBefore(CRYSTAL_SHARD_ATTACK) && num < 75))) {
           this.setMove(MOVES[0], CRYSTAL_SHARD_ATTACK, Intent.ATTACK, this.crystalShardDamage);
        }
        else if (this.front) {
            this.setMove(MOVES[1], BEAM_ATTACK, Intent.ATTACK_BUFF, this.beamDamage);
        }
        else {
            this.setMove(MOVES[2], MINERALIZE_DEBUFF, Intent.DEBUFF);
        }
    }

    @Override
    public void damage(DamageInfo info) {
        if (info.output > 0 && this.hasPower(IntangiblePower.POWER_ID)) {
            info.output = 1;
        }

        super.damage(info);
    }

    @Override
    public void die() {
        super.die();

        AbstractPower strengthPower = this.getPower(StrengthPower.POWER_ID);
        if (strengthPower != null && strengthPower.amount > 0) {
            List<AbstractPower> powers = MonsterUtil.getMonsterPowers(CrystalLinkPower.POWER_ID);
            for (AbstractPower p : powers) {
                ((CrystalLinkPower)p).onCrystalDeath(this, strengthPower.amount);
            }
        }

        if (AbstractDungeon.getMonsters().areMonstersBasicallyDead()) {
            this.useFastShakeAnimation(5.0F);
            CardCrawlGame.screenShake.rumble(4.0F);
            this.onBossVictoryLogic();
        }
    }

    static {
        monsterStrings = CardCrawlGame.languagePack.getMonsterStrings(ID);
        MOVES = monsterStrings.MOVES;
    }
}