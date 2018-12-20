package com.wkz.videoplayer.videocache.file;

import java.io.File;
import java.io.IOException;

/**
 * Declares how {@link FRFileCache} will use disc space.
 *
 * @author Alexey Danilov (danikula@gmail.com).
 */
public interface FRDiskUsage {

    void touch(File file) throws IOException;

}
