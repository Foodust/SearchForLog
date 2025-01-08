package org.foodust.searchforlog.file;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class FileAnalyzer {
    public List<File> searchFilesWithTerm(File folder, String searchTerm) {
        List<File> matchedFiles = new ArrayList<>();
        searchFilesRecursively(folder, searchTerm, matchedFiles);
        return matchedFiles;
    }

    private void searchFilesRecursively(File folder, String searchTerm, List<File> matchedFiles) {
        if (folder.isDirectory()) {
            File[] files = folder.listFiles();
            if (files != null) {
                for (File file : files) {
                    if (file.isDirectory()) {
                        searchFilesRecursively(file, searchTerm, matchedFiles);
                    } else {
                        try {
                            if (fileContainsTerm(file, searchTerm)) {
                                matchedFiles.add(file);
                            }
                        } catch (IOException e) {
                            System.err.println("Error reading file: " + file.getPath());
                        }
                    }
                }
            }
        }
    }

    private boolean fileContainsTerm(File file, String searchTerm) throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.contains(searchTerm)) {
                    return true;
                }
            }
        }
        return false;
    }

    public static String readFileContent(File file) throws IOException {
        StringBuilder content = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                content.append(line).append("\n");
            }
        }
        return content.toString();
    }
}