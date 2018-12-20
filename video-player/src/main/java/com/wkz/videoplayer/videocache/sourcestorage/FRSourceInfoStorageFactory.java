package com.wkz.videoplayer.videocache.sourcestorage;

import android.content.Context;

/**
 * Simple factory for {@link FRSourceInfoStorage}.
 *
 * @author Alexey Danilov (danikula@gmail.com).
 */
public class FRSourceInfoStorageFactory {

    public static FRSourceInfoStorage newSourceInfoStorage(Context context) {
        return new FRDatabaseSourceInfoStorage(context);
    }

    public static FRSourceInfoStorage newEmptySourceInfoStorage() {
        return new FRNoSourceInfoStorage();
    }
}
