package com.headstartech.sermo;

/**
 * @author Per Johansson
 */
public class AccountDetailState extends USSDStateAdapter {

    @Override
    public String getId() {
        return "ACCOUNT_DETAIL";
    }

    @Override
    public String onEntry(USSDSupport ussdSupport) {
        System.out.println("item key " + ussdSupport.getItemKey());
        return null;
    }
}
