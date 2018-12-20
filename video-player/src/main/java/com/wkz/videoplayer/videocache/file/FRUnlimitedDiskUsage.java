package com.wkz.videoplayer.videocache.file;

import java.io.File;
import java.io.IOException;

/**
 * Unlimited version of {@link FRDiskUsage}.
 *
 * @author Alexey Danilov (danikula@gmail.com).
 */
public class FRUnlimitedDiskUsage implements FRDiskUsage {

    @Override
    public void touch(File file) throws IOException {
        // do nothing
    }
}
