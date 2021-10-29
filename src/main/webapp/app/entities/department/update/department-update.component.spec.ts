jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { DepartmentService } from '../service/department.service';
import { IDepartment, Department } from '../department.model';
import { ITargetSystem } from 'app/entities/target-system/target-system.model';
import { TargetSystemService } from 'app/entities/target-system/service/target-system.service';

import { DepartmentUpdateComponent } from './department-update.component';

describe('Department Management Update Component', () => {
  let comp: DepartmentUpdateComponent;
  let fixture: ComponentFixture<DepartmentUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let departmentService: DepartmentService;
  let targetSystemService: TargetSystemService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [DepartmentUpdateComponent],
      providers: [FormBuilder, ActivatedRoute],
    })
      .overrideTemplate(DepartmentUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(DepartmentUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    departmentService = TestBed.inject(DepartmentService);
    targetSystemService = TestBed.inject(TargetSystemService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call TargetSystem query and add missing value', () => {
      const department: IDepartment = { id: 456 };
      const targetSystems: ITargetSystem[] = [{ id: 50168 }];
      department.targetSystems = targetSystems;

      const targetSystemCollection: ITargetSystem[] = [{ id: 15879 }];
      jest.spyOn(targetSystemService, 'query').mockReturnValue(of(new HttpResponse({ body: targetSystemCollection })));
      const additionalTargetSystems = [...targetSystems];
      const expectedCollection: ITargetSystem[] = [...additionalTargetSystems, ...targetSystemCollection];
      jest.spyOn(targetSystemService, 'addTargetSystemToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ department });
      comp.ngOnInit();

      expect(targetSystemService.query).toHaveBeenCalled();
      expect(targetSystemService.addTargetSystemToCollectionIfMissing).toHaveBeenCalledWith(
        targetSystemCollection,
        ...additionalTargetSystems
      );
      expect(comp.targetSystemsSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const department: IDepartment = { id: 456 };
      const targetSystems: ITargetSystem = { id: 97568 };
      department.targetSystems = [targetSystems];

      activatedRoute.data = of({ department });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(department));
      expect(comp.targetSystemsSharedCollection).toContain(targetSystems);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Department>>();
      const department = { id: 123 };
      jest.spyOn(departmentService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ department });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: department }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(departmentService.update).toHaveBeenCalledWith(department);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Department>>();
      const department = new Department();
      jest.spyOn(departmentService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ department });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: department }));
      saveSubject.complete();

      // THEN
      expect(departmentService.create).toHaveBeenCalledWith(department);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Department>>();
      const department = { id: 123 };
      jest.spyOn(departmentService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ department });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(departmentService.update).toHaveBeenCalledWith(department);
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
  });

  describe('Getting selected relationships', () => {
    describe('getSelectedTargetSystem', () => {
      it('Should return option if no TargetSystem is selected', () => {
        const option = { id: 123 };
        const result = comp.getSelectedTargetSystem(option);
        expect(result === option).toEqual(true);
      });

      it('Should return selected TargetSystem for according option', () => {
        const option = { id: 123 };
        const selected = { id: 123 };
        const selected2 = { id: 456 };
        const result = comp.getSelectedTargetSystem(option, [selected2, selected]);
        expect(result === selected).toEqual(true);
        expect(result === selected2).toEqual(false);
        expect(result === option).toEqual(false);
      });

      it('Should return option if this TargetSystem is not selected', () => {
        const option = { id: 123 };
        const selected = { id: 456 };
        const result = comp.getSelectedTargetSystem(option, [selected]);
        expect(result === option).toEqual(true);
        expect(result === selected).toEqual(false);
      });
    });
  });
});
