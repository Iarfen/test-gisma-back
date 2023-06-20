package com.testGisma;

import com.testGisma.dao.ActivitiesDAO;
import com.testGisma.model.Activity;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.CoreMatchers.is;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
public class TestGismaTests {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private ActivitiesDAO activitiesDAO;

	@Autowired
	private ObjectMapper objectMapper;

	@BeforeEach
	public void setUp(WebApplicationContext webApplicationContext) {
		this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
				.build();
	}

	@Test
	public void givenActivityObject_whenCreateActivity_thenReturnSavedActivity() throws Exception{
		Activity activity = Activity.builder()
				.id(1L)
				.description("Activity of test in order to test the POST method of the endpoint /activities")
				.current(true)
				.build();
		when(activitiesDAO.save(activity)).thenReturn(activity);

		ResultActions response = mockMvc.perform(post("/activities")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(activity)));

		response.andDo(print())
				.andExpect(status().isOk());
	}

	@Test
	public void givenListOfActivities_whenGetAllActivities_thenReturnActivitiesList() throws Exception{
		List<Activity> listActivities = new ArrayList<>();
		listActivities.add(Activity.builder().id(1L).description("This is Activity 1").current(true).build());
		listActivities.add(Activity.builder().id(2L).description("This is Activity 2").current(false).build());
		when(activitiesDAO.findAll()).thenReturn(listActivities);

		ResultActions response = mockMvc.perform(get("/activities"));

		response.andExpect(status().isOk())
				.andDo(print())
				.andExpect(jsonPath("$.size()",
						is(listActivities.size())));
	}

	@Test
	public void givenActivityId_whenGetActivityById_thenReturnActivityObject() throws Exception{
		Long activityId = 1L;
		Activity activity = Activity.builder()
				.id(activityId)
				.description("Description of Activity 1")
				.current(true)
				.build();
		when(activitiesDAO.findById(activityId)).thenReturn(Optional.of(activity));

		ResultActions response = mockMvc.perform(get("/activities/{activityId}", activityId));

		response.andExpect(status().isOk())
				.andDo(print())
				.andExpect(jsonPath("$.id", is(activity.getId().intValue())))
				.andExpect(jsonPath("$.description", is(activity.getDescription())))
				.andExpect(jsonPath("$.current", is(activity.getCurrent())));
	}

	@Test
	public void givenInvalidActivityId_whenGetActivityById_thenReturnEmpty() throws Exception{
		Long activityId = 1L;
		Activity activity = Activity.builder()
				.id(activityId)
				.description("Description of Activity 1")
				.current(true)
				.build();
		given(activitiesDAO.findById(activityId)).willReturn(Optional.empty());

		ResultActions response = mockMvc.perform(get("/activities/{activityId}", activityId));

		response.andExpect(status().isNotFound())
				.andDo(print());
	}

	@Test
	public void givenUpdatedActivity_whenUpdateActivity_thenReturnUpdateActivityObject() throws Exception{
		Long activityId = 1L;
		Activity activity = Activity.builder()
				.id(activityId)
				.description("Description of Activity 1")
				.current(true)
				.build();

		Activity updatedActivity = Activity.builder()
				.id(activityId)
				.description("Description of Activity 1")
				.current(true)
				.build();
		given(activitiesDAO.findById(activityId)).willReturn(Optional.of(activity));
		given(activitiesDAO.save(any(Activity.class)))
				.willAnswer((invocation)-> invocation.getArgument(0));

		ResultActions response = mockMvc.perform(put("/activities/{activityId}", activityId)
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(updatedActivity)));

		response.andExpect(status().isOk())
				.andDo(print());
	}

	@Test
	public void givenUpdatedActivity_whenUpdateActivity_thenReturn404() throws Exception{
		Long activityId = 1L;
		Activity activity = Activity.builder()
				.id(activityId)
				.description("Description of Activity 1")
				.current(true)
				.build();
		given(activitiesDAO.findById(activityId)).willReturn(Optional.empty());

		ResultActions response = mockMvc.perform(put("/activities/{activityId}", activityId)
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(activity)));

		response.andExpect(status().isNotFound())
				.andDo(print());
	}

	@Test
	public void givenActivityId_whenDeleteActivity_thenReturn200() throws Exception{
		Long activityId = 1L;
		Activity activity = Activity.builder()
				.id(activityId)
				.description("Description of Activity 1")
				.current(true)
				.build();
		when(activitiesDAO.findById(activityId)).thenReturn(Optional.of(activity));
		willDoNothing().given(activitiesDAO).deleteById(activityId);

		ResultActions response = mockMvc.perform(delete("/activities/{activityId}", activityId));

		response.andExpect(status().isOk())
				.andDo(print());
	}
}