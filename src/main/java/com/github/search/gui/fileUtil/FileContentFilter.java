package com.github.search.gui.fileUtil;

import java.nio.file.Path;
import java.util.List;

public interface FileContentFilter {
    List<Path> filterFilesByContextText(List<Path> sourceFiles, String text);
}
