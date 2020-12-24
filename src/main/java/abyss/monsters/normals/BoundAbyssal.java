package abyss.monsters.normals;

import abyss.Abyss;
import abyss.cards.Withering;
import abyss.cards.Tormented;
import basemod.abstracts.CustomMonster;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.AnimateFastAttackAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInDrawPileAction;
import com.megacrit.cardcrawl.actions.common.RollMoveAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.cards.blue.Buffer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.MonsterStrings;
import com.megacrit.cardcrawl.powers.*;

public class BoundAbyssal extends CustomMonster {
    public static final String ID = "Abyss:BoundAbyssal";
    private static final MonsterStrings monsterStrings;
    public static final String NAME;
    public static final String[] MOVES;
    private static final String IMG = Abyss.monsterImage(ID);
    private boolean firstMove = true;
    private static final byte OBSERVE_ATTACK = 1;
    private static final byte GLARE_ATTACK = 2;
    private static final byte CONTEMPLATE_ATTACK = 3;
    private static final byte SCRUTINIZE_ATTACK = 4;
    private static final int OBSERVE_DAMAGE = 13;
    private static final int A2_OBSERVE_DAMAGE = 15;
    private static final int GLARE_DAMAGE = 5;
    private static final int A2_GLARE_DAMAGE = 6;
    private static final int GLARE_HITS = 2;
    private static final int CONTEMPLATE_DAMAGE = 7;
    private static final int A2_CONTEMPLATE_DAMAGE = 8;
    private static final int CONTEMPLATE_STRENGTH = 2;
    private static final int A17_CONTEMPLATE_STRENGTH = 3;
    private static final int CONTEMPLATE_BUFFER = 1;
    private static final int A17_CONTEMPLATE_BUFFER = 3;
    private static final int CONTEMPLATE_ARTIFACT = 1;
    private static final int A17_CONTEMPLATE_ARTIFACT = 1;
    private static final int SCRUTINIZE_DAMAGE = 7;
    private static final int A2_SCRUTINIZE_DAMAGE = 8;
    private static final int SCRUTINIZE_VULNERABLE = 2;
    private static final int A17_SCRUTINIZE_VULNERABLE = 3;
    private static final int HP_MIN = 45;
    private static final int HP_MAX = 45;
    private static final int A7_HP_MIN = 50;
    private static final int A7_HP_MAX = 50;
    private static final int STARTING_BUFFER = 20;
    private static final int A17_STARTING_BUFFER = 20;
    private static final int STARTING_ARTIFACT = 3;
    private static final int A17_STARTING_ARTIFACT = 3;
    private int observeDamage;
    private int glareDamage;
    private int contemplateDamage;
    private int contemplateStrength;
    private int contemplateArtifact;
    private int contemplateBuffer;
    private int scrutinizeDamage;
    private int scrutinizeVulnerable;
    private int startingBuffer;
    private int startingArtifact;

    public BoundAbyssal() {
        this(0.0f, 0.0f);
    }

    public BoundAbyssal(final float x, final float y) {
        super(BoundAbyssal.NAME, ID, HP_MAX, -5.0F, 0, 250, 250, IMG, x, y);
        this.type = EnemyType.NORMAL;
        if (AbstractDungeon.ascensionLevel >= 7) {
            this.setHp(A7_HP_MIN, A7_HP_MAX);
        } else {
            this.setHp(HP_MIN, HP_MAX);
        }

        if (AbstractDungeon.ascensionLevel >= 2) {
            this.observeDamage = A2_OBSERVE_DAMAGE;
            this.glareDamage = A2_GLARE_DAMAGE;
            this.contemplateDamage = A2_CONTEMPLATE_DAMAGE;
            this.scrutinizeDamage = A2_SCRUTINIZE_DAMAGE;
        } else {
            this.observeDamage = OBSERVE_DAMAGE;
            this.glareDamage = GLARE_DAMAGE;
            this.contemplateDamage = CONTEMPLATE_DAMAGE;
            this.scrutinizeDamage = SCRUTINIZE_DAMAGE;
        }
        this.damage.add(new DamageInfo(this, this.observeDamage));
        this.damage.add(new DamageInfo(this, this.glareDamage));
        this.damage.add(new DamageInfo(this, this.contemplateDamage));
        this.damage.add(new DamageInfo(this, this.scrutinizeDamage));

        if (AbstractDungeon.ascensionLevel >= 17) {
            this.contemplateStrength = A17_CONTEMPLATE_STRENGTH;
            this.contemplateBuffer = A17_CONTEMPLATE_BUFFER;
            this.contemplateArtifact = A17_CONTEMPLATE_ARTIFACT;
            this.scrutinizeVulnerable = A17_SCRUTINIZE_VULNERABLE;
            this.startingBuffer = A17_STARTING_BUFFER;
            this.startingArtifact = A17_STARTING_ARTIFACT;
        } else {
            this.contemplateStrength = CONTEMPLATE_STRENGTH;
            this.contemplateBuffer = CONTEMPLATE_BUFFER;
            this.contemplateArtifact = CONTEMPLATE_ARTIFACT;
            this.scrutinizeVulnerable = SCRUTINIZE_VULNERABLE;
            this.startingBuffer = STARTING_BUFFER;
            this.startingArtifact = STARTING_ARTIFACT;
        }
    }

    @Override
    public void usePreBattleAction() {
        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(this, this, new BufferPower(this, this.startingBuffer), this.startingBuffer));
        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(this, this, new ArtifactPower(this, this.startingArtifact), this.startingArtifact));
    }

    @Override
    public void takeTurn() {
        if (this.firstMove) {
            this.firstMove = false;
        }
        switch (this.nextMove) {
            case OBSERVE_ATTACK:
                AbstractDungeon.actionManager.addToBottom(new AnimateFastAttackAction(this));
                AbstractDungeon.actionManager.addToBottom(new DamageAction(AbstractDungeon.player, this.damage.get(0), AbstractGameAction.AttackEffect.SLASH_DIAGONAL));
                break;
            case GLARE_ATTACK:
                AbstractDungeon.actionManager.addToBottom(new AnimateFastAttackAction(this));
                for (int i = 0; i < GLARE_HITS; i++) {
                    AbstractDungeon.actionManager.addToBottom(new DamageAction(AbstractDungeon.player, this.damage.get(1), AbstractGameAction.AttackEffect.SLASH_DIAGONAL));
                }
                break;
            case CONTEMPLATE_ATTACK:
                AbstractDungeon.actionManager.addToBottom(new AnimateFastAttackAction(this));
                AbstractDungeon.actionManager.addToBottom(new DamageAction(AbstractDungeon.player, this.damage.get(2), AbstractGameAction.AttackEffect.SLASH_DIAGONAL));
                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(this, this, new StrengthPower(this, this.contemplateStrength), this.contemplateStrength));
                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(this, this, new BufferPower(this, this.contemplateBuffer), this.contemplateBuffer));
                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(this, this, new ArtifactPower(this, this.contemplateArtifact), this.contemplateArtifact));
                break;
            case SCRUTINIZE_ATTACK:
                AbstractDungeon.actionManager.addToBottom(new AnimateFastAttackAction(this));
                AbstractDungeon.actionManager.addToBottom(new DamageAction(AbstractDungeon.player, this.damage.get(3), AbstractGameAction.AttackEffect.SLASH_DIAGONAL));
                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(AbstractDungeon.player, this, new VulnerablePower(AbstractDungeon.player, this.scrutinizeVulnerable, true), this.scrutinizeVulnerable));
                AbstractDungeon.actionManager.addToBottom(new MakeTempCardInDrawPileAction(new Tormented(), 1, true, true));
                if (AbstractDungeon.ascensionLevel >= 17) {
                    AbstractDungeon.actionManager.addToBottom(new MakeTempCardInDrawPileAction(new Withering(), 1, true, true));
                }
                break;
        }
        AbstractDungeon.actionManager.addToBottom(new RollMoveAction(this));
    }

    @Override
    protected void getMove(final int num) {
        if (this.firstMove || this.lastMove(CONTEMPLATE_ATTACK) || this.lastMove(SCRUTINIZE_ATTACK)) {
            if (num < 50) {
                this.setMove(MOVES[0], OBSERVE_ATTACK, Intent.ATTACK, this.observeDamage);
            }
            else {
                this.setMove(MOVES[1], GLARE_ATTACK, Intent.ATTACK, this.glareDamage, GLARE_HITS, true);
            }
        }
        else {
            // Contemplate is 3% more likely for each missing stack of buffer
            AbstractPower power = this.getPower(Buffer.ID);
            int currentBuffer = power != null ? power.amount : 0;
            int contemplatePercent = 30 + (this.startingBuffer - currentBuffer) * 3;
            if (num < contemplatePercent) {
                this.setMove(MOVES[2], CONTEMPLATE_ATTACK, Intent.ATTACK_BUFF, this.contemplateDamage);
            }
            else {
                this.setMove(MOVES[3], SCRUTINIZE_ATTACK, Intent.ATTACK_DEBUFF, this.scrutinizeDamage);
            }
        }
    }

    static {
        monsterStrings = CardCrawlGame.languagePack.getMonsterStrings(ID);
        NAME = BoundAbyssal.monsterStrings.NAME;
        MOVES = BoundAbyssal.monsterStrings.MOVES;
    }
}