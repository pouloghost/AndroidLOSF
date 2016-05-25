package gt.research.losf.gen;

import com.alibaba.fastjson.JSON;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import gt.research.losf.BlockConfig;
import gt.research.losf.TaskConfig;

/**
 * Created by GT on 2016/5/25.
 */
public class ConfigGen {
    private static final String sFilePath = "D:\\Workbench\\AndroidLOSF\\126.jpg";
    private static final String sConfigPath = "D:\\Workbench\\AndroidLOSF\\126.config";
    private static final int sBlockSize = 16 * 1024;

    public static void main(String[] args) {
        TaskConfig taskConfig = initTaskConfig();

        String json = JSON.toJSONString(taskConfig);

        writeToFile(json);
    }

    private static void writeToFile(String json) {
        File out = new File(sConfigPath);
        FileOutputStream outputStream = null;
        try {
            if (!out.exists()) {
                out.createNewFile();
            }
            outputStream = new FileOutputStream(out);
            outputStream.write(json.getBytes());
            outputStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (null != outputStream) {
                try {
                    outputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private static TaskConfig initTaskConfig() {
        TaskConfig taskConfig = new TaskConfig();
        taskConfig.network = 100;
        taskConfig.url = "$url";

        File file = new File(sFilePath);
        long length = file.length();
        int blockCount = (int) (length / sBlockSize);
        if (0 != length % sBlockSize) {
            ++blockCount;
        }

        taskConfig.blocks = new BlockConfig[blockCount];

        fillMd5(file, taskConfig);
        return taskConfig;
    }

    private static void fillMd5(File file, TaskConfig taskConfig) {
        FileInputStream inputStream = null;
        try {
            inputStream = new FileInputStream(file);
            MessageDigest fullDigest = MessageDigest.getInstance("MD5");
            MessageDigest blockDigest = MessageDigest.getInstance("MD5");
            byte[] tmp = new byte[sBlockSize];
            int offset = 0;
            int index = 0;
            int read;
            do {
                read = inputStream.read(tmp);
                fullDigest.update(tmp);
                blockDigest.reset();
                blockDigest.update(tmp);

                BlockConfig blockConfig = new BlockConfig();
                blockConfig.offset = offset;
                blockConfig.end = offset + read;
                blockConfig.md5 = getMd5String(blockDigest);

                offset += read;
                taskConfig.blocks[index] = blockConfig;
                ++index;
                System.out.println("index " + index + " offset " + offset);
            } while (sBlockSize == read);
            taskConfig.md5 = getMd5String(fullDigest);
        } catch (NoSuchAlgorithmException | IOException e) {
            e.printStackTrace();
        } finally {
            if (null != inputStream) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private static String getMd5String(MessageDigest digest) {
        if (null == digest) {
            return null;
        }
        BigInteger integer = new BigInteger(1, digest.digest());
        return integer.toString(16);
    }
}
