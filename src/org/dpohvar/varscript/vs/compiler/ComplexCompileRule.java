package org.dpohvar.varscript.vs.compiler;

import org.apache.commons.lang.StringUtils;
import org.bukkit.ChatColor;
import org.dpohvar.varscript.vs.Worker;

/**
 * Created with IntelliJ IDEA.
 * User: DPOH-VAR
 * Date: 25.06.13
 * Time: 19:54
 */
public abstract class ComplexCompileRule implements CompileRule {
    private final String name;
    private final String input;
    private final String output;
    private final String description;
    private final String[] tags;

    @Override
    public String toString() {
        return name;
    }

    public ComplexCompileRule(
            String name,
            String tags,
            String input,
            String output,
            String description
    ) {
        this.name = name;
        this.tags = tags.split(" ");
        this.input = input;
        this.output = output;
        this.description = description;
    }

    @Override
    public String[] getTags() {
        return tags;
    }

    @Override
    public String getDescription() {
        StringBuilder builder = new StringBuilder();
        builder
                .append(ChatColor.YELLOW).append(ChatColor.BOLD).append(name).append('\n')
                .append(ChatColor.YELLOW).append("tags: ").append(ChatColor.WHITE).append(StringUtils.join(tags, ' ')).append('\n')
                .append(ChatColor.YELLOW).append("input: ").append(ChatColor.WHITE).append(input).append('\n')
                .append(ChatColor.YELLOW).append("output: ").append(ChatColor.WHITE).append(output).append('\n')
                .append(ChatColor.WHITE).append(ChatColor.WHITE).append(description);
        return builder.toString();
    }

    @Override
    public Worker[] getNewWorkersWithRules() {
        return null;
    }
}
