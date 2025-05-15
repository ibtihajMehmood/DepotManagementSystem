package model;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class Log {

    private static Log instance;
    private PrintWriter writer;

    public Log() {
        try {
            // Opens the file and does not append, the file is cleared
            writer = new PrintWriter(new FileWriter("src/view/LogReport.txt", false));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //Create the setter instance
    public static Log getInstance() {
        if (instance == null) {
            instance = new Log();
        }
        return instance;
    }

    // Actions of the user 
    public void logAction(String actionMessage) {
        writeLog("Action", actionMessage);
    }

    // Errors actions of the user
    public void logError(String errorMessage) {
        writeLog("!ERROR!", errorMessage);
    }

    // Writes it in the file
    private void writeLog(String logType, String message) {
        if (writer != null) {
            writer.println(logType + ": " + message);
            writer.flush();
        }
    }
}
