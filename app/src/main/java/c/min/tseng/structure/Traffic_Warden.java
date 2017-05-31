package c.min.tseng.structure;

import com.google.gson.annotations.SerializedName;

import android.location.Location;

import java.util.ArrayList;
import java.util.UUID;

/**
 * Created by Neo on 2017/5/26.
 */

public class Traffic_Warden {
    private static final String TAG = Traffic_Warden.class.getSimpleName();
    @SerializedName("name")
    private String mName;
    @SerializedName("uuid")
    private UUID mUUID;
    @SerializedName("location")
    private ArrayList<Location> mLocations;

    public String getTraffic_Warden_Name() {
        return mName;
    }

    public void setTraffic_Warden_Name(String name) {
        this.mName = name;
    }

    public UUID getTraffic_Warden_UUID() {
        return mUUID;
    }

    public void setTraffic_Warden_UUID(String uuid) {
        this.mUUID = UUID.fromString(uuid);
    }

    public ArrayList<Location> getLocations() {
        return mLocations;
    }

    public void setLocations(ArrayList<Location> locations) {
        this.mLocations = locations;
    }

    public void addNewLocation(Location location) {
        if (mLocations == null) {
            mLocations = new ArrayList<Location>();
        }
        mLocations.add(location);
    }
}
