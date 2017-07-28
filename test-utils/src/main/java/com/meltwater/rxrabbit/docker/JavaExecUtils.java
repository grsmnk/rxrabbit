package com.meltwater.rxrabbit.docker;

import com.meltwater.rxrabbit.util.Logger;
import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.function.Function;

public class JavaExecUtils {
    private static final Logger LOGGER = new Logger(JavaExecUtils.class);

    public static String execLocal(String command, HashMap<String, String> env) throws IOException, InterruptedException {
        try {
            List<String> commands = new ArrayList<>();
            commands.add("sh");
            commands.add("-c");
            commands.add(command);

            ProcessBuilder pb = new ProcessBuilder(commands);
            pb.environment().putAll(env);
            LOGGER.infoWithParams("Running: ${pb.command()} \n with env $env", "command", pb.command(), "env", env);

            Process process = pb.start();
            String result = IOUtils.toString(process.getInputStream());
            String errors = IOUtils.toString(process.getErrorStream());

            if (process.waitFor() != 0) {
                LOGGER.infoWithParams("Failed to execute command",
                        "command", pb.command(),
                        "errors", errors,
                        "result", result);
                throw new RuntimeException(errors);
            } else {
                LOGGER.infoWithParams("Executed command", "result", result);
            }
            return result;
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

}
