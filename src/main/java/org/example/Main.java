package org.example;

import org.example.context.ApplicationContext;
import org.example.controller.CatalogController;

public class Main {
    public static void main(String[] args) {
        ApplicationContext context = ApplicationContext.getContext();
        CatalogController catalogController = CatalogController.builder()
                .bookService(context.getBookService())
                .userService(context.getUserService())
                .build();

        try {
            catalogController.start();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            context.clear();
        }


        //Tests

//        UserService userService = context.getUserService();
//
//        try {
//            userService.save(RegisterDto.builder()
//                    .login("asd")
//                    .password("asd")
//                    .role(Role.ADMIN)
//                    .build());
//            System.out.println("was saved");
//
//            userService.findByLogin("asd");
//            System.out.println("There is in system");
//            System.out.println(userService.findByLogin("asd").get());
//        } catch (CustomIOException e) {
//            e.printStackTrace();
//        }
    }
}
