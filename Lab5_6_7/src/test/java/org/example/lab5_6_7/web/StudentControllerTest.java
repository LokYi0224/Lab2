package org.example.lab5_6_7.web;

import org.example.lab5_6_7.entities.Student;
import org.example.lab5_6_7.repositories.StudentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.verification.VerificationMode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.servlet.View;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;

@ExtendWith(MockitoExtension.class)
@WebAppConfiguration
@AutoConfigureMockMvc
class StudentControllerTest {

    Student student;

    private MockMvc mockMvc;

    @Mock
    StudentRepository studentRepository;

    @Mock
    View mockView;

    @InjectMocks
    StudentController studentController;

    @BeforeEach
    void setUp() throws Exception {
        student = new Student();
        student.setId(1L);
        student.setName("John");

        String sDate1 = "2012/11/11";
        Date date1 = new SimpleDateFormat("yyyy/MM/dd").parse(sDate1);
        student.setDob(date1);
        student.setPassed(true);
        student.setGpa(3.5);

        MockitoAnnotations.openMocks(this);

        mockMvc = standaloneSetup(studentController).setSingleView(mockView).build();
    }

    @Test
    public  void findAll_ListView() throws Exception {
        List<Student> list = new ArrayList<>();
        list.add(student);
        list.add(student);

        when(studentRepository.findAll()).thenReturn(list);
        mockMvc.perform(get("/index"))
                .andExpect(status().isOk())
                .andExpect(model().attribute("listStudents",list))
                .andExpect(view().name("students"))
                .andExpect(model().attribute("listStudents",hasSize(2)));

        verify(studentRepository, times(1)).findAll();
        verifyNoMoreInteractions(studentRepository);
    }

    @Test
    void findById() throws Exception {
        List<Student> list = new ArrayList<>();
        list.add(student);

        when(studentRepository.findStudentById(1L)).thenReturn(list);
        mockMvc.perform(get("/index").param("keyword","1"))
                .andExpect(status().isOk())
                .andExpect(model().attribute("listStudents",list))
                .andExpect(view().name("students"))
                .andExpect(model().attribute("listStudents",hasSize(1)));

        verify(studentRepository, times(1)).findStudentById(anyLong());
        verifyNoMoreInteractions(studentRepository);
    }

    @Test
    void delete(){
        ArgumentCaptor<Long> idCaptor = ArgumentCaptor.forClass(Long.class);
        doNothing().when(studentRepository).deleteById(idCaptor.capture());
        studentRepository.deleteById(1L);
        assertEquals(1L, idCaptor.getValue());
        verify(studentRepository, times(1)).deleteById(1L);
    }

    @Test
    void editStudent() throws Exception {
        Student s2 = new Student();
        s2.setId(1L);
        s2.setName("John Mast");

        String sDate1 = "2012/11/11";
        Date date1 = new SimpleDateFormat("yyyy/MM/dd").parse(sDate1);
        s2.setDob(date1);
        s2.setPassed(true);
        s2.setGpa(3.5);

        Long iid= 1L;
        when(studentRepository.findById(iid)).thenReturn(Optional.of(s2));
        mockMvc.perform(get("/editStudents").param("id",String.valueOf(1L)))
                .andExpect(status().isOk())
                .andExpect(model().attribute("student",s2))
                .andExpect(view().name("editStudents"));

        verify(studentRepository, times(1)).findById(anyLong());
        verifyNoMoreInteractions(studentRepository);
    }

    @Test
    void formStudent() throws Exception {
        mockMvc.perform(get("/formStudents"))
                .andExpect(status().isOk())
                .andExpect(model().attribute("student",new Student()))
                .andExpect(view().name("formStudents"));
    }

    @Test
    void save() throws Exception {
        when(studentRepository.save(student)).thenReturn(student);
        studentRepository.save(student);
        verify(studentRepository, times(1)).save(student);
    }
}