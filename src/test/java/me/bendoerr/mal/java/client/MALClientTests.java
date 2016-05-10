package me.bendoerr.mal.java.client;

import com.github.tomakehurst.wiremock.http.Fault;
import com.github.tomakehurst.wiremock.junit.WireMockClassRule;
import me.bendoerr.mal.java.client.model.AnimeEntry;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;

import javax.ws.rs.NotAuthorizedException;
import javax.ws.rs.ProcessingException;
import javax.ws.rs.ServiceUnavailableException;
import java.util.List;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.junit.Assert.assertEquals;

public class MALClientTests {

    public static int TEST_PORT = 8089;

    @ClassRule
    public static WireMockClassRule MAL_SERVICE = new WireMockClassRule(TEST_PORT);

    @Rule
    public WireMockClassRule malService = MAL_SERVICE;

    private MALClient client =
            new MALClient("unitTest", "unitPass", "http://localhost:" + TEST_PORT);

    @Test(expected = NullPointerException.class)
    public void test_eager_username_npe() {
        new MALClient(null, "pass");
    }

    @Test(expected = NullPointerException.class)
    public void test_eager_password_npe() {
        new MALClient("user", null);
    }

    @Test(expected = NullPointerException.class)
    public void test_eager_url_npe() {
        new MALClient("user", "pass", null);
    }

    @Test(expected = NotAuthorizedException.class)
    public void test_not_authorized() throws Exception {

        malService.stubFor(get(urlPathEqualTo("/api/anime/search.xml"))
                .willReturn(aResponse()
                        .withStatus(401)
                        .withBody("Invalid credentials")
                ));

        client.animeSearch("anything");
    }

    @Test(expected = ServiceUnavailableException.class)
    public void test_gateway_timeout() throws Exception {

        malService.stubFor(get(urlPathEqualTo("/api/anime/search.xml"))
                .willReturn(aResponse()
                        .withStatus(503)
                        .withBody("Gateway Ugh")
                ));

        client.animeSearch("anything");
    }

    @Test(expected = ProcessingException.class)
    public void test_fault_empty() throws Exception {

        malService.stubFor(get(urlPathEqualTo("/api/anime/search.xml"))
                .willReturn(aResponse()
                        .withFault(Fault.EMPTY_RESPONSE)
                ));

        client.animeSearch("anything");
    }

    @Test(expected = ProcessingException.class)
    public void test_fault_malformed() throws Exception {

        malService.stubFor(get(urlPathEqualTo("/api/anime/search.xml"))
                .willReturn(aResponse()
                        .withFault(Fault.MALFORMED_RESPONSE_CHUNK)
                ));

        client.animeSearch("anything");
    }

    @Test(expected = ProcessingException.class)
    public void test_fault_junk() throws Exception {

        malService.stubFor(get(urlPathEqualTo("/api/anime/search.xml"))
                .willReturn(aResponse()
                        .withFault(Fault.RANDOM_DATA_THEN_CLOSE)
                ));

        client.animeSearch("anything");
    }

    @Test
    public void test_animeSearch_no_results() throws Exception {
        malService.stubFor(get(urlPathEqualTo("/api/anime/search.xml"))
                .willReturn(aResponse()
                        .withStatus(204)
                ));

        List<AnimeEntry> results = client.animeSearch("something");
        assertEquals(0, results.size());
    }

    @Test
    public void test_animeSearch_one_result() throws Exception {
        malService.stubFor(get(urlPathEqualTo(MALClient.PATH_ANIME_SEARCH))
                .willReturn(aResponse()
                        .withHeader("Content-Type", "application/xml; charset=UTF-8")
                        .withBody("<?xml version=\"1.0\" encoding=\"utf-8\"?>\n" +
                                "<anime>\n" +
                                "  <entry>\n" +
                                "    <id>2889</id>\n" +
                                "    <title>Bleach - The DiamondDust Rebellion</title>\n" +
                                "    <english>Bleach: Diamond Dust Rebellion</english>\n" +
                                "    <synonyms>Bleach: The Diamond Dust Rebellion - M&Aring; \n" +
                                "    Bleach - The DiamondDust Rebellion - Mou Hitotsu no Hyourinmaru</synonyms>\n" +
                                "    <episodes>1</episodes>\n" +
                                "    <type>Movie</type>\n" +
                                "    <status>Finished Airing</status>\n" +
                                "    <start_date>2007-12-22</start_date>\n" +
                                "    <end_date>2007-12-22</end_date>\n" +
                                "    <synopsis>A valuable artifact known as &amp;quot;King's Seal&amp;quot; is stolen \n" +
                                "    by a mysterious group of people during transport in Soul Society. Hitsugaya Toushiro, \n" +
                                "    the 10th division captain of Gotei 13, who is assigned to transport the seal fights the \n" +
                                "    leader of the group and shortly after goes missing. After the incident, Seireitei declares \n" +
                                "    Hitsugaya a traitor and orders the capture and execution of Hitsugaya. Kurosaki Ichigo \n" +
                                "    refuses to believe this, and along with Matsumoto Rangiku, Kuchiki Rukia and Abarai Renji \n" +
                                "    swear to uncover the real mastermind of the stolen seal, find Hitsugaya and clear his name. \n" +
                                "    Meanwhile, a rogue Hitsugaya searches for the perpetrators and uncovers a \n" +
                                "    dark secret regarding a long dead shinigami. (from ANN)</synopsis>\n" +
                                "    <image>http://cdn.myanimelist.net/images/anime/6/4052.jpg</image>\n" +
                                "  </entry>\n" +
                                "</anime>")));

        List<AnimeEntry> result =  client.animeSearch("Something");
        assertEquals(1, result.size());

        AnimeEntry r1 = result.get(0);
        assertEquals("2889", r1.getId());
    }

    @Test
    public void test_animeSearch_two_results() throws Exception {
        malService.stubFor(get(urlPathEqualTo(MALClient.PATH_ANIME_SEARCH))
                .willReturn(aResponse()
                        .withHeader("Content-Type", "application/xml; charset=UTF-8")
                        .withBody("<?xml version=\"1.0\" encoding=\"utf-8\"?>\n" +
                                "<anime>\n" +
                                "  <entry>\n" +
                                "    <id>2889</id>\n" +
                                "    <title>Bleach - The DiamondDust Rebellion</title>\n" +
                                "    <english>Bleach: Diamond Dust Rebellion</english>\n" +
                                "    <synonyms>Bleach: The Diamond Dust Rebellion - M&Aring; \n" +
                                "    Bleach - The DiamondDust Rebellion - Mou Hitotsu no Hyourinmaru</synonyms>\n" +
                                "    <episodes>1</episodes>\n" +
                                "    <type>Movie</type>\n" +
                                "    <status>Finished Airing</status>\n" +
                                "    <start_date>2007-12-22</start_date>\n" +
                                "    <end_date>2007-12-22</end_date>\n" +
                                "    <synopsis>A valuable artifact known as &amp;quot;King's Seal&amp;quot; is stolen \n" +
                                "    by a mysterious group of people during transport in Soul Society. Hitsugaya Toushiro, \n" +
                                "    the 10th division captain of Gotei 13, who is assigned to transport the seal fights the \n" +
                                "    leader of the group and shortly after goes missing. After the incident, Seireitei declares \n" +
                                "    Hitsugaya a traitor and orders the capture and execution of Hitsugaya. Kurosaki Ichigo \n" +
                                "    refuses to believe this, and along with Matsumoto Rangiku, Kuchiki Rukia and Abarai Renji \n" +
                                "    swear to uncover the real mastermind of the stolen seal, find Hitsugaya and clear his name. \n" +
                                "    Meanwhile, a rogue Hitsugaya searches for the perpetrators and uncovers a \n" +
                                "    dark secret regarding a long dead shinigami. (from ANN)</synopsis>\n" +
                                "    <image>http://cdn.myanimelist.net/images/anime/6/4052.jpg</image>\n" +
                                "  </entry>\n" +
                                "  <entry>\n" +
                                "    <id>2890</id>\n" +
                                "    <title>Bleach - The DiamondDust Rebellion</title>\n" +
                                "    <english>Bleach: Diamond Dust Rebellion</english>\n" +
                                "    <synonyms>Bleach: The Diamond Dust Rebellion - M&Aring; \n" +
                                "    Bleach - The DiamondDust Rebellion - Mou Hitotsu no Hyourinmaru</synonyms>\n" +
                                "    <episodes>1</episodes>\n" +
                                "    <type>Movie</type>\n" +
                                "    <status>Finished Airing</status>\n" +
                                "    <start_date>2007-12-22</start_date>\n" +
                                "    <end_date>2007-12-22</end_date>\n" +
                                "    <synopsis>A valuable artifact known as &amp;quot;King's Seal&amp;quot; is stolen \n" +
                                "    by a mysterious group of people during transport in Soul Society. Hitsugaya Toushiro, \n" +
                                "    the 10th division captain of Gotei 13, who is assigned to transport the seal fights the \n" +
                                "    leader of the group and shortly after goes missing. After the incident, Seireitei declares \n" +
                                "    Hitsugaya a traitor and orders the capture and execution of Hitsugaya. Kurosaki Ichigo \n" +
                                "    refuses to believe this, and along with Matsumoto Rangiku, Kuchiki Rukia and Abarai Renji \n" +
                                "    swear to uncover the real mastermind of the stolen seal, find Hitsugaya and clear his name. \n" +
                                "    Meanwhile, a rogue Hitsugaya searches for the perpetrators and uncovers a \n" +
                                "    dark secret regarding a long dead shinigami. (from ANN)</synopsis>\n" +
                                "    <image>http://cdn.myanimelist.net/images/anime/6/4052.jpg</image>\n" +
                                "  </entry>" +
                                "</anime>")));

        List<AnimeEntry> result =  client.animeSearch("Something");
        assertEquals(2, result.size());

        AnimeEntry r1 = result.get(0);
        assertEquals("2889", r1.getId());

        AnimeEntry r2 = result.get(1);
        assertEquals("2890", r2.getId());
    }
}
