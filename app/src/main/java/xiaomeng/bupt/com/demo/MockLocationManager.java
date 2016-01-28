package xiaomeng.bupt.com.demo;

import android.content.Context;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.SystemClock;

/**
 * Project: Demo.
 * Data: 2016/1/27.
 * Created by 8luerain.
 * Contact:<a href="mailto:8luerain@gmail.com">Contact_me_now</a>
 */
public class MockLocationManager {

    private static MockLocationManager INSTANCE;
    private Context mContext;
    private MockLocationProvider currentProvider;
    public static final String MOCK_PROVIDER = "mockProvider";


    LocationManager manager;

    private MockLocationManager(Context context) {
        mContext = context;
        manager = (LocationManager) mContext.getSystemService(Context.LOCATION_SERVICE);


    }

    public static synchronized MockLocationManager getInstance(Context context) {
        if (null == INSTANCE) {
            INSTANCE = new MockLocationManager(context);
        }
        return INSTANCE;
    }


    public void startMock() {
        if (null == manager.getProvider(MOCK_PROVIDER)) {
            manager.addTestProvider(MOCK_PROVIDER, false, true, false, false, false, false, false, 0, 10);
        }
        manager.setTestProviderEnabled(MOCK_PROVIDER, true);

        setLocation(getCurrentProvider().getMockLocation());
    }

    public void stopMock() {

        manager.removeTestProvider(MOCK_PROVIDER);

    }


    private void setLocation(MockLocation mockLocation) {
        Location setLocation = new Location(MOCK_PROVIDER);
        setLocation.setLongitude(getCurrentProvider().getMockLocation().longtatue);
        setLocation.setLatitude(getCurrentProvider().getMockLocation().lautatue);
        setLocation.setAccuracy(10);
        setLocation.setTime(System.currentTimeMillis());
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.JELLY_BEAN_MR1)
            setLocation.setElapsedRealtimeNanos(SystemClock.elapsedRealtimeNanos());
        manager.setTestProviderLocation(MOCK_PROVIDER, setLocation);
    }


    public MockLocationProvider getCurrentProvider() {
        return currentProvider;
    }

    public void setCurrentProvider(MockLocationProvider currentProvider) {
        this.currentProvider = currentProvider;
    }

    interface MockLocationProvider {
        MockLocation getMockLocation();
    }


    public static class staticMockLocationProvider implements MockLocationProvider {

        private MockLocation mMockLocation = new MockLocation(1.1111111, 2.222222);

        public void setMockLocation(MockLocation mockLocation) {
            mMockLocation = mockLocation;
        }

        @Override
        public MockLocation getMockLocation() {
            return mMockLocation;
        }
    }


    static class MockLocation {
        public MockLocation(double longtatue, double lautatue) {
            this.longtatue = longtatue;
            this.lautatue = lautatue;
        }

        double longtatue;
        double lautatue;
    }
}
