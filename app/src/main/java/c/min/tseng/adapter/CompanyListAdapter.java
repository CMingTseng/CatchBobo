package c.min.tseng.adapter;

import android.content.Context;
import android.content.res.Resources;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import c.min.tseng.R;

/**
 * Created by Neo on 2017/5/26.
 */

public class CompanyListAdapter extends BaseAdapter {
    private static final String TAG = CompanyListAdapter.class.getSimpleName();
    private Context mContext;
    private String[] mCompanys;//TODO  this can get from String or xml or http(json)

//    private void showCompanyList() {
//        // TODO Auto-generated method stub
//        //啟動query userauth Data的 thread
////            checkParam1="companyauth.php";
//        checkParam1 = "companyauth.php?IMEI=" + myimei;
//        Thread t_qcompauthData = new Thread(qcompauthData);
//        t_qcompauthData.start();
//        // 等待 query companyauth Data thread 完成才繼續作UI更新
//        try {
//            t_qcompauthData.join();
//        } catch (InterruptedException e) {
//            // TODO: handle exception
//            e.printStackTrace();
//        }
//
//        //將公司名單資料丟入mArea陣列並用strToken1方式處理
//        mArea = pageData1.split(strToken1);
//
//        //Spinner內容為一個陣列表
//        ArrayList<String> alluser = new ArrayList<String>();
////             for(int i=0; i<mArea.length-1; i++){ // mArea.length-1 去掉多餘的空白列(跟  php 的設定有關)
//        for (int i = 0; i < mArea.length; i++) { // mArea.length-1 去掉多餘的空白列(跟  php 的設定有關)
//            String ACompanyName[] = mArea[i].split(strToken2);
//
//            alluser.add(ACompanyName[0]);
////             Log.d("CompanyName",ACompanyName[0]);
//        }
//        ArrayAdapter<String> aAdapterArea = new ArrayAdapter<String>(this, R.layout.myspinner, alluser);
//        aAdapterArea.setDropDownViewResource(R.layout.myspinner);
//        CompanyInfo.setAdapter(aAdapterArea);
//
//    }

    public CompanyListAdapter(@NonNull Context context) {
        mContext = context;
        Resources resources = context.getResources();
        mCompanys = resources.getStringArray(R.array.default_license_company);
    }

    @Override
    public int getCount() {
        return mCompanys.length;
    }

    @Override
    public String getItem(int position) {
        return mCompanys[position];
    }

    @Override
    public long getItemId(int position) {
        return getItem(position).hashCode();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = View.inflate(parent.getContext(), android.R.layout.simple_spinner_dropdown_item, null);
            final ViewHolder holder = new ViewHolder(convertView);
            convertView.setTag(R.id.tag_view_holder, holder);
        }
        final ViewHolder holder = (ViewHolder) convertView.getTag(R.id.tag_view_holder);
        holder.mName.setText(getItem(position));
        return convertView;
    }

    private class ViewHolder {
        TextView mName;

        ViewHolder(View view) {
            mName = (TextView) view.findViewById(android.R.id.text1);
        }
    }
}

