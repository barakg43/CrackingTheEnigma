package http.client;

import com.google.gson.Gson;
import general.ApplicationType;
import general.ConstantsHTTP;
import general.HttpResponseDTO;
import okhttp3.*;

import java.io.File;
import java.io.IOException;
import java.util.Objects;
import java.util.function.Consumer;

import static general.ConstantsHTTP.UPLOAD_FILE;
import static java.net.HttpURLConnection.HTTP_INTERNAL_ERROR;

public class CustomHttpClient {

    public final static Gson GSON_INSTANCE = new Gson();
    private final static SimpleCookieManager simpleCookieManager = new SimpleCookieManager();
    private final static OkHttpClient HTTP_CLIENT =
            new OkHttpClient.Builder()
                    .cookieJar(simpleCookieManager)
                    .followRedirects(false)
                    .retryOnConnectionFailure(true)
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
        Call call = HTTP_CLIENT.newCall(request);
        call.enqueue(callback);

    }
    public HttpResponseDTO doPostSync(String urlContext,String body) {
        Request request = new Request.Builder()
                .url(ConstantsHTTP.FULL_SERVER_PATH + APP_CONTEXT_PATH + urlContext)
                .post(RequestBody.create(body.getBytes()))
                .build();

        Call call = HTTP_CLIENT.newCall(request);
        return executeRequest(call);
    }
    public void doPostASync(String urlContext,String body, Callback callback){

        Request request = new Request.Builder()
                .url(ConstantsHTTP.FULL_SERVER_PATH+APP_CONTEXT_PATH+urlContext)
                .post(RequestBody.create(body.getBytes()))
                .build();

        Call call = HTTP_CLIENT.newCall(request);
        call.enqueue(callback);
    }
    public HttpResponseDTO doGetSync(String urlContext) {
        Request request = new Request.Builder()
                .url(ConstantsHTTP.FULL_SERVER_PATH+APP_CONTEXT_PATH+urlContext)
                .build();

     //   System.out.println("requset:::::::: " + request);
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

    private HttpResponseDTO executeRequest(Call call)
    {
        try (Response response = call.execute()) {
            // blocking
            String body = CustomHttpClient.getResponseBodyAsString(response);
            return new HttpResponseDTO(response.code(), body);
        } catch (IOException e) {
            return new HttpResponseDTO(HTTP_INTERNAL_ERROR, e.getMessage());
        }
    }
    public static String getResponseBodyAsString(Response response)
    {
        try {
            return response.body()==null? "": Objects.requireNonNull(response.body()).string();
        } catch (IOException |NullPointerException e) {
            throw new RuntimeException(e);
        }finally{
            response.close();
        }

    }
    public void shutdown() {
        System.out.println("Shutting down HTTP CLIENT");
        HTTP_CLIENT.dispatcher().executorService().shutdown();
        HTTP_CLIENT.connectionPool().evictAll();
    }
}
