package chat.hola.com.app.Database;

/*
 * Created by moda on 09/01/17.
 */



import com.couchbase.lite.Misc;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

/**
 * An ordered list of TDRevisions
 * @exclude
 */
@SuppressWarnings("serial")
 class RevisionList extends ArrayList<RevisionInternal> {

     RevisionList() {
        super();
    }

    /**
     * Allow converting to RevisionList from List<RevisionInternal>
     * @param list
     */
     RevisionList(List<RevisionInternal> list) {
        super(list);
    }

     RevisionInternal revWithDocIdAndRevId(String docId, String revId) {
        Iterator<RevisionInternal> iterator = iterator();
        while (iterator.hasNext()) {
            RevisionInternal rev = iterator.next();
            if (docId.equals(rev.getDocID()) && revId.equals(rev.getRevID())) {
                return rev;
            }
        }
        return null;
    }

     List<String> getAllDocIds() {
        List<String> result = new ArrayList<String>();

        Iterator<RevisionInternal> iterator = iterator();
        while (iterator.hasNext()) {
       RevisionInternal rev = iterator.next();
            result.add(rev.getDocID());
        }

        return result;
    }

     List<String> getAllRevIds() {
        List<String> result = new ArrayList<String>();

        Iterator<RevisionInternal> iterator = iterator();
        while (iterator.hasNext()) {
          RevisionInternal rev = iterator.next();
            result.add(rev.getRevID());
        }

        return result;
    }

     void sortBySequence() {
        Collections.sort(this, new Comparator<RevisionInternal>() {
            public int compare(RevisionInternal rev1, RevisionInternal rev2) {
                return Misc.SequenceCompare(rev1.getSequence(), rev2.getSequence());
            }
        });
    }

    /**
     * in CBL_Revision.m
     * - (void) sortByDocID
     */
    public void sortByDocID() {
        Collections.sort(this, new Comparator<RevisionInternal>() {
            public int compare(RevisionInternal rev1,RevisionInternal rev2) {
                return rev1.getDocID().compareTo(rev2.getDocID());
            }
        });
    }


    public void limit(int limit) {
        if (size() > limit) {
            removeRange(limit, size());
        }
    }

     RevisionInternal revWithDocId(String docId) {
        Iterator<RevisionInternal> iterator = iterator();
        while (iterator.hasNext()) {
           RevisionInternal rev = iterator.next();
            if (rev.getDocID() != null && rev.getDocID().equals(docId)) {
                return rev;
            }
        }
        return null;
    }

     RevisionInternal removeAndReturnRev(RevisionInternal rev) {
        int index = this.indexOf(rev);
        if (index == -1) {
            return null;
        }
       RevisionInternal resultRev = this.remove(index);
        return resultRev;
    }

    @Override

    @SuppressWarnings("unchecked")
    public Object clone() {
        return new RevisionList((ArrayList<RevisionInternal>) super.clone());
    }
}
