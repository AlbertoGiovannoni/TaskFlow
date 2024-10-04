package com.example.taskflow;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Set;
import java.util.UUID;
import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Component;

import com.example.taskflow.DAOs.*;
import com.example.taskflow.DTOs.Field.*;
import com.example.taskflow.DTOs.FieldDefinition.*;
import com.example.taskflow.DTOs.*;
import com.example.taskflow.DomainModel.*;
import com.example.taskflow.DomainModel.FieldDefinitionPackage.*;
import com.example.taskflow.DomainModel.FieldDefinitionPackage.FieldDefinitionFactoryPackage.*;
import com.example.taskflow.DomainModel.FieldPackage.*;
import com.example.taskflow.DomainModel.FieldPackage.FieldFactoryPackage.*;
import com.example.taskflow.service.*;

import java.time.LocalDateTime;

import java.util.Random;

import net.bytebuddy.utility.RandomString;

@Component
public class TestUtil {

    @Autowired
    private MongoTemplate template;
    @Autowired
    private FieldDefinitionDAO fieldDefinitionDAO;
    @Autowired
    private UserInfoDAO userInfoDAO;
    @Autowired
    private UserDAO userDAO;
    @Autowired
    private ProjectDAO projectDao;
    @Autowired
    private ActivityDAO activityDAO;
    @Autowired
    private FieldDAO fieldDao;
    @Autowired
    private OrganizationDAO organizationDAO;
    @Autowired
    private ActivityService activityService;

    public void cleanDatabase() {
        Set<String> allCollections = this.template.getCollectionNames();

        for (String collectionName : allCollections) {
            this.template.dropCollection(collectionName);
        }

        UserInfo userInfo = new UserInfo(UUID.randomUUID().toString(), "admin@gmail.com", "password");
        userInfoDAO.save(userInfo);

        User admin = new User(UUID.randomUUID().toString(), userInfo, "admin");
        userDAO.save(admin);
    }

    public FieldDefinition pushGetFieldDefinitionToDatabase(FieldType type) {
        FieldDefinition fieldDefinition = FieldDefinitionFactory.getBuilder(type)
                .setName(RandomString.make(10))
                .build();

        return this.fieldDefinitionDAO.save(fieldDefinition);
    }

    public User addGetRandomUserToDatabase() {
        String plainPassword = "password";

        UserInfo info = EntityFactory.getUserInfo();
        info.setPassword(plainPassword);
        info.setEmail(RandomString.make(5) + "." + RandomString.make(5) + "@gmail.com");
        this.userInfoDAO.save(info);

        User user = EntityFactory.getUser();
        user.setUserInfo(info);
        user.setUsername(RandomString.make(10));
        return this.userDAO.save(user);
    }

    public ArrayList<User> addGetMultipleRandomUserToDatabase(int n) {
        ArrayList<User> users = new ArrayList<>();

        for (int i = 0; i < n; i++) {
            users.add(this.addGetRandomUserToDatabase());
        }

        return users;
    }

    public Activity addRandomActivityToDatabase() {
        Activity activity = new Activity(RandomString.make(10));
        return this.activityDAO.save(activity);
    }

    public ArrayList<Activity> addMultipleRandomActivitiesToDatabase(int n) {
        ArrayList<Activity> activities = new ArrayList<>();

        for (int i = 0; i < n; i++) {
            activities.add(this.addRandomActivityToDatabase());
        }

        return activities;
    }

    public Project addRandomProjectToDatabase() {
        Project project = new Project(UUID.randomUUID().toString(), RandomString.make(10));
        return this.projectDao.save(project);
    }

    public ArrayList<Project> addMultipleRandomProjectsToDatabase(int n) {
        ArrayList<Project> projects = new ArrayList<>();

        for (int i = 0; i < n; i++) {
            projects.add(this.addRandomProjectToDatabase());
        }

        return projects;
    }

    public Organization addRandomOrganizationToDatabase() {
        ArrayList<User> members = addGetMultipleRandomUserToDatabase(10);
        ArrayList<User> owners = addGetMultipleRandomUserToDatabase(1);
        ArrayList<Project> projects = addMultipleRandomProjectsToDatabase(1);
        Organization organization = new Organization(UUID.randomUUID().toString(), RandomString.make(10), owners,
                projects, members, LocalDateTime.now());
        return this.organizationDAO.save(organization);
    }

    public ArrayList<Field> pushGetNumberFieldsWithSameDefinition(int n) {
        Field field;
        FieldDefinition fieldDefinition = FieldDefinitionFactory.getBuilder(FieldType.NUMBER)
                .setName(RandomString.make(10))
                .build();

        this.fieldDefinitionDAO.save(fieldDefinition);

        Random randomGenerator = new Random();
        ArrayList<Field> allFieldsGenerated = new ArrayList<>();

        for (int i = 0; i < n; i++) {
            field = (new NumberBuilder(fieldDefinition))
                    .addParameter(BigDecimal.valueOf(randomGenerator.nextInt(1000)))
                    .build();

            allFieldsGenerated.add(field);
            this.fieldDao.save(field);
        }

        return allFieldsGenerated;
    }

    public ArrayList<Field> pushGetNumberFields(int n) {
        Field field;
        FieldDefinition fieldDefinition;
        Random randomGenerator = new Random();
        ArrayList<Field> allFieldsGenerated = new ArrayList<>();

        for (int i = 0; i < n; i++) {
            fieldDefinition = FieldDefinitionFactory.getBuilder(FieldType.NUMBER)
                    .setName(RandomString.make(10))
                    .build();
            field = (new NumberBuilder(fieldDefinition))
                    .addParameter(BigDecimal.valueOf(randomGenerator.nextInt(1000)))
                    .build();

            allFieldsGenerated.add(field);

            this.fieldDefinitionDAO.save(fieldDefinition);
            this.fieldDao.save(field);
        }

        return allFieldsGenerated;
    }

    public void checkEqualOrganizations(Organization o1, Organization o2) {

        assertEquals(o1.getName(), o2.getName());
        assertEquals(o1.getCreationDate(), o2.getCreationDate());

        for (int i = 0; i < o1.getOwners().size(); i++)
            assertEquals(o1.getOwners().get(i).getUsername(), o2.getOwners().get(i).getUsername());

        for (int i = 0; i < o1.getMembers().size(); i++)
            assertEquals(o1.getMembers().get(i).getUsername(), o2.getMembers().get(i).getUsername());

        for (int i = 0; i < o1.getProjects().size(); i++)
            assertEquals(o1.getProjects().get(i).getName(), o2.getProjects().get(i).getName());

        assertEquals(o1.getUuid(), o2.getUuid());
    }

    public void checkEqualActivities(Activity a1, Activity a2) {
        assertEquals(a1.getName(), a2.getName());
        assertEquals(a1.getUuid(), a2.getUuid());
        for (int i = 0; i < a1.getFields().size(); i++) {
            assertEquals(a1.getFields().get(i).getUuid(), a2.getFields().get(i).getUuid());
        }
    }

    public void checkEqualProject(Project p1, Project p2) {
        assertEquals(p1.getName(), p2.getName());
        assertEquals(p1.getUuid(), p2.getUuid());
        for (int i = 0; i < p1.getActivities().size(); i++) {
            assertEquals(p1.getActivities().get(i).getUuid(), p2.getActivities().get(i).getUuid());
        }
    }

    public ArrayList<Organization> getEntireDatabaseMockup(int nOrganization, 
                                                int nProjectForOrganization, 
                                                int nActivitiesForProject, 
                                                int nUsers) {
        this.cleanDatabase();
        ArrayList<User> applicationUsers = this.addGetMultipleRandomUserToDatabase(nUsers);

        ArrayList<User> owners = TestUtil.getRandomSublist(applicationUsers, nOrganization);
        ArrayList<User> members = new ArrayList<>(applicationUsers);
        members.removeAll(owners);

        ArrayList<Organization> organizations = new ArrayList<>();
        for (int i = 0; i < nOrganization; i++){
            Organization organization = EntityFactory.getOrganization();
            organization.setName(RandomString.make(10));

            ArrayList<Project> projects = new ArrayList<>();

            for (int j = 0; j < nProjectForOrganization; j++){
                Project project = EntityFactory.getProject();

                ArrayList<FieldDefinition> fieldDefinitions = new ArrayList<>();
                for (FieldType type : FieldType.values()) {
                    if (type != FieldType.DOCUMENT) {
                        FieldDefinition fieldDefinition = this.getFieldDefinition(type);

                        if (type == FieldType.ASSIGNEE) {
                            ArrayList<User> randomUsersList = TestUtil.getRandomSublist(applicationUsers, new Random().nextInt(9) + 1);
                            ((AssigneeDefinition) fieldDefinition).setPossibleAssigneeUsers(randomUsersList);
                        }
    
                        if (type == FieldType.SINGLE_SELECTION) {
                            ((SingleSelectionDefinition) fieldDefinition).setPossibleSelections(this.getRandomSelections(new Random().nextInt(4) + 1));
                        }
                        
                        fieldDefinition = this.fieldDefinitionDAO.save(fieldDefinition);
                        fieldDefinitions.add(fieldDefinition);
                        project.addFieldDefinition(fieldDefinition);
                    }
                }

                ArrayList<Activity> activities = new ArrayList<>();
                for (int k = 0; k < nActivitiesForProject; k++){
                    Activity activity = EntityFactory.getActivity();
                    ArrayList<Field> fields = new ArrayList<>();

                    for (FieldDefinition fieldDefinition : fieldDefinitions){
                        Field field = this.getField(fieldDefinition);
                        field = this.fieldDao.save(field);
                        fields.add(field);
                    }

                    activity.setFields(fields);
                    activity.setName(RandomString.make(10));
                    activities.add(this.activityDAO.save(activity));
                }

                project.setName(RandomString.make(10));
                project.setActivities(activities);
                project = this.projectDao.save(project);
                projects.add(project);
            }
            organization.setProjects(projects);
            
            organization.setOwners(TestUtil.getRandomSublist(owners, new Random().nextInt(owners.size())));
            organization.setMembers(TestUtil.getRandomSublist(members, new Random().nextInt(members.size())));

            organization = this.organizationDAO.save(organization);
            organizations.add(organization);
        }
        return organizations;
    }

    private Field getField(FieldDefinition fieldDefinition){
        Field field;
        switch (fieldDefinition.getType()) {
            case ASSIGNEE:
                field = new AssigneeBuilder(fieldDefinition)
                        .addAssignees(TestUtil.getRandomSublist(((AssigneeDefinition)fieldDefinition).getPossibleAssigneeUsers(), new Random().nextInt(((AssigneeDefinition)fieldDefinition).getPossibleAssigneeUsers().size())))
                        .build();
                break;
            case SINGLE_SELECTION:
                field = new SingleSelectionBuilder(fieldDefinition)
                        .addSelection(((SingleSelectionDefinition)fieldDefinition).getPossibleSelections().get(0))
                        .build();
                break;
            case TEXT:
                field = new TextBuilder(fieldDefinition)
                        .addText(RandomString.make(30))
                        .build();
                break;
            case DATE:
                field = new DateBuilder(fieldDefinition)
                        .addDate(LocalDateTime.now())
                        .build();
                break;
            case NUMBER:
                field = new NumberBuilder(fieldDefinition)
                        .addParameter(new BigDecimal(new Random().nextInt(100)))
                        .build();
                break;
            default:
                throw new IllegalArgumentException("Document need implementation");
        }
        return field;
    }

    private FieldDefinition getFieldDefinition(FieldType type){
        FieldDefinition fieldDefinition;
        switch (type) {
            case ASSIGNEE:
                fieldDefinition = EntityFactory.getAssigneeDefinition();
                break;
            case SINGLE_SELECTION:
                fieldDefinition = EntityFactory.getSingleSelectionDefinition();
                break;
            default:
                fieldDefinition = EntityFactory.getSimpleFieldDefinition();
                fieldDefinition.setType(type);
                break;
        }
        fieldDefinition.setName(RandomString.make(10));
        return fieldDefinition;
    }

    public static <T> ArrayList<T> getRandomSublist(ArrayList<T> list, int n) {
        if (n == 0){
            n = 1;
        }
        Collections.shuffle(list);
        return new ArrayList<T>(list.subList(0, n));
    }

    public FieldDefinitionDTO getFieldDefinitionDTO(FieldType type) {
        FieldDefinitionDTO fieldDefinitionDTO;
        switch (type) {
            case ASSIGNEE:
                fieldDefinitionDTO = new AssigneeDefinitionDTO();
                break;
            case SINGLE_SELECTION:
                fieldDefinitionDTO = new SingleSelectionDefinitionDTO();
                break;
            default:
                fieldDefinitionDTO = new SimpleFieldDefinitionDTO();
                break;
        }
        fieldDefinitionDTO.setType(type);
        return fieldDefinitionDTO;
    }

    public ArrayList<FieldDTO> getFieldDTOArray(int n, FieldType type) {
        ArrayList<FieldDTO> fieldDTOs = new ArrayList<>();

        for (int i = 0; i < n; i++) {
            fieldDTOs.add(this.getFieldDTO(type));
        }

        return fieldDTOs;
    }

    public void setupFieldDTOs(ArrayList<FieldDTO> fieldDtos, FieldDefinition fieldDefinition) {
        for (FieldDTO fieldDto : fieldDtos) {
            fieldDto.setFieldDefinitionId(fieldDefinition.getId());
            this.setValueToDTO(fieldDto, fieldDefinition);
        }
    }

    public void setValueToDTO(FieldDTO fieldDto, FieldDefinition fieldDefinition) {
        switch (fieldDto.getType()) {
            case ASSIGNEE:
                ((AssigneeDTO) fieldDto)
                        .setUserIds(
                                this.getUserIds(((AssigneeDefinition) fieldDefinition).getPossibleAssigneeUsers()));
                break;
            case SINGLE_SELECTION:
                ((SingleSelectionDTO) fieldDto)
                        .setValue((((SingleSelectionDefinition) fieldDefinition).getPossibleSelections().get(0)));
                break;
            case TEXT:
                ((TextDTO) fieldDto).setValue(RandomString.make(100));
                break;
            case DATE:
                ((DateDTO) fieldDto).setDateTime(LocalDateTime.now());
                break;
            case NUMBER:
                ((NumberDTO) fieldDto).setValue(new BigDecimal(100));
                break;
            default:
                throw new IllegalArgumentException("Document need implementation");
        }
    }

    public FieldDTO getFieldDTO(FieldType type) {
        FieldDTO fieldDTO;
        switch (type) {
            case ASSIGNEE:
                fieldDTO = new AssigneeDTO();
                break;
            case SINGLE_SELECTION:
                fieldDTO = new SingleSelectionDTO();
                break;
            case TEXT:
                fieldDTO = new TextDTO();
                break;
            case DATE:
                fieldDTO = new DateDTO();
                break;
            case NUMBER:
                fieldDTO = new NumberDTO();
                break;
            default:
                throw new IllegalArgumentException("Document need implementation");
        }
        fieldDTO.setType(type);
        return fieldDTO;
    }

    public ArrayList<ActivityDTO> getActivitieDTOsArray(ArrayList<FieldDTO> fieldDtos) {
        if (fieldDtos.size() == 0) {
            throw new IllegalArgumentException("Size must be > 0");
        }

        ArrayList<ActivityDTO> activityDtos = new ArrayList<>();
        ActivityDTO movingActivityDto;

        ArrayList<FieldDTO> fieldDTOsTempArray = new ArrayList<>();

        for (FieldDTO fieldDto : fieldDtos) {
            fieldDTOsTempArray.clear();
            fieldDTOsTempArray.add(fieldDto);

            movingActivityDto = new ActivityDTO();
            movingActivityDto.setName(RandomString.make(10));
            movingActivityDto.setFields(fieldDTOsTempArray);

            activityDtos.add(movingActivityDto);
        }

        return activityDtos;
    }

    public ArrayList<Activity> pushAllActivities(ArrayList<ActivityDTO> activities) {
        ArrayList<Activity> pushedActivities = new ArrayList<>();

        for (ActivityDTO activity : activities) {
            pushedActivities.add(this.activityService.pushNewActivity(activity));
        }

        return pushedActivities;
    }

    public ArrayList<String> getUserIds(ArrayList<User> users) {
        ArrayList<String> userIds = new ArrayList<>();

        for (User user : users) {
            userIds.add(user.getId());
        }

        return userIds;
    }

    public ArrayList<String> getRandomSelections(int n) {
        ArrayList<String> selections = new ArrayList<>();

        for (int i = 0; i < n; i++) {
            selections.add(RandomString.make(10));
        }

        return selections;
    }

}
