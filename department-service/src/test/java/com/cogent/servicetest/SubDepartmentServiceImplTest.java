package com.cogent.servicetest;

import com.cogent.controller.subDepartmnetController.dto.requestDTO.SubDepartmentRequestDTO;
import com.cogent.exceptionHandler.BadRequestDataException;
import com.cogent.exceptionHandler.DataAlreadyExistsException;
import com.cogent.modal.Department;
import com.cogent.modal.SubDepartment;
import com.cogent.repository.SubDepartmentRepository;
import com.cogent.service.DepartmentService;
import com.cogent.service.impl.SubDepartmentServiceImpl;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Date;
import java.util.Optional;

import static com.cogent.utils.SubDepartmentUtlis.parsaToSubDepartment;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

/**
 * @author Sauravi
 */

@RunWith(MockitoJUnitRunner.class)
public class SubDepartmentServiceImplTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Mock
    SubDepartmentRepository subDepartmentRepository;

    @Mock
    DepartmentService departmentService;

    @InjectMocks
    SubDepartmentServiceImpl subDepartmentService;

    private SubDepartmentRequestDTO subDepartmentRequestDTO;

    private SubDepartment savedSubDepartment;

    private Department department;

    @Before
    public void setUp(){
        subDepartmentService=new SubDepartmentServiceImpl(subDepartmentRepository,departmentService);
        subDepartmentRequestDTO=SubDepartmentRequestDTO.builder()
                .departmentId(1L)
                .name("Billing")
                .code("BILL")
                .status('Y').build();
        department=Department.builder()
                .id(1L)
                .departmentName("Account")
                .code("ACC")
                .status('Y').build();

        savedSubDepartment=SubDepartment.builder()
                .id(1L)
                .departmentId(department)
                .name("Billing")
                .code("BILL")
                .status('Y')
                .createdDate(new Date())
                .createdById(1L).build();
    }

    @Test
    public void createSubDepartment_ShouldReturnBadRequest(){
        thrown.expect(BadRequestDataException.class);

        subDepartmentService.createSubDepartment(null);

    }

    @Test
    public void createSubDepartment_ShouldReturnNameAlreadyExists(){
        thrown.expect(DataAlreadyExistsException.class);

        given(subDepartmentRepository.findByName(subDepartmentRequestDTO.getName())).willReturn(1);

        subDepartmentService.createSubDepartment(subDepartmentRequestDTO);
    }

    @Test
    public void createSubDepartment_ShouldReturnCodeAlreadyExists(){
        thrown.expect(DataAlreadyExistsException.class);

        given(subDepartmentRepository.findByName(subDepartmentRequestDTO.getName())).willReturn(0);
        given(subDepartmentRepository.findByCode(subDepartmentRequestDTO.getCode())).willReturn(1);

        subDepartmentService.createSubDepartment(subDepartmentRequestDTO);
    }

    @Test
    public void createSubDepartment_ShouldReturnCreate(){
        given(subDepartmentRepository.findByName(subDepartmentRequestDTO.getName())).willReturn(0);
        given(subDepartmentRepository.findByCode(subDepartmentRequestDTO.getCode())).willReturn(0);
        given(subDepartmentRepository.save(any(SubDepartment.class))).willReturn(savedSubDepartment);

        assertThat(subDepartmentService.createSubDepartment(subDepartmentRequestDTO)).isEqualTo(Optional.of(savedSubDepartment));
        verify(subDepartmentRepository).save(any(SubDepartment.class));
    }


}