package com.multimidia.projeto.trabalho_final.modules.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipArchiveInputStream;
import org.apache.commons.compress.archivers.zip.ZipArchiveOutputStream;

public class FileUtils {

    public static byte[] compressFile(byte[] data, String fileName) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try (ZipArchiveOutputStream zaos = new ZipArchiveOutputStream(baos)) {
            ZipArchiveEntry entry = new ZipArchiveEntry(fileName);
            entry.setSize(data.length);
            zaos.putArchiveEntry(entry);
            zaos.write(data);
            zaos.closeArchiveEntry();
        }
        return baos.toByteArray();
    }

    public static byte[] decompressFile(byte[] compressedData) throws IOException {
        ByteArrayInputStream bais = new ByteArrayInputStream(compressedData);
        try (ZipArchiveInputStream zais = new ZipArchiveInputStream(bais)) {
            ArchiveEntry entry = zais.getNextEntry();
            if (entry != null) {
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                byte[] buffer = new byte[1024];
                int count;
                while ((count = zais.read(buffer)) != -1) {
                    baos.write(buffer, 0, count);
                }
                return baos.toByteArray();
            }
            throw new IOException("No files in ZIP.");
        }
    }
}
