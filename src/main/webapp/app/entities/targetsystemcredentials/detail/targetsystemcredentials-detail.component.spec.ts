import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { TargetsystemcredentialsDetailComponent } from './targetsystemcredentials-detail.component';

describe('Targetsystemcredentials Management Detail Component', () => {
  let comp: TargetsystemcredentialsDetailComponent;
  let fixture: ComponentFixture<TargetsystemcredentialsDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [TargetsystemcredentialsDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ targetsystemcredentials: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(TargetsystemcredentialsDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(TargetsystemcredentialsDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load targetsystemcredentials on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.targetsystemcredentials).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
