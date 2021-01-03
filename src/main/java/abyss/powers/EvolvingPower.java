package abyss.powers;

import abyss.Abyss;
import abyss.actions.ChangeMaxHpAction;
import abyss.actions.CustomTextChooseOneAction;
import abyss.cards.EvolutionChoice;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInDrawPileAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.status.Burn;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.*;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;

public class EvolvingPower extends AbstractPower {
    public static final String POWER_ID = "Abyss:Evolving";
    private static final Map<String, String> evolutionStrings;
    private static final String[] evolutionNames;
    private static final String evolutionText;
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
        ArrayList<AbstractCard> options = new ArrayList<>();
        for (Evolution evolution : evolutionChoice) {
            options.add(new EvolutionChoice(evolution.name, evolution.getDescription(), () -> evolution.apply.accept(this.owner, evolution.amount)));
        }
        AbstractDungeon.actionManager.addToTop(new CustomTextChooseOneAction(options, evolutionText));
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
                new Evolution(AngerPower.NAME, evolutionStrings.get(AngerPower.POWER_ID), 2, (AbstractCreature m, Integer n) -> AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(m, m, new AngerPower(m, n), n))),
                new Evolution(RitualPower.NAME,5, (AbstractCreature m, Integer n) -> { AbstractPower power = new RitualPower(m, n, false); power.atEndOfRound(); AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(m, m, power, n)); }),
                new Evolution(StrengthPower.NAME, 20, (AbstractCreature m, Integer n) -> AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(m, m, new StrengthPower(m, n), n)))
        };
        Evolution[] o2 = new Evolution [] {
                new Evolution(BeatOfDeathPower.NAME, evolutionStrings.get(BeatOfDeathPower.POWER_ID), 1, (AbstractCreature m, Integer n) -> AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(m, m, new BeatOfDeathPower(m, n), n))),
                new Evolution(ThornsPower.NAME, 4, (AbstractCreature m, Integer n) -> AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(m, m, new ThornsPower(m, n), n))),
                new Evolution(evolutionNames[0], 4, (AbstractCreature m, Integer n) -> AbstractDungeon.actionManager.addToBottom(new MakeTempCardInDrawPileAction(new Burn(), n, true, true)))
        };
        Evolution[] o3 = new Evolution [] {
                new Evolution(BufferPower.NAME, evolutionStrings.get(BufferPower.POWER_ID), 5, (AbstractCreature m, Integer n) -> AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(m, m, new BufferPower(m, n), n))),
                new Evolution(MetallicizePower.NAME, evolutionStrings.get(MetallicizePower.POWER_ID), 25, (AbstractCreature m, Integer n) -> AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(m, m, new MetallicizePower(m, n), n))),
                new Evolution(evolutionNames[1], 100, (AbstractCreature m, Integer n) -> AbstractDungeon.actionManager.addToBottom(new ChangeMaxHpAction(m, n, true)))
        };
        Evolution[] o4 = new Evolution [] {
                new Evolution(FrailPulsePower.NAME, evolutionStrings.get(FrailPulsePower.POWER_ID), 1, (AbstractCreature m, Integer n) -> AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(m, m, new FrailPulsePower(m)))),
                new Evolution(DazedPulsePower.NAME, evolutionStrings.get(DazedPulsePower.POWER_ID), 1, (AbstractCreature m, Integer n) -> AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(m, m, new DazedPulsePower(m)))),
                new Evolution(AbysstouchedPulsePower.NAME, evolutionStrings.get(AbysstouchedPulsePower.POWER_ID), 2, (AbstractCreature m, Integer n) -> AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(m, m, new AbysstouchedPulsePower(m, n), n)))
        };
        Evolution[] o5 = new Evolution [] {
                new Evolution(ArtifactPower.NAME, 5, (AbstractCreature m, Integer n) -> AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(m, m, new ArtifactPower(m, n), n))),
                new Evolution(SlimyBodyPower.NAME, evolutionStrings.get(SlimyBodyPower.POWER_ID), 1, (AbstractCreature m, Integer n) -> AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(m, m, new SlimyBodyPower(m), n))),
                new Evolution(InvinciblePower.NAME, evolutionStrings.get(InvinciblePower.POWER_ID), 50, (AbstractCreature m, Integer n) -> AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(m, m, new InvinciblePower(m, n), n)))
        };
        return Arrays.asList(Arrays.asList(o1), Arrays.asList(o2), Arrays.asList(o3), Arrays.asList(o4), Arrays.asList(o5));
    }

    private static class Evolution {
        public final String name;
        public final String text;
        public final Integer amount;
        public final BiConsumer<AbstractCreature, Integer> apply;

        public Evolution(String name, Integer amount, BiConsumer<AbstractCreature, Integer> apply) {
            this(name, name + " {0}", amount, apply);
        }

        public Evolution(String name, String text, Integer amount, BiConsumer<AbstractCreature, Integer> apply) {
            this.name = name;
            this.text = text;
            this.amount = amount;
            this.apply = apply;
        }

        public String getDescription() {
            return MessageFormat.format(text, this.amount);
        }
    }

    static {
        evolutionStrings = CardCrawlGame.languagePack.getUIString(POWER_ID).TEXT_DICT;
        evolutionNames = CardCrawlGame.languagePack.getUIString(POWER_ID).EXTRA_TEXT;
        evolutionText = CardCrawlGame.languagePack.getUIString(POWER_ID).TEXT[0];
        powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
        NAME = powerStrings.NAME;
        DESCRIPTIONS = powerStrings.DESCRIPTIONS;
    }
}
