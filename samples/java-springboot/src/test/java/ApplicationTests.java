import hello.Application;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.test.context.junit4.SpringRunner;

import java.net.URI;
import java.net.URISyntaxException;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT, classes = {Application.class})
public class ApplicationTests {

  @Autowired
  private TestRestTemplate template;

  @LocalServerPort
  private int port;

  @Test
  public void testFirInRange() throws URISyntaxException {
    String url = String.format("http://localhost:%s/", this.port);
    String body = "{\n" +
            "  \"_links\": {\n" +
            "    \"self\": {\n" +
            "      \"href\": \"https://foo.com\"\n" +
            "    }\n" +
            "  },\n" +
            "  \"arena\": {\n" +
            "    \"dims\": [4,3],\n" +
            "    \"state\": {\n" +
            "      \"https://foo.com\": {\n" +
            "        \"x\": 2,\n" +
            "        \"y\": 2,\n" +
            "        \"direction\": \"N\",\n" +
            "        \"wasHit\": false,\n" +
            "        \"score\": 0\n" +
            "      },\n" +
            "      \"1\": {\n" +
            "        \"x\": 2,\n" +
            "        \"y\": 0,\n" +
            "        \"direction\": \"N\",\n" +
            "        \"wasHit\": false,\n" +
            "        \"score\": 0\n" +
            "      }\n" +
            "    }\n" +
            "  }\n" +
            "}";

    RequestEntity request = RequestEntity.post(new URI(url)).contentType(MediaType.APPLICATION_JSON).body(body);
    ResponseEntity<String> response = template.exchange(request, String.class);

    assertThat(response.getBody()).isEqualTo("F");
  }
}
