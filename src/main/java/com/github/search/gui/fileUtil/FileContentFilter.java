package com.github.search.gui.controller;

import java.nio.file.Path;
import java.util.List;

public interface FileContentFilter {
    List<Path> filterFilesByContextText(List<Path> sourceFiles, String text);
}
