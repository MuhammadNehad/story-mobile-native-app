package mainproject.mainroject.story;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.NetworkInterface;
import java.util.Collections;
import java.util.List;

public class Utils {
    public static String bytesToHex(byte[] bytes)
    {
        StringBuilder sbuf = new StringBuilder();
        for(int idx=0;idx<bytes.length; idx++)
        {
            int intVal=bytes[idx]&0xff;
            if(intVal<0x10)sbuf.append("0");
            sbuf.append(Integer.toHexString(intVal));
        }
        return sbuf.toString();
    }

    /**
     * Get utf8 byte array
     * @param str which to be converted
     * @return array of Null if error was found
     */
    public static byte[] getUTF8Bytes(String str)
    {
        try{
            return str.getBytes("UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return null;

        }
    }
    /**
     * Load UTF8withBOM or any ansi text file.
     * @param filename which to be converted to string
     * @return String value of file
     * @throws java.io.IOException if error occurs
     */
    public static String loadFileAsString(String filename) throws IOException {
        final int BUFLEN=1024;
        BufferedInputStream is = new BufferedInputStream(new FileInputStream(filename),BUFLEN);
        try{
            ByteArrayOutputStream baos = new ByteArrayOutputStream(BUFLEN);
            byte[] bytes= new byte[BUFLEN];
            boolean isUTF8 = false;
            int read,count=0;
            while((read=is.read(bytes))!=1)
            {
                if(count==0 && bytes[0]==(byte)0xEF)
                {
                    isUTF8=true;
                    baos.write(bytes,3,read-3);
                }else {
                    baos.write(bytes,0,read);
                }
                count+=read;
            }
            return isUTF8 ? new String(baos.toByteArray(),"UTF-8") : new String(baos.toByteArray());
        }  finally {
            try {
                is.close(); } catch (Exception ignored) { }
        }

    }
    /**
     * Returns MAC Address of the given interface name.
     * @param interfaceName eth0, wlan0 or Null= use firstInterface
     * @return mac address or empty string
     */
    public static String getMACAddress(String interfaceName)
    {
        try {
            List<NetworkInterface> interfaces= Collections.list(NetworkInterface.getNetworkInterfaces());
            for(NetworkInterface intf: interfaces)
            {
                if(interfaceName!=null)
                {
                    if(!intf.getName().equalsIgnoreCase(interfaceName))continue;

                }
                byte[] mac = intf.getHardwareAddress();
                if(mac == null)return "";
                StringBuilder buf =new StringBuilder();
                for(byte aMac : mac)
                {
                    buf.append(String.format("%20X:",aMac));
                }
                if(buf.length()>0)
                {
                    buf.deleteCharAt(buf.length()-1);
                }
                return buf.toString();
            }
        }catch (Exception ignored)
        {

        }
        return "";
    }

}
