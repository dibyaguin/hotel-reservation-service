package org.example.reservation;

import org.example.reservation.controller.ReservationController;
import io.restassured.module.mockmvc.RestAssuredMockMvc;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

//@SpringBootTest
//class ReservationApplicationTests {
//
//	@Test
//	void contextLoads() {
//	}
//
//}

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class ReservationApplicationTests {

	@Autowired
	private ReservationController reservationController;

	@BeforeEach
	void setup() {
		RestAssuredMockMvc.standaloneSetup(reservationController);
	}
}


//@RunWith(SpringRunner.class)
//@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
//public class ReservationApplicationTests {
//
//	@Autowired
//	ReservationController reservationController;
//
//	@MockBean
//	private ReservationRepository reservationRepository;
//
//	@MockBean
//	private ReservationServiceImpl reservationServiceImpl;
//
//	@BeforeEach
//	public void setup() {
//		//Book book= new Book("123", "Ferok Book", "Fero Hero");
//		when(reservationRepository.findByRoomIdAndReservationFromDateAndReservationToDate(
//				anyInt(), any(Date.class), any(Date.class))).thenReturn(Optional.of(new ReservationEntity()));
////
////		when(reservationServiceImpl.getAvailableReservation(
////				anyInt(), any(Date.class), any(Date.class))).thenReturn(false);
////
////		when(reservationController.getAvailableReservation(anyInt(), any(Date.class), any(Date.class)))
////				.thenReturn(false);
//
//		StandaloneMockMvcBuilder standaloneMockMvcBuilder = MockMvcBuilders.standaloneSetup(reservationController);
//		RestAssuredMockMvc.standaloneSetup(standaloneMockMvcBuilder);
//	}
//
//}