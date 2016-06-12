package gt.research.losf.gen;

import com.alibaba.fastjson.JSON;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import gt.research.losf.BlockConfig;
import gt.research.losf.FileConfig;

/**
 * Created by GT on 2016/5/25.
 */
public class ConfigGen {
    private static final String sFilePath = "D:\\Workbench\\AndroidLOSF\\126.jpg";
    private static final String sConfigPath = "D:\\Workbench\\AndroidLOSF\\126.config";
    private static final int sBlockSize = 64 * 1024;

    public static void main(String[] args) {
        FileConfig taskConfig = initTaskConfig();

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

    private static FileConfig initTaskConfig() {
        FileConfig fileConfig = new FileConfig();
        fileConfig.network = 0;
        fileConfig.url = "$url";
        fileConfig.retry = 2;

        File file = new File(sFilePath);
        fileConfig.length = file.length();
        int blockCount = (int) (fileConfig.length / sBlockSize);
        if (0 != fileConfig.length % sBlockSize) {
            ++blockCount;
        }

        fileConfig.blocks = new BlockConfig[blockCount];

        fillMd5(file, fileConfig);
        return fileConfig;
    }

    private static void fillMd5(File file, FileConfig fileConfig) {
        FileInputStream inputStream = null;
        try {
            inputStream = new FileInputStream(file);
            MessageDigest fileDigest = MessageDigest.getInstance("MD5");
            MessageDigest blockDigest = MessageDigest.getInstance("MD5");
            byte[] tmp = new byte[sBlockSize];
            int offset = 0;
            int index = 0;
            int read;
            do {
                read = inputStream.read(tmp);
                blockDigest.reset();
                blockDigest.update(tmp);
                fileDigest.update(tmp);

                BlockConfig blockConfig = new BlockConfig();
                blockConfig.offset = offset;
                blockConfig.length = read;
                blockConfig.md5 = getMd5String(blockDigest);

                offset += read;
                fileConfig.blocks[index] = blockConfig;
                ++index;
                System.out.println("index " + index + " offset " + offset);
            } while (sBlockSize == read);
            fileConfig.md5 = getMd5String(fileDigest);
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
