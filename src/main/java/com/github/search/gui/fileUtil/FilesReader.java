package com.github.search.gui.fileUtil;

import java.nio.file.Path;
import java.util.List;

public class FilesReader {
    private final FileContentFilter fileContentFilter;
    private final FilesFinder filesFinder;

    public FilesReader(FileContentFilter fileContentFilter, FilesFinder filesFinder) {
        this.fileContentFilter = fileContentFilter;
        this.filesFinder = filesFinder;
    }

    public List<Path> getFilesWithGivenTextInside(Path rootDir, String text, String format) {
        return fileContentFilter.filterFilesByContextText(
                filesFinder.findByExtension(rootDir, format),
                text
        );
    }
}
