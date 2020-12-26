package abyss.powers;

import abyss.Abyss;
import abyss.actions.ChooseAction;
import abyss.actions.ChangeMaxHpAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInDrawPileAction;
import com.megacrit.cardcrawl.cards.status.Burn;
import com.megacrit.cardcrawl.cards.status.Dazed;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.*;

import java.text.MessageFormat;
import java.util.Arrays;
import java.util.List;
import java.util.function.BiConsumer;

public class EvolvingPower extends AbstractPower {
    public static final String POWER_ID = "Abyss:Evolving";
    private static final PowerStrings powerStrings;
    public static final String NAME;
    public static final String[] DESCRIPTIONS;
    private final List<List<Evolution>> evolutionChoices;
    private int evolutionIndex = 0;
    private boolean firstCycle = true;

    public EvolvingPower(AbstractCreature owner) {
        this.name = NAME;
        this.ID = POWER_ID;
        this.owner = owner;
        this.updateDescription();
        this.evolutionChoices = this.getEvolutionChoices();
        Abyss.LoadPowerImage(this);
    }

    @Override
    public void atEndOfTurn(boolean isPlayer) {
        this.flash();
        this.showEvolutionChoice();
    }

    @Override
    public void updateDescription() {
        this.description = MessageFormat.format(DESCRIPTIONS[0], this.amount);
    }

    private void showEvolutionChoice() {
        List<Evolution> evolutionChoice = this.getNextEvolutionChoice();
        ChooseAction choice = new ChooseAction(new Dazed(), null, "TODO evolve question");
        for (Evolution evolution : evolutionChoice) {
            choice.add(evolution.name, evolution.getDescription(), () -> evolution.apply.accept(this.owner, evolution.amount));
        }
        AbstractDungeon.actionManager.addToTop(choice);
    }

    private List<Evolution> getNextEvolutionChoice() {
        List<Evolution> evolutionChoice = this.evolutionChoices.get(this.evolutionIndex);

        if (!this.firstCycle) {
            this.evolutionIndex = (this.evolutionIndex + 1) % 3;
        }
        else {
            this.evolutionIndex = (this.evolutionIndex + 1) % 5;
            if (this.evolutionIndex == 0) {
                this.firstCycle = false;
            }
        }

        return evolutionChoice;
    }

    private List<List<Evolution>> getEvolutionChoices() {
        Evolution[] o1 = new Evolution [] {
                new Evolution(AngerPower.NAME, 2, (AbstractCreature m, Integer n) -> AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(m, m, new AngerPower(m, n), n))),
                new Evolution(RitualPower.NAME, 4, (AbstractCreature m, Integer n) -> { AbstractPower power = new RitualPower(m, n, false); power.atEndOfRound(); AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(m, m, power, n)); }),
                new Evolution(StrengthPower.NAME, 20, (AbstractCreature m, Integer n) -> AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(m, m, new StrengthPower(m, n), n)))
        };
        Evolution[] o2 = new Evolution [] {
                new Evolution(BeatOfDeathPower.NAME, 1, (AbstractCreature m, Integer n) -> AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(m, m, new BeatOfDeathPower(m, n), n))),
                new Evolution(ThornsPower.NAME, 5, (AbstractCreature m, Integer n) -> AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(m, m, new ThornsPower(m, n), n))),
                //TODO i18n
                new Evolution("Burns", 5, (AbstractCreature m, Integer n) -> AbstractDungeon.actionManager.addToBottom(new MakeTempCardInDrawPileAction(new Burn(), n, true, true)))
        };
        Evolution[] o3 = new Evolution [] {
                new Evolution(BufferPower.NAME, 5, (AbstractCreature m, Integer n) -> AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(m, m, new BufferPower(m, n), n))),
                new Evolution(MetallicizePower.NAME, 20, (AbstractCreature m, Integer n) -> AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(m, m, new MetallicizePower(m, n), n))),
                //TODO i18n
                new Evolution("HP gain", 100, (AbstractCreature m, Integer n) -> AbstractDungeon.actionManager.addToBottom(new ChangeMaxHpAction(m, n, true)))
        };
        Evolution[] o4 = new Evolution [] {
                new Evolution(FrailPulsePower.NAME, 1, (AbstractCreature m, Integer n) -> AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(m, m, new FrailPulsePower(m, n), n))),
                new Evolution(DazedPulsePower.NAME, 1, (AbstractCreature m, Integer n) -> AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(m, m, new DazedPulsePower(m, n), n))),
                new Evolution(AbysstouchedPulsePower.NAME, 2, (AbstractCreature m, Integer n) -> AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(m, m, new AbysstouchedPulsePower(m, n), n)))
        };
        Evolution[] o5 = new Evolution [] {
                new Evolution(ArtifactPower.NAME, 5, (AbstractCreature m, Integer n) -> AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(m, m, new ArtifactPower(m, n), n))),
                new Evolution(SlimyBodyPower.NAME, 5, (AbstractCreature m, Integer n) -> AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(m, m, new SlimyBodyPower(m), n))),
                new Evolution(InvinciblePower.NAME, 50, (AbstractCreature m, Integer n) -> AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(m, m, new InvinciblePower(m, n), n)))
        };
        return Arrays.asList(Arrays.asList(o1), Arrays.asList(o2), Arrays.asList(o3), Arrays.asList(o4), Arrays.asList(o5));
    }

    private static class Evolution {
        public final String name;
        public final Integer amount;
        public final BiConsumer<AbstractCreature, Integer> apply;

        public Evolution(String name, Integer amount, BiConsumer<AbstractCreature, Integer> apply) {
            this.name = name;
            this.amount = amount;
            this.apply = apply;
        }

        public String getDescription() {
            //TODO handle more complex cases
            return name + " " + amount;
        }
    }

    static {
        powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
        NAME = powerStrings.NAME;
        DESCRIPTIONS = powerStrings.DESCRIPTIONS;
    }
}
