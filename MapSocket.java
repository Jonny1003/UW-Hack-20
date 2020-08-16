
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpClient.Version;
import java.util.LinkedList;

public class MapSocket {

    // one instance, reuse
    private final HttpClient httpClient = HttpClient.newBuilder()
            .version(HttpClient.Version.HTTP_1_1)
            .build();
    
    private String coordinateString = "";

    public static void main(String[] args) throws Exception {

        MapSocket obj = new MapSocket();

        System.out.println("Testing 1 - Send Http GET request");
        obj.sendGetPopulationInfo();
        LinkedList<int[]> L = obj.getCoordinates();
        while (!L.isEmpty()){
            int[] coord = L.pop();
            System.out.println("("+coord[0]+", "+coord[1]+", "+coord[2]+")");
        }
    }

    // request population information from server 
    public void sendGetPopulationInfo() throws Exception {

        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .uri(URI.create("https://dubhacks2020.wl.r.appspot.com/info3"))
                .setHeader("User-Agent", "Java 11 HttpClient Bot")
                .build();
        System.out.println("Request String: "+request.toString());

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());


        // print status code
        System.out.println(response.statusCode());

        // print response body
        System.out.println(response.headers());
        System.out.println("Data:");
        coordinateString = response.body();
        System.out.println(response.body());
        
    }

    // Parses internal CSV string into linked list of coordinate points
    public LinkedList<int[]> getCoordinates(){
        // process data 
        String buf = "";
        LinkedList<int[]> out = new LinkedList<int[]>();
        int[] coord = new int[3];
        boolean second = false;
        for (int i = 0; i < coordinateString.length(); i++){
            char c = coordinateString.charAt(i);
            if (Character.isDigit(c) || c == '-'){
                buf += c;
            } else if (c == ',' && second){
                try {
                    coord[1] = Integer.parseInt(buf);
                } catch (Exception e) {
                    return out;
                }
                buf = "";
                second = false;
            } else if (c == ',' && !second){
                try {
                    coord[0] = Integer.parseInt(buf);
                } catch (Exception e) {
                    return out;
                }
                buf = "";
                second = true;
            } else if (c == '\n'){
                try {
                    coord[2] = Integer.parseInt(buf);
                } catch (Exception e) {
                    return out;
                }
                buf = "";
                out.push(coord);
                coord = new int[3];
            } 
        }
        return out;
    }
}