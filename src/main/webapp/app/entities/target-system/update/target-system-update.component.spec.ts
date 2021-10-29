jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { TargetSystemService } from '../service/target-system.service';
import { ITargetSystem, TargetSystem } from '../target-system.model';

import { TargetSystemUpdateComponent } from './target-system-update.component';

describe('TargetSystem Management Update Component', () => {
  let comp: TargetSystemUpdateComponent;
  let fixture: ComponentFixture<TargetSystemUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let targetSystemService: TargetSystemService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [TargetSystemUpdateComponent],
      providers: [FormBuilder, ActivatedRoute],
    })
      .overrideTemplate(TargetSystemUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(TargetSystemUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    targetSystemService = TestBed.inject(TargetSystemService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const targetSystem: ITargetSystem = { id: 456 };

      activatedRoute.data = of({ targetSystem });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(targetSystem));
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<TargetSystem>>();
      const targetSystem = { id: 123 };
      jest.spyOn(targetSystemService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ targetSystem });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: targetSystem }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(targetSystemService.update).toHaveBeenCalledWith(targetSystem);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<TargetSystem>>();
      const targetSystem = new TargetSystem();
      jest.spyOn(targetSystemService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ targetSystem });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: targetSystem }));
      saveSubject.complete();

      // THEN
      expect(targetSystemService.create).toHaveBeenCalledWith(targetSystem);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<TargetSystem>>();
      const targetSystem = { id: 123 };
      jest.spyOn(targetSystemService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ targetSystem });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(targetSystemService.update).toHaveBeenCalledWith(targetSystem);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
