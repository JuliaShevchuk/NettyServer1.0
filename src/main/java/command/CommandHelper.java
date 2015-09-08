package command;


import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ConcurrentSkipListMap;

/**
 * Created by Julia on 26.05.2015.
 */
public class CommandHelper {


    private volatile static CommandHelper instance;
    private ConcurrentMap<String, Command> commands;
    private volatile Command command;

    private CommandHelper() {

        commands = new ConcurrentSkipListMap();

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

    public static CommandHelper getInstance() {
        if (instance == null) {
            synchronized (CommandHelper.class) {
                if (instance == null) {
                    instance = new CommandHelper();
                }
            }
        }
        return instance;
    }
}