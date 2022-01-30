package starter.stepdefinitions;

import static net.serenitybdd.rest.SerenityRest.restAssuredThat;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import net.serenitybdd.rest.SerenityRest;
import org.apache.http.HttpStatus;

public class SearchStepDefinitions extends BaseTest{

    private static String product = ServiceConstants.BLANK_STRING;

    @Given("the request with the given product {string}")
    public void heCallsEndpoint(String product) {
        this.product = product;
        getData(product, true);
    }

    @Then("validate the processed {string} request")
    public void heSeesTheResultsDisplayedForApple(String expectedOutput) {
        if(expectedOutput.equalsIgnoreCase(MessageConstants.VALID_MESSAGE)) {
            restAssuredThat(response -> response.statusCode(HttpStatus.SC_OK));
        }
        else if(expectedOutput.equalsIgnoreCase(MessageConstants.INVALID_MESSAGE)){
            restAssuredThat(response -> response.statusCode(HttpStatus.SC_NOT_FOUND));
            restAssuredThat(response -> response.extract().jsonPath().getObject(ServiceConstants.BLANK_STRING, WaarkoopModal.class)
                    .getDetail().getError().equals(Boolean.TRUE));
            restAssuredThat(response -> response.extract().jsonPath().getObject(ServiceConstants.BLANK_STRING, WaarkoopModal.class)
                    .getDetail().getMessage().equals(ServiceConstants.NOT_FOUND));
            restAssuredThat(response -> response.extract().jsonPath().getObject(ServiceConstants.BLANK_STRING, WaarkoopModal.class)
                    .getDetail().getRequested_item().equals(product));
            restAssuredThat(response -> response.extract().jsonPath().getObject(ServiceConstants.BLANK_STRING, WaarkoopModal.class)
                    .getDetail().getServed_by().equals(SERVICE_PROVIDER));
        }
        else{
            restAssuredThat(response -> response.statusCode(HttpStatus.SC_UNAUTHORIZED));
            restAssuredThat(response -> response.extract().jsonPath().getObject(ServiceConstants.BLANK_STRING, BlankDetailModal.class)
                    .getDetail().equals(expectedOutput));
        }
        product = ServiceConstants.BLANK_STRING;
    }

    @Given("the requested url does not exist")
    public void urlLinkNotExist() {
        getData(ServiceConstants.BLANK_STRING, false);
    }

    @Then("validate the response as url not found")
    public void validateUrlNotExist(){
        restAssuredThat(response -> response.statusCode(HttpStatus.SC_NOT_FOUND));
        restAssuredThat(response -> response.extract().jsonPath().getObject(ServiceConstants.BLANK_STRING, BlankDetailModal.class)
                .getDetail().equals(ServiceConstants.NOT_FOUND));
    }

    private void getData(String endPoint, boolean isApiRequired){
        SerenityRest.given()
                .get(BASE_URI+(isApiRequired?API_URI:ServiceConstants.BLANK_STRING)+endPoint);
    }
}