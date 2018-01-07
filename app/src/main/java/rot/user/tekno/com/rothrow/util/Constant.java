package rot.user.tekno.com.rothrow.util;

/**
 * Created by USER on 5/5/2017.
 */

public class Constant {
    public static final String BASE_URL = "http://yippytech.com:2828/";

    public static final String ENDPOINT_LOGIN = BASE_URL + "user/auth";
    public static final String ENDPOINT_REGISTER_USER = BASE_URL + "user/regis";
    public static final String ENDPOINT_GET_PROFILE = BASE_URL + "user/profile/";

    public static final String ENDPOINT_GET_ORDER = BASE_URL + "order/getdata";
    public static final String ENDPOINT_INSERT_ORDER = BASE_URL + "order/insert_order2";

    public static final String KEY_SHAREDPREFS_USER = "user";
    public static final String KEY_SHAREDPREFS_LOGIN_STATUS = "statusLogin";
    public static final String KEY_SHAREDPREFS_USER_DATA = "data";
    public static final String KEY_ID_PENGGUNA = "id";
    public static final String KEY_SHAREDPREFS_TOKEN = "token";
}
