package me.bendoerr.mal.java.client;

import lombok.NonNull;
import me.bendoerr.mal.java.client.model.AnimeEntry;
import me.bendoerr.mal.java.client.model.AnimeListEntry;
import me.bendoerr.mal.java.client.model.AnimeListEntryValues;
import org.glassfish.jersey.client.ClientConfig;
import org.glassfish.jersey.client.authentication.HttpAuthenticationFeature;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.Form;
import javax.ws.rs.core.GenericType;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import java.io.StringWriter;
import java.util.List;

import static java.util.Collections.emptyList;
import static javax.ws.rs.client.Entity.entity;
import static javax.ws.rs.core.MediaType.*;

/**
 * A Java-based client to the Documented MyAnimeList API as well as undocumented usages (which will be prefixed with
 * '{@code x_}' to denote that they are not listed in the official documentation.
 * <p>
 * The {@code myanimelist.net} domain is not hosted behind TLS and as such username and password will be passed in
 * cleartext just as it does when you login to the website.
 *
 * @see <a href="http://myanimelist.net/modules.php?go=api">http://myanimelist.net/modules.php?go=api</a>
 */
public class MALClient implements AutoCloseable {

    public static final String MAL_URL = "http://myanimelist.net";
    public static final String PATH_ANIME_SEARCH = "/api/anime/search.xml";
    public static final String PATH_ANIME_ADD = "/api/animelist/add/%id.xml";
    public static final String PATH_ANIME_UPDATE = "/api/animelist/update/%id.xml";
    public static final String PATH_ANIME_DELETE = "/api/animelist/delete/%id.xml";
    public static final String X_PATH_MALAPPINFO = "/malappinfo.php";

    private final Client client;
    private final String malUrl;

    /**
     * Create a new instance of the MALClient given a set of credentials.
     *
     * @param username {@code [required]} of the MAL user
     * @param password {@code [required]} of the MAL user
     * @throws NullPointerException if any of the parameters are null
     */
    public MALClient(
            @NonNull final String username,
            @NonNull final String password) {

        this(username, password, MAL_URL);
    }

    /**
     * Create a new instance of the MALClient using a non-standard SCHEME, AUTHORITY or root PATH.
     *
     * @param username {@code [required]} of the MAL user
     * @param password {@code [required]} of the MAL user
     * @param malUrl {@code [required]} the base URL of the MAL domain.
     * @throws NullPointerException if any of the parameters are null
     */
    public MALClient(
            @NonNull final String username,
            @NonNull final String password,
            @NonNull final String malUrl) {

        final HttpAuthenticationFeature feature = HttpAuthenticationFeature
                .basicBuilder()
                .credentials(username, password)
                .build();

        client = ClientBuilder.newClient(
                new ClientConfig()
                        .register(feature));

        this.malUrl = malUrl;
    }

    /**
     * Execute a search query against the MAL database.
     * <p>
     * <b>Documented API</b>
     * <p>
     * If the search query returns no results MAL will return an HTTP {@code 204 No Content} status code. This method
     * will return an empty list in that case rather than {@code null}.
     * <p>
     * A successful query will produce something like the following that will be marshaled into a POJO.
     * <pre>
     * {@code
     * <?xml version="1.0" encoding="utf-8"?>
     * <anime>
     *   <entry>
     *     <id>2889</id>
     *     <title>Bleach - The DiamondDust Rebellion</title>
     *     <english>Bleach: Diamond Dust Rebellion</english>
     *     <synonyms>Bleach: The Diamond Dust Rebellion - M&Aring;
     *       Bleach - The DiamondDust Rebellion - Mou Hitotsu no Hyourinmaru</synonyms>
     *     <episodes>1</episodes>
     *     <type>Movie</type>
     *     <status>Finished Airing</status>
     *     <start_date>2007-12-22</start_date>
     *     <end_date>2007-12-22</end_date>
     *     <synopsis>A valuable artifact known as &amp;quot;King's Seal&amp;quot; is stolen
     *       by a mysterious group of people during transport in Soul Society. Hitsugaya Toushiro,
     *       the 10th division captain of Gotei 13, who is assigned to transport the seal fights the
     *       leader of the group and shortly after goes missing. After the incident, Seireitei declares
     *       Hitsugaya a traitor and orders the capture and execution of Hitsugaya. Kurosaki Ichigo
     *       refuses to believe this, and along with Matsumoto Rangiku, Kuchiki Rukia and Abarai Renji
     *       swear to uncover the real mastermind of the stolen seal, find Hitsugaya and clear his name.
     *       Meanwhile, a rogue Hitsugaya searches for the perpetrators and uncovers a
     *       dark secret regarding a long dead shinigami. (from ANN)</synopsis>
     *     <image>http://cdn.myanimelist.net/images/anime/6/4052.jpg</image>
     *   </entry>
     * </anime>
     * }
     * </pre>
     *
     * @param query to run against the MAL database.
     * @return a list of marshaled records based on the search or an empty list if no results were returned.
     * @throws NullPointerException             if query is not provided.
     * @throws javax.ws.rs.ClientErrorException if MAL returns a HTTP {@code 4xx} status code
     * @throws javax.ws.rs.ServerErrorException if MAL returns a HTTP {@code 5xx} status code
     * @throws javax.ws.rs.ProcessingException  if we can't understand the response
     */
    public List<AnimeEntry> animeSearch(
            @NonNull final String query) {

        final List<AnimeEntry> results = client.target(malUrl)
                .path(PATH_ANIME_SEARCH)
                .queryParam("q", query)
                .request(APPLICATION_XML_TYPE)
                .get(new GenericType<List<AnimeEntry>>() {});

        if (results == null)
            return emptyList();

        return results;
    }

    /**
     * Add Anime to the authenticated user's Anime List.
     * <p>
     * <b>Documented API</b>
     * <p>
     * Note that this will not also "update" anime that are already in the anime list and will not error or fail if an
     * anime is already in the list. It's up to the caller to somehow check if the anime is already in the list and then
     * use the {@link #animeListUpdate(String, AnimeListEntryValues)} method instead.
     * <p>
     * Given an invalid ID or an ID of an anime already on the list this API endpoint does not return an error.
     * Successfully calling this method without error provides very little assurance that the anime has been added
     * the the anime list.
     *
     * @param id maldb id of the anime to add
     * @param record of the values to add along with the anime to the list
     * @throws NullPointerException if any of the parameters are null
     * @throws javax.ws.rs.ClientErrorException if MAL returns a HTTP {@code 4xx} status code
     * @throws javax.ws.rs.ServerErrorException if MAL returns a HTTP {@code 5xx} status code
     * @throws javax.ws.rs.ProcessingException  if we can't understand the response
     */
    public void animeListAdd(
            @NonNull final String id,
            @NonNull final AnimeListEntryValues record) {

        final Form form = new Form();
        form.param("data", xml(record, record.getClass()));

        client.target(malUrl)
                .path(PATH_ANIME_ADD.replace("%id", id))
                .request(WILDCARD_TYPE)
                .post(entity(form, APPLICATION_FORM_URLENCODED_TYPE));
    }

    /**
     * Update Anime in the authenticated user's Anime List.
     * <p>
     * <b>Documented API</b>
     * <p>
     * Given an invalid ID or an ID of an anime already on the list this API endpoint does not return an error.
     * Successfully calling this method without error provides very little assurance that the anime has been added
     * the the anime list.
     *
     * @param id maldb id of the anime to update
     * @param record of the values to update along with the anime to the list
     * @throws NullPointerException if any of the parameters are null
     * @throws javax.ws.rs.ClientErrorException if MAL returns a HTTP {@code 4xx} status code
     * @throws javax.ws.rs.ServerErrorException if MAL returns a HTTP {@code 5xx} status code
     * @throws javax.ws.rs.ProcessingException  if we can't understand the response
     */
    public void animeListUpdate(
            @NonNull final String id,
            @NonNull final AnimeListEntryValues record) {

        final Form form = new Form();
        form.param("data", xml(record, record.getClass()));

        client.target(malUrl)
                .path(PATH_ANIME_UPDATE.replace("%id", id))
                .request(WILDCARD_TYPE)
                .post(entity(form, APPLICATION_FORM_URLENCODED_TYPE));
    }

    /**
     * Delete an Anime from the authenticated user's Anime List.
     * <p>
     * <b>Documented API</b>
     * <p>
     * Similar to {@link #animeListUpdate(String, AnimeListEntryValues)} and
     * {@link #animeListAdd(String, AnimeListEntryValues)} this interface doesn't really error much. So it's might be
     * best to confirm after calling by using {@link #x_animeList(String)}.
     *
     * @param id maldb id of the anime to delete
     * @throws NullPointerException if any of the parameters are null
     * @throws javax.ws.rs.ClientErrorException if MAL returns a HTTP {@code 4xx} status code
     * @throws javax.ws.rs.ServerErrorException if MAL returns a HTTP {@code 5xx} status code
     * @throws javax.ws.rs.ProcessingException  if we can't understand the response
     */
    public void animeListDelete(
            @NonNull final String id) {

        client.target(malUrl)
                .path(PATH_ANIME_DELETE.replace("%id", id))
                .request(WILDCARD_TYPE)
                .delete(String.class);
    }

    /**
     * Get the Anime List of a specific user.
     * <p>
     * <b>Undocumented API</b>
     *
     * @param username
     * @return the AnimeList for a specific user
     * @throws NullPointerException if any of the parameters are null
     * @throws javax.ws.rs.ClientErrorException if MAL returns a HTTP {@code 4xx} status code
     * @throws javax.ws.rs.ServerErrorException if MAL returns a HTTP {@code 5xx} status code
     * @throws javax.ws.rs.ProcessingException  if we can't understand the response
     */
    public List<AnimeListEntry> x_animeList(
            @NonNull final String username) {

        return client.target(malUrl)
                .path(X_PATH_MALAPPINFO)
                .queryParam("u", username)
                .queryParam("type", "anime")
                .queryParam("status", "all")
                .request(APPLICATION_XML_TYPE)
                .get(AnimeListEntry.Holder.class).getRecords();
    }


    private String xml(
            @NonNull final Object o,
            @NonNull final Class t) {

        final StringWriter sw = new StringWriter();

        try {
            JAXBContext.newInstance(t)
                    .createMarshaller()
                    .marshal(o, sw);
        } catch (JAXBException e) {
            throw new RuntimeException(e);
        }

        return sw.toString();
    }

    @Override
    public void close() throws Exception {
        if (client != null) {
            client.close();
        }
    }
}
