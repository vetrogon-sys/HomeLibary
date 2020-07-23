package org.example;

import org.example.context.ApplicationContext;
import org.example.controller.CatalogController;

public class Main {
    public static void main(String[] args) {
        CatalogController catalogController = ApplicationContext.getInstance()
                .getObject(CatalogController.class);

        try {
            catalogController.start();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
