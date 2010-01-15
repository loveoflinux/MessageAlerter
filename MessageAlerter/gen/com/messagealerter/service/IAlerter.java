/*
 * This file is auto-generated.  DO NOT MODIFY.
 * Original file: /home/ogi/work/workspace/MessageAlerter/src/com/messagealerter/service/IAlerter.aidl
 */
package com.messagealerter.service;
import java.lang.String;
import android.os.RemoteException;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Binder;
import android.os.Parcel;
import com.messagealerter.data.TwoDByteArray;
public interface IAlerter extends android.os.IInterface
{
/** Local-side IPC implementation stub class. */
public static abstract class Stub extends android.os.Binder implements com.messagealerter.service.IAlerter
{
private static final java.lang.String DESCRIPTOR = "com.messagealerter.service.IAlerter";
/** Construct the stub at attach it to the interface. */
public Stub()
{
this.attachInterface(this, DESCRIPTOR);
}
/**
 * Cast an IBinder object into an IAlerter interface,
 * generating a proxy if needed.
 */
public static com.messagealerter.service.IAlerter asInterface(android.os.IBinder obj)
{
if ((obj==null)) {
return null;
}
android.os.IInterface iin = (android.os.IInterface)obj.queryLocalInterface(DESCRIPTOR);
if (((iin!=null)&&(iin instanceof com.messagealerter.service.IAlerter))) {
return ((com.messagealerter.service.IAlerter)iin);
}
return new com.messagealerter.service.IAlerter.Stub.Proxy(obj);
}
public android.os.IBinder asBinder()
{
return this;
}
@Override public boolean onTransact(int code, android.os.Parcel data, android.os.Parcel reply, int flags) throws android.os.RemoteException
{
switch (code)
{
case INTERFACE_TRANSACTION:
{
reply.writeString(DESCRIPTOR);
return true;
}
case TRANSACTION_feedPDUs:
{
data.enforceInterface(DESCRIPTOR);
com.messagealerter.data.TwoDByteArray _arg0;
if ((0!=data.readInt())) {
_arg0 = com.messagealerter.data.TwoDByteArray.CREATOR.createFromParcel(data);
}
else {
_arg0 = null;
}
this.feedPDUs(_arg0);
reply.writeNoException();
return true;
}
case TRANSACTION_acknowledge:
{
data.enforceInterface(DESCRIPTOR);
int _arg0;
_arg0 = data.readInt();
this.acknowledge(_arg0);
reply.writeNoException();
return true;
}
}
return super.onTransact(code, data, reply, flags);
}
private static class Proxy implements com.messagealerter.service.IAlerter
{
private android.os.IBinder mRemote;
Proxy(android.os.IBinder remote)
{
mRemote = remote;
}
public android.os.IBinder asBinder()
{
return mRemote;
}
public java.lang.String getInterfaceDescriptor()
{
return DESCRIPTOR;
}
public void feedPDUs(com.messagealerter.data.TwoDByteArray pdus) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
if ((pdus!=null)) {
_data.writeInt(1);
pdus.writeToParcel(_data, 0);
}
else {
_data.writeInt(0);
}
mRemote.transact(Stub.TRANSACTION_feedPDUs, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
public void acknowledge(int alert) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeInt(alert);
mRemote.transact(Stub.TRANSACTION_acknowledge, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
}
static final int TRANSACTION_feedPDUs = (IBinder.FIRST_CALL_TRANSACTION + 0);
static final int TRANSACTION_acknowledge = (IBinder.FIRST_CALL_TRANSACTION + 1);
}
public void feedPDUs(com.messagealerter.data.TwoDByteArray pdus) throws android.os.RemoteException;
public void acknowledge(int alert) throws android.os.RemoteException;
}
