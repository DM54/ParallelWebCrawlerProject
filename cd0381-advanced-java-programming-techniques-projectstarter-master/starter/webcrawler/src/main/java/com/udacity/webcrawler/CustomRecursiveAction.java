package com.udacity.webcrawler;
import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.*;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;
import java.util.concurrent.RecursiveAction;
import com.udacity.webcrawler.parser.PageParser;
import com.udacity.webcrawler.parser.PageParserFactory;

final class CustomRecursiveAction extends RecursiveAction{
    private final String url;
    private final Instant deadline;
    private final int maxDepth;
    private final Map<String, Integer> counts;
    private final Set<String> visitedUrls;
    public CustomRecursiveAction(String url, Instant deadline, int maxDepth, Map<String,Integer> counts,
                                 Set<String> visitedUrls){
        this.url=url;
        this.deadline=deadline;
        this.maxDepth=maxDepth;
        this.counts=counts;
        this.visitedUrls=visitedUrls;
    }
    @Override
    protected void compute(){
        /**
         * UrlsVisited output should not count twice in the same crawl.
         * might need Collections.synchronizedCollection or java.util.concurrent
         * Reminder: think carefully about which methods are and are not atomic
         * and what guarantees those method provides.
         */

        //downloads and parse webpages.
        List<CustomRecursiveAction> subTask = new ArrayList<>();
        /**wordCount Map -> might need Collections.synchronization or concurrentHashmap?
         * to avoid counting the results twice from the same page.
         * Reminder: that the crawler will be downloading and processing multiple webpages at the same time.
         */


        PageParser.Result result = parserFactory.get(url).parse();
       for (Map.Entry<String, Integer> e : result.getWordCounts().entrySet()) {
            if (counts.containsKey(e.getKey())) {
                counts.put(e.getKey(), e.getValue() + counts.get(e.getKey()));
                subTask.add(new CustomRecursiveAction(url,deadline,maxDepth,counts,visitedUrl));
            } else {
                counts.put(e.getKey(), e.getValue());
                subTask.add(new CustomRecursiveAction(url,deadline,maxDepth,counts,visitedUrls));
            }
        }
        for (String link : result.getLinks()) {
          subTask.add(new CustomRecursiveAction(link, deadline, maxDepth - 1, counts, visitedUrls));
        }
        invokeAll(subTask);

        for (CustomRecursiveAction tasks: subTask
             ) {
            tasks.fork();
        }r

          /*subTask.stream().map(p->p.getWordCount().entrySet())
                        .filter(c->counts.containsKey(c.getKey())? counts.put(c.getKey(), c.getValue() + counts.get(c.getKey())) :
                                counts.put(c.getKey(),c.getValue()));*/

       /* subTask.stream()
                .map(s->s::getLinks)
                .filter(s->new CustomRecursiveAction(s,deadline,maxDepth-1,counts,visitedUrls));*/

    }

}