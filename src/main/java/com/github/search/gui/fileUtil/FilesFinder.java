package com.github.search.gui.fileUtil;

import java.nio.file.Path;
import java.util.List;

public interface FilesFinder {
    List<Path> findByExtension(Path rootDir, String extension);
}
