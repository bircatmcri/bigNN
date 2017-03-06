package edu.mfldclin.mcrf.deepsparktext.tools;

import com.google.common.io.Files;
import edu.mfldclin.mcrf.deepsparktext.setting.Setting;
import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import org.apache.commons.io.FileUtils;

/**
 *
 * @author Ehsun Behravesh <ehsun.behravesh@openet.com>
 */
public class ThreeShotsTest extends ParagraphVectorsClassifierExample {

    private final int sizeOfShots;
    protected List<ClassifiedFile> firstShot, secondShot, thirdShot;

    public ThreeShotsTest(File trainingDir, File dataDir, Setting setting, int sizeOfShots) {
        super(trainingDir, dataDir, setting);
        this.sizeOfShots = sizeOfShots;
    }

    public void copyFirstShot() throws IOException {
        //moveToTempAllThreeShots();
        cleanDataDir();

        for (ClassifiedFile file : firstShot) {

            File newFile = new File(dataDir.getAbsolutePath() + "/" + file.getClassName());
            boolean mkdirs = true;
            if (!newFile.exists()) {
                newFile.mkdirs();
            }

            newFile = new File(newFile, file.getFile().getName());
            if (mkdirs) {
                Files.move(file.getFile(), newFile);
                file.setFile(newFile);
            } else if (!mkdirs) {
                throw new IOException("Can not create dirs. " + newFile.getAbsolutePath());
            }
        }
        
        int s=1;
    }

    /*
    public void copySecondShot() throws IOException {
        cleanDataDir();

        for (ClassifiedFile file : secondShot) {

            File newFile = new File(dataDir.getAbsolutePath() + "/" + file.getClassName());
            boolean mkdirs = true;
            if (!newFile.exists()) {
                newFile.mkdirs();
            }

            newFile = new File(newFile, file.getFile().getName());
            if (mkdirs) {
                Files.move(file.getFile(), newFile);
                file.setFile(newFile);
            } else if (!mkdirs) {
                throw new IOException("Can not create dirs. " + newFile.getAbsolutePath());
            }
        }
        
        int v = 10;
    }

    public void copyThirdShot() throws IOException {
        cleanDataDir();

        for (ClassifiedFile file : thirdShot) {

            File newFile = new File(dataDir.getAbsolutePath() + "/" + file.getClassName());
            boolean mkdirs = true;
            if (!newFile.exists()) {
                newFile.mkdirs();
            }

            newFile = new File(newFile, file.getFile().getName());
            if (mkdirs) {
                Files.move(file.getFile(), newFile);
                file.setFile(newFile);
            } else if (!mkdirs) {
                throw new IOException("Can not create dirs. " + newFile.getAbsolutePath());
            }
        }
        
        int c =1;
    }
    */
    public void moveToTempAllThreeShots() throws IOException {
        File tmp = new File(System.getProperty("user.home") + "/deep_temp/" + new Date().getTime());
        tmp.mkdirs();

        firstShot = new LinkedList<>();
        secondShot = new LinkedList<>();
        thirdShot = new LinkedList<>();

        if (tmp.exists() && tmp.isDirectory()) {
            Random rnd = new Random(System.currentTimeMillis());

            File[] trainingDirs = trainingDir.listFiles((File f) -> f.isDirectory());
            for (int j = 0; j < 1; j++) {

                for (int count = 0; count < sizeOfShots;) {
                    int trainingDirIndex = rnd.nextInt(trainingDirs.length);
                    File trainingDir = trainingDirs[trainingDirIndex];
                    File[] trainingFiles = trainingDir.listFiles();
                    int fileIndex = rnd.nextInt(trainingFiles.length);
                    File tFile = trainingFiles[fileIndex];
                    File newFile = new File(tmp, tFile.getName());

                    if (newFile.exists()) {
                        newFile = new File(getNewFilePath(newFile.getAbsolutePath()));
                    }

                    boolean renamed = tFile.renameTo(newFile);

                    if (renamed) {
                        ClassifiedFile classifiedFile = new ClassifiedFile(trainingDir.getName(), newFile);
                        switch (j) {
                            case 0:
                                firstShot.add(classifiedFile);
                                break;
                            case 1:
                                secondShot.add(classifiedFile);
                                break;
                            case 2:
                                thirdShot.add(classifiedFile);
                                break;
                        }
                        count++;
                    }
                }
            }

            int b = 10;
        } else {
            throw new IOException("Directory " + tmp.getAbsolutePath() + " does not exist!");
        }
    }

    private void cleanDataDir() throws IOException {
        File[] files = dataDir.listFiles();

        for (File file : files) {
            //boolean deleted = file.delete();
            boolean deleted = FileUtils.deleteQuietly(file);

            if (!deleted) {
                throw new IOException("Can not delete the file " + file.getAbsolutePath());
            }
        }
    }

    private static String getNewFilePath(String path) {
        if (path.lastIndexOf(".") > 0) {
            return path.substring(0, path.lastIndexOf(".")).concat("_").concat(path.substring(path.lastIndexOf(".")));
        } else {
            return path.concat("_");
        }
    }

}
