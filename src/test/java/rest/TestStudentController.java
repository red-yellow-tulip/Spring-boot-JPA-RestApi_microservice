package rest;

import base.StudentMicroserviceRunner;
import base.datasource.entity.Group;
import base.datasource.entity.Student;
import base.datasource.entity.University;
import base.datasource.DatabaseService;
import base.web.config.SourceParameterWrapperStudent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest (classes = StudentMicroserviceRunner.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class TestStudentController {

    private static final Logger log = LogManager.getLogger(TestStudentController.class.getName());

    @Resource
    private DatabaseService service;

    //@Value("${server.port}")
    @LocalServerPort
    String port;

    private TestRestTemplate restTemplate = new TestRestTemplate("USER1","pswd");

    private final String url =      "http://localhost:%s/";
    private final String getAll =   url+"student/all";
    private final String filtr =    url+"student/filtr?name=nam&sname=surnam";
    private final String filtr1 =   url+"student/filtr?name=name5&sname=surname5";
    private final String groupId =  url+"student/group?id=54";
    private final String post =     url+"student?id=54";
    private final String del =      url+"student/delete?name=name40&sname=surname40";

    @BeforeEach
    public void testBefore() {
        assertNotNull(service);
        service.clearTable();
        service.createDemoData();
    }

    @Test
    public void TestGetAll(){

        List<Student> allStudent = restTemplate.getForObject(String.format(getAll,port), SourceParameterWrapperStudent.ListWrapper.class);
        assertNotNull(allStudent);
        assertEquals(allStudent.size() , 50);
        log.trace(allStudent);
    }

    @Test
    public void TestFiltr(){

        //http://localhost:8080/student/filtr?name=name1&sname=surname1
        List<Student> allStudent = restTemplate.getForObject(String.format(filtr,port), SourceParameterWrapperStudent.ListWrapper.class);
        assertNotNull(allStudent);
        assertEquals(allStudent.size() , 50);
        log.trace(allStudent);

        List<Student> allStudent1 = restTemplate.getForObject(String.format(filtr1,port), SourceParameterWrapperStudent.ListWrapper.class);
        assertNotNull(allStudent1);
        assertEquals(allStudent1.size(), 1);
        log.trace(allStudent1);
    }

    @Test
    public void TestGroupId(){

        List<Student> allStudent = restTemplate.getForObject(String.format(groupId,port), SourceParameterWrapperStudent.ListWrapper.class);
        assertNotNull(allStudent);
        assertEquals(allStudent.size() , 10);
        log.trace(allStudent);
    }


    @Test
    public void TestAddNewStudent(){

        long id = 101L, groupIdn = 10;
        University un = new University(id,"Best university");

        Group gr = new Group(groupIdn,"group1");
        gr.setUniversity(un);
        un.getListGroup().add(gr);
        service.saveUniversity(un);

        for(long i = 0; i< 10; i++){
            Student s = new Student("name"+60+i,"surname"+60+i,new Date());
            ResponseEntity<Student> e = restTemplate.postForEntity(String.format(post,port),s,Student.class);
            assertEquals(e.getStatusCode(),  HttpStatus.CREATED);
            log.trace(e.getStatusCode());
        }
        List<Student> allStudent = restTemplate.getForObject(String.format(groupId,port), SourceParameterWrapperStudent.ListWrapper.class);
        assertNotNull(allStudent);
        assertEquals(allStudent.size(),10);
        log.trace(allStudent);
    }

    @Test
    public void TestDelete(){

        restTemplate.delete(String.format(del,port));

        List<Student> allStudent = restTemplate.getForObject(String.format(groupId,port), SourceParameterWrapperStudent.ListWrapper.class);
        assertNotNull(allStudent);
        assertEquals(allStudent.size() , 9);
        log.trace(allStudent);
    }
}
