package com.capitalone.dashboard.exec.model;

public class PeopleRoleRelation extends RelationShip<Owner, RoleRelationShipType>{

    public PeopleRoleRelation(Owner relatedTo, RoleRelationShipType relation) {
        super(relatedTo, relation);
    }


}
