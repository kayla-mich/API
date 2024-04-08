import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class App {
    public static void main(String[] args) {
        String accessToken = "19304~9IQ18ukLl8JQl5oXCXe5mzl7VAUEK4FFgjBZbSUdY2LLb2YPFf9yCA1ZfxhnXeUn";

        try {
            @SuppressWarnings("deprecation")
            URL url = new URL("https://canvas.houstonisd.org/api/v1/courses?enrollment_state=active&include[]=total_scores&include[]=current_grading_period_scores");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Authorization", "Bearer " + accessToken);

            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            System.out.println(response.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
