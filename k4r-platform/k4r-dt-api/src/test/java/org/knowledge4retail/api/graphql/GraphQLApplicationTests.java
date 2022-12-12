package org.knowledge4retail.api.graphql;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.knowledge4retail.api.test.AbstractIntegrationTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

//@RunWith(SpringRunner.class)
//@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class GraphQLApplicationTests extends AbstractIntegrationTest {

    private static final String URI = "/graphql";
    private static final String URI2 = "/playground?operationName=stores";

    @BeforeEach
    @Override
    public void setUp() throws Exception {

        super.setUp();
    }

    @Test
    public void graphQLTestWithStores() throws Exception {
        String expectedResponse = "{\"data\":{\"foods\":[" +
                "{\"id\":4,\"name\":\"Avocado\",\"isGood\":false}" +
                "]}}";

        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.get(URI2)
                .content("query store{ store(id: \"1000\") { id storeName addressCountry} }")
                //.content("/queries/find-stores.graphql")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                //.andExpect(content().json(expectedResponse))
                .andReturn();
    }
}


// Json directly
    /*@Test
    public void testGraphqlArgumentsJson() throws Exception {
        String json = "{\"query\": \"{Book(title: \\\"title\\\"){title genre}\", \"arguments\": {\"title\": \"title\"}}";
        ok(json);
        verify(executor).execute("{Book(title: \"title\"){title genre}", null);
    }*/







    /*@Autowired
    ApplicationContext applicationContext;

    void assertThatTestTemplateAutoConfigurationWorksCorrectly() throws IOException {
        // GIVEN
        final GraphQLTestTemplate testTemplate = applicationContext.getBean(GraphQLTestTemplate.class);
        // WHEN - THEN
        testTemplate
                .postForResource("queries/find-stores.graphql")
                .assertThatNoErrorsArePresent()
                .assertThatField("$.data.testQuery")
                .asString()
                .isNotEmpty();
    }*/

    /*@Autowired
    private GraphQLTestTemplate graphQLTestTemplate;

    @Test
    public void testGraphQLWithStores() throws IOException {

        GraphQLResponse findResponse = graphQLTestTemplate.perform("queries/find-stores.graphql", "stores");
        assertTrue(findResponse.isOk());

        /*MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.get(ITEM_URI)
                .accept(MediaType.APPLICATION_JSON_VALUE)).andReturn();
        assertResponseCodeEquals(HttpStatus.OK, mvcResult);

        //GraphQLInputQuery query = new GraphQLInputQuery("query BookQuery($title: String!){Book(title: $title){title genre}}");

        // asserts to check the content
    }*/


