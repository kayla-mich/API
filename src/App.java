import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class App {
    public static void main(String[] args) {
        String accessToken = "19304~9IQ18ukLl8JQl5oXCXe5mzl7VAUEK4FFgjBZbSUdY2LLb2YPFf9yCA1ZfxhnXeUn";

        try {

            //This grabs the link to see you're courses 
            @SuppressWarnings("deprecation")
            URL coursesUrl  = new URL("https://canvas.houstonisd.org/api/v1/courses?enrollment_state=active&include[]=total_scores&include[]=current_grading_period_scores");
            HttpURLConnection conn = (HttpURLConnection) coursesUrl.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Authorization", "Bearer " + accessToken);

            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            String coursesResponse = response.toString();
            Pattern courseIdPattern = Pattern.compile("\"id\":(\\d+),");
            Matcher matcher = courseIdPattern.matcher(coursesResponse);

            while (matcher.find()) {
                String courseId = matcher.group(1);
                String courseName = getCourseName(coursesResponse, courseId);
                System.out.println("Course: " + courseName);

                String assignmentsResponse = getAssignmentsResponse(courseId, accessToken);
                printMissingAssignments(assignmentsResponse);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static String getCourseName(String coursesResponse, String courseId) {
        Pattern courseNamePattern = Pattern.compile("\"id\":" + courseId + ",\"name\":\"([^\"]+)\"");
        Matcher matcher = courseNamePattern.matcher(coursesResponse);
        if (matcher.find()) {
            return matcher.group(1);
        }
        return null;
    }

    private static String getAssignmentsResponse(String courseId, String accessToken) throws Exception {
        @SuppressWarnings("deprecation")
        URL assignmentsUrl = new URL("https://canvas.houstonisd.org/api/v1/courses/" + courseId + "/assignments?include[]=submission");
        HttpURLConnection conn = (HttpURLConnection) assignmentsUrl.openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("Authorization", "Bearer " + accessToken);

        BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        String inputLine;
        StringBuffer response = new StringBuffer();

        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();

        return response.toString();
    }

    private static void printMissingAssignments(String assignmentsResponse) {
        Pattern assignmentNamePattern = Pattern.compile("\"name\":\"([^\"]+)\"");
        Matcher matcher = assignmentNamePattern.matcher(assignmentsResponse);
        while (matcher.find()) {
            String assignmentName = matcher.group(1);
            System.out.println("Missing assignment: " + assignmentName);
        }
    }
}





         //   System.out.println(response.toString());
       // } catch (Exception e) {
        //    e.printStackTrace();
     