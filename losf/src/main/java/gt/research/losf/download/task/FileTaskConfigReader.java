package gt.research.losf.download.task;

import com.alibaba.fastjson.JSON;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import gt.research.losf.FileConfig;

/**
 * Created by GT on 2016/5/25.
 */
public class FileTaskConfigReader implements ITaskConfigReader {
    private static final String sConfigPath = "D:\\Workbench\\AndroidLOSF\\126.config";

    @Override
    public FileConfig readConfig() {
        File file = new File(sConfigPath);
        try {
            FileReader reader = new FileReader(file);
            BufferedReader bufferedReader = new BufferedReader(reader);
            String json = bufferedReader.readLine();
            return JSON.parseObject(json, FileConfig.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
