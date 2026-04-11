package com.aedn.cli;

import java.io.Console;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.aedn.exception.UserCreationException;
import com.aedn.service.AuthService;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class CreateAdminCommand implements CommandLineRunner {

    private final AuthService authService;

    @Override
    public void run(String... args) {

        if (args.length == 0 || !args[0].equals("createAdmin")) {
            return;
        }

        Console console = System.console();
        if (console == null) {
            System.out.println("No console available.");
            return;
        }


        System.out.println("=== Create Superuser ===");

        String username = console.readLine("Username: ");
        String email = console.readLine("Email: ");

        String password = new String(console.readPassword("Password: "));
        String confirm = new String(console.readPassword("Confirm Password: "));

        if (!password.equals(confirm)) {
            System.out.println("Passwords do not match.");
            return;
        }

        try {
            authService.createAdmin(username, email, password);
            System.out.println("Admin user created successfully. We send you email verification, please verify your email first");
        } catch (UserCreationException e) {
            System.out.println(e.getMessage());
        } catch (Exception e) {
            System.out.println("Unexpected error: " + e.getMessage());
        }

        System.exit(0);
    }
}
