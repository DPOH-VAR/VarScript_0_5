package me.dpohvar.varscript.vs.compiler;

import me.dpohvar.varscript.vs.CommandDebug;
import me.dpohvar.varscript.vs.SimpleWorker;
import me.dpohvar.varscript.vs.Worker;
import me.dpohvar.varscript.vs.exception.SourceException;
import org.apache.commons.lang.StringUtils;
import org.bukkit.ChatColor;

/**
 * Created with IntelliJ IDEA.
 * User: DPOH-VAR
 * Date: 25.06.13
 * Time: 19:54
 */
public class SimpleCompileRule implements CompileRule {
    private final String name;
    private final String[] aliases;
    private final String input;
    private final String output;
    private final String[] tags;
    private final String description;
    private final SimpleWorker worker;

    @Override
    public String toString() {
        return name;
    }

    public SimpleCompileRule(
            String name,
            String aliases,
            String input,
            String output,
            String tags,
            String description,
            SimpleWorker worker
    ) {
        this.name = name;
        this.aliases = aliases.split(" ");
        this.input = input;
        this.output = output;
        this.tags = tags.split(" ");
        this.description = description;
        this.worker = worker;
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
                .append(ChatColor.YELLOW).append("alias: ").append(ChatColor.WHITE).append(StringUtils.join(aliases, ' ')).append('\n')
                .append(ChatColor.YELLOW).append("tags: ").append(ChatColor.WHITE).append(StringUtils.join(tags, ' ')).append('\n')
                .append(ChatColor.YELLOW).append("input: ").append(ChatColor.WHITE).append(input).append('\n')
                .append(ChatColor.YELLOW).append("output: ").append(ChatColor.WHITE).append(output).append('\n')
                .append(ChatColor.WHITE).append(ChatColor.WHITE).append(description);
        return builder.toString();
    }

    @Override
    public boolean checkCondition(String string) {
        for (String s : aliases) if (s.equals(string)) return true;
        return false;
    }

    @Override
    public void apply(VSSmartParser.ParsedOperand operand, VSCompiler.FunctionSession functionSession, VSCompiler.CompileSession compileSession) throws SourceException {
        functionSession.addCommand(new CommandDebug<Void>(worker, null, compileSession.getSource(), operand));
    }

    @Override
    public Worker[] getNewWorkersWithRules() {
        return new Worker[]{worker};
    }
}
