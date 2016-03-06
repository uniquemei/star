package yu.cleaner.util;

/**
 * Created by Administrator on 2015/10/5.
 */
public class UrlUtils {

    //http://192.168.253.1:8080/Jiejing/cleaner?cleaner=1
    //http://192.168.253.1:8080/Jiejing/fly?action=order
//    public static String ROOT = "http://172.18.71.1:8080";
//    public static String ROOT = "http://121.42.206.157:8080";
    public static String ROOT = "http://172.17.154.1:8080";
    public static String ROOT_URL = ROOT+"/Jiejing/";
    public static String IMG_URL = ROOT+"/Jiejing/";
    public static String SHOW_ITEMS_URL = ROOT_URL+"item";//展示所有服务项目的信息
    public static String CLEANER_URL = ROOT_URL+"cleaner";//所有阿姨
    public static String BLACKCLEANER_URL = ROOT_URL+"blackCleaner";//与用户关联的阿姨
    public static String COMMONPLACE_URL = ROOT_URL+"address";//与常用地址有关的
    public static String ORDER_URL = ROOT_URL+"order";//订单详情
    public static String USER_URL = ROOT_URL+"user";//用户信息


}
