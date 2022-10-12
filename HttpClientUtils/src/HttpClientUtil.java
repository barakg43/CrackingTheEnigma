import general.ApplicationType;
import general.ConstantsHTTP;
import okhttp3.*;

import java.io.IOException;
import java.util.Objects;
import java.util.function.Consumer;

import static general.ConstantsHTTP.FULL_SERVER_PATH;
import static java.net.HttpURLConnection.HTTP_OK;

public class HttpClientUtil {
    private final String BASE_URL = "http://localhost:8080";
    private final ApplicationType type;
    private static String APP_CONTEXT_PATH;
    public HttpClientUtil(ApplicationType type) {
        this.type=type;
        APP_CONTEXT_PATH=type.getURLContext();
    }

    private final static SimpleCookieManager simpleCookieManager = new SimpleCookieManager();
    private final static OkHttpClient HTTP_CLIENT =
            new OkHttpClient.Builder()
                    .cookieJar(simpleCookieManager)
                    .followRedirects(false)
                    .build();

    public static void setCookieManagerLoggingFacility(Consumer<String> logConsumer) {
        simpleCookieManager.setLogData(logConsumer);
    }

    public static void removeCookiesOf(String domain) {
        simpleCookieManager.removeCookiesOf(domain);
    }

    public static void runAsync(String urlContext, Callback callback) {
        Request request = new Request.Builder()
                .url(ConstantsHTTP.FULL_SERVER_PATH+APP_CONTEXT_PATH+urlContext)
                .build();

        Call call = HTTP_CLIENT.newCall(request);

        call.enqueue(callback);
    }

    public static String getBodyRunSync(String urlContext) {
        Request request = new Request.Builder()
                .url(ConstantsHTTP.FULL_SERVER_PATH + APP_CONTEXT_PATH + urlContext)
                .build();

        Call call = HTTP_CLIENT.newCall(request);
         Response response = null;
        try {
            // blocking
             response = call.execute();
            if (response.code() != HTTP_OK)
                throw new RuntimeException(Objects.requireNonNull(response.body()).string());
            return Objects.requireNonNull(response.body()).string();
        } catch (IOException e) {
            System.out.println("Error: " + e.getMessage());
        }
        finally {
            Objects.requireNonNull(response).close();
        }
        return null;
    }
    public static void shutdown() {
        System.out.println("Shutting down HTTP CLIENT");
        HTTP_CLIENT.dispatcher().executorService().shutdown();
        HTTP_CLIENT.connectionPool().evictAll();
    }
}
