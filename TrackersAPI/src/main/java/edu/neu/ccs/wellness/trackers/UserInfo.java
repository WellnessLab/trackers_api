package edu.neu.ccs.wellness.trackers;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.TimeZone;

import edu.neu.ccs.wellness.trackers.miband2.model.Protocol;

public class UserInfo implements Parcelable {

    public static final int BIOLOGICAL_SEX_MALE = 1;
    public static final int BIOLOGICAL_SEX_FEMALE = 0;
    public static final int OTHER = 2;

    private int uid;
    private byte biologicalSex;
    private byte age;
    private byte height;        // cm
    private byte weight;        // kg
    private String alias = "";
    private byte type;

    private UserInfo() {

    }

    public UserInfo(int uid, int biologicalSex, int age,
                    int heightCm, int weightKg, String alias, int type) {
        this.uid = uid;
        this.biologicalSex = (byte) biologicalSex;
        this.age = (byte) age;
        this.height = (byte) (heightCm & 0xFF);
        this.weight = (byte) weightKg;
        this.alias = alias;
        this.type = (byte) type;
    }

    public static UserInfo fromByteData(byte[] data) {
        if (data.length < 20) {
            return null;
        }
        UserInfo info = new UserInfo();

        info.uid = data[3] << 24 | (data[2] & 0xFF) << 16 | (data[1] & 0xFF) << 8 | (data[0] & 0xFF);
        info.biologicalSex = data[4];
        info.age = data[5];
        info.height = data[6];
        info.weight = data[7];
        info.type = data[8];
        try {
            info.alias = new String(data, 9, 8, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            info.alias = "";
        }

        return info;
    }

    public byte[] getBytes() {
        Calendar cal = GregorianCalendar.getInstance(TimeZone.getDefault());
        int birthYear = cal.get(Calendar.YEAR) - this.age;
        int birthMonth = 7;
        int birthDay = 1;
        int userId = this.alias.hashCode();

        ByteBuffer bf = ByteBuffer.allocate(16);
        bf.put(Protocol.COMMAND_SET_USERINFO);
        bf.put((byte) 0);
        bf.put((byte) 0);
        bf.put((byte) (birthYear & 0xff));
        bf.put((byte) ((birthYear >> 8) & 0xff));
        bf.put((byte) birthMonth);
        bf.put((byte) birthDay);
        bf.put(this.biologicalSex);
        bf.put((byte) (this.height & 0xff));
        bf.put((byte) ((this.height >> 8) & 0xff));
        bf.put((byte) ((this.weight >> 8) & 0xff));
        bf.put((byte) ((this.weight >> 8) & 0xff));
        bf.put((byte) (userId & 0xff));
        bf.put((byte) (userId >> 8 & 0xff));
        bf.put((byte) (userId >> 16 & 0xff));
        bf.put((byte) (userId >> 24 & 0xff));

        return bf.array();
    }

    public String toString() {
        return "uid:" + this.uid
                + ",biological sex:" + this.biologicalSex
                + ",age:" + this.age
                + ",height:" + this.getHeight()
                + ",weight:" + this.getWeight()
                + ",alias:" + this.alias
                + ",type:" + this.type;
    }

    /**
     * @return the uid
     */
    public int getUid() {
        return uid;
    }

    /**
     * @return the user's biological sex.
     */
    public byte getBiologicalSex() {
        return biologicalSex;
    }

    /**
     * @return the age
     */
    public byte getAge() {
        return age;
    }

    /**
     * @return the height in centimeters.
     */
    public int getHeight() {
        return (height & 0xFF);
    }

    /**
     * @return the weight in kilograms.
     */
    public int getWeight() {
        return weight & 0xFF;
    }

    /**
     * @return the alias
     */
    public String getAlias() {
        return alias;
    }

    /**
     * @return the type
     */
    public byte getType() {
        return type;
    }

    protected UserInfo(Parcel in) {
        uid = in.readInt();
        biologicalSex = in.readByte();
        age = in.readByte();
        height = in.readByte();
        weight = in.readByte();
        alias = in.readString();
        type = in.readByte();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(uid);
        dest.writeByte(biologicalSex);
        dest.writeByte(age);
        dest.writeByte(height);
        dest.writeByte(weight);
        dest.writeString(alias);
        dest.writeByte(type);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<UserInfo> CREATOR = new Parcelable.Creator<UserInfo>() {
        @Override
        public UserInfo createFromParcel(Parcel in) {
            return new UserInfo(in);
        }

        @Override
        public UserInfo[] newArray(int size) {
            return new UserInfo[size];
        }
    };
}