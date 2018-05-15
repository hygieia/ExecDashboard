package com.capitalone.dashboard.exec.model;

import java.util.Objects;

public class RelationShip<T, R> {
    private T relatedTo;
    private R relation;

    public RelationShip(T relatedTo, R relation) {
        this.relatedTo = relatedTo;
        this.relation = relation;
    }

    public T getRelatedTo() {
        return relatedTo;
    }

    public void setRelatedTo(T relatedTo) {
        this.relatedTo = relatedTo;
    }

    public R getRelation() {
        return relation;
    }

    public void setRelation(R relation) {
        this.relation = relation;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof RelationShip)) return false;
        RelationShip<?, ?> that = (RelationShip<?, ?>) o;
        return Objects.equals(relatedTo, that.relatedTo) &&
                Objects.equals(relation, that.relation);
    }

    @Override
    public int hashCode() {

        return Objects.hash(relatedTo, relation);
    }
}
