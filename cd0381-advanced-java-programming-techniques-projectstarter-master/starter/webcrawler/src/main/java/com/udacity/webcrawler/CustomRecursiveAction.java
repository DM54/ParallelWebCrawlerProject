package com.udacity.webcrawler;
import com.udacity.webcrawler.json.CrawlResult;
import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.*;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.*;
import java.util.concurrent.locks.ReentrantLock;
import java.util.regex.Pattern;
import java.util.concurrent.RecursiveAction;
import com.udacity.webcrawler.parser.PageParser;
import com.udacity.webcrawler.parser.PageParserFactory;

final class CustomRecursiveAction extends RecursiveAction {
    private final Clock clock;
    private final Duration timeout;
    private final String url;
    private final Instant deadline;
    private final int maxDepth;
    private final Map<String, Integer> counts;
    private final Set<String> visitedUrls;
    private final PageParserFactory parserFactory;
    private final List<Pattern> ignoredUrls;

    public CustomRecursiveAction(Clock clock, Duration timeout, String url, Instant deadline, int maxDepth, Map<String, Integer> counts,
                                 Set<String> visitedUrls, PageParserFactory parserFactory, List<Pattern> ignoredUrls) {
        this.clock = clock;
        this.timeout = timeout;
        this.url = url;
        this.deadline = deadline;
        this.maxDepth = maxDepth;
        this.counts = counts;
        this.visitedUrls = visitedUrls;
        this.parserFactory = parserFactory;
        this.ignoredUrls = ignoredUrls;
    }

    @Override
    protected void compute() {

        //downloads and parse webpages.
        List<CustomRecursiveAction> subTask = new ArrayList<>();

        List<CustomRecursiveAction> subtasks = new ArrayList<>();
        ReentrantLock lock = new ReentrantLock();

            if (maxDepth == 0 || clock.instant().isAfter(deadline)) {
                return;
            }

            for (Pattern pattern : ignoredUrls) {
                if (pattern.matcher(url).matches()) {
                    return;
                }
            }

        try {
            lock.lock();
            if (visitedUrls.contains(url)) {
                return;
            }
            visitedUrls.add(url);
        } finally {
            lock.unlock();
        }
        PageParser.Result result = parserFactory.get(url).parse();

            for (Map.Entry<String, Integer> e : result.getWordCounts().entrySet()) {
                if (counts.containsKey(e.getKey())) {
                    counts.put(e.getKey(), e.getValue() + counts.get(e.getKey()));
                } else {
                    counts.put(e.getKey(), e.getValue());
                }
            }

            for (String link : result.getLinks()) {

                CustomRecursiveAction recursiveAction = new CustomRecursiveAction(clock, timeout, link, deadline, maxDepth - 1, counts
                        , visitedUrls, parserFactory, ignoredUrls);
                subtasks.add(recursiveAction);
            }


        subTask.addAll(subtasks);
        for (CustomRecursiveAction t : subTask
        ) {
            t.fork();
            t.join();
        }
    }
}
