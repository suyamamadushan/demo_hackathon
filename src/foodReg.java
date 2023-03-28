import io.restassured.RestAssured;

import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

import java.io.File;


public class foodReg {





    public static String sendRequest2(String imagePath){



        String url = "https://api.logmeal.es/v2/image/segmentation/complete/v1.0?language=eng";
        String token = "8d5a1d85bf6e4cf954800e4fe58be2e38200a622";
        File imageFile = new File(imagePath);

        RequestSpecification request = RestAssured.given();
        request.header("accept", "application/json");
        request.header("Authorization", "Bearer " + token);
        request.contentType("multipart/form-data");
        request.multiPart("image", imageFile, "image/jpeg");

        Response response = request.post(url);



        int statusCode = response.getStatusCode();
        String responseBody = response.getBody().asString();

        System.out.println("Response Status Code: " + statusCode);
        System.out.println("Response Body: " + responseBody);

        return "";


    }





}
