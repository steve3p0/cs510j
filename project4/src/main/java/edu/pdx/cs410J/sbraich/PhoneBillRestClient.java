package edu.pdx.cs410J.sbraich;

import com.google.common.annotations.VisibleForTesting;
import edu.pdx.cs410J.web.HttpRequestHelper;

import java.io.IOException;
import java.util.Map;

import static java.net.HttpURLConnection.HTTP_OK;

/**
 * A helper class for accessing the rest client.  Note that this class provides
 * an example of how to make gets and posts to a URL.  You'll need to change it
 * to do something other than just send dictionary entries.
 */
public class PhoneBillRestClient extends HttpRequestHelper
{
    private static final String WEB_APP = "phonebill";
    private static final String SERVLET = "calls";

    private final String url;


    /**
     * Creates a client to the Phone Bil REST service running on the given host and port
     * @param hostName The name of the host
     * @param port The port
     */
    public PhoneBillRestClient( String hostName, int port )
    {
        this.url = String.format( "http://%s:%d/%s/%s", hostName, port, WEB_APP, SERVLET );
    }

    /**
     * Returns all dictionary entries from the server
     */
    public Map<String, String> getAllDictionaryEntries() throws IOException {
      Response response = get(this.url);
      return Messages.parseDictionary(response.getContent());
    }

    /**
     * Returns the definition for the given word
     */
    public String getDefinition(String word) throws IOException {
      Response response = get(this.url, "word", word);
      throwExceptionIfNotOkayHttpStatus(response);
      String content = response.getContent();
      return Messages.parseDictionaryEntry(content).getValue();
    }

    public void addDictionaryEntry(String word, String definition) throws IOException {
      Response response = postToMyURL("word", word, "definition", definition);
      throwExceptionIfNotOkayHttpStatus(response);
    }

    @VisibleForTesting
    Response postToMyURL(String... dictionaryEntries) throws IOException {
      return post(this.url, dictionaryEntries);
    }

    public void removeAllDictionaryEntries() throws IOException {
      Response response = delete(this.url);
      throwExceptionIfNotOkayHttpStatus(response);
    }

    private Response throwExceptionIfNotOkayHttpStatus(Response response) {
      int code = response.getCode();
      if (code != HTTP_OK) {
        throw new PhoneBillRestException(code);
      }
      return response;
    }

    private class PhoneBillRestException extends RuntimeException {
      public PhoneBillRestException(int httpStatusCode) {
        super("Got an HTTP Status Code of " + httpStatusCode);
      }
    }

}
