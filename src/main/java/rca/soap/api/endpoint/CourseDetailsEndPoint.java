package rca.soap.api.endpoint;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;

import rca.soap.classc.courses.CourseDetails;
import rca.soap.classc.courses.CreateCourseDetailsRequest;
import rca.soap.classc.courses.CreateCourseDetailsResponse;
import rca.soap.classc.courses.DeleteCourseDetailsRequest;
import rca.soap.classc.courses.DeleteCourseDetailsResponse;
import rca.soap.classc.courses.GetAllCourseDetailsRequest;
import rca.soap.classc.courses.GetAllCourseDetailsResponse;
import rca.soap.classc.courses.GetCourseDetailsRequest;
import rca.soap.classc.courses.GetCourseDetailsResponse;
import rca.soap.api.bean.Course;
import rca.soap.api.repository.ICourseRepository;



@Endpoint
public class CourseDetailsEndPoint {

	@Autowired
	private ICourseRepository courseRepository;

	// method
	// request ---- GetCourseDetailsRequest
	// response --- GetCourseDetailsResponse
	@PayloadRoot(namespace = "http://soap.rca/classc/courses", localPart = "GetCourseDetailsRequest")
	@ResponsePayload
	public GetCourseDetailsResponse findById(@RequestPayload GetCourseDetailsRequest request) {

		Course course = courseRepository.findById(request.getId()).get();

		GetCourseDetailsResponse courseDetailsResponse = mapCourseDetails(course);
		return courseDetailsResponse;
	}

	@PayloadRoot(namespace = "http://soap.rca/classc/courses", localPart = "GetAllCourseDetailsRequest")
	@ResponsePayload
	public GetAllCourseDetailsResponse findAll(@RequestPayload GetAllCourseDetailsRequest request) {

		GetAllCourseDetailsResponse allCourseDetailsResponse = new GetAllCourseDetailsResponse();
		List<Course> courses = courseRepository.findAll();
		for (Course course : courses) {
			GetCourseDetailsResponse courseDetailsResponse = mapCourseDetails(course);
			allCourseDetailsResponse.getCourseDetails().add(courseDetailsResponse.getCourseDetails());
		}

		return allCourseDetailsResponse;
	}
	@PayloadRoot(namespace = "http://soap.rca/classc/courses", localPart = "CreateCourseDetailsRequest")
	@ResponsePayload
	public CreateCourseDetailsResponse save(@RequestPayload CreateCourseDetailsRequest request) {
		courseRepository.save(new Course(request.getCourseDetails().getId(),
				request.getCourseDetails().getName(),
				request.getCourseDetails().getDescription()
				));
		CreateCourseDetailsResponse courseDetailsResponse = new CreateCourseDetailsResponse();
		courseDetailsResponse.setCourseDetails(request.getCourseDetails());
		courseDetailsResponse.setMessage("Created Successfully");
		return courseDetailsResponse;
	}
	
	@PayloadRoot(namespace = "http://soap.rca/classc/courses", localPart = "DeleteCourseDetailsRequest")
	@ResponsePayload
	public DeleteCourseDetailsResponse save(@RequestPayload DeleteCourseDetailsRequest request) {
		
		courseRepository.deleteById(request.getId());
		
		DeleteCourseDetailsResponse courseDetailsResponse = new DeleteCourseDetailsResponse();
		courseDetailsResponse.setMessage("Deleted Successfully");
		return courseDetailsResponse;
	}

	private GetCourseDetailsResponse mapCourseDetails(Course course) {
		CourseDetails courseDetails = mapCourse(course);

		GetCourseDetailsResponse courseDetailsResponse = new GetCourseDetailsResponse();

		courseDetailsResponse.setCourseDetails(courseDetails);
		return courseDetailsResponse;
	}

	private CourseDetails mapCourse(Course course) {
		CourseDetails courseDetails = new CourseDetails();
		courseDetails.setDescription(course.getDescription());
		courseDetails.setId(course.getId());
		courseDetails.setName(course.getName());
		return courseDetails;
	}

}
