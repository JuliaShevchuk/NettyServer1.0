package command;


import java.util.HashMap;

/**
 * Created by Julia on 26.05.2015.
 */
public class CommandHelper {
    private static CommandHelper instance = null;
    private HashMap<String, Command> commands = new HashMap<String, Command>();
    private Command command;

    private CommandHelper() {
        commands.put("HELLO", new CommandHello());
        commands.put("REDIRECT", new CommandRedirect());
        commands.put("STATUS", new CommandStatus());
        commands.put("MISSING", new CommandMissing());

    }

    public synchronized Command getCommand(String key) {
        command = commands.get(key);

        if (command == null) {
            command = new CommandMissing();
        }

        return command;

    }

    public synchronized static CommandHelper getInstance() {
        if (instance == null) {
            instance = new CommandHelper();
        }
        return instance;
    }
}