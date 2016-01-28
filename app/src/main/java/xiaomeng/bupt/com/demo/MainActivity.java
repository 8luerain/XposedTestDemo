package xiaomeng.bupt.com.demo;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    private IdCheckerHelper helper;
    private ListView mListView;
    private IdListViewAdapter mIdListViewAdapter;
    private Button mButtonGet;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initData();
        initView();

    }

    private void initView() {
        mListView = (ListView) findViewById(R.id.id_lv_main_content);
        mIdListViewAdapter = new IdListViewAdapter(this);
        mIdListViewAdapter.setmLayoutId(R.layout.item_listview_main);
        mIdListViewAdapter.setMdata(helper.getData());
        mListView.setAdapter(mIdListViewAdapter);
        mButtonGet = (Button) findViewById(R.id.id_btn_main_get);
        mButtonGet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                helper.getImei();
                helper.write2SD("wwwww");
                helper.getAndroidId();
                helper.getSerialNumber();
                helper.getMacAddress();
                helper.getImsi();
                helper.getBuildInfo();
                helper.getGpsLocation(new IdCheckerHelper.GpsLocationUpdate() {
                    @Override
                    public void OnLocationChangeed() {
                        mIdListViewAdapter.notifyDataSetChanged();
                    }
                });
                mIdListViewAdapter.notifyDataSetChanged();
            }
        });
    }

    private void initData() {
        helper = new IdCheckerHelper(this);
    }


    static class IdListViewAdapter extends BaseAdapter {
        private Context mContext;
        private List<IdBean> mdata;
        private int mLayoutId;

        public IdListViewAdapter(Context context) {
            mContext = context;
        }

        public IdListViewAdapter(List<IdBean> mdata, int mLayoutId) {
            this.mdata = mdata;
            this.mLayoutId = mLayoutId;
        }

        public List<IdBean> getMdata() {
            return mdata;
        }

        public void setMdata(List<IdBean> mdata) {
            this.mdata = mdata;
        }

        public int getmLayoutId() {
            return mLayoutId;
        }

        public void setmLayoutId(int mLayoutId) {
            this.mLayoutId = mLayoutId;
        }

        @Override
        public int getCount() {
            return mdata.size();
        }

        @Override
        public Object getItem(int position) {
            return mdata.get(position);
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (null == convertView) {
                convertView = LayoutInflater.from(mContext).inflate(mLayoutId, null);
            }
            TextView tv_title = (TextView) convertView.findViewById(R.id.id_txv_item_listview_main_title);
            TextView tv_vaule = (TextView) convertView.findViewById(R.id.id_txv_item_listview_main_cotent);
            IdBean bean = (IdBean) getItem(position);
            tv_title.setText(bean.idName);
            tv_vaule.setText(bean.idVaule);
            return convertView;
        }
    }

}
