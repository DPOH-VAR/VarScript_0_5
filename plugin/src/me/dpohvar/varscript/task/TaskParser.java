package me.dpohvar.varscript.task;

import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: DPOH-VAR
 * Date: 21.11.13
 * Time: 3:26
 */
public class TaskParser {

    private static class ParseResult {
        public List<Object> args = new ArrayList<Object>();
        public Map<String, Object> params = new HashMap<String, Object>();
    }

    private static enum ParseMode {
        SPACE, // возможно лишний пробел
        ARG, // обычный аргумент
        QARG, // аргумент с кавычками
        ASPACE, // возможны пробелы после аргумента
        WSPACE, // возможны пробелы после знака =
        VAL, // значение
        QVAL, // значение в кавычках
    }

    public static LinkedHashMap<String, String> parseMap(String input) {
        LinkedHashMap<String, String> result = new LinkedHashMap<String, String>();
        StringBuilder arg = new StringBuilder();
        StringBuilder val = new StringBuilder();
        Queue<Character> chars = new LinkedList<Character>();
        for (char c : input.toCharArray()) chars.add(c);
        ParseMode mode = ParseMode.SPACE;

        chars:
        while (!chars.isEmpty()) {
            char c = chars.poll();
            switch (mode) {

                case SPACE: {
                    if (c <= 32) {
                        continue chars;
                    } else if (c == '"') {
                        mode = ParseMode.QARG;
                        arg.append('"');
                    } else {
                        mode = ParseMode.ARG;
                        arg.append(c);
                    }
                    continue chars;
                }

                case QARG: {
                    if (c == '\\') {
                        arg.append('\\');
                        char t = chars.poll();
                        if (t == 'u') {
                            arg.append('u');
                            for (int i = 0; i < 4; i++) arg.append(chars.poll());
                        } else {
                            arg.append(t);
                        }
                    } else if (c == '"') {
                        arg.append('"');
                        mode = ParseMode.ASPACE;
                    } else {
                        arg.append(c);
                    }
                    continue chars;
                }

                case ARG: {
                    if (c <= 32) {
                        mode = ParseMode.ASPACE;
                    } else if (c == '=') {
                        mode = ParseMode.WSPACE;
                    } else {
                        arg.append(c);
                    }
                    continue chars;
                }

                case ASPACE: {
                    if (c <= 32) {
                        continue chars;
                    } else if (c == '=') {
                        mode = ParseMode.WSPACE;
                    } else if (c == '"') {
                        result.put(arg.toString(), null);
                        arg = new StringBuilder();
                        arg.append('"');
                        mode = ParseMode.QARG;
                    } else {
                        result.put(arg.toString(), null);
                        arg = new StringBuilder();
                        arg.append(c);
                        mode = ParseMode.ARG;
                    }
                    continue chars;
                }

                case WSPACE: {
                    if (c <= 32) {
                        continue chars;
                    } else if (c == '"') {
                        val.append('"');
                        mode = ParseMode.QVAL;
                    } else {
                        val.append(c);
                        mode = ParseMode.VAL;
                    }
                    continue chars;
                }

                case VAL: {
                    if (c <= 32) {
                        result.put(arg.toString(), val.toString());
                        arg = new StringBuilder();
                        val = new StringBuilder();
                        mode = ParseMode.SPACE;
                    } else {
                        val.append(c);
                    }
                    continue chars;
                }

                case QVAL: {
                    if (c == '\\') {
                        val.append('\\');
                        char t = chars.poll();
                        if (t == 'u') {
                            val.append('u');
                            for (int i = 0; i < 4; i++) val.append(chars.poll());
                        } else {
                            val.append(t);
                        }
                    } else if (c == '"') {
                        val.append('"');
                        result.put(arg.toString(), val.toString());
                        arg = new StringBuilder();
                        val = new StringBuilder();
                        mode = ParseMode.SPACE;
                    } else {
                        val.append(c);
                    }
                    continue chars;
                }

                default:
                    break chars;
            }
        }

        switch (mode) {
            case QARG:
                result.put(arg.append('"').toString(), null);
                break;
            case ARG:
                result.put(arg.toString(), null);
                break;
            case ASPACE:
                result.put(arg.toString(), null);
                break;
            case WSPACE:
                result.put(arg.toString(), "");
                break;
            case VAL:
                result.put(arg.toString(), val.toString());
                break;
            case QVAL:
                result.put(arg.toString(), val.append('"').toString());
                break;
        }

        return result;
    }


    public static List<String> parseList(String input) {
        List<String> result = new ArrayList<String>();
        StringBuilder arg = new StringBuilder();
        Queue<Character> chars = new LinkedList<Character>();
        for (char c : input.toCharArray()) chars.add(c);
        ParseMode mode = ParseMode.SPACE;

        chars:
        while (!chars.isEmpty()) {
            char c = chars.poll();
            switch (mode) {

                case SPACE: {
                    if (c <= 32) {
                        continue chars;
                    } else if (c == '"') {
                        mode = ParseMode.QARG;
                        arg.append('"');
                    } else {
                        mode = ParseMode.ARG;
                        arg.append(c);
                    }
                    continue chars;
                }

                case QARG: {
                    if (c == '\\') {
                        arg.append('\\');
                        char t = chars.poll();
                        if (t == 'u') {
                            arg.append('u');
                            for (int i = 0; i < 4; i++) arg.append(chars.poll());
                        } else {
                            arg.append(t);
                        }
                    } else if (c == '"') {
                        arg.append('"');
                        result.add(arg.toString());
                        arg = new StringBuilder();
                        mode = ParseMode.SPACE;
                    } else {
                        arg.append(c);
                    }
                    continue chars;
                }

                case ARG: {
                    if (c <= 32) {
                        result.add(arg.toString());
                        arg = new StringBuilder();
                        mode = ParseMode.SPACE;
                    } else {
                        arg.append(c);
                    }
                    continue chars;
                }

                default:
                    break chars;
            }
        }

        switch (mode) {
            case QARG:
                result.add(arg.append('"').toString());
                break;
            case ARG:
                result.add(arg.toString());
                break;
        }

        return result;
    }
}
