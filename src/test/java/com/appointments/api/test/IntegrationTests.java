package com.appointments.api.test;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MvcResult;

import com.appointments.api.entities.Appointment;
import com.appointments.api.entities.Patient;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class IntegrationTests extends AbstractTest {

	/**
	 * as a doctor I want to create my patients
	 * 
	 * Tests a successful patient creation
	 * 
	 * @throws Exception
	 */
	@Test
	public void createPatientTest() throws Exception {

		String id = null;

		try {

			final Patient patient = new Patient("Andrew", "Colins");

			final String json = mapper.writeValueAsString(patient);

			MvcResult result = mockMvc
					.perform(post(PATIENTS_URL).contentType(MediaType.APPLICATION_JSON).content(json)
							.accept(MediaType.APPLICATION_JSON))
					.andExpect(status().isCreated()).andDo(print()).andReturn();

			final Patient patientResponse = mapper.readValue(result.getResponse().getContentAsString(),
					Patient.class);

			id = patientResponse.getId();
			Assert.assertEquals(patientResponse.getName(), patient.getName());
			Assert.assertEquals(patientResponse.getSurname(), patient.getSurname());
		} finally {
			if (id != null)
				patientRepository.delete(id);
		}
	}

	/**
	 * Tests a patient without surname creation. Patient's surname has been
	 * assumed as mandatory field
	 * 
	 * @throws Exception
	 */
	@Test
	public void createInvalidPatientTest() throws Exception {

		final Patient patient = new Patient("Noah", null);

		final String json = mapper.writeValueAsString(patient);

		mockMvc.perform(post(PATIENTS_URL).contentType(MediaType.APPLICATION_JSON).content(json)
				.accept(MediaType.APPLICATION_JSON)).andExpect(status().isBadRequest()).andDo(print());
	}

	/**
	 * as a doctor I want to create appointments for a patient
	 * 
	 * Tests a successful appointment creation
	 * 
	 * @throws Exception
	 */
	@Test
	public void createAppointmentTest() throws Exception {

		String idAppointment = null;
		String idPatient = null;

		try {

			final Appointment appointment = createAppointment("Joseph", LocalDateTime.now().plusHours(2),
					LocalDateTime.now().plusHours(3));

			final String json = mapper.writeValueAsString(appointment);

			final MvcResult result = mockMvc
					.perform(post(APPOINTMENT_URL).contentType(MediaType.APPLICATION_JSON).content(json)
							.accept(MediaType.APPLICATION_JSON))
					.andExpect(status().isCreated()).andDo(print()).andReturn();

			final Appointment appointmentResponse = mapper.readValue(result.getResponse().getContentAsString(),
					Appointment.class);

			idAppointment = appointmentResponse.getId();
			idPatient = appointmentResponse.getPatientId();

			Assert.assertEquals(appointmentResponse.getPatientId(), appointment.getPatientId());
			Assert.assertEquals(appointmentResponse.getStart(), appointment.getStart());
			Assert.assertEquals(appointmentResponse.getEnd(), appointment.getEnd());
		} finally {
			if (idAppointment != null)
				appointmentRepository.delete(idAppointment);
			if (idPatient != null)
				patientRepository.delete(idPatient);
		}

	}

	/**
	 * Tests creating an appointment for an invalid user
	 * 
	 * @throws Exception
	 */
	@Test
	public void createInvalidPatientAppointmentTest() throws Exception {

		final Appointment appointment = createAppointmentInvalidPatient();

		final String json = mapper.writeValueAsString(appointment);

		mockMvc.perform(post(APPOINTMENT_URL).contentType(MediaType.APPLICATION_JSON).content(json)
				.accept(MediaType.APPLICATION_JSON)).andExpect(status().isNotFound()).andDo(print());
	}

	/**
	 * Tests start time not null protection
	 * 
	 * @throws Exception
	 */
	@Test
	public void createStartNullDateAppointmentTest() throws Exception {

		String patientId = null;

		try {

			final Appointment appointment = createAppointment("Test", null, LocalDateTime.now().plusHours(1));
			patientId = appointment.getPatientId();
			final String json = mapper.writeValueAsString(appointment);

			mockMvc.perform(post(APPOINTMENT_URL).contentType(MediaType.APPLICATION_JSON).content(json)
					.accept(MediaType.APPLICATION_JSON)).andExpect(status().isBadRequest()).andDo(print());
		} finally {
			if (patientId != null)
				patientRepository.delete(patientId);
		}

	}

	/**
	 * Tests end date is always after start
	 * 
	 * @throws Exception
	 */
	@Test
	public void createEndBeforeStartDateAppointmentTest() throws Exception {

		String patientId = null;

		try {

			final Appointment appointment = createAppointment("Test", LocalDateTime.now().plusHours(2),
					LocalDateTime.now().plusHours(1));
			patientId = appointment.getPatientId();
			final String json = mapper.writeValueAsString(appointment);

			mockMvc.perform(post(APPOINTMENT_URL).contentType(MediaType.APPLICATION_JSON).content(json)
					.accept(MediaType.APPLICATION_JSON)).andExpect(status().isBadRequest()).andDo(print());
		} finally {
			if (patientId != null)
				patientRepository.delete(patientId);
		}
	}

	/**
	 * Test invalid rating path
	 * 
	 * @throws Exception
	 */
	@Test
	public void createInvalidRateAppointmentTest() throws Exception {

		String patientId = null;

		try {

			final Appointment appointment = createAppointment("Test", LocalDateTime.now().plusHours(1),
					LocalDateTime.now().plusHours(2));
			appointment.setRate(8);
			patientId = appointment.getPatientId();
			final String json = mapper.writeValueAsString(appointment);

			mockMvc.perform(post(APPOINTMENT_URL).contentType(MediaType.APPLICATION_JSON).content(json)
					.accept(MediaType.APPLICATION_JSON)).andExpect(status().isBadRequest()).andDo(print());
		} finally {
			if (patientId != null)
				patientRepository.delete(patientId);
		}
	}

	/**
	 * as a doctor I want to see an overview of all appointments and their
	 * ratings
	 * 
	 * @throws Exception
	 */
	@Test
	public void getAppointmentsTest() throws Exception {

		List<Appointment> appointments = createAppointments();
		try {
			MvcResult result = mockMvc.perform(
					get(APPOINTMENT_URL).contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
					.andExpect(status().isOk()).andDo(print()).andReturn();

			@SuppressWarnings("unchecked")
			List<Appointment> appointmentsResponse = mapper.readValue(result.getResponse().getContentAsString(),
					List.class);

			Assert.assertEquals(appointmentsResponse.size(), appointmentRepository.count());
		} finally {

			for (Appointment appointment : appointments) {
				appointmentRepository.delete(appointment.getId());
				patientRepository.delete(appointment.getPatientId());
			}

		}
	}

	/**
	 * as a doctor I want to see an overview of the next week’s
	 * appointments
	 * 
	 * @throws Exception
	 */
	@Test
	public void getNextWeekAppointmentsTest() throws Exception {

		List<Appointment> appointments = createAppointments();

		try {
			MvcResult result = mockMvc.perform(get(APPOINTMENT_URL + "next-week")
					.contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
					.andExpect(status().isOk()).andDo(print()).andReturn();

			@SuppressWarnings("unchecked")
			List<Appointment> appointmentsResponse = mapper.readValue(result.getResponse().getContentAsString(),
					List.class);

			Assert.assertEquals(appointmentsResponse.size(), 5);
		} finally {

			for (Appointment appointment : appointments) {
				appointmentRepository.delete(appointment.getId());
				patientRepository.delete(appointment.getPatientId());
			}

		}
	}

	/**
	 * as a patient I want to see my next appointment
	 * 
	 * @throws Exception
	 */
	@Test
	public void getNextAppointmentTest() throws Exception {

		final String patientId = createPatient("John", "Stott");
		List<Appointment> appointments = createAppointmentsForPatient(patientId);

		try {
			MvcResult result = mockMvc.perform(get(PATIENTS_URL + patientId + "/next-appointment")
					.contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
					.andExpect(status().isOk()).andDo(print()).andReturn();

			Appointment appointmentResponse = mapper.readValue(result.getResponse().getContentAsString(),
					Appointment.class);

			List<Appointment> futureAppointments = appointments.stream()
					.filter(x -> x.getStart().isAfter(LocalDateTime.now())).collect(Collectors.toList());

			futureAppointments.sort(Comparator.comparing(x -> x.getStart()));

			Assert.assertEquals(appointmentResponse, futureAppointments.get(0));
		} finally {

			for (Appointment appointment : appointments) {
				appointmentRepository.delete(appointment.getId());
			}
			patientRepository.delete(patientId);

		}
	}

	//

	/**
	 * as a patient I want to rate my last appointment
	 * 
	 * @throws Exception
	 */
	@Test
	public void rateLastAppointmentTest() throws Exception {

		final String patientId = createPatient("John", "Stott");
		List<Appointment> appointments = createAppointmentsForPatient(patientId);

		final String json = "{\"rate\":\"8\"}";

		try {
			MvcResult result = mockMvc.perform(put(PATIENTS_URL + patientId + "/rate").contentType(MediaType.APPLICATION_JSON)
					.content(json).accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk()).andDo(print())
					.andReturn();

			Appointment appointmentResponse = mapper.readValue(result.getResponse().getContentAsString(),
					Appointment.class);

			List<Appointment> pastAppointments = appointments.stream()
					.filter(x -> x.getStart().isBefore(LocalDateTime.now())).collect(Collectors.toList());

			pastAppointments.sort(Comparator.comparing(x -> x.getStart()));

			Assert.assertEquals(appointmentResponse.getRate(), new Integer(8));
			Assert.assertEquals(appointmentResponse.getId(), pastAppointments.get(pastAppointments.size() - 1).getId());
		} finally {

			for (Appointment appointment : appointments) {
				appointmentRepository.delete(appointment.getId());
			}
			patientRepository.delete(patientId);

		}
	}

	/**
	 * Tests an invalid rate not between 0 and 10
	 * 
	 * @throws Exception
	 */
	@Test
	public void invalidRateTest() throws Exception {
		final String json = "{\"rate\":\"15\"}";
		mockMvc.perform(put(PATIENTS_URL + 1 + "/rate").contentType(MediaType.APPLICATION_JSON).content(json)
				.accept(MediaType.APPLICATION_JSON)).andExpect(status().isBadRequest()).andDo(print());
	}

}
