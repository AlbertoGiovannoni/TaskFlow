package com.example.taskflow.DomainModel.FieldDefinitionPackage.FieldDefinitionFactoryPackage;
import java.util.ArrayList;
import com.example.taskflow.DomainModel.User;
import com.example.taskflow.DomainModel.FieldDefinitionPackage.AssigneeDefinition;
import com.example.taskflow.DomainModel.FieldDefinitionPackage.FieldType;

public class AssigneeDefinitionBuilder extends FieldDefinitionBuilder{
    private ArrayList<User>  possibleAssignees;

    AssigneeDefinitionBuilder(FieldType type) {
        super(type);
        this.possibleAssignees = new ArrayList<>();
    }

    @Override
    public AssigneeDefinitionBuilder setSpecificField(ArrayList<Object> possibleAssignees) {
        if (this.possibleAssignees != null){
            if (!this.possibleAssignees.isEmpty()){
                this.possibleAssignees.clear();
            }
        }
        else{
            throw new IllegalArgumentException(this.getClass().getSimpleName() + " possibleAssignees is null");
        }

        for (Object obj : possibleAssignees){
            this.possibleAssignees.add((User)obj);
        }

        return this.self();
    }

    @Override
    protected AssigneeDefinitionBuilder self() {
        return this;
    }

    @Override
    public AssigneeDefinition build() {
        if (this.possibleAssignees == null){
            throw new IllegalAccessError("possibleAssignees are null");
        }
        return new AssigneeDefinition(name, this.type, possibleAssignees);
    }
}
