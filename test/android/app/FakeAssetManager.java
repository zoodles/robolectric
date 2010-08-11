package android.app;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import android.util.TypedValue;
import com.xtremelabs.droidsugar.view.FakeActivity;

@SuppressWarnings({"UnusedDeclaration"})
public class FakeAssetManager {
    private List<File> resDirs = new ArrayList<File>();

    public int addAssetPath(String path) {
        System.out.println("addAssetPath " + path);
        return 1;
    }

    public final boolean isUpToDate() {
        return true;
    }

    public int loadResourceValue(int ident, TypedValue outValue, boolean resolve) {
        String resourceName = FakeActivity.viewLoader.getNameById(ident);

        for (File resDir : resDirs) {
            File layoutFile = new File(resDir, resourceName + ".xml");
            if (layoutFile.exists()) {
                try {
                    outValue.type = TypedValue.TYPE_REFERENCE;
                    outValue.string = readFile(layoutFile);
                    return 1;
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }

        return 0;
    }

    private String readFile(File file) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(file));
        StringBuilder buf = new StringBuilder();
        String ls = System.getProperty("line.separator");
        String line;
        while ((line = reader.readLine()) != null) {
            buf.append(line);
            buf.append(ls);
        }
        reader.close();
        return buf.toString();
    }


}
