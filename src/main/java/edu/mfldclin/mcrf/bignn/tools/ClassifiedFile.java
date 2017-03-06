package edu.mfldclin.mcrf.bignn.tools;

import java.io.File;

/**
 *
 * @author Ehsun Behravesh <ehsun.behravesh@openet.com>
 */
public class ClassifiedFile {
    protected String className;
    protected File file;

    public ClassifiedFile() {
    }

    public ClassifiedFile(String className, File file) {
        this.className = className;
        this.file = file;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }
}
