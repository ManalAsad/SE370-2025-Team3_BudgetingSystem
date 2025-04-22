package org.project.util;

import java.util.ArrayList;

public class FileHandler
{
    private static ArrayList<String> uploadedFiles = new ArrayList<>();

    public void addFile(String filePath)
    {
        uploadedFiles.add(filePath);
    }

    public String getFile(int index)
    {
        if (!uploadedFiles.isEmpty() && index >= 0)
            return uploadedFiles.get(index);

        return "";
    }

    public boolean isEmpty()
    {
        return uploadedFiles.isEmpty();
    }

    public int getSize()
    {
        return uploadedFiles.size();
    }
}
