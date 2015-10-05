package core.CommunicationManager.HTTP;

import android.util.Log;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.CookieHandler;
import java.net.CookiePolicy;
import java.net.CookieManager;
import java.net.CookieStore;
import java.net.HttpCookie;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.Map;

import core.PreferenceEditor;

/**
 * Created by kakashi on 01/07/15.
 */
public class HTTP_Manager {

    public static final String DOMAIN = "tesseract-tesseract.rhcloud.com";
    public static final String HOME = "http://tesseract-tesseract.rhcloud.com/";
    public static final String FACEBOOK_LOGIN = "http://tesseract-tesseract.rhcloud.com/auth/facebook";
    public static final String GPLUS_LOGIN = "http://tesseract-tesseract.rhcloud.com/auth/google";
    public static final String TESSERACT_LOGIN="http://tesseract-tesseract.rhcloud.com/auth/tesseract";
    public static final String GET_USER_INFO = "http://tesseract-tesseract.rhcloud.com/users/get";
    public static final String SET_USER_INFO = "http://tesseract-tesseract.rhcloud.com/users/set";
    public static final String ADD_ROUTE = "http://tesseract-tesseract.rhcloud.com/routes/set";
    public static final String GET_ROUTES = "http://tesseract-tesseract.rhcloud.com/routes/get";
    public static final String ADD_TRANSACTION = "http://tesseract-tesseract.rhcloud.com/transactions/set";
    public static final String GET_TRANSACTIONS = "http://tesseract-tesseract.rhcloud.com/transactions/get";
    public static final String ADD_CAR = "http://tesseract-tesseract.rhcloud.com/cars/set";
    public static final String GET_CARS = "http://tesseract-tesseract.rhcloud.com/cars/get";
    public static final String GET_TOOLBOTHS = "http://tesseract-tesseract.rhcloud.com/toolboths/get";
    public static final String GET_NEAREST_TOOLBOTHS = "http://tesseract-tesseract.rhcloud.com/toolboths/nearest";
    public static final String START_ROUTE = "http://tesseract-tesseract.rhcloud.com/routes/start";
    public static final String END_ROUTE = "http://tesseract-tesseract.rhcloud.com/routes/end";
    public static final String REGISTER = "http://tesseract-tesseract.rhcloud.com/users/register";

    public final String USER_AGENT = "Android-Tesseract 1.0";
    private URL targetUrl;
    private HttpURLConnection connection;
    private static HTTP_Manager instance;
    private CookieManager cookieManager;
    private HttpCookie cookie;
    private String accessToken;
    private String identityProvider;
    private String loginUrl;
    private static final String TAG = "HTTP_MANAGER";

    private HTTP_Manager() {
        cookieManager = new CookieManager(null, CookiePolicy.ACCEPT_ALL);
        CookieHandler.setDefault(cookieManager);
    }
    public void init(String accessToken, String provider){
        this.identityProvider=provider;
        this.accessToken=accessToken;

        if(provider.equals("facebook"))
            loginUrl = FACEBOOK_LOGIN;
        else {
            if (provider.equals("google"))
                loginUrl = GPLUS_LOGIN;
            else
                loginUrl = TESSERACT_LOGIN;
        }

    }
    public static HTTP_Manager getInstance() {
        if (instance == null) {
            instance = new HTTP_Manager();
        }
        return instance;
    }

    private boolean fetchCookie() {

        try {
            if(accessToken==null)
                throw new Exception("NULL Access Token");
            //Building HTTP POST request
            targetUrl = new URL(loginUrl);
            connection = (HttpURLConnection) targetUrl.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("User-Agent", "Mozilla/5.0 ( compatible ) ");
            connection.setRequestProperty("Accept", "*/*");
            String urlParameter;
            if(identityProvider.equals("facebook"))
                urlParameter = "access_token=" + PreferenceEditor.getInstance().getAccessToken();
            else
                urlParameter = "email="+PreferenceEditor.getInstance().getEmail()+"&password="+PreferenceEditor.getInstance().getPassword();
            //Sending Request
            connection.setDoOutput(true);
            DataOutputStream wr = new DataOutputStream(connection.getOutputStream());
            wr.writeBytes(urlParameter);
            wr.flush();
            wr.close();
            int responseCode = connection.getResponseCode();
            Log.i(TAG, "response code fetch cookie: " +responseCode);

            if (responseCode == 200) {
                Log.i(TAG, connection.getHeaderField("set-cookie"));
                CookieStore cookieStore = cookieManager.getCookieStore();
                List<HttpCookie> cookieList = cookieStore.getCookies();
                for (HttpCookie mCookie : cookieList) {
                    Log.i(TAG, mCookie.getDomain());
                    if (mCookie.getDomain().equals(DOMAIN)) {
                        cookie = mCookie;
                        Log.i(TAG, cookie.toString());
                        connection.disconnect();
                        //return true;
                    }

                }
                return true;
            }
            connection.disconnect();
            return false;

        } catch (Exception e) {
            Log.i("Fetch cookie", e.getMessage());
            connection.disconnect();
            return false;
        }
    }

    public String doPOST(String URL, Map<String, String> parameters) {
        try {
            if (cookie == null) {
                if (!fetchCookie()) {
                    throw new Exception("Unable to fetch cookie");
                }
            }
            targetUrl = new URL(URL);
            connection = (HttpURLConnection) targetUrl.openConnection();
            connection.setRequestMethod("POST");
            //connection.setRequestProperty("User-Agent", "Mozilla/5.0 ( compatible ) ");
            connection.setRequestProperty("Accept","*/*");
            connection.setRequestProperty("Cookie", cookie.toString());

            connection.setDoOutput(true);
            Log.i(TAG, cookie.toString());

            String urlParameters = "";
            int i = 0;
            for (String key : parameters.keySet()) {
                if (i == 0) {
                    urlParameters += "?"+ key + "=" + parameters.get(key);
                } else {
                    urlParameters += "&" + key + "=" + parameters.get(key);
                }
                i++;
            }
            connection.setDoOutput(true);

            DataOutputStream wr = new DataOutputStream(connection.getOutputStream());
            wr.writeBytes(urlParameters);
            wr.flush();
            wr.close();
            int responseCode = connection.getResponseCode();
            String response = "";
            if (responseCode == 200) {
                BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String inputLine;
                while ((inputLine = in.readLine()) != null) {
                    response += inputLine;
                }
                in.close();
            }
            else{
                response="" + responseCode;
            }
            Log.i(TAG, "response code: " +responseCode);
            connection.disconnect();
            return response;

        } catch (Exception e) {
            if (!(e instanceof MalformedURLException)) {
                connection.disconnect();
            }
            Log.e(TAG, e.toString());
            return ""+401;
        }
    }

    public String doGET(String URL, Map<String, String> parameters) {


        Log.i(TAG, URL);
        try {
            if (cookie == null && !URL.equals(REGISTER) && !URL.equals(TESSERACT_LOGIN) && !URL.equals(GET_TOOLBOTHS)) {
                if (!fetchCookie())
                    throw new Exception("Unable to fetch cookie");

            }
            int i =0;
            for (String key : parameters.keySet()) {
                if(i==0)
                    URL += "?" + key + "=" + parameters.get(key);
                else
                    URL += "&" + key + "=" + parameters.get(key);
                i++;
            }

            targetUrl = new URL(URL);
            connection = (HttpURLConnection) targetUrl.openConnection();
            connection.setRequestMethod("GET");
            //connection.setRequestProperty("User-Agent", "Mozilla/5.0 ( compatible ) ");
            connection.setRequestProperty("Accept", "*/*");
            //connection.setRequestProperty("Cookie", cookie.getValue());

            //Log.i(TAG, cookie.toString());
            int responseCode = connection.getResponseCode();
            Log.i(TAG, "Response code: " + responseCode);


            String response = "";
            if (responseCode == 200) {
                connection.getContent();
                String inputLine;
                BufferedReader in = new BufferedReader(
                        new InputStreamReader(connection.getInputStream()));
                while ((inputLine = in.readLine()) != null) {
                    response += inputLine;
                }
                in.close();
            }
            else{
                response+=""+401;
            }
            connection.disconnect();
            return response;
        } catch (Exception e) {
            Log.e(TAG, e.toString());
            return ""+401;
        }
    }
}
