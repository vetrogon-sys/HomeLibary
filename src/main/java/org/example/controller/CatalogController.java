package org.example.controller;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.context.ApplicationContext;
import org.example.dto.LoginDto;
import org.example.dto.RegisterDto;
import org.example.entity.*;
import org.example.exception.CustomWrongDataException;
import org.example.service.BookService;
import org.example.service.UserService;
import org.example.service.impl.BookServiceImpl;
import org.example.service.impl.UserServiceImpl;

import java.util.List;
import java.util.Optional;
import java.util.Scanner;

@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CatalogController {
    private static final long PAGE_SIZE = 5;
    private long currentPageNum = 0;

    private BookService bookService = ApplicationContext.getInstance()
            .getObject(BookServiceImpl.class);
    private UserService userService = ApplicationContext.getInstance()
            .getObject(UserServiceImpl.class);

    private User currentUser;

    private void showMenu() {
        System.out.println("What you want to do:");
        if (currentUser != null) {
            System.out.println("1. Logout");
            System.out.println("2. Watch books");
            if (Role.ADMIN.equals(currentUser.getRole())) {
                System.out.println("3. Remove book");
                System.out.println("4. Add new book");
                System.out.println("5. Change book");
            } else {
                System.out.println("3. Suggest adding a book");
            }
            System.out.println("6. Find book");
            System.out.println("7. Send message");
        } else {
            System.out.println("1. Register");
            System.out.println("2. Login");
        }

        System.out.println("8. Exit");
    }

    private void showMessages() {
        List<Message> messageList = currentUser.getMessageList();
        System.out.println("Messages: {");
        for (Message message : messageList) {
            if (!message.isViewed()) {
                System.out.println("    " + message);
                message.setViewed(true);
            }
        }
        System.out.println("}");
        currentUser.setMessageList(messageList);
        userService.update(currentUser);
    }

    public void showBooks() {
        Page page = Page.builder()
                .number(currentPageNum)
                .size(PAGE_SIZE)
                .build();
        int move = 0;

        do {
            Scanner in = new Scanner(System.in);

            for (Optional<Book> book : bookService.getPageOfBook(page)) {
                book.ifPresent(System.out::println);
            }
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
        } while (move != 2);
    }

    private RegisterDto registerForm() {
        Scanner in = new Scanner(System.in);

        RegisterDto registerDto = new RegisterDto();
        //I now that need to check user input
        System.out.println("Enter login, using form:");
        System.out.println("example@example.com");
        String login = in.nextLine();
        while (login.matches("[a-zA-Z]@[a-zA-Z].[a-z]")) {
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
        loginDto.setPassword(in.nextLine());
        return loginDto;
    }

    public void start() {
        boolean fExit = false;
        do {
            if (currentUser != null) {
                if (currentUser.getMessageList() != null) {
                    showMessages();
                }
            }
            showMenu();
            Scanner in = new Scanner(System.in);
            int sw1;
            String input = in.nextLine();
            sw1 = Character.getNumericValue(input.charAt(0));

            while (currentUser == null
                    && (sw1 >= 3 && sw1 <= 7) ) {
                System.out.println("You need login or register");
                sw1 = in.nextInt();
            }
            switch (sw1) {
                case 1: {
                    if (currentUser != null) {
                        currentUser = null;
                    } else {
                        Optional<User> registeredUser = userService.save(registerForm());
                        if (registeredUser.isPresent()) {
                            currentUser = registeredUser.get();
                        } else {
                            System.out.println("Login is already in use");
                        }
                    }
                    break;
                }
                case 2: {
                    if (currentUser != null) {
                        showBooks();
                    } else {
                        try {
                            Optional<User> loggedUser = userService.login(loginForm());
                            if (loggedUser.isPresent()) {
                                currentUser = loggedUser.get();
                            } else {
                                System.out.println("Wrong login or password");
                            }
                        } catch (CustomWrongDataException e) {
                            System.out.println(e.getMessage());
                        }
                    }
                    break;
                }
                case 3: {
                    if (Role.ADMIN.equals(currentUser.getRole())) {
                        System.out.println("Enter book name");
                        String bookName = in.nextLine();
                        Book book = new Book();
                        book.setName(bookName);
                        if (bookService.remove(book)) {
                            System.out.println("Book was deleted");
                        } else {
                            System.out.println("Something went wrong.\nBook was't deleted");
                        }
                    } else {
                        System.out.println("Enter name of book you want to add");
                        Message message = Message.builder()
                                .text(in.nextLine())
                                .sender(currentUser)
                                .build();
                        userService.sendMessageToAdmins(message);

                        System.out.println("Sent for review");
                    }
                    break;
                }
                case 4: {
                    if (Role.ADMIN.equals(currentUser.getRole())) {
                        System.out.println("Enter book name");
                        Book book = Book.builder()
                                .name(in.nextLine())
                                .build();
                        if (bookService.save(book)) {
                            System.out.println("Book was saved");
                        } else {
                            System.out.println("Something went wrong.\nBook was't saved");
                        }
                    }
                    break;
                }
                case 5: {
                    if (Role.ADMIN.equals(currentUser.getRole())) {
                        System.out.println("Enter book name");
                        Book book;
                        book = bookService.findByName(in.nextLine()).orElse(null);

                        if (book != null) {
                            System.out.println("Enter book description");
                            book.setDescription(in.nextLine());
                            if (bookService.update(book)) {
                                System.out.println("Information about book was updated");
                            } else {
                                System.out.println("Something went wrong.\nInformation about book was't updated");
                            }
                        }
                    }
                    break;
                }
                case 6: {
                    System.out.println("Enter part of book name");
                    System.out.println(bookService.findByName(in.nextLine()));
                    break;
                }
                case 7: {
                    System.out.println("Enter name of user to send message");
                    String userName = in.nextLine();
                    System.out.println("Enter message");
                    Message message = Message.builder()
                            .text(in.nextLine())
                            .sender(currentUser)
                            .build();
                    userService.sendMessage(userName, message);
                    break;
                }
                case 8: {
                    fExit = true;
                    if (currentUser != null) {
                        userService.update(currentUser);
                    }
                    break;
                }
                default: {
                    System.out.println("You need to enter value from list");
                    break;
                }
            }

        } while (!fExit);
    }
}
