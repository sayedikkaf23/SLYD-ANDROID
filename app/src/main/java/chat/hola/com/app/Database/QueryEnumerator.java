package chat.hola.com.app.Database;

/*
 * Created by moda on 09/01/17.
 */


import com.couchbase.lite.internal.InterfaceAudience;

import java.util.Iterator;
import java.util.List;


/**
 * An enumerator for Couchbase Lite View Query results.
 */
class QueryEnumerator implements Iterator<QueryRow>, Iterable<QueryRow> {

    private Database database;
    private List<QueryRow> rows;
    private int nextRow;
    private long sequenceNumber;

    /**
     * Constructor
     */
    @InterfaceAudience.Private
    /* package */ QueryEnumerator(Database database, List<QueryRow> rows, long sequenceNumber) {
        this.database = database;
        this.rows = rows;
        this.sequenceNumber = sequenceNumber;

        // Fill in the rows' database reference now
        for (QueryRow row : rows) {
            row.setDatabase(database);
        }
    }

    /**
     * Constructor
     */
    @InterfaceAudience.Private
    /* package */ QueryEnumerator(QueryEnumerator other) {
        this.database = other.database;
        this.rows = other.rows;
        this.sequenceNumber = other.sequenceNumber;
    }

    /**
     * Gets the number of rows in the QueryEnumerator.
     */
    @InterfaceAudience.Public
    public int getCount() {
        return rows.size();
    }

    /**
     * Gets the Database's current sequence number at the time the View was generated for the results.
     */
    @InterfaceAudience.Public
    public long getSequenceNumber() {
        return sequenceNumber;
    }

    /**
     * Gets the next QueryRow from the results, or null
     * if there are no more results.
     */
    @Override
    @InterfaceAudience.Public
    public QueryRow next() {
        if (nextRow >= rows.size()) {
            return null;
        }
        return rows.get(nextRow++);
    }

    /**
     * Gets the QueryRow at the specified index in the results.
     */
    @InterfaceAudience.Public
    public QueryRow getRow(int index) {
        return rows.get(index);
    }

    /**
     * Compare this to given QueryEnumerator to check if equals.
     * This compares the underlying rows of the two QueryEnumerator instances.
     *
     * @param o the QueryEnumerator to compare this instance with.
     * @return true if equal, false otherwise.
     */
    @Override
    @InterfaceAudience.Public
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        QueryEnumerator that = (QueryEnumerator) o;

        return rows != null ? rows.equals(that.rows) : that.rows == null;

    }

    /**
     * Required to satisfy java Iterator interface
     */
    @Override
    @InterfaceAudience.Public
    public boolean hasNext() {
        return nextRow < rows.size();
    }

    /**
     * Required to satisfy java Iterator interface
     */
    @Override
    @InterfaceAudience.Public
    public void remove() {
        throw new UnsupportedOperationException("QueryEnumerator does not allow remove() to be called");
    }

    /**
     * True if the database has changed since the view was generated.
     */
    @InterfaceAudience.Public
    public boolean isStale() {
        return sequenceNumber < database.getLastSequenceNumber();
    }

    /**
     * Resets the enumeration so the next call to -nextObject or -nextRow will return the first row.
     */
    @InterfaceAudience.Public
    public void reset() {
        nextRow = 0;
    }

    @Override
    public Iterator<QueryRow> iterator() {
        return this;
    }
}
