package me.dpohvar.varscript.utils;

import me.dpohvar.varscript.VarScript;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;

/**
 * Created with IntelliJ IDEA.
 * User: DPOH-VAR
 * Date: 25.07.13
 * Time: 3:42
 */
public class IOStreamPackUtils {
    public static IOException exception = new IOException();

    public static void put(OutputStream out,int value) throws IOException {
        if(-127<value&&value<127){
            out.write(value);
        } else {
            out.write(-128);
            out.write(ByteBuffer.allocate(4).putInt(value).array());
        }
    }

    public static int getInt(InputStream input) throws IOException {
        int t = input.read();
        if (t == -1) throw exception;
        if(t == 128){
            byte[] buffer = new byte[4];
            if(input.read(buffer)!=buffer.length) throw exception;
            return ByteBuffer.wrap(buffer).getInt();
        } else {
            return t;
        }
    }

    public static byte getByte(InputStream input) throws IOException {
        int t = input.read();
        if (t == -1) throw exception;
        return (byte)t;
    }

    public static void put(OutputStream out,short value) throws IOException {
        if(-127<value&&value<127){
            out.write(value);
        } else {
            out.write(-128);
            out.write(ByteBuffer.allocate(2).putShort(value).array());
        }
    }

    public static int getShort(InputStream input) throws IOException {
        byte[] buffer = new byte[2];
        if(input.read(buffer)!=buffer.length) throw exception;
        return ByteBuffer.wrap(buffer).getShort();
    }

    public static void put(OutputStream out,long value) throws IOException {
        if(-127<value&&value<127){
            out.write((int)value);
        } else {
            out.write(-128);
            out.write(ByteBuffer.allocate(8).putLong(value).array());
        }
    }

    public static long getLong(InputStream input) throws IOException {
        int t = input.read();
        if (t == -1) throw exception;
        if(t == 128){
            byte[] buffer = new byte[8];
            if(input.read(buffer)!=buffer.length) throw exception;
            return ByteBuffer.wrap(buffer).getLong();
        } else {
            return t;
        }
    }

    public static void put(OutputStream out,float value) throws IOException {
        if( value%0.0==0 &&  -127<value && value<127 ){
            out.write((int)value);
        } else {
            out.write(-128);
            out.write(ByteBuffer.allocate(4).putFloat(value).array());
        }
    }

    public static float getFloat(InputStream input) throws IOException {
        int t = input.read();
        if (t == -1) throw exception;
        if(t == 128){
            byte[] buffer = new byte[4];
            if(input.read(buffer)!=buffer.length) throw exception;
            return ByteBuffer.wrap(buffer).getFloat();
        } else {
            return (float)t;
        }
    }

    public static void put(OutputStream out,double value) throws IOException {
        if( value%0.0==0 &&  -127<value && value<127 ){
            out.write((int)value);
        } else {
            out.write(-128);
            out.write(ByteBuffer.allocate(8).putDouble(value).array());
        }
    }

    public static double getDouble(InputStream input) throws IOException {
        int t = input.read();
        if (t == -1) throw exception;
        if(t == 128){
            byte[] buffer = new byte[8];
            if(input.read(buffer)!=buffer.length) throw exception;
            return ByteBuffer.wrap(buffer).getDouble();
        } else {
            return (double)t;
        }
    }

    public static void put(OutputStream out,byte[] value) throws IOException {
        put(out,value.length);
        out.write(value);
    }

    public static byte[] getBytes(InputStream input) throws IOException {
        int size = getInt(input);
        byte[] buffer = new byte[size];
        if(input.read(buffer)!=buffer.length) throw exception;
        return buffer;
    }

    public static void put(OutputStream out,String value) throws IOException {
        put(out,value.getBytes(VarScript.UTF8));
    }

    public static String getString(InputStream input) throws IOException {
        return new String(getBytes(input),VarScript.UTF8);
    }

    public static void putBytes(OutputStream out,String value) throws IOException {
        out.write(value.getBytes(VarScript.UTF8));
    }

    public static String getString(InputStream input,int length) throws IOException {
        byte[] buffer = new byte[length];
        if(input.read(buffer)!=buffer.length) throw exception;
        return new String(buffer,VarScript.UTF8);
    }
}

















