import io.cucumber.java.en.Given;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.testng.Assert;

import static io.restassured.RestAssured.baseURI;
import static io.restassured.RestAssured.given;

public class ApiTests {

    public String bookingID;
    Response response;

    @Given("user is able to create a booking request with details {string}, {string}, {string}, {string}, {string} and booking dates as {string}, {string}")
    public void userIsAbleToCreateBookingRequestWithDetailsAndBookingDatesAs(String firstName, String additionalNeeds, String totalPrice, String depositPaid, String lastName, String checkIn, String checkOut) throws ParseException {
        RestAssured.baseURI = "https://restful-booker.herokuapp.com/booking";
        Response response = given().header("Content-Type", "application/json").body(generateJsonHeader(firstName, additionalNeeds, totalPrice, depositPaid, lastName, checkIn, checkOut)).when().post(baseURI);

        System.out.println("Post response body is: " + response.getBody().asString());
        System.out.println("Post response status code - " + response.getStatusCode());
        Assert.assertEquals(response.getStatusCode(), 200);

        JSONParser jsonParser = new JSONParser();
        JSONObject jsonObject = (JSONObject) jsonParser.parse(response.getBody().asString());
        bookingID = jsonObject.get("bookingid").toString();
        System.out.println("Booking ID is - " + bookingID);
    }

    @Given("user tries to fetch the booking details for {string}")
    public void userTriesToFetchTheBookingDetailsFor(String inValidBookingID) {
        response = RestAssured.get(baseURI + "/" + inValidBookingID);
        System.out.println("Getting the booking details for in-valid booking ID: " + inValidBookingID);

        System.out.println("get response is: " + response.getBody().asString() + "get status is " + response.getStatusCode());
        Assert.assertEquals(response.getStatusCode(), 404);

    }

    @Given("user fetches the booking details based on booking ID and verifies {string}, {string}, {string}, {string}, {string}, {string}, {string}")
    public void userFetchesTheBookinDetailsBasedOnBookingIDAndVerify(String firstName, String additionalNeeds, String totalPrice, String depositPaid, String lastName, String checkIn, String checkOut) throws ParseException {
        Response response = RestAssured.get(baseURI + "/" + bookingID);
        System.out.println("Getting the booking details for valid booking ID: " + bookingID);
        System.out.println("Get response for valid booking ID: " + response.getBody().asString());
        System.out.println("Get status for valid booking ID: " + response.getStatusCode());
        Assert.assertEquals(response.getStatusCode(), 200);

        JSONParser jsonParser = new JSONParser();
        JSONObject jsonObject = (JSONObject) jsonParser.parse(response.getBody().asString());

        Assert.assertEquals(jsonObject.get("firstname").toString(), firstName);
        Assert.assertEquals(jsonObject.get("lastname").toString(), lastName);
        Assert.assertEquals(jsonObject.get("totalprice").toString(), totalPrice);
        Assert.assertEquals(jsonObject.get("depositpaid").toString(), depositPaid);
        Assert.assertEquals(jsonObject.get("additionalneeds").toString(), additionalNeeds);
    }

    public String generateJsonHeader(String FirstName, String additionalNeeds, String totalPrice, String depositPaid, String LastName, String checkIn, String checkOut) {
        JSONObject jsonObj = new JSONObject();
        jsonObj.put("firstname", FirstName);
        jsonObj.put("lastname", LastName);
        jsonObj.put("totalprice", totalPrice);
        jsonObj.put("depositpaid", depositPaid);
        JSONObject arr1 = new JSONObject();
        arr1.put("checkin", checkIn);
        arr1.put("checkout", checkOut);
        jsonObj.put("bookingdates", arr1);
        jsonObj.put("additionalneeds", additionalNeeds);
        return jsonObj.toString();

    }

}
