import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;

/**
 * Created by Ricardo on 29/06/2017.
 */
public class Retriever {

    private static String API_KEY = "29a3599e75cc9f95557283c10f79d4e4";
    private static JSONObject movies = new JSONObject();
    private static JSONObject shows = new JSONObject();
    private static Collection<JSONObject> moviesCollection = new ArrayList<JSONObject>();
    private static Collection<JSONObject> showsCollection = new ArrayList<JSONObject>();

    public static void main(String [] args) throws IOException, InterruptedException {

        listMovies();
        listShows();

        //movies.put("movies", new JSONArray(moviesCollection));
        //shows.put("shows", new JSONArray(showsCollection));
        //System.out.println(movies.toString());
        //System.out.println(shows.toString());
    }

    public static void listMovies() throws IOException {
        for(int i=1; i<11; i++){
            String targetURL = "https://api.themoviedb.org/3/movie/popular?api_key="+API_KEY+"&language=en-US&page="+i;
            URL url = new URL(targetURL);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();

            // optional default is GET
            con.setRequestMethod("GET");

            int responseCode = con.getResponseCode();
            //System.out.println("\nSending 'GET' request to URL : " + url);
            //System.out.println("Response Code : " + responseCode);

            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }

            in.close();

            //print result
            //System.out.println(response.toString());
            JSONObject jsonObj = new JSONObject(response.toString());
            JSONArray array = jsonObj.getJSONArray("results");
            //System.out.println("Array Len = " + array.length());
            for (Object o : array) {
                moviesCollection.add((JSONObject) o);
                //System.out.println(o);
            }
        }
    }

    public static void listShows() throws IOException, InterruptedException {
        for(int i=1; i<11; i++){
            String targetURL = "https://api.themoviedb.org/3/tv/popular?api_key="+API_KEY+"&language=en-US&page="+i;
            URL url = new URL(targetURL);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();

            // optional default is GET
            con.setRequestMethod("GET");

            int responseCode = con.getResponseCode();
            System.out.println("\nSending 'GET' request to URL : " + url);
            System.out.println("Response Code : " + responseCode);

            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }

            in.close();

            //print result
            System.out.println(response.toString());
            JSONObject jsonObj = new JSONObject(response.toString());
            JSONArray array = jsonObj.getJSONArray("results");
            System.out.println("Array Len = " + array.length());
            for (Object o : array) {
                listSeasons((JSONObject)o);
                showsCollection.add((JSONObject) o);
                Thread.sleep(2000);

                //System.out.println(o);
            }
            Thread.sleep(2000);
        }

    }

    public static void listSeasons(JSONObject obj) throws IOException, InterruptedException {
        String targetURL = "https://api.themoviedb.org/3/tv/"+obj.get("id")+"?api_key="+API_KEY+"&language=en-US";
        URL url = new URL(targetURL);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();

        // optional default is GET
        con.setRequestMethod("GET");

        int responseCode = con.getResponseCode();
        System.out.println("\nSending 'GET' request to URL : " + url);
        System.out.println("Response Code : " + responseCode);

        BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuffer response = new StringBuffer();

        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }

        in.close();
        JSONObject jsonObj = new JSONObject(response.toString());
        JSONArray Seasons = jsonObj.getJSONArray("seasons");

        int seasonNumber = 0;
        for (Object s: Seasons) {
            JSONObject season = (JSONObject) s;
            String id = season.get("id").toString();
            targetURL = "https://api.themoviedb.org/3/tv/"+obj.get("id").toString()+"/season/"+seasonNumber+"?api_key="+API_KEY+"&language=en-US";
            url = new URL(targetURL);
            con = (HttpURLConnection) url.openConnection();

            // optional default is GET
            con.setRequestMethod("GET");

            responseCode = con.getResponseCode();
            System.out.println("\nSending 'GET' request to URL : " + url);
            System.out.println("Response Code : " + responseCode);

            in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            StringBuffer newresponse = new StringBuffer();

            while ((inputLine = in.readLine()) != null) {
                newresponse.append(inputLine);
            }

            in.close();

            System.out.println(newresponse.toString());
            JSONObject newjsonObj = new JSONObject(newresponse.toString());
            JSONArray episodes = newjsonObj.getJSONArray("episodes");

            int episodeNumber = 1;
            for (Object o : episodes) {
                Thread.sleep(1000);
                listEpisodes((JSONObject)o, obj.get("id").toString(), seasonNumber, episodeNumber);
                episodeNumber ++;
                //showsCollection.add((JSONObject) o);
                //System.out.println(o);
            }
            seasonNumber ++;
            Thread.sleep(2000);

        }
    }

    public static void listEpisodes(JSONObject obj, String showID, Integer seasonID, Integer episodeID) throws IOException {
        String targetURL = "https://api.themoviedb.org/3/tv/"+showID+"/season/"+seasonID+"/episode/"+episodeID+"?api_key="+API_KEY+"&language=en-US";
        URL url = new URL(targetURL);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();

        // optional default is GET
        con.setRequestMethod("GET");

        int responseCode = con.getResponseCode();
        if(responseCode == 200) {
            System.out.println("\nSending 'GET' request to URL : " + url);
            System.out.println("Response Code : " + responseCode);

            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }

            in.close();
            JSONObject jsonObj = new JSONObject(response.toString());
            System.out.println(jsonObj.toString());
            //JSONArray episode = jsonObj.getJSONArray("seasons");
        }
    }

}
