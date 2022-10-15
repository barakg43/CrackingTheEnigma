package http.client;

import com.google.gson.Gson;
import general.ApplicationType;
import general.ConstantsHTTP;
import okhttp3.*;

import java.io.*;
import java.util.Objects;
import java.util.Scanner;
import java.util.function.Consumer;

import static general.ConstantsHTTP.UPLOAD_FILE;
import static java.net.HttpURLConnection.HTTP_OK;

public class CustomHttpClient {

    public final static Gson GSON_INSTANCE = new Gson();
    private final static SimpleCookieManager simpleCookieManager = new SimpleCookieManager();
    private final static OkHttpClient HTTP_CLIENT =
            new OkHttpClient.Builder()
                    .cookieJar(simpleCookieManager)
                    .followRedirects(false)
                    .build();
    private static String APP_CONTEXT_PATH;
    public CustomHttpClient(ApplicationType type) {
        APP_CONTEXT_PATH=type.getURLContext();
    }
    public static void setCookieManagerLoggingFacility(Consumer<String> logConsumer) {
        simpleCookieManager.setLogData(logConsumer);
    }
    public static void removeCookiesOf(String domain) {
        simpleCookieManager.removeCookiesOf(domain);
    }
    public Gson getGson()
    {
        return  GSON_INSTANCE;
    }
    public void uploadFileRequest(String filePath,Callback callback)  {
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
        call.enqueue(callback);
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
    public void doPostASync(String urlContext,String body, Callback callback){

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
            return response.body()!=null ? Objects.requireNonNull(response.body()).string() :null;
        } catch (IOException e) {
            System.out.println("Error: " + e.getMessage());
        }
        finally {
            if (response != null)
                response.close();

        }
        return null;
    }
    public String readFromInputStream(InputStream inputStream) throws IOException {
        inputStream.reset();
        return new Scanner(inputStream).useDelimiter("\\Z").next();
    }
    public void shutdown() {
        System.out.println("Shutting down HTTP CLIENT");
        HTTP_CLIENT.dispatcher().executorService().shutdown();
        HTTP_CLIENT.connectionPool().evictAll();
    }
}
