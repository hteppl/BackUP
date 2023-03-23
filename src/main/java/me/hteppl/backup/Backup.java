package me.hteppl.backup;

import cn.nukkit.Server;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@Log4j2
@RequiredArgsConstructor
public class Backup {

    private final String outputPath;
    private final String identifyName;
    private final int compression;
    private final boolean logFiles;

    public void create() {
        File outputDirectory = new File(outputPath);
        if (!(outputDirectory.exists() || outputDirectory.mkdirs())) {
            log.error("Can't create directory for backups: {}", outputPath);
            return;
        }

        // The name and path of the compressed file
        String fileName = getFileName();
        File outputFile = new File(outputDirectory, fileName);

        if (outputFile.exists()) {
            log.error("[BackUP] Backup entry {} already exists!", fileName);
            return;
        }

        long start = System.currentTimeMillis();
        log.info("[BackUP] Backup creation started!");

        try (ZipOutputStream zipOutput = new ZipOutputStream(Files.newOutputStream(outputFile.toPath()))) {
            // Compression level
            zipOutput.setLevel(compression);

            // Traverse the directory and add all files
            zip(zipOutput, new File(Server.getInstance().getDataPath()));
        } catch (IOException e) {
            e.printStackTrace();
        }

        log.info("[BackUP] Backup created in {} sec.", (System.currentTimeMillis() - start) / 60);
    }

    private void zip(ZipOutputStream zipOutput, File inputFile) throws IOException {
        // We don't want to add older backups in the final backup file
        if (inputFile.getPath().contains(outputPath)) {
            return;
        }

        // Recursive check directories
        if (inputFile.isDirectory()) {
            File[] files = inputFile.listFiles();
            if (files != null) {
                for (File file : files) {
                    zip(zipOutput, file);
                }
            }
            return;
        }

        long start = System.currentTimeMillis();

        // Write file contents
        try (FileInputStream fis = new FileInputStream(inputFile)) {
            zipOutput.putNextEntry(new ZipEntry(inputFile.getAbsolutePath()));

            byte[] buffer = new byte[1024];
            int length;
            while ((length = fis.read(buffer)) >= 0) {
                zipOutput.write(buffer, 0, length);
            }
        }

        // Close ZipOutputStream
        zipOutput.closeEntry();

        // Log file name if needed
        if (logFiles) {
            log.info("- {} in {} ms.", inputFile.getName(), System.currentTimeMillis() - start);
        }
    }

    private String getFileName() {
        return "backup-" + identifyName + "-" + new SimpleDateFormat("dd.MM.yyyy_HH.mm.ss").format(Calendar.getInstance().getTime()) + ".zip";
    }

    public String getOutputFilePath() {
        return outputPath + "/" + getFileName();
    }
}
