package com.github.search.gui.controller;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.Collections.emptyList;

public class FilesFinderImpl implements FilesFinder {
    @Override
    public List<Path> findByExtension(Path rootDir, String extension) {
        try {
            return Files.walk(rootDir)
                    .filter(Files::isRegularFile)
                    .filter(path -> path.getFileName().toString().endsWith(extension))
                    .collect(Collectors.toList());
        } catch (IOException e) {
            return emptyList();
        }
    }
}
