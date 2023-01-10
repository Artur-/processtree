package com.example.application;

import java.lang.ProcessHandle.Info;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import com.vaadin.open.OSUtils;

public class Application {

    public static void main(String[] args) {
        System.out.println("isMac? " + OSUtils.isMac());
        System.out.println("isLinux? " + OSUtils.isLinux());
        System.out.println("isWindows? " + OSUtils.isWindows());
        System.out.println("isWsl? " + OSUtils.isWsl());
        printProcessTree(System.out::println);
    }

    private static void printProcessTree(Consumer<String> printer) {
        printer.accept("Process tree:");
        for (Info info : getProcessTree()) {
            info.command().ifPresent(
                    value -> printer.accept("String cmd = \"" + value + "\";"));
            info.arguments().ifPresent(values -> {
                printer.accept("String[] args = new String[]{");
                for (int i = 0; i < values.length; i++) {
                    printer.accept("\"" + values[i].replace("\"", "\\\"") + "\",");
                }
                printer.accept("};");
                info.commandLine().ifPresent(
                        value -> printer.accept("Command line: " + value));
            });
            printer.accept("");
        }

    }

    private static List<Info> getProcessTree() {
        return getParentProcesses().stream().map(p -> p.info())
                .collect(Collectors.toList());
    }

    private static List<ProcessHandle> getParentProcesses() {
        List<ProcessHandle> proceses = new ArrayList<>();
        ProcessHandle p = ProcessHandle.current();
        while (p != null) {
            proceses.add(p);
            p = p.parent().orElse(null);
        }
        return proceses;
    }

}
