package org.example.reservation;

import org.example.reservation.ReservationApplicationTests;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import io.restassured.module.mockmvc.specification.MockMvcRequestSpecification;
import io.restassured.response.ResponseOptions;

import static org.springframework.cloud.contract.verifier.assertion.SpringCloudContractAssertions.assertThat;
import static org.springframework.cloud.contract.verifier.util.ContractVerifierUtil.*;
import static io.restassured.module.mockmvc.RestAssuredMockMvc.*;

@SuppressWarnings("rawtypes")
public class ContractVerifierTest extends ReservationApplicationTests {

	@Test
	public void validate_shouldReturnTrueBookingAvailable() throws Exception {
		// given:
			MockMvcRequestSpecification request = given();


		// when:
			ResponseOptions response = given().spec(request)
					.queryParam("roomId","16")
					.queryParam("fromDate","2021-04-12")
					.queryParam("toDate","2021-04-15")
					.get("/reservation/getAvailableReservation");

		// then:
			assertThat(response.statusCode()).isEqualTo(200);

		// and:
			String responseBody = response.getBody().asString();
			assertThat(responseBody).isEqualTo("false");
	}

}
