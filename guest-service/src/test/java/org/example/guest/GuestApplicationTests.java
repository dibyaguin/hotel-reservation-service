//package org.example.guest;
//
//import org.example.guest.service.helper.ReservationFeignClient;
//import org.junit.jupiter.api.Assertions;
//import org.junit.jupiter.api.Test;
//import org.junit.runner.RunWith;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
//import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.cloud.contract.stubrunner.spring.AutoConfigureStubRunner;
//import org.springframework.cloud.contract.stubrunner.spring.StubRunnerProperties;
//import org.springframework.test.context.junit4.SpringRunner;
//
//
//@RunWith(SpringRunner.class)
//@AutoConfigureMockMvc
//@AutoConfigureJsonTesters
//@SpringBootTest
//		(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
//				properties = {"reservation.service.getAvailableReservation.path=/reservation/getAvailableReservation",
//                "reservation.service.name=reservation-service"})
//@AutoConfigureStubRunner(
//		stubsMode = StubRunnerProperties.StubsMode.LOCAL,
//		ids = "org.example:reservation-service:+:stubs:9090")
//class GuestApplicationTests {
//
//	@Value("${reservation.service.name}")
//	private String serviceName;
//
//	@Value("${reservation.service.getAvailableReservation.path}")
//	private String path;
//
//
//	@Autowired
//	ReservationFeignClient reservationFeignClient;
//
////	@Test
////	void contextLoads() {
////	}
//
//	@Test
//	void getAvailableReservationTest() {
//		System.out.println("===========================================");
//		System.out.println(serviceName);
//		System.out.println(path);
//		System.out.println("===========================================");
//
//		Boolean response = reservationFeignClient.getAvailableReservation(
//				16, "2021-04-12", "2021-04-15");
//		Assertions.assertFalse(response);
//
//	}
//
//}
//
//
