package com.squeed.guice.example.standalone;

import com.google.inject.Inject;
import com.squeed.guice.example.business.BusinessLogic;
import com.squeed.guice.example.business.Transaction;
import com.squeed.guice.example.standalone.guice.StandaloneGuiceModule;

public class StandaloneApplicationMain {

    @Inject
    private BusinessLogic businessLogic;

    @Inject
    private Transaction transaction;

    public static void main(String[] args) throws InterruptedException {
        for (int i = 0; i < 5; i++) {
            new Thread() {
                @Override
                public void run() {
                    StandaloneApplicationMain standaloneApplicationMain = StandaloneGuiceModule.getInjector().getInstance(StandaloneApplicationMain.class);
                    standaloneApplicationMain.run();
                }
            }.start();
            Thread.sleep(500);
        }
    }

    public void run() {
        System.out.println(businessLogic.toString());
        System.out.println(transaction.toString());
        System.out.println();
    }
}
