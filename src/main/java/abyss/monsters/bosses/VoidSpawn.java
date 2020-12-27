package abyss.monsters.bosses;

import abyss.Abyss;
import abyss.powers.BerserkerPower;
import abyss.powers.ResummonPower;
import basemod.abstracts.CustomMonster;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.AnimateFastAttackAction;
import com.megacrit.cardcrawl.actions.animations.AnimateSlowAttackAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.actions.common.RollMoveAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.MonsterStrings;

public class VoidSpawn extends CustomMonster
{
    public static final String ID = "Abyss:VoidSpawn";
    private static final MonsterStrings monsterStrings;
    public static final String NAME;
    public static final String[] MOVES;
    private static final String IMG = Abyss.monsterImage(ID);
    private boolean firstMove = true;
    private static final byte CLOBBER_ATTACK = 1;
    private static final byte BEAKED_MAW_ATTACK = 2;
    private static final byte TENTACLE_BARRAGE_ATTACK = 3;
    private static final int CLOBBER_DAMAGE = 8;
    private static final int A4_CLOBBER_DAMAGE = 9;
    private static final int BEAKED_MAW_DAMAGE = 10;
    private static final int A4_BEAKED_MAW_DAMAGE = 12;
    private static final int BEAKED_MAW_BLOCK = 10;
    private static final int A9_BEAKED_MAW_BLOCK = 15;
    private static final int TENTACLE_BARRAGE_DAMAGE = 3;
    private static final int A4_TENTACLE_BARRAGE_DAMAGE = 4;
    private static final int TENTACLE_BARRAGE_HITS = 4;
    private static final int BERSERKER_STRENGTH = 5;
    private static final int HP = 90;
    private static final int A9_HP = 100;
    private int clobberDamage;
    private int beakedMawDamage;
    private int beakedMawBlock;
    private int tentacleBarrageDamage;

    public VoidSpawn() {
        this(0.0f, 0.0f);
    }

    public VoidSpawn(final float x, final float y) {
        super(VoidSpawn.NAME, ID, HP, -5.0F, 0, 225, 235, IMG, x, y);
        this.type = EnemyType.BOSS;
        if (AbstractDungeon.ascensionLevel >= 9) {
            this.setHp(A9_HP);
            this.beakedMawBlock = A9_BEAKED_MAW_BLOCK;
        } else {
            this.setHp(HP);
            this.beakedMawBlock = BEAKED_MAW_BLOCK;
        }

        if (AbstractDungeon.ascensionLevel >= 4) {
            this.clobberDamage = A4_CLOBBER_DAMAGE;
            this.beakedMawDamage = A4_BEAKED_MAW_DAMAGE;
            this.tentacleBarrageDamage = A4_TENTACLE_BARRAGE_DAMAGE;
        } else {
            this.clobberDamage = CLOBBER_DAMAGE;
            this.beakedMawDamage = BEAKED_MAW_DAMAGE;
            this.tentacleBarrageDamage = TENTACLE_BARRAGE_DAMAGE;
        }
        this.damage.add(new DamageInfo(this, this.clobberDamage));
        this.damage.add(new DamageInfo(this, this.beakedMawDamage));
        this.damage.add(new DamageInfo(this, this.tentacleBarrageDamage));

        if (AbstractDungeon.ascensionLevel >= 19) {
            //TODO How does this get harder at A19?
        }
        else {
        }
    }

    @Override
    public void usePreBattleAction() {
        CardCrawlGame.music.unsilenceBGM();
        AbstractDungeon.scene.fadeOutAmbiance();
        AbstractDungeon.getCurrRoom().playBgmInstantly("BOSS_BEYOND");

        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(this, this, new BerserkerPower(this, BERSERKER_STRENGTH)));
        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(this, this, new ResummonPower(this)));
    }

    @Override
    public void takeTurn() {
        if (this.firstMove) {
            this.firstMove = false;
        }
        switch (this.nextMove) {
            case CLOBBER_ATTACK:
                AbstractDungeon.actionManager.addToBottom(new AnimateSlowAttackAction(this));
                AbstractDungeon.actionManager.addToBottom(new DamageAction(AbstractDungeon.player, this.damage.get(0), AbstractGameAction.AttackEffect.BLUNT_HEAVY));
                break;
            case BEAKED_MAW_ATTACK:
                AbstractDungeon.actionManager.addToBottom(new AnimateFastAttackAction(this));
                AbstractDungeon.actionManager.addToBottom(new DamageAction(AbstractDungeon.player, this.damage.get(1), AbstractGameAction.AttackEffect.SLASH_VERTICAL));
                AbstractDungeon.actionManager.addToBottom(new GainBlockAction(this, this.beakedMawBlock));
                break;
            case TENTACLE_BARRAGE_ATTACK:
                AbstractDungeon.actionManager.addToBottom(new AnimateFastAttackAction(this));
                for (int i = 0; i < TENTACLE_BARRAGE_HITS; i++) {
                    AbstractDungeon.actionManager.addToBottom(new DamageAction(AbstractDungeon.player, this.damage.get(2), AbstractGameAction.AttackEffect.BLUNT_LIGHT));
                }
                break;
        }
        AbstractDungeon.actionManager.addToBottom(new RollMoveAction(this));
    }

    @Override
    protected void getMove(final int num) {
        if (this.firstMove || this.lastMove(TENTACLE_BARRAGE_ATTACK)) {
            this.setMove(MOVES[0], CLOBBER_ATTACK, Intent.ATTACK, this.clobberDamage);
        }
        else if (this.lastMove(CLOBBER_ATTACK)) {
            this.setMove(MOVES[1], BEAKED_MAW_ATTACK, Intent.ATTACK_DEFEND, this.beakedMawDamage);
        }
        else {
            this.setMove(MOVES[2], TENTACLE_BARRAGE_ATTACK, Intent.ATTACK, this.tentacleBarrageDamage, TENTACLE_BARRAGE_HITS, true);
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
        NAME = VoidSpawn.monsterStrings.NAME;
        MOVES = VoidSpawn.monsterStrings.MOVES;
    }
}