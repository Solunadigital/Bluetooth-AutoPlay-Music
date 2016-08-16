package maderski.bluetoothautoplaymusic;

import android.content.Context;
import android.media.AudioManager;
import android.test.AndroidTestCase;
import android.test.suitebuilder.annotation.SmallTest;

/**
 * Created by Jason on 8/3/16.
 */
public class SingletonTests extends AndroidTestCase {

    @Override
    protected void setUp() throws Exception {
        super.setUp();
    }

    @SmallTest
    public void testWakeLockNotNull(){
        assertNotNull(ScreenONLock.getInstance());
    }

    @SmallTest
    public void testWakeLockIsEnabled(){
        ScreenONLock.getInstance().enableWakeLock(getContext());
        assertEquals(true, ScreenONLock.getInstance().wakeLockHeld());
    }

    @SmallTest
    public void testWakeLockIsDisabled(){
        ScreenONLock.getInstance().releaseWakeLock();
        assertEquals(false, ScreenONLock.getInstance().wakeLockHeld());
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }
}
