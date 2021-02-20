package com.feup.nemiprotouno;
// ----------------------------------------------------------------------------
//
//  NEMMI is developed by Alexandre Clément at the University of Porto's Faculty of Engineering
//  Released under GNU Affero General Public License version 3
//  https://opensource.org/licenses/AGPL-3.0
//
// ----------------------------------------------------------------------------
//
//  LogManager
//  Class responsible for data logging
//  Usage: LogManager(context, log file name, optional single log boolean)
//  Single log means the app is supposed to have only one logfile at any time
//  And overwrite it with each logging session
//  Otherwise it will generate a new log file each time, and timecode it
//
// ----------------------------------------------------------------------------

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.media.MediaScannerConnection;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class LogManager {

    private static volatile LogManager logManagerInstance;

    private final Boolean debug = true;

    private StringBuffer log_buffer;
    private int testNumber;
    private String fileTestID = "";

    private boolean isLogging = false;
    private FileOutputStream logFile;
    private File nemiLog;
    private boolean logSetup = false;

    private final String TAG = "NEMI_OUT";

    private Context context;
    private String logFileName;
    private Boolean singleLog;
    private File docsDir;

    private LogManager() {
        if (logManagerInstance != null){
            throw new RuntimeException("Use getInstance() method to get the single instance of this class.");
        }
    }

    public synchronized static LogManager initInstance(Context context, String logFileName, File docsDir, Boolean singleLog) {
        if (logManagerInstance == null){
            synchronized (LogManager.class) {
                if (logManagerInstance == null)
                    logManagerInstance = new LogManager();

                logManagerInstance.singleLog = singleLog;
                logManagerInstance.context = context;
                logManagerInstance.logFileName = logFileName;
                logManagerInstance.docsDir = docsDir;
            }
        }

        return logManagerInstance;
    }

    public synchronized static LogManager getInstance() {
        return logManagerInstance;
    }

    public void startNew() {
        log_buffer = new StringBuffer();

        if(!singleLog) {
            fileTestID = "_" + stripChars(getTime());
        }
        else {
            testNumber = 0;
        }

        setupFiles();
    }

    private void setupFiles() {
        String appName = stripChars(getApplicationName(this.context));
        final File nemiDir = new File(docsDir, appName);

        if(debug)
            Log.d(TAG, "Starting setupFiles: " + nemiDir.getAbsolutePath());

        if(logFile != null) {
            try {
                logFile.close();
                logFile = null;
            } catch (IOException e) {
                Log.d(TAG, "Log already open. Error closing logfile" + e.getMessage());
                return;
            }
        }

        if(docsDir.exists() && docsDir.isDirectory()) {
            if(debug)
                Log.d(TAG, "Main directory ok.");
        }
        else {
            if(!docsDir.exists()) {
                if (debug)
                    Log.d(TAG, "Main directory doesn't exist.");
            }
            else {
                if (debug)
                    Log.d(TAG, "Main directory is not a directory.");
            }
        }

        if(nemiDir.exists() && nemiDir.isDirectory()) {
            if(debug)
                Log.d(TAG, "Log directory ok.");
        }
        else {
            if(nemiDir.exists() && !nemiDir.isDirectory()) {
                if(debug)
                    Log.d(TAG, "Path not a directory.");

                try {
                    if (!nemiDir.delete())
                        throw new IOException();
                } catch (Exception e) {
                    Log.d(TAG, "Error deleting directory.");
                    return;
                }
            } else {
                if(debug)
                    Log.d(TAG, "Directory doesn't exist.");
            }

            try {
                if(!nemiDir.mkdir())
                    throw new IOException();

                if(debug)
                    Log.d(TAG, "Log directory created");
            }
            catch (IOException e) {
                Log.d(TAG, "Error creating directory.");
                e.printStackTrace();
                return;
            }
        }

        // if we reached here, log directory has been created ok
        nemiLog = new File(nemiDir + File.separator + logFileName + fileTestID + ".txt");

        if(debug)
            Log.d(TAG, "Creating log: " + nemiLog.getAbsolutePath());

        if(nemiLog.exists()) {
            Log.d(TAG, "Log exists.");

            try {
                if(!nemiLog.delete()) {
                    throw new IOException();
                }
            } catch (Exception e) {
                Log.d(TAG, "Failed deleting log file.");
                return;
            }
        }

        try {
            if(!nemiLog.createNewFile())
                throw new IOException();
        } catch (Exception e) {
            Log.d(TAG, "Error creating log file.");
            return;
        }

        try {
            logFile = new FileOutputStream(nemiLog, true);
            logSetup = true;
        } catch (IOException e) {
            Log.d(TAG, "Error opening file for logging.");
            return;
        }

        if(debug)
            Log.d(TAG, "File ready for logging.");

        logSetup = true;
    }

    public boolean isLogSetup(){
        return logSetup;
    }

    public void startLogging() {
        if (debug)
            Log.d(TAG, "Start logging...");

        logManagerInstance.startNew();

        if(!isLogSetup())
            setupFiles();

        isLogging = true;
        log_buffer.insert(0, getTime() + "\ttest\t" + testNumber++ + "\n");
    }

    public void stopLogging (Boolean writeLog) {
        if (debug) {
            String action = "saved";

            if(!writeLog)
                action = "cleared";

            Log.d(TAG, "Stopped logging and " + action + " log.");
        }


        writeLog();
        clearLog();
    }

    private void clearLog() {
        if(isLogging &&!(log_buffer.length() == 0))
            log_buffer.delete(0, log_buffer.length());
    }

    private void writeLog() {
        if (isLogging && !(log_buffer.length() == 0)) {
            try {
                logFile.write(log_buffer.toString().getBytes());

                // register log file with media scanner
                MediaScannerConnection.scanFile(context,
                        new String[]{nemiLog.getAbsolutePath()},
                        new String[]{"text/plain"},
                        null);

            } catch (IOException e) {
                Log.d(TAG, "doLog: error writing buffer to file");
                clearLog();
            }

            log_buffer.delete(0, log_buffer.length());
            isLogging = false;
        }
    }

    private String getTime() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyy_HH:mm:ss.SSS", Locale.ENGLISH);

        return dateFormat.format(new Date());
    }

    private static String getApplicationName(Context context) {
        ApplicationInfo applicationInfo = context.getApplicationInfo();
        int stringId = applicationInfo.labelRes;
        return stringId == 0 ? applicationInfo.nonLocalizedLabel.toString() : context.getString(stringId);
    }

    private String stripChars(String original) {
        return original.replaceAll("([:. ])", "_");
    }

    public void logData(String label, String value) {
        if (isLogging) {
            String line = getTime() + "\t" + label + "\t" + value + "\n";
            log_buffer.append(line);
        }
    }
}
