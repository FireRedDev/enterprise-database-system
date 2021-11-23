jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { SystemuserService } from '../service/systemuser.service';
import { ISystemuser, Systemuser } from '../systemuser.model';
import { IDepartment } from 'app/entities/department/department.model';
import { DepartmentService } from 'app/entities/department/service/department.service';

import { SystemuserUpdateComponent } from './systemuser-update.component';

describe('Systemuser Management Update Component', () => {
  let comp: SystemuserUpdateComponent;
  let fixture: ComponentFixture<SystemuserUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let systemuserService: SystemuserService;
  let departmentService: DepartmentService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [SystemuserUpdateComponent],
      providers: [FormBuilder, ActivatedRoute],
    })
      .overrideTemplate(SystemuserUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(SystemuserUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    systemuserService = TestBed.inject(SystemuserService);
    departmentService = TestBed.inject(DepartmentService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Department query and add missing value', () => {
      const systemuser: ISystemuser = { id: 456 };
      const departments: IDepartment[] = [{ id: 72165 }];
      systemuser.departments = departments;

      const departmentCollection: IDepartment[] = [{ id: 12145 }];
      jest.spyOn(departmentService, 'query').mockReturnValue(of(new HttpResponse({ body: departmentCollection })));
      const additionalDepartments = [...departments];
      const expectedCollection: IDepartment[] = [...additionalDepartments, ...departmentCollection];
      jest.spyOn(departmentService, 'addDepartmentToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ systemuser });
      comp.ngOnInit();

      expect(departmentService.query).toHaveBeenCalled();
      expect(departmentService.addDepartmentToCollectionIfMissing).toHaveBeenCalledWith(departmentCollection, ...additionalDepartments);
      expect(comp.departmentsSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const systemuser: ISystemuser = { id: 456 };
      const departments: IDepartment = { id: 83735 };
      systemuser.departments = [departments];

      activatedRoute.data = of({ systemuser });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(systemuser));
      expect(comp.departmentsSharedCollection).toContain(departments);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Systemuser>>();
      const systemuser = { id: 123 };
      jest.spyOn(systemuserService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ systemuser });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: systemuser }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(systemuserService.update).toHaveBeenCalledWith(systemuser);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Systemuser>>();
      const systemuser = new Systemuser();
      jest.spyOn(systemuserService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ systemuser });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: systemuser }));
      saveSubject.complete();

      // THEN
      expect(systemuserService.create).toHaveBeenCalledWith(systemuser);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Systemuser>>();
      const systemuser = { id: 123 };
      jest.spyOn(systemuserService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ systemuser });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(systemuserService.update).toHaveBeenCalledWith(systemuser);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Tracking relationships identifiers', () => {
    describe('trackDepartmentById', () => {
      it('Should return tracked Department primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackDepartmentById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });
  });

  describe('Getting selected relationships', () => {
    describe('getSelectedDepartment', () => {
      it('Should return option if no Department is selected', () => {
        const option = { id: 123 };
        const result = comp.getSelectedDepartment(option);
        expect(result === option).toEqual(true);
      });

      it('Should return selected Department for according option', () => {
        const option = { id: 123 };
        const selected = { id: 123 };
        const selected2 = { id: 456 };
        const result = comp.getSelectedDepartment(option, [selected2, selected]);
        expect(result === selected).toEqual(true);
        expect(result === selected2).toEqual(false);
        expect(result === option).toEqual(false);
      });

      it('Should return option if this Department is not selected', () => {
        const option = { id: 123 };
        const selected = { id: 456 };
        const result = comp.getSelectedDepartment(option, [selected]);
        expect(result === option).toEqual(true);
        expect(result === selected).toEqual(false);
      });
    });
  });
});
