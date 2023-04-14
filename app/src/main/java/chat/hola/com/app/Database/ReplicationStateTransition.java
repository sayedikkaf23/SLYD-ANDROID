package chat.hola.com.app.Database;

/*
 * Created by moda on 07/01/17.
 */


import com.couchbase.lite.replicator.ReplicationState;
import com.github.oxo42.stateless4j.transitions.Transition;


/**
 * Represents a state transition that happens within the replicator
 */
class ReplicationStateTransition {

    private ReplicationState source;
    private ReplicationState destination;
    private ReplicationTrigger trigger;

    ReplicationStateTransition(Transition<ReplicationState, ReplicationTrigger> transition) {
        this(transition.getSource(), transition.getDestination(), transition.getTrigger());
    }

    ReplicationStateTransition(ReplicationState source, ReplicationState destination, ReplicationTrigger trigger) {
        this.source = source;
        this.destination = destination;
        this.trigger = trigger;
    }

    public ReplicationState getSource() {
        return source;
    }

    /* package */ void setSource(ReplicationState source) {
        this.source = source;
    }

    ReplicationState getDestination() {
        return destination;
    }

    /* package */ void setDestination(ReplicationState destination) {
        this.destination = destination;
    }

    /**
     * @exclude
     */
    public ReplicationTrigger getTrigger() {
        return trigger;
    }

    /* package */ void setTrigger(ReplicationTrigger trigger) {
        this.trigger = trigger;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ReplicationStateTransition that = (ReplicationStateTransition) o;

        if (source != that.source) return false;
        if (destination != that.destination) return false;
        return trigger == that.trigger;

    }

    @Override
    public int hashCode() {
        int result = source.hashCode();
        result = 31 * result + destination.hashCode();
        result = 31 * result + trigger.hashCode();
        return result;
    }
}