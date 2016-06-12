package gt.research.losf.download.component;

import android.content.Context;
import android.os.Environment;
import android.os.PowerManager;
import android.text.TextUtils;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.net.CookieManager;
import java.net.CookiePolicy;
import java.net.HttpCookie;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import gt.research.losf.LosfApplication;
import gt.research.losf.download.control.ControlStateCenter;
import gt.research.losf.journal.IBlockInfo;
import gt.research.losf.journal.IFileInfo;
import gt.research.losf.journal.IJournal;
import gt.research.losf.journal.JournalMaker;
import gt.research.losf.util.LogUtils;

import static java.net.HttpURLConnection.HTTP_OK;
import static java.net.HttpURLConnection.HTTP_PARTIAL;

/**
 * Created by GT on 2016/5/25.
 */
public class DownloadRunnable implements Runnable {
    private static final Pattern sRangePattern = Pattern.compile("bytes\\s{1,}(\\d{1,})-(\\d{1,})/\\d{1,}");
    private static final String sRootPath = "losf";

    private IBlockInfo mBlock;
    private IJournal mJournal;

    private byte[] mTmp;
    private MessageDigest mDigest;
    private RandomAccessFile mSaveFile;

    private PowerManager.WakeLock mWakeLock;

    private CookieManager mCookieManager;
    private URI mCookieUri;

    public DownloadRunnable(IBlockInfo blockInfo) {
        mJournal = JournalMaker.get();
        mBlock = blockInfo;

        if (!TextUtils.isEmpty(blockInfo.getMd5())) {
            try {
                mDigest = MessageDigest.getInstance("MD5");
            } catch (NoSuchAlgorithmException e) {
                LogUtils.exception(this, e);
            }
        }
        if (null == mDigest) {
            mDigest = new MockMessageDigest("");
        }

        mCookieManager = new CookieManager();
        mCookieManager.setCookiePolicy(CookiePolicy.ACCEPT_ALL);
        mCookieUri = URI.create(mBlock.getUrl());
    }

    /**
     * download stage:
     * download data
     * verify md5
     */
    @Override
    public void run() {
        ControlStateCenter state = ControlStateCenter.getInstance();
        try {
            mTmp = ByteArrayPool.getInstance().get();
            acquireLock();
            checkStatus(state);
            HttpURLConnection connection = startConnection();
            if (null == connection) {
                return;
            }
            ensureFile();
            while (mBlock.getRead() < mBlock.getLength()) {
                checkStatus(state);
            }
        } catch (DownloadControlException | DownloadException e) {
// TODO: 2016/6/12 call callback
        } finally {
            ByteArrayPool.getInstance().offer(mTmp);
            if (null != mWakeLock) {
                mWakeLock.release();
            }
        }
    }

    private void ensureFile() throws DownloadException {
        if (!Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
            DownloadException.noFile();
        }
        StringBuilder path = new StringBuilder(Environment.getExternalStorageDirectory().getAbsolutePath());
        if ('/' != path.charAt(path.length() - 1)) {
            path.append('/');
        }
        path.append(sRootPath);
        File file = new File(path.toString(), mBlock.getFile());
        boolean fileExist = file.exists();
        if (!fileExist) {
            if (file.getParentFile().exists() || file.getParentFile().mkdirs()) {
                try {
                    fileExist = file.createNewFile();
                } catch (IOException e) {
                    DownloadException.noFile();
                }
            }
        }
        if (!fileExist) {
            DownloadException.noFile();
        }
        try {
            mSaveFile = new RandomAccessFile(file, "rwd");
            IFileInfo fileInfo = mJournal.getFile(mBlock.getFileId());
            mSaveFile.setLength(fileInfo.getLength());
            mSaveFile.seek(mBlock.getFileOffset() + mBlock.getRead());
        } catch (Exception e) {
            LogUtils.exception(this, e);
            DownloadException.noFile();
        }
    }

    private HttpURLConnection startConnection() {
        try {
            URL url = new URL(mBlock.getUrl());
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setInstanceFollowRedirects(true);
            connection.setConnectTimeout(20 * 1000);
            connection.setReadTimeout(20 * 1000);
            addRequestHeaders(connection);
            handleResponse(connection);
            return connection;
        } catch (IOException | DownloadException e) {
            LogUtils.exception(this, e);
        }
        return null;
    }

    private void handleResponse(HttpURLConnection connection) throws IOException, DownloadException {
        final int code = connection.getResponseCode();
        switch (code) {
            case HTTP_OK:
                if (0 != mBlock.getRead()) {
                    DownloadException.wrongCode();
                }
                break;
            case HTTP_PARTIAL:
                if (0 == mBlock.getRead()) {
                    DownloadException.wrongCode();
                }
                checkPartialResponse(connection);
                break;
        }
        checkResponse(connection);
        updateFileInfo(connection);
    }

    private void checkResponse(HttpURLConnection connection) throws DownloadException {
        if ("chunked".equalsIgnoreCase(connection.getHeaderField("Transfer-Encoding"))) {
            DownloadException.error();
        }
    }

    private void checkPartialResponse(HttpURLConnection connection) throws DownloadException {
        String range = connection.getHeaderField("Content-Range");
        Matcher matcher = sRangePattern.matcher(range);
        if (3 != matcher.groupCount()) {
            DownloadException.error();
        }
        int start = Integer.valueOf(matcher.group(1)) - mBlock.getDownloadOffset();
        int end = Integer.valueOf(matcher.group(2)) - mBlock.getDownloadOffset();
        if (start != mBlock.getRead() || end != mBlock.getLength()) {
            DownloadException.error();
        }
    }

    private void updateFileInfo(HttpURLConnection connection) {
        try {
            mCookieManager.put(mCookieUri, connection.getHeaderFields());
            String eTag = connection.getHeaderField("ETag");
            if (!TextUtils.isEmpty(eTag)) {
                IFileInfo fileInfo = mJournal.getFile(mBlock.getFileId());
                fileInfo.setEtag(eTag);
                mJournal.addFile(fileInfo);
            }
        } catch (IOException e) {
            LogUtils.exception(this, e);
        }
    }

    private void addRequestHeaders(HttpURLConnection connection) throws DownloadException {
        // TODO: 2016/6/12 add header config in FileConfig
        // referer, ua
        IFileInfo fileInfo = mJournal.getFile(mBlock.getFileId());
        if (null == fileInfo) {
            DownloadException.error();
        }

        List<HttpCookie> cookies = mCookieManager.getCookieStore().get(mCookieUri);
        for (HttpCookie cookie : cookies) {
            if (!cookie.hasExpired()) {
                // TODO: 2016/6/12 is this right?
                connection.addRequestProperty("Cookie", cookie.getValue());
                break;
            }
        }

        if (!TextUtils.isEmpty(fileInfo.getEtag())) {
            connection.addRequestProperty("If-Match", fileInfo.getEtag());
        }
        connection.addRequestProperty("Accept-Encoding", "identity");
        connection.addRequestProperty("Connection", "close");
        connection.addRequestProperty("Range", "bytes=" + mBlock.getDownloadOffset() +
                "-" + (mBlock.getDownloadOffset() + mBlock.getLength()));
    }

    private void acquireLock() {
        // keep wake
        PowerManager pm = (PowerManager) LosfApplication.getInstance().getSystemService(Context.POWER_SERVICE);
        mWakeLock = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "Download");
        mWakeLock.acquire();
    }

    private void checkStatus(ControlStateCenter state) throws DownloadControlException {
        if (state.needCancel(mBlock.getId())) {
            mJournal.deleteBlock(mBlock.getId());
            state.canceled(mBlock.getId());
            DownloadControlException.cancel();
        }
        if (state.needPause(mBlock.getId())) {
            state.paused(mBlock.getId());
            DownloadControlException.pause();
        }
        //check network type
        if (mBlock.getNetwork() > state.getNetwork()) {
            state.waitForNetwork(mBlock);
            DownloadControlException.network();
        }
    }

    private static class MockMessageDigest extends MessageDigest {
        private static byte[] sEmptyBytes = new byte[0];

        /**
         * Constructs a new instance of {@code MessageDigest} with the name of
         * the algorithm to use.
         *
         * @param algorithm the name of algorithm to use
         */
        protected MockMessageDigest(String algorithm) {
            super(algorithm);
        }

        @Override
        protected void engineUpdate(byte input) {

        }

        @Override
        protected void engineUpdate(byte[] input, int offset, int len) {

        }

        @Override
        protected byte[] engineDigest() {
            return sEmptyBytes;
        }

        @Override
        protected void engineReset() {

        }
    }

    private static class DownloadControlException extends Exception {
        public static final int STATUS_CANCEL = 0;
        public static final int STATUS_PAUSE = 1;
        public static final int STATUS_NETWORK = 2;

        public int status;

        public DownloadControlException(int status) {
            this.status = status;
        }

        public static void cancel() throws DownloadControlException {
            throw new DownloadControlException(STATUS_CANCEL);
        }

        public static void pause() throws DownloadControlException {
            throw new DownloadControlException(STATUS_PAUSE);
        }

        public static void network() throws DownloadControlException {
            throw new DownloadControlException(STATUS_NETWORK);
        }
    }

    private static class DownloadException extends Exception {
        public static final int STATUS_ERROR = 1;
        public static final int STATUS_WRONG_CODE = 2;
        public static final int STATUS_NO_FILE = 3;

        public int status;

        public DownloadException(int status) {
            this.status = status;
        }

        public static void error() throws DownloadException {
            throw new DownloadException(STATUS_ERROR);
        }

        public static void wrongCode() throws DownloadException {
            throw new DownloadException(STATUS_WRONG_CODE);
        }

        public static void noFile() throws DownloadException {
            throw new DownloadException(STATUS_NO_FILE);
        }
    }
}
