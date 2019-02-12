package edu.studyup.serviceImpl;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import edu.studyup.entity.Event;
import edu.studyup.entity.Location;
import edu.studyup.entity.Student;
import edu.studyup.util.DataStorage;
import edu.studyup.util.StudyUpException;

class EventServiceImplTest {

	EventServiceImpl eventServiceImpl;

	@BeforeAll
	static void setUpBeforeClass() throws Exception {
	}

	@AfterAll
	static void tearDownAfterClass() throws Exception {
	}

	@BeforeEach
	void setUp() throws Exception {
		eventServiceImpl = new EventServiceImpl();
		//Create Student
		Student student = new Student();
		student.setFirstName("John");
		student.setLastName("Doe");
		student.setEmail("JohnDoe@email.com");
		student.setId(1);
		
		//Create Event1
		Event event = new Event();
		event.setEventID(1);
		event.setDate(new Date());
		event.setName("Event 1");
		Location location = new Location(-122, 37);
		event.setLocation(location);
		List<Student> eventStudents = new ArrayList<>();
		eventStudents.add(student);
		event.setStudents(eventStudents);
		
		DataStorage.eventData.put(event.getEventID(), event);
	}

	@AfterEach
	void tearDown() throws Exception {
		DataStorage.eventData.clear();
	}

	@Test
	void testUpdateEventName_GoodCase() throws StudyUpException {
		int eventID = 1;
		eventServiceImpl.updateEventName(eventID, "Renamed Event 1");
		assertEquals("Renamed Event 1", DataStorage.eventData.get(eventID).getName());
	}
	
	@Test
	void testUpdateEvent_WrongEventID_badCase() {
		int eventID = 3;
		Assertions.assertThrows(StudyUpException.class, () -> {
			eventServiceImpl.updateEventName(eventID, "Renamed Event 3");
		  });
	}
	
	@Test
	void testUpdateEventName_20charater() throws StudyUpException {
		int eventID = 1;
		eventServiceImpl.updateEventName(eventID, "12345678901234567890");
		assertEquals("12345678901234567890", DataStorage.eventData.get(eventID).getName());
	}
	
	@Test
	void testUpdateEventName_badcase() {
		int eventID = 1;
		Assertions.assertThrows(StudyUpException.class, () -> {
			eventServiceImpl.updateEventName(eventID, "123456789012345678901");
		  });
	}

	@SuppressWarnings("deprecation")
	@Test
	void testgetActiveEvent_goodcase() {
		Event event = new Event();
		event.setEventID(1);
		event.setDate(new Date(2019,12,13)); /*future event*/
		DataStorage.eventData.put(event.getEventID(), event);
		List<Event> myactive = eventServiceImpl.getActiveEvents();
		assertEquals(1, myactive.size());	
	}

	@Test
	void testgetActiveEvent_badcase() {
		Event event = new Event();
		event.setEventID(1);
		event.setDate(new Date(10)); /*past event*/
		DataStorage.eventData.put(event.getEventID(), event);
		List<Event> myactive = eventServiceImpl.getActiveEvents();
		assertEquals(0, myactive.size());	
	}
	
	@SuppressWarnings("deprecation")
	@Test
	void testgetPastEvent_goodcase() {
		Event event = new Event();
		event.setEventID(1);
		event.setDate(new Date(2019,12,13)); /*future event*/
		Event event2 = new Event();
		event2.setEventID(2);
		event2.setDate(new Date(10)); /*past event*/
		DataStorage.eventData.put(event.getEventID(), event);
		DataStorage.eventData.put(event2.getEventID(), event2);
		List<Event> mypast = eventServiceImpl.getPastEvents();
		assertEquals (1,mypast.size());
	}

	@Test
	void testgetPastEvent_goodcase2() {
		Event event = new Event();
		event.setEventID(1);
		event.setDate(new Date(15)); /*past event*/
		Event event2 = new Event();
		event2.setEventID(2);
		event2.setDate(new Date(10)); /*past event*/
		DataStorage.eventData.put(event.getEventID(), event);
		DataStorage.eventData.put(event2.getEventID(), event2);
		List<Event> mypast = eventServiceImpl.getPastEvents();
		assertEquals (2,mypast.size());
	}
	
	@Test
	void testAddStudenttoEvent_goodcase() throws StudyUpException {
		int eventID = 1;
		
		Student student = new Student();
		student.setFirstName("John");
		student.setLastName("Doe");
		student.setEmail("JohnDoe@email.com");
		student.setId(2);
		
		eventServiceImpl.addStudentToEvent (student, eventID);
		assertEquals (2, DataStorage.eventData.get(eventID).getStudents().size());
	}
	
	@Test
	void testAddStudenttoEvent_badcase() throws StudyUpException {
		int eventID = 1;
		
		Student student = new Student();
		student.setFirstName("John");
		student.setLastName("Doe");
		student.setEmail("JohnDoe@email.com");
		student.setId(2);
		
		Student student2 = new Student();
		student2.setFirstName("John");
		student2.setLastName("Doe");
		student2.setEmail("JohnDoe@email.com");
		student2.setId(3);
		
		eventServiceImpl.addStudentToEvent (student, eventID);
		
		Assertions.assertThrows(StudyUpException.class, () -> {
			eventServiceImpl.addStudentToEvent (student2, eventID);;
		  });
	}
	
	@Test
	void testAddStudenttoEvent_invalidevent() throws StudyUpException {
		int eventID = 4;
		
		Student student = new Student();
		student.setFirstName("John");
		student.setLastName("Doe");
		student.setEmail("JohnDoe@email.com");
		student.setId(2);
		
		Assertions.assertThrows(StudyUpException.class, () -> {
			eventServiceImpl.addStudentToEvent (student, eventID);;
		  });
	}
	
	@Test
	void testAddStudenttoEvent_invalidstudent() throws StudyUpException {
		Event event = new Event();
		event.setEventID(2);
		
		DataStorage.eventData.put(event.getEventID(),event);
		
		Student student = new Student();
		
		int eventID = 2;
		eventServiceImpl.addStudentToEvent(student, eventID);
		assertEquals(1,DataStorage.eventData.get(eventID).getStudents().size());
	}
	
	@Test
	void testdeleteEvent () {
		int eventID = 1;
		assertEquals(1,eventServiceImpl.deleteEvent(eventID).getEventID()); 
	}
	
	@Test
	void testdeleteEvent_invalidevent() {
		int eventID = 2;
		assertNull(eventServiceImpl.deleteEvent(eventID));
	}
}
