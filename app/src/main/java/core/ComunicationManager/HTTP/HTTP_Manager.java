package core.ComunicationManager.HTTP;

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

/**
 * Created by kakashi on 01/07/15.
 */
public class HTTP_Manager {

    public static final String DOMAIN = "tesseract-tesseract.rhcloud.com";
    public static final String HOME = "http://tesseract-tesseract.rhcloud.com/";
    public static final String FACEBOOK_LOGIN = "http://tesseract-tesseract.rhcloud.com/auth";
    public static final String GPLUS_LOGIN = "";
    public static final String TESSERACT_LOGIN="";
    public static final String GET_USER_INFO = "http://tesseract-tesseract.rhcloud.com/users/getInfo";
    public static final String SET_USER_INFO = "http://tesseract-tesseract.rhcloud.com/users/setInfo";
    public static final String ADD_ROUTE="http://tesseract-tesseract.rhcloud.com/routes/set";
    public static final String GET_ROUTES="http://tesseract-tesseract.rhcloud.com/routes/get";
    public static final String ADD_TRANSACTION="http://tesseract-tesseract.rhcloud.com/transactions/set";
    public static final String GET_TRANSACTIONS="http://tesseract-tesseract.rhcloud.com/transactions/get";
    public static final String ADD_CAR="http://tesseract-tesseract.rhcloud.com/cars/set";
    public static final String GET_CARS="http://tesseract-tesseract.rhcloud.com/cars/get";
    public static final String GET_TOOLBOTHS = "http://tesseract-tesseract.rhcloud.com/toolboth/get";
    public static final String GET_NEAREST_TOOLBOTHS= "http://tesseract-tesseract.rhcloud.com/toolboth/nearest";



    public final String USER_AGENT = "Android-Tesseract 1.0";
    private URL targetUrl;
    private HttpURLConnection connection;
    private static HTTP_Manager instance;
    private CookieManager cookieManager;
    private HttpCookie cookie;
    private String accessToken;
    private String identityProvider;
    private String loginUrl;

    private HTTP_Manager() {
        cookieManager = new CookieManager(null, CookiePolicy.ACCEPT_ALL);
        CookieHandler.setDefault(cookieManager);
    }
    public void init(String accessToken, String provider){
        this.identityProvider=provider;
        this.accessToken=accessToken;

        if(provider.equals("facebook"))
            loginUrl=FACEBOOK_LOGIN;
        else
            if(provider.equals("google"))
                loginUrl=GPLUS_LOGIN;
            else
                loginUrl=TESSERACT_LOGIN;


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
            connection.setRequestProperty("User-Agent", USER_AGENT);
            String urlParameter = "access_token=" + accessToken;
            //Sending Request
            connection.setDoOutput(true);
            DataOutputStream wr = new DataOutputStream(connection.getOutputStream());
            wr.writeBytes(urlParameter);
            wr.flush();
            wr.close();
            int responseCode = connection.getResponseCode();
            if (responseCode == 200) {
                CookieStore cookieStore = cookieManager.getCookieStore();
                List<HttpCookie> cookieList = cookieStore.getCookies();
                for (HttpCookie mCookie : cookieList) {
                    if (mCookie.getDomain().equals(DOMAIN)) {
                        cookie = mCookie;
                        connection.disconnect();
                        return true;
                    }

                }
            }
            return false;

        } catch (Exception e) {
            Log.i("Fetch cookie", e.getMessage());
            return false;
        }
    }

    public String doPOST(String URL, Map<String, String> parameters) {
        try {
            targetUrl = new URL(URL);
            connection = (HttpURLConnection) targetUrl.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("User-Agent", USER_AGENT);
            if (cookie == null) {
                if (!fetchCookie()) {
                    throw new Exception("Unable to fetch cookie");
                }
            }
            connection.setRequestProperty("Cookie", cookie.toString());
            String urlParameters = "";
            int i = 0;
            for (String key : parameters.keySet()) {
                if (i == 0) {
                    urlParameters += key + "=" + parameters.get(key);
                } else {
                    urlParameters += "?" + key + "=" + parameters.get(key);
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
            if (responseCode == 401) {
                response="" + responseCode;
            }
            connection.disconnect();
            return response;

        } catch (Exception e) {
            if (!(e instanceof MalformedURLException)) {
                connection.disconnect();
            }
            return null;
        }
    }

    public String doGET(String URL, Map<String, String> parameters) {

        for (String key : parameters.keySet()) {
            URL += "?" + key + "=" + parameters.get(key);
        }
        try {
            targetUrl = new URL(URL);
            connection = (HttpURLConnection) targetUrl.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("User-Agent", USER_AGENT);
            if (cookie == null) {
                if (!fetchCookie()) {
                    throw new Exception("Unable to fetch cookie");
                }
            }
            connection.setRequestProperty("Cookie", cookie.toString());
            connection.getContent();
            int responseCode = connection.getResponseCode();
            String response = "";
            if (responseCode == 200) {
                String inputLine;
                BufferedReader in = new BufferedReader(
                        new InputStreamReader(connection.getInputStream()));
                while ((inputLine = in.readLine()) != null) {
                    response += inputLine;
                }
                in.close();
            }
            if(responseCode==401){
                response+=""+responseCode;
            }
            connection.disconnect();
            return response;
        } catch (Exception e) {

            return null;
        }
    }
}
