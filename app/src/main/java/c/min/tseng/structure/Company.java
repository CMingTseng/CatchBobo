package c.min.tseng.structure;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Neo on 2017/5/26.
 */

public class Company {
    private static final String TAG = Company.class.getSimpleName();
    @SerializedName("name")
    private String mName;
    @SerializedName("httpurl")
    private String mUrl;

    public String getCompanyName() {
        return mName;
    }

    public void setCompanyName(String name) {
        this.mName = name;
    }

    public String getCompanyUrl() {
        return mUrl;
    }

    public void setCompanyUrl(String url) {
        this.mUrl = url;
    }
}
