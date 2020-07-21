package org.example.controller;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.dto.LoginDto;
import org.example.dto.RegisterDto;
import org.example.entity.Book;
import org.example.entity.Page;
import org.example.entity.Role;
import org.example.entity.User;
import org.example.exception.CustomIOException;
import org.example.exception.CustomWrongDataException;
import org.example.service.BookService;
import org.example.service.UserService;
import org.example.service.impl.BookServiceImpl;
import org.example.service.impl.UserServiceImpl;

import java.util.Scanner;

@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CatalogController {
    private static final long PAGE_SIZE = 5;
    private long currentPageNum = 0;

    private BookService bookService = new BookServiceImpl();
    private UserService userService = new UserServiceImpl();
    private User currentUser;

    private void showMenu() {
        System.out.println("What you want to do:");
        if (currentUser != null) {
            System.out.println("1. Logout");
            System.out.println("2. Watch books");
            if (Role.ADMIN.equals(currentUser.getRole())) {
                System.out.println("3. Remove book");
                System.out.println("4. Add new book");
            } else {
                System.out.println("3. Suggest adding a book");
            }
            System.out.println("5. Find book");
        } else {
            System.out.println("1. Register");
            System.out.println("2. Login");
        }

        System.out.println("6. Exit");
    }

    private void showBooks() {
        Scanner in = new Scanner(System.in);
        Page page = Page.builder()
                .number(currentPageNum)
                .size(PAGE_SIZE)
                .build();
        int move = 0;

        do {
            try {
                System.out.println(bookService.getPageOfBook(page));

                System.out.println("1.Previous    2.Back    3.Next");
                move = in.nextInt();

                while (move > 3 || move < 1) {
                    move = in.nextInt();
                }

                if (move == 1) {
                    page.previousPage();
                } else if (move == 3) {
                    page.nextPage();
                }
            } catch (CustomIOException e) {
                e.printStackTrace();
            }
        } while (move != 2);

        in.close();
        start();
    }

    private RegisterDto registerForm() {
        Scanner in = new Scanner(System.in);
        RegisterDto registerDto = new RegisterDto();
        //I now that need to check user input
        System.out.println("Enter login, using form:");
        System.out.println("example@example.com");
        String login = in.nextLine();
        while (login.matches("[a-zA-Z]@[a-zA-Z].[a-zA-Z]")) {
            System.out.println("Login does not match");
            System.out.println("Try again");
            login = in.nextLine();
        }
        registerDto.setLogin(login);
        System.out.println("Enter password");
        registerDto.setPassword(in.nextLine());

        System.out.println("Select role");
        System.out.println("1. Admin");
        System.out.println("2. User");

        int roleNun = in.nextInt();
        Role role;
        if (roleNun == 1) {
            role = Role.ADMIN;
        } else {
            role = Role.USER;
        }
        registerDto.setRole(role);

        return registerDto;
    }

    private LoginDto loginForm() {
        Scanner in = new Scanner(System.in);
        LoginDto loginDto = new LoginDto();
        //I now that need to check user input
        System.out.println("Enter login:");
        loginDto.setLogin(in.nextLine());
        System.out.println("Enter password");
        loginDto.setLogin(in.nextLine());
        return loginDto;
    }

    public void start() {
        boolean fExit = false;
        Scanner in = new Scanner(System.in);
        do {
            showMenu();
            int sw1 = in.nextInt();
            switch (sw1) {
                case 1: {
                    if (currentUser != null) {
                        currentUser = null;
                    } else {
                        try {
                            currentUser = userService.save(registerForm()).orElse(null);
                        } catch (CustomIOException e) {
                            System.out.println(e.getMessage());
                        }
                    }
                }
                case 2: {
                    if (currentUser != null) {
                        showBooks();
                    } else {
                        try {
                            currentUser = userService.login(loginForm()).orElse(null);
                        } catch (CustomWrongDataException | CustomIOException e) {
                            System.out.println(e.getMessage());
                        }
                    }
                }
                case 3: {
                    if (currentUser != null && Role.ADMIN.equals(currentUser.getRole())) {
                        System.out.println("Enter book name");
                        Book book = new Book(in.nextLine());
                        try {
                            bookService.remove(book);
                        } catch (CustomIOException e) {
                            System.out.println(e.getMessage());
                        }
                    } else {
                        //TODO: add opportunity to send request adding book
                    }

                }
                case 4: {
                    if (currentUser != null && Role.ADMIN.equals(currentUser.getRole())) {
                        System.out.println("Enter book name");
                        Book book = new Book(in.nextLine());
                        try {
                            bookService.save(book);
                        } catch (CustomIOException e) {
                            System.out.println(e.getMessage());
                        }
                    }
                }
                case 5: {
                    System.out.println("Enter part of book name");
                    try {
                        System.out.println(bookService.findByPrefix(in.nextLine()));
                    } catch (CustomIOException e) {
                        System.out.println(e.getMessage());
                    }
                }
                case 6: {
                    fExit = true;
                }
            }

        } while (!fExit);

        in.close();
    }
}
