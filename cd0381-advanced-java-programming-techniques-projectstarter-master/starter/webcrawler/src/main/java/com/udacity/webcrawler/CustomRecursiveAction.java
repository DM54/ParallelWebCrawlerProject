

//final class CustomRecursiveAction extends RecursiveAction{
   /* private final String url;
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

    }*/
   /* @Override
    protected void compute(){
        if (maxDepth == 0 || clock.instant().isAfter(deadline)) {
            return;
        }
        for (Pattern pattern : ignoredUrls) {
            if (pattern.matcher(url).matches()) {
                return;
            }
        }
        if (visitedUrls.contains(url)) {
            return;
        }
        visitedUrls.add(url);

        PageParser.Result result = parserFactory.get(url).parse();
        List<CustomRecursiveAction> list;
        for (Map.Entry<String, Integer> e : result.getWordCounts().entrySet()) {
            if (counts.containsKey(e.getKey())) {
                counts.put(e.getKey(), e.getValue() + counts.get(e.getKey()));
            } else {
                counts.put(e.getKey(), e.getValue());
            }
        }
        for (String link : result.getLinks()) {
            crawlInternal(link, deadline, maxDepth - 1, counts, visitedUrls);
        }
    }*/

//}