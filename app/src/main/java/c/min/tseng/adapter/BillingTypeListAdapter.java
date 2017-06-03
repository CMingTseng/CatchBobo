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

public class BillingTypeListAdapter extends BaseAdapter {
    private static final String TAG = BillingTypeListAdapter.class.getSimpleName();
    private Context mContext;
    private String[] mBillingTypes;//TODO  this can get from String or xml or http(json)

    public BillingTypeListAdapter(@NonNull Context context) {
        mContext = context;
        Resources resources = context.getResources();
        mBillingTypes = resources.getStringArray(R.array.billing_type_lists);
    }

    @Override
    public int getCount() {
        return mBillingTypes.length;
    }

    @Override
    public String getItem(int position) {
        return mBillingTypes[position];
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

