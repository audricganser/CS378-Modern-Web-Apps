import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import static org.mockito.Mockito.*;

public class TestOpenStackMeetingsServicesImpl {
	
	OpenStackService OpenStackService;
	OpenStackMeetingsController osmc;
	OpenStackService mockEditor = null;
	
	
	@Before
	public void setup() {
		OpenStackService = new OpenStackMeetingsParamImpl();
		osmc = new OpenStackMeetingsController();
		mockEditor = mock(OpenStackService.class);		
	}
	
	@Test //1. Test when both operands are null
	public void testOpenStackWhenBothOperandsNull() {
		String ret = OpenStackService.getMeetings("_fuel", "2015");
		assertEquals("Number of meeting files: 4", ret);
	}
	
	@Test //2. Test when when operator is null
	public void testOpenStackWhenProjectDoesNotExist() {
		String ret = OpenStackService.getMeetings("Audric", "2015");
		assertEquals("Project with < audric > not found", ret);		
	}
	
	@Test //3. Test when when values array is null
	public void testOpenStackWhenYearWrong() {
		String ret = OpenStackService.getMeetings("_fuel", "1111");
		assertEquals("Project with < 1111 > not found", ret);		
	}
	
	@Test //4. Test when when values array is null
	public void testOpenStackWhenYearIsMissing() {
		String ret = OpenStackService.getMeetings("_fuel", "");
		assertEquals("Required parameter < year > missing", ret);		
	}
	
	@Test //6
	public void  testOpenStackWhenProjectIsMissing() {
		String reply = osmc.getYear("2015");
		assertEquals("Required paramter < project > is missing", reply);
		verify(mockEditor, never()).getMeetings("_fuel", "2015");
	}
	
}