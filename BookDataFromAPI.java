import javax.net.ssl.*;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.cert.X509Certificate;

public class BookDataFromAPI {

   private static void trustAllHosts() {
        try {
            TrustManager[] trustAllCerts = new TrustManager[]{
                new X509TrustManager() {
                    public X509Certificate[] getAcceptedIssuers() {
                        return new X509Certificate[]{};
                    }

                    public void checkClientTrusted(X509Certificate[] certs, String authType) {
                    }

                    public void checkServerTrusted(X509Certificate[] certs, String authType) {
                    }
                }
            };

            SSLContext sc = SSLContext.getInstance("SSL");
            sc.init(null, trustAllCerts, new java.security.SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());

            HostnameVerifier allHostsValid = (hostname, session) -> true;
            HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private String getFullBookInfoFromGoogleAPI(String targetISBN){
       String requestUrl = "https://www.googleapis.com/books/v1/volumes?q=isbn:" + targetISBN;
       try {
            trustAllHosts();
            URL url = new URL(requestUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Accept", "application/json");

            if (connection.getResponseCode() != 200) {
                throw new RuntimeException("Failed : HTTP error code : " + connection.getResponseCode());
            }

            BufferedReader br = new BufferedReader(new InputStreamReader((connection.getInputStream())));
            StringBuilder response = new StringBuilder();
            String output;
            while ((output = br.readLine()) != null) {
                response.append(output);
            }
            connection.disconnect();
            return response.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    private void extractDataFromFullInfo(String fullBookInfo){
        try {
            JSONObject jsonObject = new JSONObject(fullBookInfo);
            JSONArray items = jsonObject.getJSONArray("items");
            JSONObject volumeInfo = items.getJSONObject(0).getJSONObject("volumeInfo");

            String title = volumeInfo.getString("title");
            String authors = volumeInfo.getString("authors");
            String publisher = volumeInfo.getString("publisher");
            String publishedDate = volumeInfo.getString("publishedDate");
            String description = volumeInfo.getString("description");
        }catch(Error error){
            System.out.print(error);
        }

    }


    public static void main(String[] args) {
        // 替换ISBN号码
        String isbn = "9780134685991"; // 示例ISBN，请替换为实际需要查询的ISBN
    }
}
