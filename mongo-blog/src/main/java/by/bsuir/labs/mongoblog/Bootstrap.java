package by.bsuir.labs.mongoblog;

import by.bsuir.labs.mongoblog.controller.BlogController;

import java.io.IOException;

public class Bootstrap {
    public static void main(String[] args) throws IOException {
        if (args.length == 0) {
            new BlogController("mongodb://localhost");
        } else {
            new BlogController(args[0]);
        }
    }
}