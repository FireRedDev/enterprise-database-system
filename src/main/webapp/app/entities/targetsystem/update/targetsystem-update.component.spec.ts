jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { TargetsystemService } from '../service/targetsystem.service';
import { ITargetsystem, Targetsystem } from '../targetsystem.model';

import { TargetsystemUpdateComponent } from './targetsystem-update.component';

describe('Targetsystem Management Update Component', () => {
  let comp: TargetsystemUpdateComponent;
  let fixture: ComponentFixture<TargetsystemUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let targetsystemService: TargetsystemService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [TargetsystemUpdateComponent],
      providers: [FormBuilder, ActivatedRoute],
    })
      .overrideTemplate(TargetsystemUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(TargetsystemUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    targetsystemService = TestBed.inject(TargetsystemService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const targetsystem: ITargetsystem = { id: 456 };

      activatedRoute.data = of({ targetsystem });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(targetsystem));
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Targetsystem>>();
      const targetsystem = { id: 123 };
      jest.spyOn(targetsystemService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ targetsystem });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: targetsystem }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(targetsystemService.update).toHaveBeenCalledWith(targetsystem);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Targetsystem>>();
      const targetsystem = new Targetsystem();
      jest.spyOn(targetsystemService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ targetsystem });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: targetsystem }));
      saveSubject.complete();

      // THEN
      expect(targetsystemService.create).toHaveBeenCalledWith(targetsystem);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Targetsystem>>();
      const targetsystem = { id: 123 };
      jest.spyOn(targetsystemService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ targetsystem });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(targetsystemService.update).toHaveBeenCalledWith(targetsystem);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
