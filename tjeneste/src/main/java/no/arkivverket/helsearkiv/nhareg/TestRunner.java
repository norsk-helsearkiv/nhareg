package no.arkivverket.helsearkiv.nhareg;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLContextBuilder;
import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
import org.apache.http.entity.BasicHttpEntity;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;

/**
 * Created by haraldk on 26.03.15.
 */
public class TestRunner {
    public static void main(String[] args) throws KeyStoreException, NoSuchAlgorithmException, KeyManagementException {




            try {
                SSLContextBuilder builder = new SSLContextBuilder();
                builder.loadTrustMaterial(null, new TrustSelfSignedStrategy());
                SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(builder.build(),
                        SSLConnectionSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
                final CloseableHttpClient c = HttpClients.custom().setSSLSocketFactory(
                        sslsf).build();
               // runPasientjournaler(c);
                runDiagnose(c);
    //        HttpClient c = HttpClientBuilder.create().build();

            } catch (Throwable t) {
                t.printStackTrace();
            }

    }

    public static void runDiagnose(HttpClient c) throws Exception{
        String url = "https://localhost:8443/api/pasientjournaler/<uuid>/diagnoser";
        for (int j=21;j<Puuids.uuids.length;j++) {
            String urlid =Puuids.uuids[j];
            url = url.replace("<uuid>", urlid);
            final HttpPost post = new HttpPost(url);
            post.addHeader("Content-Type", "application/json;charset=UTF-8");
            post.addHeader("Authorization", "Basic TkhBQnJ1a2VyMToxMjM=");
            post.addHeader("Accept", "application/json, text/plain, */*");
            for (int i=0;i<10;i++) {
                post.setEntity(new StringEntity("{\"diagnosedato\":\"1950\",\"diagnosetekst\":\"Tarminfeksjon forårsaket av andre organismer\",\"diagnosekode\":\"008\"}", "UTF-8"));
                HttpResponse res = c.execute(post);
                HttpEntity n = res.getEntity();
                String value = inputStreamToString(n.getContent());
                System.out.println(value);
            }
            System.out.println(urlid+" got 10 diagnoser( "+j+"/"+Puuids.uuids.length);
        }
    }

    public static void runPasientjournaler(HttpClient c) throws Exception{
        for (int i =0;i<10000;i++) {
            final HttpPost post = new HttpPost("https://localhost:8443/api/avleveringer/avlevering/pasientjournaler");
            post.addHeader("Content-Type", "application/json;charset=UTF-8");
            post.addHeader("Authorization", "Basic TkhBQnJ1a2VyMToxMjM=");
            post.addHeader("Accept", "application/json, text/plain, */*");
            post.setEntity(new StringEntity("{\"lagringsenheter\": [\"enhet\"], \"journalnummer\": \"1\", \"navn\": \"Navn\", \"kjonn\": \"M\", \"fodt\": \"1900\", \"dod\": \"mors\"}"));

            HttpResponse res = c.execute(post);
            HttpEntity n = res.getEntity();
            String value = inputStreamToString(n.getContent());
            int idstart = value.indexOf("uuid");
            idstart += 7;
            int idEnd = value.indexOf("\"", idstart);
            String id = value.substring(idstart, idEnd);
            System.out.println("\"" + id + "\",");
        }
    }
    // Fast Implementation
    private static String inputStreamToString(InputStream is) throws Exception{
        String line = "";
        StringBuilder total = new StringBuilder();

        // Wrap a BufferedReader around the InputStream
        BufferedReader rd = new BufferedReader(new InputStreamReader(is));

        // Read response until the end
        while ((line = rd.readLine()) != null) {
            total.append(line);
        }

        // Return full string
        return total.toString();
    }

}
