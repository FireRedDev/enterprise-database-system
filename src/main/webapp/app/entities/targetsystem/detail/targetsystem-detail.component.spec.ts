import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { TargetsystemDetailComponent } from './targetsystem-detail.component';

describe('Targetsystem Management Detail Component', () => {
  let comp: TargetsystemDetailComponent;
  let fixture: ComponentFixture<TargetsystemDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [TargetsystemDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ targetsystem: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(TargetsystemDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(TargetsystemDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load targetsystem on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.targetsystem).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
