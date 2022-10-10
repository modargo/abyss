package abyss.patches;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireInstrumentPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.screens.GameOverStat;
import javassist.CannotCompileException;
import javassist.expr.ExprEditor;
import javassist.expr.MethodCall;

@SpirePatch(
        clz = GameOverStat.class,
        method = "render",
        paramtypez = {SpriteBatch.class, float.class, float.class}
)
public class RenderGameOverStatsPatch {
    public static class RenderGameOverStatsPatchExprEditor extends ExprEditor {
        @Override
        public void edit(MethodCall methodCall) throws CannotCompileException {
            if (methodCall.getClassName().equals(FontHelper.class.getName()) && methodCall.getMethodName().equals("renderFontLeftTopAligned")) {
                methodCall.replace(String.format("{ $2 = this.label.length() >= 30 ? %1$s.tipHeaderFont : $2; $_ = $proceed($$); }", FontHelper.class.getName()));
            }
        }
    }

    @SpireInstrumentPatch
    public static ExprEditor renderGameOverStatsPatch() {
        return new RenderGameOverStatsPatchExprEditor();
    }
}
