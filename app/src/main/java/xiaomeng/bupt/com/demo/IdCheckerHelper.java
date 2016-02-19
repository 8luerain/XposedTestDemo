package xiaomeng.bupt.com.demo;

import android.content.Context;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Environment;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.telephony.gsm.GsmCellLocation;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import xiaomeng.bupt.com.demo.utils.CommonUtils;

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

    public IdBean getImsi() {
        IdBean bean = new IdBean();
        bean.idName = "IMSI";
        mTelecomManager = (TelephonyManager) mContext.getSystemService(Context.TELEPHONY_SERVICE);
        String imsi = null;
        if ((imsi = mTelecomManager.getSubscriberId()) != null) {
            bean.idVaule = imsi;
        }
        mData.add(bean);
        return bean;
    }


    public IdBean getSerialInfo() {
        IdBean bean = new IdBean();
        bean.idName = "SERIAL  ";
        String serial = Build.SERIAL;
        bean.idVaule = serial;
        mData.add(bean);
        return bean;
    }

    public IdBean getAndroidId() {
        IdBean bean = new IdBean();
        bean.idName = "Android ID";
        String android = Settings.Secure.getString(mContext.getContentResolver(), Settings.Secure.ANDROID_ID);
        bean.idVaule = android;
        mData.add(bean);
        return bean;
    }

    public IdBean getUniqueNum() {
        IdBean bean = new IdBean();
        bean.idName = "UniqueNum ";
        String serialnum = null;
        try {
            Class<?> c = Class.forName("android.os.SystemProperties");
            Method get = c.getMethod("get", String.class, String.class);
            serialnum = (String) (get.invoke(c, "ro.serialno", "unknown"));
            bean.idVaule = serialnum;
        } catch (Exception ignored) {
        }
        mData.add(bean);
        return bean;
    }

    public IdBean getSIMSerialId() {
        IdBean bean = new IdBean();
        bean.idName = "SIMSerialId";
        mTelecomManager = (TelephonyManager) mContext.getSystemService(Context.TELEPHONY_SERVICE);
        String simSerialNumber = mTelecomManager.getSimSerialNumber();
        bean.idVaule = simSerialNumber;
        mData.add(bean);
        return bean;
    }

    public IdBean getWifiIpAddress() {
        IdBean bean = new IdBean();
        bean.idName = "Wifi Address";
        WifiManager wifiManager = (WifiManager) mContext.getSystemService(Context.WIFI_SERVICE);
        int ipAddress = wifiManager.getConnectionInfo().getIpAddress();
        int wei = (int) (Math.random()*10);
        ipAddress=ipAddress>> wei;
        String ipAddress1 = CommonUtils.int2ip(ipAddress);
        bean.idVaule = ipAddress1;
        mData.add(bean);
        return bean;
    }


    public IdBean getPhoneNum() {
        IdBean bean = new IdBean();
        bean.idName = "phoneNum ";
        mTelecomManager = (TelephonyManager) mContext.getSystemService(Context.TELEPHONY_SERVICE);
        String phoneNum = mTelecomManager.getLine1Number();
        bean.idVaule = phoneNum;
        mData.add(bean);
        return bean;
    }


    public IdBean getBuildInfo() {
        IdBean bean = new IdBean();
        bean.idName = "Buid info";
        String model = Build.MODEL;
        String os = Build.VERSION.RELEASE;
        String fingerprint = Build.FINGERPRINT;
        String bootloader = Build.BOOTLOADER;
        String serial = Build.SERIAL;
        bean.idVaule = "fingerprint is: " + fingerprint + "\n";
        bean.idVaule += "bootloader is: " + bootloader + "\n";
        bean.idVaule += "serial is: " + serial + "\n";
        bean.idVaule += "model is: " + model + "\n";
        bean.idVaule += "os is: " + os + "\n";
        mData.add(bean);
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

//    public void getGpsLocation(final GpsLocationUpdate locationUpdateLinsenter) {
//        final IdBean bean = new IdBean("GPS");
//        LocationManager locationManager = (LocationManager) mContext.getSystemService(Context.LOCATION_SERVICE);
//        Location lastKnownLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
//        bean.idVaule = "lastlocation : " + lastKnownLocation + "\n";
//        MockLocationManager.getInstance(mContext).setCurrentProvider(new MockLocationManager.staticMockLocationProvider());
//        MockLocationManager.getInstance(mContext).setIsDynimic(true);
//        MockLocationManager.getInstance(mContext).startMock();
//        locationManager.requestLocationUpdates(MockLocationManager.MOCK_PROVIDER, 1000, 10, new LocationListener() {
//            @Override
//            public void onLocationChanged(Location location) {
//                String updateLocation = "经度：" + location.getLongitude() + " 纬度：" + location.getLatitude() + " 海拔：" + location.getAltitude() + " " +
//                        "方向：" + location.getBearing() + " 精度：" + location.getAccuracy() + "\n";
//                IdBean bean_updata = new IdBean("GPS UPDATE");
//                bean_updata.idVaule = updateLocation;
//                mData.add(bean_updata);
//                locationUpdateLinsenter.OnLocationChangeed();
//
//            }
//
//            @Override
//            public void onStatusChanged(String provider, int status, Bundle extras) {
//
//            }
//
//            @Override
//            public void onProviderEnabled(String provider) {
//
//            }
//
//            @Override
//            public void onProviderDisabled(String provider) {
//
//            }
//        });
//        mData.add(bean);
//    }


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

    public void getInternetAction() {
        new HttpRequestThread().start();
    }

    public List<IdBean> getData() {
        return mData;
    }

    interface GpsLocationUpdate {
        void OnLocationChangeed();
    }

    static class HttpRequestThread extends Thread {
        @Override
        public void run() {
            OutputStream outputStream = null;
            BufferedReader reader = null;
            try {
                String s = "wo ha ha ha ha";
                byte[] bytes = s.getBytes();
                URL url = new URL("http://www.baidu.com");
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("POST");
                outputStream = connection.getOutputStream();
                outputStream.write(bytes);
                outputStream.flush();
                InputStream inputStream = connection.getInputStream();
                reader = new BufferedReader(new InputStreamReader(inputStream));
                StringBuilder builder = new StringBuilder();
                String tmp;
                while ((tmp = reader.readLine()) != null) {
                    builder.append(tmp);
                }
                Log.d("TAG", builder.toString());
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {

            } finally {
                if (null != outputStream) {
                    try {
                        outputStream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

            }
            super.run();
        }
    }
}