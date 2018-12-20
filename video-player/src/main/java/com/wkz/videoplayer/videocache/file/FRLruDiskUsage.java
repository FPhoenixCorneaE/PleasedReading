package com.wkz.videoplayer.videocache.file;

import com.wkz.videoplayer.utils.FRVideoLogUtils;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * {@link FRDiskUsage} that uses LRU (Least Recently Used) strategy to trim cache.
 *
 * @author Alexey Danilov (danikula@gmail.com).
 */
public abstract class FRLruDiskUsage implements FRDiskUsage {

    private final ExecutorService workerThread = Executors.newSingleThreadExecutor();

    @Override
    public void touch(File file) throws IOException {
        workerThread.submit(new TouchCallable(file));
    }

    private void touchInBackground(File file) throws IOException {
        FRFiles.setLastModifiedNow(file);
        List<File> files = FRFiles.getLruListFiles(file.getParentFile());
        trim(files);
    }

    protected abstract boolean accept(File file, long totalSize, int totalCount);

    private void trim(List<File> files) {
        long totalSize = countTotalSize(files);
        int totalCount = files.size();
        for (File file : files) {
            boolean accepted = accept(file, totalSize, totalCount);
            if (!accepted) {
                long fileSize = file.length();
                boolean deleted = file.delete();
                if (deleted) {
                    totalCount--;
                    totalSize -= fileSize;
                    FRVideoLogUtils.i("FRCache file " + file.getAbsolutePath() + " is deleted because it exceeds cache limit");
                } else {
                    FRVideoLogUtils.e("Error deleting file " + file.getAbsolutePath() + " for trimming cache");
                }
            }
        }
    }

    private long countTotalSize(List<File> files) {
        long totalSize = 0;
        for (File file : files) {
            totalSize += file.length();
        }
        return totalSize;
    }

    private class TouchCallable implements Callable<Void> {

        private final File file;

        public TouchCallable(File file) {
            this.file = file;
        }

        @Override
        public Void call() throws Exception {
            touchInBackground(file);
            return null;
        }
    }
}
