package chat.hola.com.app.Database;

/*
 * Created by moda on 09/01/17.
 */


import java.util.List;

/**
 * Standard query options for views.
 *
 * @exclude
 */
class QueryOptions {

    static int QUERY_OPTIONS_DEFAULT_LIMIT = Integer.MAX_VALUE;

    private Object startKey = null;
    private Object endKey = null;
    private List<Object> keys = null;
    private int skip = 0;
    private int limit = QUERY_OPTIONS_DEFAULT_LIMIT;
    private int groupLevel = 0;
    private int prefixMatchLevel = 0;
    private boolean descending = false;
    private boolean includeDocs = false;
    private boolean updateSeq = false;
    private boolean inclusiveStart = true;
    private boolean inclusiveEnd = true;
    private boolean reduce = false;
    private boolean reduceSpecified = false;
    private boolean group = false;
    private Query.IndexUpdateMode stale = Query.IndexUpdateMode.BEFORE;
    private Query.AllDocsMode allDocsMode;

    private String startKeyDocId;
    private String endKeyDocId;

    private Predicate<QueryRow> postFilter;

    Object getStartKey() {
        return startKey;
    }

    void setStartKey(Object startKey) {
        this.startKey = startKey;
    }

    Object getEndKey() {
        return endKey;
    }

    void setEndKey(Object endKey) {
        this.endKey = endKey;
    }

    public int getSkip() {
        return skip;
    }

    public void setSkip(int skip) {
        this.skip = skip;
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    boolean isDescending() {
        return descending;
    }

    void setDescending(boolean descending) {
        this.descending = descending;
    }

    boolean isIncludeDocs() {
        return includeDocs;
    }

    void setIncludeDocs(boolean includeDocs) {
        this.includeDocs = includeDocs;
    }

    boolean isUpdateSeq() {
        return updateSeq;
    }

    void setUpdateSeq(boolean updateSeq) {
        this.updateSeq = updateSeq;
    }

    boolean isInclusiveStart() {
        return inclusiveStart;
    }

    void setInclusiveStart(boolean inclusiveStart) {
        this.inclusiveStart = inclusiveStart;
    }

    boolean isInclusiveEnd() {
        return inclusiveEnd;
    }

    void setInclusiveEnd(boolean inclusiveEnd) {
        this.inclusiveEnd = inclusiveEnd;
    }

    int getGroupLevel() {
        return groupLevel;
    }

    void setGroupLevel(int groupLevel) {
        this.groupLevel = groupLevel;
    }

    int getPrefixMatchLevel() {
        return prefixMatchLevel;
    }

    void setPrefixMatchLevel(int prefixMatchLevel) {
        this.prefixMatchLevel = prefixMatchLevel;
    }

    boolean isReduce() {
        return reduce;
    }

    void setReduce(boolean reduce) {
        this.reduce = reduce;
    }

    boolean isGroup() {
        return group;
    }

    public void setGroup(boolean group) {
        this.group = group;
    }

    public List<Object> getKeys() {
        return keys;
    }

    public void setKeys(List<Object> keys) {
        this.keys = keys;
    }

    Query.IndexUpdateMode getStale() {
        return stale;
    }

    void setStale(Query.IndexUpdateMode stale) {
        this.stale = stale;
    }

    boolean isReduceSpecified() {
        return reduceSpecified;
    }

    void setReduceSpecified(boolean reduceSpecified) {
        this.reduceSpecified = reduceSpecified;
    }

    Query.AllDocsMode getAllDocsMode() {
        return allDocsMode;
    }

    void setAllDocsMode(Query.AllDocsMode allDocsMode) {
        this.allDocsMode = allDocsMode;
    }

    String getStartKeyDocId() {
        return startKeyDocId;
    }

    void setStartKeyDocId(String startKeyDocId) {
        this.startKeyDocId = startKeyDocId;
    }

    String getEndKeyDocId() {
        return endKeyDocId;
    }

    void setEndKeyDocId(String endKeyDocId) {
        this.endKeyDocId = endKeyDocId;
    }

    Predicate<QueryRow> getPostFilter() {
        return postFilter;
    }

    void setPostFilter(Predicate<QueryRow> postFilter) {
        this.postFilter = postFilter;
    }

    @Override
    public String toString() {
        return "QueryOptions{" +
                "startKey=" + startKey +
                ", endKey=" + endKey +
                ", keys=" + keys +
                ", skip=" + skip +
                ", limit=" + limit +
                ", groupLevel=" + groupLevel +
                ", prefixMatchLevel=" + prefixMatchLevel +
                ", descending=" + descending +
                ", includeDocs=" + includeDocs +
                ", updateSeq=" + updateSeq +
                ", inclusiveStart=" + inclusiveStart +
                ", inclusiveEnd=" + inclusiveEnd +
                ", reduce=" + reduce +
                ", reduceSpecified=" + reduceSpecified +
                ", group=" + group +
                ", stale=" + stale +
                ", allDocsMode=" + allDocsMode +
                ", startKeyDocId='" + startKeyDocId + '\'' +
                ", endKeyDocId='" + endKeyDocId + '\'' +
                ", postFilter=" + postFilter +
                '}';
    }
}
