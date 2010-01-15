package com.messagealerter.data;

import android.os.Parcel;
import android.os.Parcelable;

public class TwoDByteArray implements Parcelable {

	private final byte[][] data;
	
	public TwoDByteArray(byte[][] data) {
		this.data = data;
	}

	public byte[][] getData() {
		return data;
	}

	public static final Parcelable.Creator<TwoDByteArray> CREATOR =
		new Parcelable.Creator<TwoDByteArray>() {

			public TwoDByteArray createFromParcel(Parcel source) {
				int count = source.readInt();
				
				byte[][] data = new byte[count][];
				
				for (int i = 0; i < count; i++)
					data[i] = source.createByteArray();
				
				return new TwoDByteArray(data);
			}
	
			public TwoDByteArray[] newArray(int size) {
				// TODO Auto-generated method stub
				return null;
			}
		
	};

	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	public void writeToParcel(Parcel dest, int flags) {
		
		dest.writeInt(data.length);
		for (byte[] array : data)
			dest.writeByteArray(array);
			
	}

}
