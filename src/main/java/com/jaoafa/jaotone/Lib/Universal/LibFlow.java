package com.jaoafa.jaotone.Lib.Universal;

import static com.jaoafa.jaotone.Lib.Universal.LibTerminalColor.*;

public class LibFlow {
    public static void print(Type type, String index) {
        log(Thread.currentThread().getName(), type, index);
    }

    public static void print(String taskName, Type type, String index) {
        log(taskName, type, index);
    }

    private static void log(String name, Type type, String index) {
        System.out.printf("|%s| [%s]> %s%n", type.getSymbol(), name, index);
    }

    public enum Type {
        Task(BLUE + ">" + RESET),
        Success(GREEN_BRIGHT + "+" + RESET),
        Failure(RED_BRIGHT + "#" + RESET),
        Debug(BLACK_BRIGHT + "=" + RESET);

        private final String symbol;

        Type(final String symbol) {
            this.symbol = symbol;
        }

        public String getSymbol() {
            return symbol;
        }
    }
}
