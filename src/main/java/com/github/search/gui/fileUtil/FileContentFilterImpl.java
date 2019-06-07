package com.github.search.gui.fileUtil;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static java.util.stream.Collectors.toList;

public class FileContentFilterImpl implements FileContentFilter {
    @Override
    public List<Path> filterFilesByContextText(List<Path> sourceFiles, String text) {
        return sourceFiles.stream().parallel().filter(file -> this.isFileContainsText(file, text)).collect(toList());
    }

    private boolean isFileContainsText(Path file, String text) {
        try {
            List<String> rows = Files.readAllLines(file.toAbsolutePath());
            for (int i = 0; i < rows.size() - 1; i++) {
                String a = rows.get(i) + rows.get(i + 1);
                if (a.contains(text)) { return true; }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return false;
    }
}
