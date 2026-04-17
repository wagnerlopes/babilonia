package br.mil.eb.sermil.webservices.conectagov.apresentacao.v1;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.BeforeAll;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;
import com.fasterxml.jackson.databind.ObjectWriter;

import br.mil.eb.sermil.webservices.conectagov.Conectagov;

import org.springframework.http.MediaType;

import java.nio.charset.Charset;

@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
@SpringBootTest(classes = Conectagov.class)
@ActiveProfiles("h2")
public class BaseIntegration {

  protected MediaType contentType = new MediaType(MediaType.APPLICATION_JSON.getType(), MediaType.APPLICATION_JSON.getSubtype(), Charset.forName("utf8"));

  protected MockMvc mockMvc;

  @Autowired
  protected WebApplicationContext webApplicationContext;

  protected ObjectMapper mapper;

  private ObjectWriter ow;

  @BeforeAll
  public void setup() throws Exception {
    this.mockMvc = webAppContextSetup(this.webApplicationContext).build();
    this.mapper = new ObjectMapper();
    this.ow = this.mapper.writer().withDefaultPrettyPrinter();
  }

  protected String toJson(Object object) throws JsonProcessingException {
    return ow.writeValueAsString(object);
  }

}
