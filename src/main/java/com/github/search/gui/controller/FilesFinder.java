package com.github.search.gui.controller;

import java.nio.file.Path;
import java.util.List;

public interface FilesFinder {
    List<Path> findByExtension(Path rootDir, String extension);
}
