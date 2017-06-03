package c.min.tseng.structure;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by Neo on 2017/5/26.
 */

public class CompanyDetail extends Company {
    private static final String TAG = CompanyDetail.class.getSimpleName();
    @SerializedName("trafficwardenlists")
    private ArrayList<Traffic_Warden> mTrafficWardens;

    @SerializedName("parkingtickettype")
    private ArrayList<ParkingTicket> mParkingTicketTypes;

    public ArrayList<Traffic_Warden> getTraffic_Wardens() {
        return mTrafficWardens;
    }

    public void setTraffic_Wardens(ArrayList<Traffic_Warden> lists) {
        this.mTrafficWardens = lists;
    }

    public void addNewTraffic_Warden(Traffic_Warden user) {
        if (mTrafficWardens == null) {
            mTrafficWardens = new ArrayList<Traffic_Warden>();
        }
        mTrafficWardens.add(user);
    }

    public ArrayList<ParkingTicket> getParkingTicketTypes() {
        return mParkingTicketTypes;
    }

    public void setmParkingTicketTypes(ArrayList<ParkingTicket> types) {
        this.mParkingTicketTypes = types;
    }

    public void addNewParkingTicketType(ParkingTicket type) {
        if (mParkingTicketTypes == null) {
            mParkingTicketTypes = new ArrayList<ParkingTicket>();
        }
        mParkingTicketTypes.add(type);
    }
}
