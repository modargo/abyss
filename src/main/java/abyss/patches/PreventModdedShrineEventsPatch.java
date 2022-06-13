package abyss.patches;

import abyss.act.AbyssAct;
import basemod.eventUtil.EventUtils;
import basemod.patches.com.megacrit.cardcrawl.dungeons.AbstractDungeon.AddEvents;
import com.evacipated.cardcrawl.modthespire.lib.SpireInstrumentPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import javassist.CannotCompileException;
import javassist.expr.ExprEditor;
import javassist.expr.FieldAccess;

import java.util.HashMap;

public class PreventModdedShrineEventsPatch {
    @SpirePatch(clz = AddEvents.NormalAndShrineEvents.class, method = "insert")
    @SpirePatch(clz = AddEvents.SaveAndLoadShrineEvents.class, method = "insert")
    public static class ShrineEvents {
        public static class ShrineEventsExprEditor extends ExprEditor {
            @Override
            public void edit(FieldAccess fieldAccess) throws CannotCompileException {
                if (fieldAccess.getClassName().equals(EventUtils.class.getName()) && fieldAccess.getFieldName().equals("shrineEvents")) {
                    fieldAccess.replace(String.format("{ $_ = %1$s.id.equals(%2$s.ID) ? new %3$s() : $proceed($$); }", AbstractDungeon.class.getName(), AbyssAct.class.getName(), HashMap.class.getName()));
                }
            }
        }

        @SpireInstrumentPatch
        public static ExprEditor shrineEvents() {
            return new ShrineEventsExprEditor();
        }
    }
}
