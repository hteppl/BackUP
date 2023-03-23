package me.hteppl.backup;

import cn.nukkit.plugin.PluginBase;
import lombok.Getter;
import lombok.extern.log4j.Log4j2;

@Log4j2
public class Loader extends PluginBase {

    @Getter
    private static Loader instance;

    @Override
    public void onLoad() {
        instance = this;
        this.saveDefaultConfig();
    }

    @Override
    public void onEnable() {
        BackupManager manager = new BackupManager(this.getConfig());
        if (!manager.isBackupTime()) {
            log.info("[BackUP] It's not backup time, skipping...");
            return;
        }

        manager.createBackup();
    }
}
