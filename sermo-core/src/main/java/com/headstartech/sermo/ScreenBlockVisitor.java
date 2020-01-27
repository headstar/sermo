package com.headstartech.sermo;

/**
 * @author Per Johansson
 */
public interface ScreenBlockVisitor {

    void visit(EmptyLine emptyLine);

    void visit(Text text);

    void visit(MenuGroup menuGroup);

    void visit(StaticMenuItem staticMenuItem);
}
