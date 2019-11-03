package usecase;

import material.Position;
import material.tree.narytree.LinkedTree;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class VirtualFileSystem {
    //TODO: Ejercicio 4 Caso de uso
    private LinkedTree<TreeElem> tree = new LinkedTree<>();
    private List<Position<TreeElem>> positionList = new ArrayList<>();
    private int id = 0;
    private class TreeElem {
        private File file;
        private int id;

        public TreeElem(File file, int id) {
            this.file = file;
            this.id = id;
        }

        public File getFile() {
            return file;
        }

        public void setFile(File file) {
            this.file = file;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

    }

    private void allocateTree (Position<TreeElem> parentPos) {
        File[] fchildren = parentPos.getElement().getFile().listFiles();

        if (fchildren != null) {
            Arrays.sort(fchildren);
            for (File file : fchildren) {
                if (!file.isHidden()) {
                    TreeElem newElem = new TreeElem(file, id);
                    Position<TreeElem> newParent = this.tree.add(newElem, parentPos);
                    this.positionList.add(newParent);
                    id++;
                    allocateTree(newParent);
                }
            }
        }
    }

    public void loadFileSystem (String path) {
        if (!this.tree.isEmpty()) {
            this.tree = new LinkedTree<>();
            this.positionList.clear();
            id = 0;
        }

        TreeElem newElem = new TreeElem(new File(path),id);
        this.positionList.add(this.tree.addRoot(newElem));
        id++;
        allocateTree(this.tree.root());
    }

    public String getFileSystem() {
        StringBuilder sb = new StringBuilder();
        StringBuilder tabBuilder = new StringBuilder();
        int level;

        for (Position<TreeElem> te: this.tree) {
            level = this.tree.posLevel(te);
            tabBuilder.setLength(0);
            for (int i = 1; i < level; i++)
                tabBuilder.append("\t");

            sb.append(te.getElement().getId() + " " + tabBuilder + te.getElement().getFile().getName() + "\n");
        }

        return sb.toString();
    }

    public void moveFileById(int idFile, int idTargetFolder) throws RuntimeException {
        Position<TreeElem> positionRemoveOrigin = null;
        Position<TreeElem> positionRemoveDest = null;
        for (Position<TreeElem> p: this.positionList) {
            if (p.getElement().getId() == idFile)
                positionRemoveOrigin = p;
            if (p.getElement().getId() == idTargetFolder)
                positionRemoveDest = p;
        }
        if (positionRemoveOrigin == null)
            throw new RuntimeException("Invalid ID.");
        if (positionRemoveDest != null) {
            if (positionRemoveDest.getElement().getFile().isFile())
                throw new RuntimeException("Target can't be a file.");
        }
        else
            throw new RuntimeException("Invalid ID.");


        this.tree.moveSubtree(positionRemoveOrigin,positionRemoveDest);
    }

    public void removeFileById(int idFile) throws RuntimeException {
        Position<TreeElem> positionRemove = null;
        for (Position<TreeElem> p: this.positionList) {
            if (p.getElement().getId() == idFile)
                positionRemove = p;
        }
        if (positionRemove == null)
            throw new RuntimeException("Invalid ID.");

        this.tree.remove(positionRemove);
        this.positionList.remove(positionRemove);
    }

    public Iterable<String> findBySubstring(int idStartFile, String substring) throws RuntimeException {
        List<String> listResult = new LinkedList<>();
        Position<TreeElem> positionStart = null;
        for (Position<TreeElem> p: this.positionList) {
            if (p.getElement().getId() == idStartFile)
                positionStart = p;
        }
        if (positionStart == null)
            throw new RuntimeException("Invalid ID.");

        for (int i = this.positionList.indexOf(positionStart); i < this.positionList.size(); i++) {
            if (this.positionList.get(i).getElement().getFile().getName().contains(substring))
                listResult.add(this.positionList.get(i).getElement().getId()+"\t"+this.positionList.get(i).getElement().getFile().getName());
        }

        return listResult;
    }

    public Iterable<String> findBySize(int idStartFile, long minSize, long maxSize) throws RuntimeException {
        List<String> listResult = new LinkedList<>();
        Position<TreeElem> positionStart = null;
        long size;

        if (minSize > maxSize)
            throw new RuntimeException("Invalid range.");

        for (Position<TreeElem> p: this.positionList) {
            if (p.getElement().getId() == idStartFile)
                positionStart = p;
        }
        if (positionStart == null)
            throw new RuntimeException("Invalid ID.");

        for (int i = this.positionList.indexOf(positionStart); i < this.positionList.size(); i++) {
            if (this.positionList.get(i).getElement().getFile().isFile()) {
                size = this.positionList.get(i).getElement().getFile().length();
                if ((size <= maxSize) && (size >= minSize))
                    listResult.add(this.positionList.get(i).getElement().getId()+"\t"+this.positionList.get(i).getElement().getFile().getName());
            }
        }

        return listResult;
    }

    public String getFileVirtualPath(int idFile) throws RuntimeException {
        Position<TreeElem> positionVirtualPath = null;
        for (Position<TreeElem> p: this.positionList) {
            if (p.getElement().getId() == idFile)
                positionVirtualPath = p;
        }
        if (positionVirtualPath == null)
            throw new RuntimeException("Invalid ID.");

        StringBuilder sb = new StringBuilder(positionVirtualPath.getElement().getFile().getName());
        while (!this.tree.isRoot(positionVirtualPath)) {
            positionVirtualPath = this.tree.parent(positionVirtualPath);
            sb.insert(0, positionVirtualPath.getElement().getFile().getName() + "/");
        }

        return "vfs:/"+sb.toString();
    }

    public String getFilePath(int idFile) throws RuntimeException {
        Position<TreeElem> positionPath = null;
        for (Position<TreeElem> p: this.positionList) {
            if (p.getElement().getId() == idFile)
                positionPath = p;
        }
        if (positionPath == null)
            throw new RuntimeException("Invalid ID.");

        return positionPath.getElement().getFile().getPath();
    }

}
