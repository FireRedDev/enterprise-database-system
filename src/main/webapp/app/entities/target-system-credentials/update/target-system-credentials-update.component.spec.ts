jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { TargetSystemCredentialsService } from '../service/target-system-credentials.service';
import { ITargetSystemCredentials, TargetSystemCredentials } from '../target-system-credentials.model';
import { ITargetSystem } from 'app/entities/target-system/target-system.model';
import { TargetSystemService } from 'app/entities/target-system/service/target-system.service';
import { IEmployee } from 'app/entities/employee/employee.model';
import { EmployeeService } from 'app/entities/employee/service/employee.service';
import { IDepartment } from 'app/entities/department/department.model';
import { DepartmentService } from 'app/entities/department/service/department.service';

import { TargetSystemCredentialsUpdateComponent } from './target-system-credentials-update.component';

describe('TargetSystemCredentials Management Update Component', () => {
  let comp: TargetSystemCredentialsUpdateComponent;
  let fixture: ComponentFixture<TargetSystemCredentialsUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let targetSystemCredentialsService: TargetSystemCredentialsService;
  let targetSystemService: TargetSystemService;
  let employeeService: EmployeeService;
  let departmentService: DepartmentService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [TargetSystemCredentialsUpdateComponent],
      providers: [FormBuilder, ActivatedRoute],
    })
      .overrideTemplate(TargetSystemCredentialsUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(TargetSystemCredentialsUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    targetSystemCredentialsService = TestBed.inject(TargetSystemCredentialsService);
    targetSystemService = TestBed.inject(TargetSystemService);
    employeeService = TestBed.inject(EmployeeService);
    departmentService = TestBed.inject(DepartmentService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call targetSystem query and add missing value', () => {
      const targetSystemCredentials: ITargetSystemCredentials = { id: 456 };
      const targetSystem: ITargetSystem = { id: 23166 };
      targetSystemCredentials.targetSystem = targetSystem;

      const targetSystemCollection: ITargetSystem[] = [{ id: 9885 }];
      jest.spyOn(targetSystemService, 'query').mockReturnValue(of(new HttpResponse({ body: targetSystemCollection })));
      const expectedCollection: ITargetSystem[] = [targetSystem, ...targetSystemCollection];
      jest.spyOn(targetSystemService, 'addTargetSystemToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ targetSystemCredentials });
      comp.ngOnInit();

      expect(targetSystemService.query).toHaveBeenCalled();
      expect(targetSystemService.addTargetSystemToCollectionIfMissing).toHaveBeenCalledWith(targetSystemCollection, targetSystem);
      expect(comp.targetSystemsCollection).toEqual(expectedCollection);
    });

    it('Should call employee query and add missing value', () => {
      const targetSystemCredentials: ITargetSystemCredentials = { id: 456 };
      const employee: IEmployee = { id: 92446 };
      targetSystemCredentials.employee = employee;

      const employeeCollection: IEmployee[] = [{ id: 38461 }];
      jest.spyOn(employeeService, 'query').mockReturnValue(of(new HttpResponse({ body: employeeCollection })));
      const expectedCollection: IEmployee[] = [employee, ...employeeCollection];
      jest.spyOn(employeeService, 'addEmployeeToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ targetSystemCredentials });
      comp.ngOnInit();

      expect(employeeService.query).toHaveBeenCalled();
      expect(employeeService.addEmployeeToCollectionIfMissing).toHaveBeenCalledWith(employeeCollection, employee);
      expect(comp.employeesCollection).toEqual(expectedCollection);
    });

    it('Should call department query and add missing value', () => {
      const targetSystemCredentials: ITargetSystemCredentials = { id: 456 };
      const department: IDepartment = { id: 10890 };
      targetSystemCredentials.department = department;

      const departmentCollection: IDepartment[] = [{ id: 82565 }];
      jest.spyOn(departmentService, 'query').mockReturnValue(of(new HttpResponse({ body: departmentCollection })));
      const expectedCollection: IDepartment[] = [department, ...departmentCollection];
      jest.spyOn(departmentService, 'addDepartmentToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ targetSystemCredentials });
      comp.ngOnInit();

      expect(departmentService.query).toHaveBeenCalled();
      expect(departmentService.addDepartmentToCollectionIfMissing).toHaveBeenCalledWith(departmentCollection, department);
      expect(comp.departmentsCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const targetSystemCredentials: ITargetSystemCredentials = { id: 456 };
      const targetSystem: ITargetSystem = { id: 51249 };
      targetSystemCredentials.targetSystem = targetSystem;
      const employee: IEmployee = { id: 25399 };
      targetSystemCredentials.employee = employee;
      const department: IDepartment = { id: 50530 };
      targetSystemCredentials.department = department;

      activatedRoute.data = of({ targetSystemCredentials });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(targetSystemCredentials));
      expect(comp.targetSystemsCollection).toContain(targetSystem);
      expect(comp.employeesCollection).toContain(employee);
      expect(comp.departmentsCollection).toContain(department);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<TargetSystemCredentials>>();
      const targetSystemCredentials = { id: 123 };
      jest.spyOn(targetSystemCredentialsService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ targetSystemCredentials });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: targetSystemCredentials }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(targetSystemCredentialsService.update).toHaveBeenCalledWith(targetSystemCredentials);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<TargetSystemCredentials>>();
      const targetSystemCredentials = new TargetSystemCredentials();
      jest.spyOn(targetSystemCredentialsService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ targetSystemCredentials });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: targetSystemCredentials }));
      saveSubject.complete();

      // THEN
      expect(targetSystemCredentialsService.create).toHaveBeenCalledWith(targetSystemCredentials);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<TargetSystemCredentials>>();
      const targetSystemCredentials = { id: 123 };
      jest.spyOn(targetSystemCredentialsService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ targetSystemCredentials });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(targetSystemCredentialsService.update).toHaveBeenCalledWith(targetSystemCredentials);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Tracking relationships identifiers', () => {
    describe('trackTargetSystemById', () => {
      it('Should return tracked TargetSystem primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackTargetSystemById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });

    describe('trackEmployeeById', () => {
      it('Should return tracked Employee primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackEmployeeById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });

    describe('trackDepartmentById', () => {
      it('Should return tracked Department primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackDepartmentById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });
  });
});
