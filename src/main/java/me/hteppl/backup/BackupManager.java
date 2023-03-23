package me.hteppl.backup;

import cn.nukkit.Server;
import cn.nukkit.utils.Config;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

public class BackupManager {

    private final int frequency;
    private final int compression;
    private final String path;
    private final boolean log;

    private final int lastBackupTime;

    public BackupManager(Config config) {
        this.frequency = config.getInt("frequency", 2);
        this.compression = config.getInt("compression", 9);
        this.path = config.getString("path", "backups");
        this.log = config.getBoolean("log", false);
        this.lastBackupTime = getLastBackupTime(config.get("backups", new LinkedHashMap<>()));
    }

    public void createBackup() {
        Backup backup = new Backup(path, String.valueOf(Server.getInstance().getPort()), compression, log);

        try {
            forceUpdateConfig(backup);
            backup.create();
        } catch (NullPointerException | IOException ex) {
            ex.printStackTrace();
        }
    }

    public boolean isBackupTime() {
        return System.currentTimeMillis() / 1000L - lastBackupTime > 86400L * frequency;
    }

    // This method is used, because `config.save` delete all the comments in file
    private void forceUpdateConfig(Backup backup) throws NullPointerException, IOException {
        // Get the config.yml file manually
        File config = new File(Loader.getInstance().getDataFolder(), "config.yml");

        try (FileReader reader = new FileReader(config)) {
            // Read the existing text from the file
            StringBuilder sb = new StringBuilder();
            int ch;
            while ((ch = reader.read()) != -1) {
                sb.append((char) ch);
            }

            // Check if backups section exists
            int index = sb.indexOf("backups:");
            if (index == -1) {
                throw new NullPointerException("Can't find backups section in config.yml");
            }

            try (FileWriter writer = new FileWriter(config)) {
                // Write the new text back to the file
                writer.write(sb.substring(0, index + 8) + "\n  " + backup.getOutputFilePath() + ": " + (System.currentTimeMillis() / 1000L) + sb.substring(index + 8));
            }
        }
    }

    private int getLastBackupTime(LinkedHashMap<String, Object> backups) {
        int time = 0;
        if (backups != null) {
            for (Map.Entry<String, Object> entry : backups.entrySet()) {
                int currTime = (int) entry.getValue();
                if (currTime > time) {
                    time = currTime;
                }
            }
        }
        return time;
    }
}
