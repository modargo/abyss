package abyss.monsters.normals;

import abyss.Abyss;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.FastShakeAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.MonsterStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.ArtifactPower;
import com.megacrit.cardcrawl.powers.CuriosityPower;

public class CroakingSeer extends AbstractCroaking {
    public static final String ID = "Abyss:CroakingSeer";
    private static final MonsterStrings monsterStrings;
    public static final String NAME;
    public static final String[] MOVES;
    public static final String[] DIALOG;
    private static final String IMG = Abyss.monsterImage(ID);
    private static final byte PROTECTIVE_WARD_BUFF = 2;
    private static final int PROTECTIVE_WARD_BLOCK = 3;
    private static final int A7_PROTECTIVE_WARD_BLOCK = 5;
    private static final int PROTECTIVE_WARD_ARTIFACT = 1;
    private int protectiveWardBlock;

    public CroakingSeer() {
        this(0.0f, 0.0f);
    }

    public CroakingSeer(final float x, final float y) {
        super(CroakingSeer.NAME, ID, -5.0F, 0, 195.0f, 205.0f, IMG, x, y);

        if (AbstractDungeon.ascensionLevel >= 7) {
            this.protectiveWardBlock = A7_PROTECTIVE_WARD_BLOCK;
        } else {
            this.protectiveWardBlock = PROTECTIVE_WARD_BLOCK;
        }
    }

    @Override
    protected AbstractPower getBuffPower(int amount) {
        return new CuriosityPower(this, amount);
    }

    @Override
    protected String getDialog() {
        return DIALOG[0];
    }

    @Override
    protected String getFirstMoveName() {
        return MOVES[0];
    }

    @Override
    protected AbstractGameAction.AttackEffect getFirstMoveAttackEffect() {
        return AbstractGameAction.AttackEffect.SLASH_VERTICAL;
    }

    @Override
    protected void executeSecondMove() {
        AbstractDungeon.actionManager.addToBottom(new FastShakeAction(this, 0.5F, 0.2F));
        for (AbstractMonster m : AbstractDungeon.getMonsters().monsters) {
            if (m == this || !m.isDying) {
                AbstractDungeon.actionManager.addToBottom(new GainBlockAction(m, this, this.protectiveWardBlock));
                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(m, this, new ArtifactPower(m, PROTECTIVE_WARD_ARTIFACT), PROTECTIVE_WARD_ARTIFACT));
            }
        }
    }

    @Override
    protected void setSecondMoveIntent() {
        this.setMove(MOVES[1], PROTECTIVE_WARD_BUFF, Intent.DEFEND_BUFF);
    }

    static {
        monsterStrings = CardCrawlGame.languagePack.getMonsterStrings(ID);
        NAME = CroakingSeer.monsterStrings.NAME;
        MOVES = CroakingSeer.monsterStrings.MOVES;
        DIALOG = CroakingSeer.monsterStrings.DIALOG;
    }
}