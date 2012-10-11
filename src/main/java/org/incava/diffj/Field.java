package org.incava.diffj;

import java.util.List;
import net.sourceforge.pmd.ast.ASTFieldDeclaration;
import net.sourceforge.pmd.ast.ASTType;
import net.sourceforge.pmd.ast.ASTVariableDeclarator;
import net.sourceforge.pmd.ast.SimpleNode;
import org.incava.pmdx.SimpleNodeUtil;

public class Field extends Item {
    private final ASTFieldDeclaration field;

    public Field(ASTFieldDeclaration field) {
        super(field);
        this.field = field;
    }

    public void diff(Field toField, Differences differences) {
        compareAccess(toField, differences);
        compareModifiers(toField, differences);
        compareVariables(toField, differences);
    }

    protected void compareModifiers(Field toField, Differences differences) {
        SimpleNode fromParent = getParent();
        SimpleNode toParent = toField.getParent();
        FieldModifiers fromMods = new FieldModifiers(fromParent);
        FieldModifiers toMods = new FieldModifiers(toParent);
        fromMods.diff(toMods, differences);
    }

    protected ASTType getType() {
        return (ASTType)SimpleNodeUtil.findChild(field, "net.sourceforge.pmd.ast.ASTType");
    }

    protected List<ASTVariableDeclarator> getVariables() {
        return SimpleNodeUtil.snatchChildren(field, "net.sourceforge.pmd.ast.ASTVariableDeclarator");
    }

    protected void compareVariables(Field toField, Differences differences) {
        Variables fromVariables = new Variables(getType(), getVariables());
        Variables toVariables = new Variables(toField.getType(), toField.getVariables());
        fromVariables.diff(toVariables, differences);
    }
}
