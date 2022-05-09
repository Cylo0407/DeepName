package com.example.deepname.VO;

import java.util.List;

public class DirVO {
    private String parentPath;

    private List<String> javaFiles;

    private List<String> otherFiles;

    private List<String> dirs;

    public String getParentPath() {
        return parentPath;
    }

    public void setParentPath(String parentPath) {
        this.parentPath = parentPath;
    }

    public List<String> getJavaFiles() {
        return javaFiles;
    }

    public void setJavaFiles(List<String> javaFiles) {
        this.javaFiles = javaFiles;
    }

    public List<String> getOtherFiles() {
        return otherFiles;
    }

    public void setOtherFiles(List<String> otherFiles) {
        this.otherFiles = otherFiles;
    }

    public List<String> getDirs() {
        return dirs;
    }

    public void setDirs(List<String> dirs) {
        this.dirs = dirs;
    }
}
