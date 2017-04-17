package program_assignment2;

import java.io.*;
import java.net.URL;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * This class is used to crawl Wiki.
 */
public class WikiCrawler {

    // base url of wikipedia.
    public static final String BASE_URL = "https://en.wikipedia.org";

    // relative address of the seed url.
    private final String seedUrl;

    // maximum number pages to be crawled.
    private final int max;

    // name of a file to write the graph.
    private final String fileName;

    /**
     * Constructor.
     */
    public WikiCrawler(String seedUrl, int max, String fileName) {
        this.seedUrl = seedUrl;
        this.max = max;
        this.fileName = fileName;
    }

    /**
     * Extracts wiki links from the html code.
     */
    public ArrayList<String> extractLinks(String doc) {

        // Remove html before the first occurrence of the html tag <p>.
        int pTagPos = doc.toLowerCase().indexOf("<p>");
        doc = doc.substring(pTagPos + "<p>".length());

        // Create an ArrayList to save the links.
        ArrayList<String> links = new ArrayList<String>();

        // Find all the links of form "wiki/XXXXXX" without "#" and ":"
        Matcher matcher = Pattern.compile("\"/wiki/[^\"#:]+\"").matcher(doc);
        while (matcher.find()) {
            String link = matcher.group();
            links.add(link.substring(1, link.length() - 1));
        }
        return links;
    }

    /**
     * Fetches a page and returns the html body.
     */
    private String fetchPage(String path) throws Exception {
        URL url = new URL(BASE_URL + path);
        BufferedReader reader = null;
        StringBuilder html = new StringBuilder();
        String line;
        try {
            reader = new BufferedReader(new InputStreamReader(url.openStream()));
            while ((line = reader.readLine()) != null) {
                html.append(line).append("\n");
            }
            return html.toString();
        } finally {
            if (reader != null) {
                reader.close();
            }
        }
    }

    /**
     * construct the web graph only over those pages and writes the graph
     * to the file fileName.
     */
    public void crawl() {

        int requestNumber = 0;

        // Initialize a Queue Q and a set visited.
        Queue<String> Q = new LinkedList<String>();
        Set<String> visited = new HashSet<String>();

        // Place root in Q and visited.
        Q.add(seedUrl);
        visited.add(seedUrl);

        PrintWriter output = null;

        try {
            // Open the output file.
            output = new PrintWriter(new FileOutputStream(fileName));

            // Write the number of vertices.
            output.println(max);
            System.out.println(max);

            // while Q is not empty Do
            while (!Q.isEmpty()) {

                // Let currentPage be the first element of Q.
                String currentPage = Q.poll();

                try {
                    // Send a request to server at currentPage and download currentPage.
                    String html = fetchPage(currentPage);

                    // Wait for at least 3 seconds after every 100 requests.
                    requestNumber++;
                    if (requestNumber % 100 == 0) {
                        System.out.println("Sleep for 3 seconds...");
                        Thread.sleep(3000);
                    }

                    // Extract all links from currentPage, and remove self loops and multiple edges.
                    Set<String> links = new LinkedHashSet<String>(extractLinks(html));
                    links.remove(currentPage);

                    // For every link u that appears in currentPage
                    for (String link : links) {

                        // If u isn't visited, add u to the end of Q, and add u to visited.
                        if (!visited.contains(link) && visited.size() < max) {
                            Q.add(link);
                            visited.add(link);
                        }

                        // If link is visited, write the edge to the output file.
                        if (visited.contains(link)) {
                            output.println(currentPage + " " + link);
                            System.out.println(currentPage + " " + link);
                        }
                    }

                } catch (Exception e) {
                    System.out.println("Error: failed to crawl url: " + currentPage);
                }
            }

        } catch (IOException e) {
            System.out.println("Error: " + e.getMessage());

        } finally {

            // Close
            if (output != null) {
                output.close();
            }
        }
    }

    public static void main(String[] args) {
        WikiCrawler w = new WikiCrawler("/wiki/Computer_Science", 500, "WikiCS.txt");
        w.crawl();
    }
}
