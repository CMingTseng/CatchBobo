package c.min.tseng.structure;

import com.google.gson.annotations.SerializedName;

import android.location.Location;
import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
import java.util.UUID;

/**
 * Created by Neo on 2017/5/26.
 */

public class ParkingTicket {
    private static final String TAG = ParkingTicket.class.getSimpleName();
    public static final int TRANSPORT_TYPE_CAR = 0;
    public static final int TRANSPORT_TYPE_MOTO = 1;

    @IntDef({
            TRANSPORT_TYPE_CAR, TRANSPORT_TYPE_MOTO,
    })
    @Retention(RetentionPolicy.SOURCE)
    public @interface Transsport_Type {
    }

    public static final int VALUATION_TYPE_STABLE = 0;
    public static final int VALUATION_TYPE_PROGRESSIVE = 1;

    @IntDef({
            VALUATION_TYPE_STABLE, VALUATION_TYPE_PROGRESSIVE,
    })
    @Retention(RetentionPolicy.SOURCE)
    public @interface Valuation_Type {
    }

    @Transsport_Type
    @SerializedName("transsport")
    private int mTranssport_Type;

    @Valuation_Type
    @SerializedName("transsport")
    private int mValuation_Type;

    @SerializedName("uuid")
    private UUID mUUID;
    @SerializedName("datetime")
    private ArrayList<Long> mDateTimes;
    @SerializedName("trafficwarden")
    private String mTrafficWarden;
    @SerializedName("location")
    private Location mLocation;
    @SerializedName("basiccost")
    private int mBasicCost;
    @SerializedName("total")
    private int mTotalCost;

    public ParkingTicket(String uuid, @Transsport_Type int transport, @Valuation_Type int valuation, String trafficwarden, Location location, ArrayList<Long> datetimes, int basiccost) {
        setParkingTicketUUID(uuid);
        setTransportType(transport);
        setValuationType(valuation);
        mTrafficWarden = trafficwarden;
        mLocation = location;
        mDateTimes = datetimes;
        mBasicCost = basiccost;
    }

    public void setParkingTicketUUID(String uuid) {
        if (uuid != null) {
            mUUID = UUID.fromString(uuid);
        } else {
            mUUID = UUID.randomUUID();
        }
    }

    public UUID getParkingTicketUUID() {
        return mUUID;
    }

    public void setTransportType(@Transsport_Type int transport) {
        mTranssport_Type = transport;
    }

    @Transsport_Type
    public int getTransportType() {
        return mTranssport_Type;
    }

    public void setValuationType(@Valuation_Type int valuation) {
        mValuation_Type = valuation;
    }

    @Valuation_Type
    public int getValuationType() {
        return mValuation_Type;
    }

    public void addNewDateTime(long datetime) {
        if (mDateTimes == null) {
            mDateTimes = new ArrayList<Long>();
        }
        mDateTimes.add(datetime);
    }
}
