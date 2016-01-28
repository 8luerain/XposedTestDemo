package xiaomeng.bupt.com.demo;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.telephony.gsm.GsmCellLocation;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by rain on 2016/1/22.
 */
public class IdCheckerHelper {

    private Context mContext;

    private List<IdBean> mData = new ArrayList<>();
    private TelephonyManager mTelecomManager;
    private GpsLocationUpdate locationListener;

    public IdCheckerHelper(Context mContext) {
        this.mContext = mContext;
    }

    public IdBean getImei() {
        IdBean bean = new IdBean();
        bean.idName = "IMEI";
        mTelecomManager = (TelephonyManager) mContext.getSystemService(Context.TELEPHONY_SERVICE);
        String imei = null;
        if ((imei = mTelecomManager.getDeviceId()) != null) {
            bean.idVaule = imei;
            mData.add(bean);
        }

        return bean;
    }

    public IdBean getBuildInfo() {
        IdBean bean = new IdBean();
        bean.idName = "Buid info";
        String fingerprint = Build.FINGERPRINT;
        String bootloader = Build.BOOTLOADER;
        String serial = Build.SERIAL;
        bean.idVaule = "fingerprint is: " + fingerprint + "\n";
        bean.idVaule += "bootloader is: " + bootloader + "\n";
        bean.idVaule += "serial is: " + serial + "\n";
        mData.add(bean);
        return bean;
    }

    public IdBean getImsi() {
        IdBean bean = new IdBean();
        bean.idName = "IMSI";
        mTelecomManager = (TelephonyManager) mContext.getSystemService(Context.TELEPHONY_SERVICE);
        String imsi = null;
        if ((imsi = mTelecomManager.getSubscriberId()) != null) {
            bean.idVaule = imsi;
            mData.add(bean);
        }

        return bean;
    }

    public IdBean getPhoneStatus() {
        IdBean bean = new IdBean();
        bean.idName = "Phone statues";
        mTelecomManager = (TelephonyManager) mContext.getSystemService(Context.TELEPHONY_SERVICE);
        String networkOperator = mTelecomManager.getNetworkOperator();
        GsmCellLocation gsmCellLocation = (GsmCellLocation) mTelecomManager.getCellLocation();
        bean.idVaule = networkOperator;
        bean.idVaule += "位置编号: " + gsmCellLocation.getLac() + "  基站ID是：" + gsmCellLocation.getCid() + "\n ";
        mData.add(bean);
        return bean;
    }

    public void getGpsLocation(final GpsLocationUpdate locationUpdateLinsenter) {
        final IdBean bean = new IdBean("GPS");
        LocationManager locationManager = (LocationManager) mContext.getSystemService(Context.LOCATION_SERVICE);
        Location lastKnownLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        bean.idVaule = "lastlocation : " + lastKnownLocation + "\n";
        MockLocationManager.getInstance(mContext).setCurrentProvider(new MockLocationManager.staticMockLocationProvider());
        MockLocationManager.getInstance(mContext).setIsDynimic(true);
        MockLocationManager.getInstance(mContext).startMock();
        locationManager.requestLocationUpdates(MockLocationManager.MOCK_PROVIDER, 1000, 10, new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                String updateLocation = "经度：" + location.getLongitude() + " 纬度：" + location.getLatitude() + " 海拔：" + location.getAltitude() + " " +
                        "方向：" + location.getBearing() + " 精度：" + location.getAccuracy() + "\n";
                IdBean bean_updata = new IdBean("GPS UPDATE");
                bean_updata.idVaule = updateLocation;
                mData.add(bean_updata);
                locationUpdateLinsenter.OnLocationChangeed();

            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {

            }
        });
        mData.add(bean);
    }

    public IdBean getAndroidId() {
        IdBean bean = new IdBean();
        bean.idName = "Android ID";
        String android = Settings.Secure.getString(mContext.getContentResolver(), Settings.Secure.ANDROID_ID);
        bean.idVaule = android;
        mData.add(bean);
        return bean;
    }

    //Serial Number
    public IdBean getSerialNumber() {
        IdBean bean = new IdBean();
        bean.idName = "Serial Number";
        bean.idVaule = Build.SERIAL + " board id " + Build.BOARD + "id" + Build.ID + " manufactory" + Build.MANUFACTURER + " MODEL " + Build.MODEL
                + "TAGs  " + Build.TAGS;
        mData.add(bean);
        return bean;
    }

    public String getMacAddress() {
        IdBean bean = new IdBean();
        bean.idName = "exec mac";
        String macAddress = null;
        String str = "";
        try {
            //linux下查询网卡mac地址的命令
            Process pp = Runtime.getRuntime().exec("cat /sys/class/net/wlan0/address");
            InputStreamReader ir = new InputStreamReader(pp.getInputStream());
            LineNumberReader input = new LineNumberReader(ir);

            for (; null != str; ) {
                str = input.readLine();
                if (str != null) {
                    macAddress = str.trim();// 去空格
                    break;
                }
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        bean.idVaule = macAddress;
        mData.add(bean);
        return macAddress;
    }

    public void write2SD(String fileName) {
        boolean isMounted = Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
        if (isMounted) {
            FileWriter writer = null;
            try {
                String rootPath = Environment.getExternalStorageDirectory().getCanonicalPath();
                if (!TextUtils.isEmpty(rootPath)) {
                    File file = new File(rootPath + "/" + fileName);
                    Log.d("TAG", "file is ---->" + file.getCanonicalPath());
                    writer = new FileWriter(file);
                    writer.write("wo  ri a ");
                    Toast.makeText(mContext, "writer file to" + file.getAbsolutePath(), Toast.LENGTH_SHORT).show();
                }

            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (null != writer) {
                    try {
                        writer.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    public List<IdBean> getData() {
        return mData;
    }

    interface GpsLocationUpdate {
        void OnLocationChangeed();
    }
}
