package com.headstartech.sermo;

/**
 * @author Per Johansson
 */
public class DefaultScreenRenderer implements ScreenRenderer {

    private InputMap inputMap = new InputMap();
    private StringBuilder sb = new StringBuilder();

    @Override
    public InputMap getInputMap() {
        return inputMap;
    }

    @Override
    public String getScreenOutput() {
        return sb.toString();
    }

    @Override
    public void visit(EmptyLine emptyLine) {
        sb.append("\n");
    }

    @Override
    public void visit(Text text) {
        sb.append(text.getText())
                .append("\n");
    }

    @Override
    public void visit(MenuGroup menuGroup) {
        int i = 1;
        for (MenuItem menuItem : menuGroup.getMenuItems()) {
            String input = String.format("%d", i);
            sb.append(String.format("%s. %s\n", input, menuItem.getLabel()));
            inputMap.addMapping(input, menuItem.getTransition(), menuItem.getItemData());
            ++i;
        }
    }

    @Override
    public void visit(StaticMenuItem staticMenuItem) {
        sb.append(String.format("%s %s\n", staticMenuItem.getInput(), staticMenuItem.getLabel()));
        inputMap.addMapping(staticMenuItem.getInput(), staticMenuItem.getTransition(), staticMenuItem.getItemData());
    }
}
