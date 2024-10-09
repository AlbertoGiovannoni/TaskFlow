package com.unifi.taskflow.domainModel.fieldDefinitions.fieldDefinitionBuilders;

import java.util.ArrayList;

import com.unifi.taskflow.domainModel.EntityFactory;
import com.unifi.taskflow.domainModel.Organization;
import com.unifi.taskflow.domainModel.User;
import com.unifi.taskflow.domainModel.fieldDefinitions.AssigneeDefinition;
import com.unifi.taskflow.domainModel.fieldDefinitions.FieldType;

public class AssigneeDefinitionBuilder extends FieldDefinitionBuilder{
    private ArrayList<User>  possibleAssignees;
    private Organization organization;

    public AssigneeDefinitionBuilder() {
        super(FieldType.ASSIGNEE);
    }

    @Override
    public AssigneeDefinition build() {
        if (this.possibleAssignees == null){
            throw new IllegalAccessError("possibleAssignees are null");
        }

        AssigneeDefinition assigneeDefinition = EntityFactory.getAssigneeDefinition();

        assigneeDefinition.setName(this.name);
        assigneeDefinition.setType(this.type);
        assigneeDefinition.setPossibleAssigneeUsers(this.possibleAssignees);

        return assigneeDefinition;
    }

    @Override
    public AssigneeDefinitionBuilder reset() {
        this.possibleAssignees.clear();
        return this;
    }

    public AssigneeDefinitionBuilder setUsers(ArrayList<User> users){
        if (this.organization == null){
            throw new IllegalArgumentException("You must setup the organization before call this method (AssigneeDefinitionBUilder.setOrganization)");
        }
        
        ArrayList<User> possibleUsers = new ArrayList<>();
        
        for (User user : users){
            if (this.organization.getUsers().contains(user)){
                possibleUsers.add(user);
            }
        }

        this.possibleAssignees = possibleUsers;
        return this;
    }

    public AssigneeDefinitionBuilder setOrganization(Organization organization){
        this.organization = organization;
        return this;
    }

}
