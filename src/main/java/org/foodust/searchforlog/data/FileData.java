package org.foodust.searchforlog.data;

import lombok.Getter;
import lombok.Setter;

import java.io.File;

@Setter
@Getter
public class FileData {
    private String name;
    private String path;
    private long size;

    public FileData(File file) {
        this.name = file.getName();
        this.path = file.getAbsolutePath();
        this.size = file.length();
    }

}