package usecase;
import material.Position;
import material.tree.narytree.LinkedTree;

import java.io.IOException;
import java.nio.file.*;
import java.util.Iterator;

public class VirtualFileSystem {
    //TODO: Ejercicio 4 Caso de uso
    private LinkedTree<String> tree = new LinkedTree<>();

    public void loadFileSystem (String path) {
        Position<String> pRoot = tree.addRoot(path);
        //Position<String> pSubA = tree.add();
    }

    public String getFileSystem() {
        return tree.root().getElement();
    }

    public void moveFileById(int idFile, int idTargetFolder) {
        throw new RuntimeException("Not yet implemented");
    }

    public void removeFileById(int idFile) {
        throw new RuntimeException("Not yet implemented");
    }


    public Iterable<String> findBySubstring(int idStartFile, String substring) {
        throw new RuntimeException("Not yet implemented");
    }

    public Iterable<String> findBySize(int idStartFile, long minSize, long maxSize) {
        throw new RuntimeException("Not yet implemented");
    }

    public String getFileVirtualPath(int idFile) {
        throw new RuntimeException("Not yet implemented");
    }

    public String getFilePath(int idFile) {
        throw new RuntimeException("Not yet implemented");
    }

}
