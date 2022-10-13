package http.client;

import general.ApplicationType;
import general.ConstantsHTTP;
import okhttp3.*;

import java.io.File;
import java.io.IOException;
import java.util.Objects;
import java.util.function.Consumer;

import static general.ConstantsHTTP.UPLOAD_FILE;
import static java.net.HttpURLConnection.HTTP_OK;

public class HttpClientUtil {


    private final static SimpleCookieManager simpleCookieManager = new SimpleCookieManager();
    private final static OkHttpClient HTTP_CLIENT =
            new OkHttpClient.Builder()
                    .cookieJar(simpleCookieManager)
                    .followRedirects(false)
                    .build();
    private static String APP_CONTEXT_PATH;
    public HttpClientUtil(ApplicationType type) {
        APP_CONTEXT_PATH=type.getURLContext();
    }
    public void setCookieManagerLoggingFacility(Consumer<String> logConsumer) {
        simpleCookieManager.setLogData(logConsumer);
    }

    public void removeCookiesOf(String domain) {
        simpleCookieManager.removeCookiesOf(domain);
    }

    public void uploadFileRequest(String filePath)  {
        File file = new File(filePath);
        RequestBody body =
                new MultipartBody.Builder()
                        .addFormDataPart("file1", file.getName(),
                                RequestBody.create(file,
                                        MediaType.parse("text/plain")))
                        //.addFormDataPart("key1", "value1") // you can add multiple, different parts as needed
                        .build();
        Request request = new Request.Builder()
                .url(ConstantsHTTP.FULL_SERVER_PATH  + APP_CONTEXT_PATH+UPLOAD_FILE)
                .post(body)
                .build();
        Response response=null;
        Call call = HTTP_CLIENT.newCall(request);
        try {
             response = call.execute();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        finally {
            Objects.requireNonNull(response).close();
        }
        //  System.out.println(response.body().string());
    }
    public void doPostSync(String urlContext,String body) {
        Request request = new Request.Builder()
                .url(ConstantsHTTP.FULL_SERVER_PATH + APP_CONTEXT_PATH + urlContext)
                .post(RequestBody.create(body.getBytes()))
                .build();

        Call call = HTTP_CLIENT.newCall(request);
        executeRequest(call);
    }
    public void doPostASync(String urlContext, Callback callback,String body){

        Request request = new Request.Builder()
                .url(ConstantsHTTP.FULL_SERVER_PATH+APP_CONTEXT_PATH+urlContext)
                .post(RequestBody.create(body.getBytes()))
                .build();

        Call call = HTTP_CLIENT.newCall(request);
        call.enqueue(callback);
    }
    public String doGetSync(String urlContext) {
        Request request = new Request.Builder()
                .url(ConstantsHTTP.FULL_SERVER_PATH+APP_CONTEXT_PATH+urlContext)
                .build();

        Call call = HTTP_CLIENT.newCall(request);
        return executeRequest(call);
    }
    public void doGetASync(String urlContext, Callback callback) {
        Request request = new Request.Builder()
                .url(ConstantsHTTP.FULL_SERVER_PATH+APP_CONTEXT_PATH+urlContext)
                .build();

        Call call = HTTP_CLIENT.newCall(request);
        call.enqueue(callback);
    }

    private String executeRequest(Call call)
    {
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

    public void shutdown() {
        System.out.println("Shutting down HTTP CLIENT");
        HTTP_CLIENT.dispatcher().executorService().shutdown();
        HTTP_CLIENT.connectionPool().evictAll();
    }
}
