import java.net.InetAddress;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class demo {
    public static void main(String[] args) throws UnknownHostException {
        String das="2020-1-1 10:10:10";

        Date date=new Date();
        SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-mm-dd hh:mm:ss");
        String format = simpleDateFormat.format(date);
        int i = das.compareTo(format);
//        System.out.println(i);

        String addr = InetAddress.getLocalHost().getHostAddress();
        System.out.println(addr);
    }
}
