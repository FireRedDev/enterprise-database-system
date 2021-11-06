jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { TargetsystemcredentialsService } from '../service/targetsystemcredentials.service';
import { ITargetsystemcredentials, Targetsystemcredentials } from '../targetsystemcredentials.model';
import { ISystemuser } from 'app/entities/systemuser/systemuser.model';
import { SystemuserService } from 'app/entities/systemuser/service/systemuser.service';
import { ITargetsystem } from 'app/entities/targetsystem/targetsystem.model';
import { TargetsystemService } from 'app/entities/targetsystem/service/targetsystem.service';

import { TargetsystemcredentialsUpdateComponent } from './targetsystemcredentials-update.component';

describe('Targetsystemcredentials Management Update Component', () => {
  let comp: TargetsystemcredentialsUpdateComponent;
  let fixture: ComponentFixture<TargetsystemcredentialsUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let targetsystemcredentialsService: TargetsystemcredentialsService;
  let systemuserService: SystemuserService;
  let targetsystemService: TargetsystemService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [TargetsystemcredentialsUpdateComponent],
      providers: [FormBuilder, ActivatedRoute],
    })
      .overrideTemplate(TargetsystemcredentialsUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(TargetsystemcredentialsUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    targetsystemcredentialsService = TestBed.inject(TargetsystemcredentialsService);
    systemuserService = TestBed.inject(SystemuserService);
    targetsystemService = TestBed.inject(TargetsystemService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Systemuser query and add missing value', () => {
      const targetsystemcredentials: ITargetsystemcredentials = { id: 456 };
      const systemuser: ISystemuser = { id: 7785 };
      targetsystemcredentials.systemuser = systemuser;

      const systemuserCollection: ISystemuser[] = [{ id: 72095 }];
      jest.spyOn(systemuserService, 'query').mockReturnValue(of(new HttpResponse({ body: systemuserCollection })));
      const additionalSystemusers = [systemuser];
      const expectedCollection: ISystemuser[] = [...additionalSystemusers, ...systemuserCollection];
      jest.spyOn(systemuserService, 'addSystemuserToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ targetsystemcredentials });
      comp.ngOnInit();

      expect(systemuserService.query).toHaveBeenCalled();
      expect(systemuserService.addSystemuserToCollectionIfMissing).toHaveBeenCalledWith(systemuserCollection, ...additionalSystemusers);
      expect(comp.systemusersSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Targetsystem query and add missing value', () => {
      const targetsystemcredentials: ITargetsystemcredentials = { id: 456 };
      const targetsystem: ITargetsystem = { id: 552 };
      targetsystemcredentials.targetsystem = targetsystem;

      const targetsystemCollection: ITargetsystem[] = [{ id: 6432 }];
      jest.spyOn(targetsystemService, 'query').mockReturnValue(of(new HttpResponse({ body: targetsystemCollection })));
      const additionalTargetsystems = [targetsystem];
      const expectedCollection: ITargetsystem[] = [...additionalTargetsystems, ...targetsystemCollection];
      jest.spyOn(targetsystemService, 'addTargetsystemToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ targetsystemcredentials });
      comp.ngOnInit();

      expect(targetsystemService.query).toHaveBeenCalled();
      expect(targetsystemService.addTargetsystemToCollectionIfMissing).toHaveBeenCalledWith(
        targetsystemCollection,
        ...additionalTargetsystems
      );
      expect(comp.targetsystemsSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const targetsystemcredentials: ITargetsystemcredentials = { id: 456 };
      const systemuser: ISystemuser = { id: 96229 };
      targetsystemcredentials.systemuser = systemuser;
      const targetsystem: ITargetsystem = { id: 38392 };
      targetsystemcredentials.targetsystem = targetsystem;

      activatedRoute.data = of({ targetsystemcredentials });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(targetsystemcredentials));
      expect(comp.systemusersSharedCollection).toContain(systemuser);
      expect(comp.targetsystemsSharedCollection).toContain(targetsystem);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Targetsystemcredentials>>();
      const targetsystemcredentials = { id: 123 };
      jest.spyOn(targetsystemcredentialsService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ targetsystemcredentials });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: targetsystemcredentials }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(targetsystemcredentialsService.update).toHaveBeenCalledWith(targetsystemcredentials);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Targetsystemcredentials>>();
      const targetsystemcredentials = new Targetsystemcredentials();
      jest.spyOn(targetsystemcredentialsService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ targetsystemcredentials });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: targetsystemcredentials }));
      saveSubject.complete();

      // THEN
      expect(targetsystemcredentialsService.create).toHaveBeenCalledWith(targetsystemcredentials);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Targetsystemcredentials>>();
      const targetsystemcredentials = { id: 123 };
      jest.spyOn(targetsystemcredentialsService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ targetsystemcredentials });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(targetsystemcredentialsService.update).toHaveBeenCalledWith(targetsystemcredentials);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Tracking relationships identifiers', () => {
    describe('trackSystemuserById', () => {
      it('Should return tracked Systemuser primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackSystemuserById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });

    describe('trackTargetsystemById', () => {
      it('Should return tracked Targetsystem primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackTargetsystemById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });
  });
});
