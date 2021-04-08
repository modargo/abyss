package abyss.monsters.normals;

import abyss.Abyss;
import basemod.abstracts.CustomMonster;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.AnimateFastAttackAction;
import com.megacrit.cardcrawl.actions.animations.FastShakeAction;
import com.megacrit.cardcrawl.actions.common.*;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.cards.status.Dazed;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.MonsterStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.FrailPower;
import com.megacrit.cardcrawl.powers.SporeCloudPower;
import com.megacrit.cardcrawl.powers.VulnerablePower;

public class Demolisher extends CustomMonster {
    public static final String ID = "Abyss:Demolisher";
    private static final MonsterStrings monsterStrings;
    public static final String NAME;
    public static final String[] MOVES;
    private static final String IMG = Abyss.monsterImage(ID);
    private boolean firstMove = true;
    private static final byte HEAD_TOXIN_DEBUFF = 1;
    private static final byte BODY_TOXIN_DEBUFF = 2;
    private static final byte SAFEGUARD_MOVE = 3;
    private static final byte FRENZY_ATTACK = 4;
    private static final int HEAD_TOXIN_DAZES = 1;
    private static final int A17_HEAD_TOXIN_DAZES = 1;
    private static final int BODY_TOXIN_FRAIL = 2;
    private static final int A17_BODY_TOXIN_FRAIL = 3;
    private static final int BODY_TOXIN_VULNERABLE = 1;
    private static final int A17_BODY_TOXIN_VULNERABLE = 2;
    private static final int SAFEGUARD_BLOCK = 8;
    private static final int A7_SAFEGUARD_BLOCK = 10;
    private static final int FRENZY_DAMAGE = 3;
    private static final int A2_FRENZY_DAMAGE = 4;
    private static final int FRENZY_HITS = 3;
    private static final int A17_FRENZY_HITS = 4;
    private static final int SPORE_CLOUD_AMOUNT = 2;
    private static final int HP_MIN = 74;
    private static final int HP_MAX = 78;
    private static final int A7_HP_MIN = 76;
    private static final int A7_HP_MAX = 80;
    private int headToxinDazes;
    private int bodyToxinFrail;
    private int bodyToxinVulnerable;
    private int safeguardBlock;
    private int frenzyDamage;
    private int frenzyHits;

    public Demolisher() {
        this(0.0f, 0.0f);
    }

    public Demolisher(final float x, final float y) {
        super(Demolisher.NAME, ID, HP_MAX, -5.0F, 0, 280.0f, 180.0f, IMG, x, y);
        this.type = EnemyType.NORMAL;
        if (AbstractDungeon.ascensionLevel >= 7) {
            this.setHp(A7_HP_MIN, A7_HP_MAX);
            this.safeguardBlock = A7_SAFEGUARD_BLOCK;
        } else {
            this.setHp(HP_MIN, HP_MAX);
            this.safeguardBlock = SAFEGUARD_BLOCK;
        }

        if (AbstractDungeon.ascensionLevel >= 2) {
            this.frenzyDamage = A2_FRENZY_DAMAGE;
        } else {
            this.frenzyDamage = FRENZY_DAMAGE;
        }
        this.damage.add(new DamageInfo(this, this.frenzyDamage));

        if (AbstractDungeon.ascensionLevel >= 17) {
            this.headToxinDazes = A17_HEAD_TOXIN_DAZES;
            this.bodyToxinFrail = A17_BODY_TOXIN_FRAIL;
            this.bodyToxinVulnerable = A17_BODY_TOXIN_VULNERABLE;
            this.frenzyHits = A17_FRENZY_HITS;
        } else {
            this.headToxinDazes = HEAD_TOXIN_DAZES;
            this.bodyToxinFrail = BODY_TOXIN_FRAIL;
            this.bodyToxinVulnerable = BODY_TOXIN_VULNERABLE;
            this.frenzyHits = FRENZY_HITS;
        }
    }

    @Override
    public void usePreBattleAction() {
        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(this, this, new SporeCloudPower(this, SPORE_CLOUD_AMOUNT)));
    }

    @Override
    public void takeTurn() {
        if (this.firstMove) {
            this.firstMove = false;
        }
        switch (this.nextMove) {
            case HEAD_TOXIN_DEBUFF:
                AbstractDungeon.actionManager.addToBottom(new FastShakeAction(this, 0.5F, 0.2F));
                AbstractDungeon.actionManager.addToBottom(new MakeTempCardInDrawPileAction(new Dazed(), this.headToxinDazes, false, true));
                break;
            case BODY_TOXIN_DEBUFF:
                AbstractDungeon.actionManager.addToBottom(new FastShakeAction(this, 0.5F, 0.2F));
                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(AbstractDungeon.player, this, new FrailPower(AbstractDungeon.player, this.bodyToxinFrail, true), this.bodyToxinFrail));
                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(AbstractDungeon.player, this, new VulnerablePower(AbstractDungeon.player, this.bodyToxinVulnerable, true), this.bodyToxinVulnerable));
                break;
            case SAFEGUARD_BLOCK:
                AbstractDungeon.actionManager.addToBottom(new FastShakeAction(this, 0.5F, 0.2F));
                for (AbstractMonster m : AbstractDungeon.getCurrRoom().monsters.monsters) {
                    if (!m.isDying) {
                        AbstractDungeon.actionManager.addToBottom(new GainBlockAction(m, this.safeguardBlock));
                    }
                }
                break;
            case FRENZY_ATTACK:
                AbstractDungeon.actionManager.addToBottom(new AnimateFastAttackAction(this));
                for (int i = 0; i < this.frenzyHits; i++) {
                    AbstractDungeon.actionManager.addToBottom(new DamageAction(AbstractDungeon.player, this.damage.get(0), AbstractGameAction.AttackEffect.BLUNT_LIGHT));
                }
        }
        AbstractDungeon.actionManager.addToBottom(new RollMoveAction(this));
    }

    @Override
    protected void getMove(final int num) {
        boolean hasAllies = false;
        for (AbstractMonster m : AbstractDungeon.getCurrRoom().monsters.monsters) {
            if (m != this && !m.isDying) {
                hasAllies = true;
                break;
            }
        }
        if (!hasAllies && !this.lastMove(FRENZY_ATTACK)) {
            this.setMove(MOVES[3], FRENZY_ATTACK, Intent.ATTACK, this.frenzyDamage, this.frenzyHits, true);
        } else if (!hasAllies || this.lastMove(HEAD_TOXIN_DEBUFF)) {
            this.setMove(MOVES[1], BODY_TOXIN_DEBUFF, Intent.STRONG_DEBUFF);
        } else if (this.lastMove(BODY_TOXIN_DEBUFF)) {
            this.setMove(MOVES[2], SAFEGUARD_MOVE, Intent.DEFEND);
        } else {
            this.setMove(MOVES[0], HEAD_TOXIN_DEBUFF, Intent.DEBUFF);
        }
    }

    static {
        monsterStrings = CardCrawlGame.languagePack.getMonsterStrings(ID);
        NAME = Demolisher.monsterStrings.NAME;
        MOVES = Demolisher.monsterStrings.MOVES;
    }
}