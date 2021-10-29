import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { TargetSystemDetailComponent } from './target-system-detail.component';

describe('TargetSystem Management Detail Component', () => {
  let comp: TargetSystemDetailComponent;
  let fixture: ComponentFixture<TargetSystemDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [TargetSystemDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ targetSystem: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(TargetSystemDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(TargetSystemDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load targetSystem on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.targetSystem).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
