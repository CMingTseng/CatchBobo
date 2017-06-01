package c.min.tseng.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import c.min.tseng.C;
import c.min.tseng.R;
import c.min.tseng.adapter.CompanyListAdapter;

//import org.jivesoftware.smackx.packet.StreamInitiation.File;

public class MainFragment extends Fragment {
    private static final String TAG = MainFragment.class.getSimpleName();
    private CompanyListAdapter mAdapter;

    @Override
    public void onAttach(final Context context) {
        super.onAttach(context);
        onHiddenChanged(false);
        mAdapter = new CompanyListAdapter(context);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View child = inflater.inflate(R.layout.fragment_main, container, false);
        final Spinner companylist = (Spinner) child.findViewById(R.id.companylist);
        companylist.setAdapter(mAdapter);
        final EditText account = (EditText) child.findViewById(R.id.account);
        final EditText password = (EditText) child.findViewById(R.id.password);
        final Button registered_device = (Button) child.findViewById(R.id.registered_device);
        registered_device.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Bundle arguments = new Bundle();
                arguments.putString(C.ACCOUNT_NAME, account.getText().toString());
                Fragment fragment = new FunctionFragment();
                fragment.setArguments(arguments);
                getFragmentManager().beginTransaction().setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out, android.R.anim.fade_in, android.R.anim.fade_out)
                        .replace(R.id.main_content, fragment)
                        .commit();
            }
        });
        return child;
    }
}