package com.wkz.videoplayer.videocache.file;

import java.io.File;

/**
 * {@link FRDiskUsage} that uses LRU (Least Recently Used) strategy and trims cache size to max files count if needed.
 *
 * @author Alexey Danilov (danikula@gmail.com).
 */
public class FRTotalCountLruDiskUsage extends FRLruDiskUsage {

    private final int maxCount;

    public FRTotalCountLruDiskUsage(int maxCount) {
        if (maxCount <= 0) {
            throw new IllegalArgumentException("Max count must be positive number!");
        }
        this.maxCount = maxCount;
    }

    @Override
    protected boolean accept(File file, long totalSize, int totalCount) {
        return totalCount <= maxCount;
    }
}
